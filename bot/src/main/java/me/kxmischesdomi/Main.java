package me.kxmischesdomi;

import me.kxmischesdomi.bot.DomiBot;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class Main {

	public static void main(String[] args) {
		try {
			new DomiBot();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

}
