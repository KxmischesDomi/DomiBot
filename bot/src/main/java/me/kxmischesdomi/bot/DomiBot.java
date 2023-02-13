package me.kxmischesdomi.bot;

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
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
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
	private final Config config;

	@Getter
	private final DatabaseAccessor databaseAccessor;

	@Getter
	private final CommandExecutor commandExecutor;

	private final Set<IModule> modules;

	@Getter
	private JDA jda;

	public DomiBot() throws Exception {
		modules = new LinkedHashSet<>();

		config = new ConfigLoader().loadConfigConfiguration();

		MorphiaConfig config = MorphiaConfig.builder()
				.databaseHost(this.getConfig().getMorphiaConfig().getHost())
				.databasePort(this.getConfig().getMorphiaConfig().getPort())
				.databaseUser(this.getConfig().getMorphiaConfig().getUsername())
				.databasePassword(this.getConfig().getMorphiaConfig().getPassword())
				.databaseName(this.getConfig().getMorphiaConfig().getDatabase())
				.build();

		EasyInjector injector = Easy.createInjector(new CommonModule(config), new AbstractModule() {
			@Provides
			public DomiBot provideBot() {
				return DomiBot.this;
			}
		});

		System.out.println("Create Modules");
		databaseAccessor = injector.getInstance(DatabaseAccessor.class);
		jda = JDABuilder.createDefault(this.getConfig().getToken()).build();
		commandExecutor = new CommandExecutor(jda);
		addModule(injector.getInstance(LevelingModule.class));
		addModule(injector.getInstance(GenericModule.class));

		System.out.println("Init Modules");
		for (IModule module : modules) {
			module.init();
		}

		System.out.println("Enable Modules");
		jda.addEventListener(commandExecutor);
//		jda.updateCommands().queue();

		System.out.println("Finished starting");
	}

	public void addModule(IModule module) {
		modules.add(module);
	}

}
