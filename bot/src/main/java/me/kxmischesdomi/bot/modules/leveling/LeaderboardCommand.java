package me.kxmischesdomi.bot.modules.leveling;

import dev.morphia.query.FindOptions;
import dev.morphia.query.MorphiaCursor;
import dev.morphia.query.Query;
import dev.morphia.query.Sort;
import dev.morphia.query.filters.Filters;
import me.kxmischesdomi.bot.DomiBot;
import me.kxmischesdomi.bot.base.CommandProvider;
import me.kxmischesdomi.bot.db.DatabaseAccessor;
import me.kxmischesdomi.db.models.LevelingModel;
import me.kxmischesdomi.db.models.MemberModel;
import me.kxmischesdomi.utils.NumberFormatter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public record LeaderboardCommand(DomiBot bot, DatabaseAccessor accessor) implements CommandProvider {

	@Override
	public CommandData provideCommand() {
		return new CommandDataImpl("leaderboard", "View the servers ranking").setGuildOnly(true);
	}

	@Override
	public void runCommand(SlashCommandInteractionEvent event) {
		if (event.getGuild() == null) return;

		Member member = event.getMember();
		EmbedBuilder embed = new EmbedBuilder()
				.setColor(event.getGuild().getSelfMember().getColor())
				.setAuthor(member.getUser().getName(), bot.getConfig().getInvite(), member.getEffectiveAvatarUrl());

		Query<MemberModel> query = accessor.getMemberRepository().createQuery().filter(Filters.eq("guildId", event.getGuild().getIdLong()));
		MorphiaCursor<MemberModel> iterator = query.iterator(new FindOptions().sort(Sort.descending("memberLevelingModel.xp")));

		int i = 0;
		StringBuilder builder = new StringBuilder();
		while (iterator.hasNext()) {
			System.out.println("a");
			MemberModel model = iterator.next();
			LevelingModel levelingModel = model.getMemberLevelingModel();

			Member currentMember = event.getGuild().getMemberById(model.getUserId());
			builder.append("\n**" + (i + 1) + "**. " + (currentMember != null ? currentMember.getAsMention() : "**" +
					String.join("#", model.getUserModel().getCachedName(), model.getUserModel().getCachedDiscriminator()) + "**") + " • " +
					"Level **" + NumberFormatter.BIG_NUMBER.format(levelingModel.getCachedLevel()) + "**｜**" + NumberFormatter.BIG_NUMBER.format(levelingModel.getXp()) + "** XP");
			i++;
		}

		embed.setDescription(builder);
		event.replyEmbeds(embed.build()).queue();
	}

}
