package me.kxmischesdomi.bot.modules.leveling;

import me.kxmischesdomi.DomiBot;
import me.kxmischesdomi.bot.db.DatabaseAccessor;
import me.kxmischesdomi.db.models.GuildModel;
import me.kxmischesdomi.db.models.LevelingModel;
import me.kxmischesdomi.db.models.MemberModel;
import me.kxmischesdomi.db.models.UserModel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class LevelingListener extends ListenerAdapter {

	private final DomiBot bot;
	private final LevelingModule levelingModule;

	public LevelingListener(DomiBot bot, LevelingModule levelingModule) {
		this.bot = bot;
		this.levelingModule = levelingModule;
	}

	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if (event.getAuthor().isBot() || event.getMember() == null) return;

		DatabaseAccessor accessor = bot.getDatabaseAccessor();

		MemberModel memberModel = accessor.findMemberOrCreateOne(event.getMember());
		LevelingModel memberLevelingModel = memberModel.getMemberLevelingModel();
		memberLevelingModel.setMessages(memberLevelingModel.getMessages() + 1);

		UserModel userModel = accessor.findUserOrCreateOne(event.getAuthor());
		LevelingModel userLevelingModel = userModel.getUserLevelingProfile();
		userLevelingModel.setMessages(userLevelingModel.getMessages() + 1);

		GuildModel guildModel = accessor.findGuildByIdOrCreateOne(event.getGuild());
		LevelingModel guildLevelingModel = guildModel.getGuildLevelingProfile();
		guildLevelingModel.setMessages(guildLevelingModel.getMessages() + 1);

		// Check for leveling cooldown
		long lastMillis = memberLevelingModel.getLastTimeLeveledMillis();
		long currentMillis = System.currentTimeMillis();
		long difference = currentMillis - lastMillis;
		if (difference >= 1000 /* * 60 */) {
			memberLevelingModel.setLastTimeLeveledMillis(currentMillis);

			int addedXp = levelingModule.generateRandomXp();

			int previousMemberLevel = memberLevelingModel.getCachedLevel();
			int previousGuildLevel = guildLevelingModel.getCachedLevel();

			// Add Xp and recalculate levels
			memberLevelingModel.setXp(memberLevelingModel.getXp() + addedXp);
			memberLevelingModel.recalculateCachedValues(1);
			userLevelingModel.setXp(userLevelingModel.getXp() + addedXp);
			userLevelingModel.recalculateCachedValues(1);
			guildLevelingModel.setXp(guildLevelingModel.getXp() + addedXp);
			guildLevelingModel.recalculateCachedValues(10);

			// Check for level up
			if (memberLevelingModel.getCachedLevel() > previousMemberLevel) {
				// LEVEL UP
				event.getChannel().sendMessage(event.getMember().getAsMention() + " hat Level **" + memberLevelingModel.getCachedLevel() + "** erreicht!").queue();
			}

			if (guildLevelingModel.getCachedLevel() > previousGuildLevel) {
				// Guild level up
				event.getChannel().sendMessage("Der Server hat Level **" + guildLevelingModel.getCachedLevel() + "** erreicht!").queue();
			}

		}

		accessor.getMemberRepository().save(memberModel);
		accessor.getUserRepository().save(userModel);
		accessor.getGuildRepository().save(guildModel);





	}

}
