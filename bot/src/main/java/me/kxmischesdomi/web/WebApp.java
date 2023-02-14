package me.kxmischesdomi.web;

import com.google.gson.JsonObject;
import io.javalin.Javalin;
import io.javalin.vue.VueComponent;
import lombok.Getter;
import me.kxmischesdomi.DomiBot;
import me.kxmischesdomi.config.Config;
import me.kxmischesdomi.db.models.UserModel;
import me.kxmischesdomi.db.models.WebClientModel;
import org.bson.BsonDocument;
import org.bson.BsonString;
import xyz.juliandev.easy.annotations.Inject;

import java.util.HashMap;

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
		 }).start(config.getWebConfig().getHost(), config.getWebConfig().getPort());

		 javalin.get("/", new VueComponent("home"));
		 javalin.get("/dashboard", new VueComponent("dashboard"));

		 javalin.get("/login", ctx -> {
			 String token = ctx.cookie("token");
			 if (token == null || !discordSessionHandler.isTokenValid(token)) {
			 	ctx.redirect(config.getApplicationOAuth2());
			 } else {
			 	ctx.redirect("/dashboard");
			 }
		 });

		 javalin.get("/oauth2", ctx -> {
			 String discordAuthCode = ctx.queryParam("code");
			 JsonObject document = discordRequestHandler.requestAccessToken(discordAuthCode);
			 System.out.println(document);
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
							 .cachedDiscriminator(user.get("3313").getAsString())
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
		 });

		 javalin.get("/api/session", ctx -> {
			 UserModel model = discordSessionHandler.fetchUserByToken(ctx.cookie("token"));
			 if (model == null) return;
			 BsonDocument document = new BsonDocument();
			 document.put("name", new BsonString(model.getCachedName()));
			 document.put("discriminator", new BsonString(model.getCachedDiscriminator()));
			 ctx.json(document.toString());
		 });
		javalin.get("/api/guilds", ctx -> {
			String token = ctx.cookie("token");
			UserModel model = discordSessionHandler.fetchUserByToken(token);
			if (model == null) return;
			ctx.json(discordRequestHandler.requestUserGuilds(token).toString());
		});

		Runtime.getRuntime().addShutdownHook(new Thread(javalin::stop));
	}

}
