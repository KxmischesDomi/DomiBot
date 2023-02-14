package me.kxmischesdomi.web;

import io.javalin.Javalin;
import io.javalin.vue.VueComponent;
import lombok.Getter;
import me.kxmischesdomi.config.Config;
import org.bson.BsonDocument;
import xyz.juliandev.easy.annotations.Inject;

import java.util.HashMap;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class WebApp {

	@Getter
	private final Javalin javalin;

	@Getter
	private final RequestHandler requestHandler;

	@Getter
	private final DiscordRequestHandler discordRequestHandler;

	@Inject
	public WebApp(Config config) {
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
			 	ctx.redirect("/dashboard");
			 }
		 });

		Runtime.getRuntime().addShutdownHook(new Thread(javalin::stop));
	}

}
