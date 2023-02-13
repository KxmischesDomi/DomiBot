package me.kxmischesdomi.bot.modules.generic;

import me.kxmischesdomi.bot.DomiBot;
import me.kxmischesdomi.bot.modules.base.IModule;
import xyz.juliandev.easy.annotations.Inject;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class GenericModule implements IModule {

	private final DomiBot bot;

	@Inject
	public GenericModule(DomiBot bot) {
		this.bot = bot;
	}

	@Override
	public void init() {
		bot.getCommandExecutor().addCommand(new HelpCommand(bot));
	}

}
