package me.kxmischesdomi.bot.modules.leveling;

import me.kxmischesdomi.bot.DomiBot;
import me.kxmischesdomi.bot.base.CommandProvider;
import me.kxmischesdomi.bot.db.DatabaseAccessor;
import me.kxmischesdomi.db.models.LevelingModel;
import me.kxmischesdomi.db.models.MemberModel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public record RankCommand(DomiBot bot, DatabaseAccessor accessor) implements CommandProvider {

	@Override
	public CommandData provideCommand() {
		return new CommandDataImpl("rank", "View your current ranking").addOption(OptionType.MENTIONABLE, "member", "View other member's ranking").setGuildOnly(true);
	}

	@Override
	public void runCommand(SlashCommandInteractionEvent event) {
		if (event.getMember() == null) return;

		Member member = event.getMember();

		OptionMapping memberOption = event.getOption("member");
		if (memberOption != null) {
			member = memberOption.getAsMember().getUser().isBot() ? event.getMember() : memberOption.getAsMember();
		}

		MemberModel memberModel = accessor.getMemberRepository().findByGuildAndUserId(event.getGuild().getIdLong(), member.getIdLong());

		if (memberModel == null) {
			event.reply("Keine Daten zu dieser Person vorhanden.").queue();
			return;
		}

		LevelingModel levelingModel = memberModel.getMemberLevelingModel();

		LevelingModel guildLevelingProfile = accessor.findGuildByIdOrCreateOne(event.getGuild()).getGuildLevelingProfile();

		event.replyEmbeds(
				new EmbedBuilder()
						.setColor(event.getGuild().getSelfMember().getColor())
						.setAuthor(member.getUser().getName(), bot.getConfig().getInvite(), member.getEffectiveAvatarUrl())
						.addField("Level", String.valueOf(levelingModel.getCachedLevel()), true)
						.addField("Xp", String.valueOf(levelingModel.getXp()), true)
						.addField("Messages", String.valueOf(levelingModel.getMessages()), true)
						.addField("Next Level", (levelingModel.getCachedXpToNextLevel() - levelingModel.getXp()) + " Xp", false)
						.addField("Guild Level", String.valueOf(guildLevelingProfile.getCachedLevel()), true)
						.addField("Guild Xp", String.valueOf(guildLevelingProfile.getXp()), true)
						.addField("Guild Messages", String.valueOf(guildLevelingProfile.getMessages()), true)
						.addField("Next Level", (guildLevelingProfile.getCachedXpToNextLevel() - levelingModel.getXp()) + " Xp", false)
						.build()
		).queue();
	}

}
