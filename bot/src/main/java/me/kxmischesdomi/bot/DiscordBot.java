package me.kxmischesdomi.bot;

import me.kxmischesdomi.DomiBot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import xyz.juliandev.easy.annotations.Inject;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class DiscordBot {

	private final DomiBot bot;

	private JDA jda;

	@Inject
	public DiscordBot(DomiBot bot) {
		this.bot = bot;
	}

	public void init() {

		jda = JDABuilder.createDefault(this.bot.getConfig().getToken()).build();

	}

}
