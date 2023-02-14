package me.kxmischesdomi.web;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.javalin.Javalin;
import io.javalin.vue.VueComponent;
import lombok.Getter;
import me.kxmischesdomi.DomiBot;
import me.kxmischesdomi.config.Config;
import me.kxmischesdomi.db.models.UserModel;
import me.kxmischesdomi.db.models.WebClientModel;
import net.dv8tion.jda.api.Permission;
import org.bson.BsonDocument;
import org.bson.BsonString;
import xyz.juliandev.easy.annotations.Inject;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class WebApp {

	@Getter
	private final DomiBot bot;

	@Getter
	private final Javalin javalin;

	@Getter
	private final RequestHandler requestHandler;

	@Getter
	private final DiscordRequestHandler discordRequestHandler;

	@Getter
	private final DiscordSessionHandler discordSessionHandler;

	@Inject
	public WebApp(DomiBot bot, Config config) {
		this.bot = bot;
		requestHandler = new RequestHandler();
		discordRequestHandler = new DiscordRequestHandler(requestHandler, config);
		discordSessionHandler = new DiscordSessionHandler(bot.getDatabaseAccessor());

		 javalin = Javalin.create(javalinConfig -> {
		 	javalinConfig.staticFiles.enableWebjars();
		 	javalinConfig.vue.vueAppName = "app";
		 	if (config.isDev()) {
		 		javalinConfig.vue.rootDirectory("/vue");
			}
		 	javalinConfig.vue.stateFunction = context -> {
				HashMap<String, String> map = new HashMap<>();
				return map;
			};
		 	javalinConfig.accessManager((handler, context, permittedRoles) -> {
		 		if (permittedRoles.contains(Role.ANYONE)) {
		 			handler.handle(context);
				} else if (permittedRoles.contains(Role.LOGGED_IN) && context.cookie("token") != null) {
		 			handler.handle(context);
				} else {
//		 			context.status(401).header(Header.WWW_AUTHENTICATE, "Basic");
					context.redirect("/login");
				}
			});
		 }).start(config.getWebConfig().getHost(), config.getWebConfig().getPort());

		 javalin.get("/", new VueComponent("home"), Role.ANYONE);
		 javalin.get("/dashboard", new VueComponent("dashboard"), Role.LOGGED_IN);

		 javalin.get("/logout", ctx -> {
		 	ctx.removeCookie("token");
		 	ctx.redirect("/");
		 }, Role.ANYONE);

		 javalin.get("/login", ctx -> {
			 String token = ctx.cookie("token");
			 if (token == null || !discordSessionHandler.isTokenValid(token)) {
			 	ctx.redirect(config.getApplicationOAuth2());
			 } else {
			 	ctx.redirect("/dashboard");
			 }
		 }, Role.ANYONE);

		 javalin.get("/oauth2", ctx -> {
			 String discordAuthCode = ctx.queryParam("code");
			 System.out.println(discordAuthCode);
			 JsonObject document = discordRequestHandler.requestAccessToken(discordAuthCode);
			 if (document.has("access_token")) {
				 String access_token = document.get("access_token").getAsString();
				 JsonObject userInfo = discordRequestHandler.requestUserInfo(access_token);
				 JsonObject user = userInfo.get("user").getAsJsonObject();
				 String id = user.get("id").getAsString();
				 long idLong = Long.parseLong(id);

				 UserModel model = bot.getDatabaseAccessor().getUserRepository().findByUserId(idLong);
				 if (model == null) {
					 model = UserModel.builder()
							 .userId(idLong)
							 .cachedName(user.get("username").getAsString())
							 .cachedDiscriminator(user.get("discriminator").getAsString())
							 .build();
				 }

				 if (model.getWebClientModel() == null) model.setWebClientModel(new WebClientModel());

				 WebClientModel clientModel = model.getWebClientModel();
				 clientModel.setAccessToken(access_token);
				 clientModel.setRefreshToken(document.get("refresh_token").getAsString());
				 String scope = document.get("scope").getAsString();
				 clientModel.setScopes(scope.split(" "));
				 clientModel.setTokenResetMillis(System.currentTimeMillis() + document.get("expires_in").getAsInt() * 1000L);

				 bot.getDatabaseAccessor().getUserRepository().save(model);

				 ctx.cookie("token", access_token);

				 ctx.redirect("/login");
				 return;
			 }
			 ctx.redirect("/");
		 }, Role.ANYONE);

		 javalin.get("/api/session", ctx -> {
			 UserModel model = discordSessionHandler.fetchUserByToken(ctx.cookie("token"));
			 if (model == null) return;
			 BsonDocument document = new BsonDocument();
			 document.put("name", new BsonString(model.getCachedName()));
			 document.put("discriminator", new BsonString(model.getCachedDiscriminator()));
			 ctx.json(document.toString());
		 }, Role.LOGGED_IN);
		javalin.get("/api/guilds", ctx -> {
			String token = ctx.cookie("token");
			if (!discordSessionHandler.isTokenValid(token)) return;

			JsonArray guilds = discordRequestHandler.requestUserGuilds(token);
			System.out.println(guilds);
			JsonArray arrayJoined = new JsonArray();
			JsonArray arrayNotJoined = new JsonArray();
			List<JsonElement> list = guilds.asList().stream().filter(jsonElement -> {
				JsonObject guild = jsonElement.getAsJsonObject();
				String permissions = guild.get("permissions").getAsString();
				for (Permission permission : Permission.getPermissions(Long.parseLong(permissions))) {
					if (permission == Permission.ADMINISTRATOR || permission == Permission.MANAGE_SERVER) {
						guild.addProperty("botJoined", bot.getJda().getGuildById(guild.get("id").getAsString()) != null);
						return true;
					}
				}
				return false;
			}).sorted(Comparator.comparing(o -> o.getAsJsonObject().get("name").getAsString())).collect(Collectors.toList());

			for (JsonElement element : list) {
				JsonObject guild = element.getAsJsonObject();
				boolean joined = guild.get("botJoined").getAsBoolean();
				System.out.println(joined);
				if (joined) {
					arrayJoined.add(element);
				} else {
					arrayNotJoined.add(element);
				}
			}

			arrayJoined.addAll(arrayNotJoined);
			ctx.json(arrayJoined.toString());
		}, Role.LOGGED_IN);

		Runtime.getRuntime().addShutdownHook(new Thread(javalin::stop));
	}

}
