package me.kxmischesdomi.bot.base;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public interface CommandProvider {

	CommandData provideCommand();

	void runCommand(SlashCommandInteractionEvent event);

}
