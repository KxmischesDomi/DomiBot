package me.kxmischesdomi.bot;

import me.kxmischesdomi.db.models.UserModel;
import me.kxmischesdomi.db.repositories.UserRepository;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import xyz.juliandev.easy.annotations.Inject;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class TestListener extends ListenerAdapter {

	private final UserRepository userRepository;

	@Inject
	public TestListener(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;

		long idLong = event.getAuthor().getIdLong();
		UserModel userModel = userRepository.findOrCreateUser(event.getAuthor());

		event.getChannel().sendMessage(userModel.toString()).queue();

	}

}
