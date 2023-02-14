package me.kxmischesdomi.bot.modules.leveling;

import me.kxmischesdomi.DomiBot;
import me.kxmischesdomi.bot.modules.base.IModule;
import xyz.juliandev.easy.annotations.Inject;

import java.util.Random;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class LevelingModule implements IModule {

	private final DomiBot bot;

	private final Random random;

	@Inject
	public LevelingModule(DomiBot bot) {
		this.bot = bot;
		this.random = new Random();
	}

	@Override
	public void init() {
		bot.getJda().addEventListener(new LevelingListener(bot, this));

		bot.getCommandExecutor().addCommand(new RankCommand(bot, bot.getDatabaseAccessor()));
		bot.getCommandExecutor().addCommand(new LeaderboardCommand(bot, bot.getDatabaseAccessor()));
	}

	public int generateRandomXp() {
		return random.nextInt(5 - 1) + 1;
	}

}
