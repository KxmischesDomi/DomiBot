package me.kxmischesdomi.web;

import me.kxmischesdomi.bot.db.DatabaseAccessor;
import me.kxmischesdomi.db.models.UserModel;
import me.kxmischesdomi.db.models.WebClientModel;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public record DiscordSessionHandler(DatabaseAccessor databaseAccessor) {

	public UserModel fetchUserByToken(String token) {
		if (token == null) return null;
		UserModel userModel = databaseAccessor.getUserRepository().findByAccessToken(token);
		if (userModel == null) return null;
		WebClientModel model = userModel.getWebClientModel();
		if (model == null) return null;
		long millis = model.getTokenResetMillis();
		return userModel;
	}

	public boolean isTokenValid(String token) {
		return fetchUserByToken(token) != null;
	}

}
