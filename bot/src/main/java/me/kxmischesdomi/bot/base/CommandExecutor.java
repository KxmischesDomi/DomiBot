package me.kxmischesdomi.bot.base;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class CommandExecutor extends ListenerAdapter {

	private final JDA jda;
	private final Map<String, CommandProvider> commands;
	private final Map<String, CommandData> commandDataCache;

	public CommandExecutor(JDA jda) {
		this.jda = jda;
		commands = new ConcurrentHashMap<>();
		commandDataCache = new HashMap<>();
	}

	public void addCommand(CommandProvider provider) {
		CommandData commandData = provider.provideCommand();
		commands.put(commandData.getName(), provider);
		commandDataCache.put(commandData.getName(), commandData);
	}

	@Override
	public void onReady(@NotNull ReadyEvent event) {

		commands.forEach((s, provider) -> {
			// for testing

			for (Guild guild : jda.getGuilds()) {
				guild.upsertCommand(commandDataCache.remove(s)).queue();
			}

//			jda.upsertCommand(commandDataCache.remove(s)).queue();
		});


	}

	@Override
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
		CommandProvider provider = commands.get(event.getName());
		if (provider == null) return;
		provider.runCommand(event);
	}

}
