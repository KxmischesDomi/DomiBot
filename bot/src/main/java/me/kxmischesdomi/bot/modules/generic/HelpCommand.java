package me.kxmischesdomi.bot.modules.generic;

import me.kxmischesdomi.DomiBot;
import me.kxmischesdomi.bot.base.CommandProvider;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public record HelpCommand(DomiBot bot) implements CommandProvider {

	@Override
	public CommandData provideCommand() {
		return new CommandDataImpl("help", "Get helping information help about me");
	}

	@Override
	public void runCommand(SlashCommandInteractionEvent event) {
		Guild guild = event.getGuild();

		event.replyEmbeds(
				new EmbedBuilder()
						.setColor(guild == null ? null : guild.getSelfMember().getColor())
						.setAuthor("» " + event.getJDA().getSelfUser().getName(), bot.getConfig().getInvite(), event.getJDA().getSelfUser().getEffectiveAvatarUrl())
						.setDescription("Current version ``" + bot.getConfig().getVersion() + "``")
						.addField("Member Commands (1)",
								"\n» `/rank [@member]` • Look at yours or others ranking" +
								"\n» `/leaderboard` • View the server leaderboard",
								false)
						.addField("Information (3)",
								"\n» [Invite](" + bot.getConfig().getInvite() + ") the bot to your server" +
										"\n» [Join](" + bot.getConfig().getSupport() + ") the support server" +
										"\n» [Visit](" + bot.getConfig().getWebsite() + ") our website"
								,false)
						.build()
		).queue();

	}

}
