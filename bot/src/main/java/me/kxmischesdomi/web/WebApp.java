package me.kxmischesdomi.web;

import io.javalin.Javalin;
import io.javalin.vue.VueComponent;
import lombok.Getter;
import me.kxmischesdomi.DomiBot;
import me.kxmischesdomi.config.Config;
import me.kxmischesdomi.db.models.UserModel;
import me.kxmischesdomi.db.models.WebClientModel;
import org.bson.BsonDocument;
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

	@Inject
	public WebApp(DomiBot bot, Config config) {
		this.bot = bot;
		requestHandler = new RequestHandler();
		discordRequestHandler = new DiscordRequestHandler(requestHandler, config);

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

		 javalin.get("/login", ctx -> {
			 String discordAuthCode = ctx.queryParam("code");
			 BsonDocument document = discordRequestHandler.requestAccessToken(discordAuthCode);

			 if (document.containsKey("access_token")) {

				 String access_token = document.getString("access_token").getValue();
				 BsonDocument userInfo = discordRequestHandler.requestUserInfo(access_token);
				 BsonDocument user = userInfo.getDocument("user");
				 String id = user.getString("id").getValue();
				 long idLong = Long.parseLong(id);

				 UserModel model = bot.getDatabaseAccessor().getUserRepository().findByUserId(idLong);
				 if (model == null) {
				 	model = UserModel.builder()
							 .userId(idLong)
							 .cachedName(user.getString("username").getValue())
							 .cachedDiscriminator(user.getString("3313").getValue())
							 .build();
				 }

				 if (model.getWebClientModel() == null) model.setWebClientModel(new WebClientModel());

				 WebClientModel clientModel = model.getWebClientModel();
				 clientModel.setAccessToken(access_token);
				 clientModel.setRefreshToken(document.getString("refresh_token").getValue());
				 String scope = document.getString("scope").getValue();
				 clientModel.setScopes(scope.split(" "));
				 clientModel.setTokenResetMillis(System.currentTimeMillis() + document.getInt32("expires_in").getValue() * 1000L);

				 bot.getDatabaseAccessor().getUserRepository().save(model);

				 ctx.redirect("/dashboard");
				 return;
			 }
			 ctx.redirect("/");
		 });

		Runtime.getRuntime().addShutdownHook(new Thread(javalin::stop));
	}

}
