package me.kxmischesdomi.bot;

import lombok.Getter;
import me.kxmischesdomi.config.Config;
import me.kxmischesdomi.config.ConfigLoader;
import me.kxmischesdomi.db.config.MorphiaConfig;
import me.kxmischesdomi.db.modules.CommonModule;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import xyz.juliandev.easy.Easy;
import xyz.juliandev.easy.annotations.Provides;
import xyz.juliandev.easy.injector.EasyInjector;
import xyz.juliandev.easy.module.AbstractModule;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class DomiBot {

	@Getter
	private final Config config;

	@Getter
	private JDA jda;

	public DomiBot() throws Exception {
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

		System.out.println("Create Bot");
		jda = JDABuilder.createDefault(this.getConfig().getToken()).build();

		System.out.println("Init Bot Modules");
		jda.addEventListener(injector.getInstance(TestListener.class));

		System.out.println("Finished starting");
	}

}
