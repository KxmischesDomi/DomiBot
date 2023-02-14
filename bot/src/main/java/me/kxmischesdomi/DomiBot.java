package me.kxmischesdomi;

import lombok.Getter;
import me.kxmischesdomi.bot.base.CommandExecutor;
import me.kxmischesdomi.bot.db.DatabaseAccessor;
import me.kxmischesdomi.bot.modules.base.IModule;
import me.kxmischesdomi.bot.modules.generic.GenericModule;
import me.kxmischesdomi.bot.modules.leveling.LevelingModule;
import me.kxmischesdomi.config.Config;
import me.kxmischesdomi.config.ConfigLoader;
import me.kxmischesdomi.db.config.MorphiaConfig;
import me.kxmischesdomi.db.modules.CommonModule;
import me.kxmischesdomi.web.WebApp;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.juliandev.easy.Easy;
import xyz.juliandev.easy.annotations.Provides;
import xyz.juliandev.easy.injector.EasyInjector;
import xyz.juliandev.easy.module.AbstractModule;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class DomiBot {

	@Getter
	private final Logger logger = LoggerFactory.getLogger(DomiBot.class);

	@Getter
	private final Config config;

	@Getter
	private final DatabaseAccessor databaseAccessor;

	@Getter
	private final CommandExecutor commandExecutor;

	private final Set<IModule> modules;

	@Getter
	private final JDA jda;

	@Getter
	private final WebApp webApp;

	public DomiBot() throws Exception {
		modules = new LinkedHashSet<>();

		config = new ConfigLoader().loadConfigConfiguration();

		MorphiaConfig morphiaConfig = MorphiaConfig.builder()
				.databaseHost(this.getConfig().getMorphiaConfig().getHost())
				.databasePort(this.getConfig().getMorphiaConfig().getPort())
				.databaseUser(this.getConfig().getMorphiaConfig().getUsername())
				.databasePassword(this.getConfig().getMorphiaConfig().getPassword())
				.databaseName(this.getConfig().getMorphiaConfig().getDatabase())
				.build();

		EasyInjector injector = Easy.createInjector(new CommonModule(morphiaConfig), new AbstractModule() {
			@Provides
			public DomiBot provideBot() {
				return DomiBot.this;
			}
			@Provides
			public Config provideConfig() {
				return getConfig();
			}
		});

		jda = JDABuilder.createDefault(this.getConfig().getToken()).build();
		injector.addDynamicModule(new AbstractModule() {
			@Provides
			public JDA provideJDA() {
				return jda;
			}
		});
		databaseAccessor = injector.getInstance(DatabaseAccessor.class);
		commandExecutor = injector.getInstance(CommandExecutor.class);

		addModule(injector.getInstance(LevelingModule.class));
		addModule(injector.getInstance(GenericModule.class));

		webApp = injector.getInstance(WebApp.class);

		logger.info("Init Modules");
		for (IModule module : modules) {
			module.init();
		}

		logger.info("Enable Modules");
		jda.addEventListener(commandExecutor);
//		jda.updateCommands().queue();

		logger.info("Finished Starting");
	}

	public void addModule(IModule module) {
		modules.add(module);
	}

}
