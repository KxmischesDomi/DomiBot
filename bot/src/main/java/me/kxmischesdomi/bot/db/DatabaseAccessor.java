package me.kxmischesdomi.bot.db;

import lombok.Getter;
import me.kxmischesdomi.db.models.GuildModel;
import me.kxmischesdomi.db.models.LevelingModel;
import me.kxmischesdomi.db.models.MemberModel;
import me.kxmischesdomi.db.models.UserModel;
import me.kxmischesdomi.db.repositories.GuildRepository;
import me.kxmischesdomi.db.repositories.MemberRepository;
import me.kxmischesdomi.db.repositories.UserRepository;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import xyz.juliandev.easy.annotations.Inject;

import java.util.Objects;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class DatabaseAccessor {

	@Getter
	private final UserRepository userRepository;

	@Getter
	private final GuildRepository guildRepository;

	@Getter
	private final MemberRepository memberRepository;

	@Inject
	public DatabaseAccessor(UserRepository userRepository, GuildRepository guildRepository, MemberRepository memberRepository) {
		this.userRepository = userRepository;
		this.guildRepository = guildRepository;
		this.memberRepository = memberRepository;
	}

	public UserModel findUserOrCreateOne(User user) {
		UserModel userModel = userRepository.findByUserId(user.getIdLong());
		if (userModel == null) {
			userModel = UserModel.builder()
					.userId(user.getIdLong())
					.cachedName(user.getName())
					.cachedDiscriminator(user.getDiscriminator())
					.cachedAvatarURL(user.getAvatarUrl())
					.userLevelingProfile(new LevelingModel())
					.build();
			userRepository.save(userModel);
		} else if (updateUserInformation(userModel, user)) {
			userRepository.save(userModel);
		}

		return userModel;
	}

	public boolean updateUserInformation(UserModel userModel, User user) {
		boolean changes = false;
		if (!Objects.equals(userModel.getCachedName(), user.getName())) {
			userModel.setCachedName(user.getName());
			changes = true;
		}
		if (!Objects.equals(userModel.getCachedDiscriminator(), user.getDiscriminator())) {
			userModel.setCachedDiscriminator(user.getDiscriminator());
			changes = true;
		}
		if (!Objects.equals(userModel.getCachedAvatarURL(), user.getAvatarUrl())) {
			userModel.setCachedAvatarURL(user.getAvatarUrl());
			changes = true;
		}
		return changes;
	}

	public MemberModel findMemberOrCreateOne(Member member) {
		MemberModel memberModel = memberRepository.findByGuildAndUserId(member.getGuild().getIdLong(), member.getIdLong());
		if (memberModel == null) {
			memberModel = MemberModel.builder()
					.userId(member.getIdLong())
					.guildId(member.getGuild().getIdLong())
					.userModel(findUserOrCreateOne(member.getUser()))
					.memberLevelingModel(new LevelingModel())
					.build();
			memberRepository.save(memberModel);
		}
		return memberModel;
	}

	public GuildModel findGuildByIdOrCreateOne(Guild guild) {
		GuildModel guildModel = guildRepository.findByGuildId(guild.getIdLong());
		if (guildModel == null) {
			guildModel = GuildModel.builder()
					.guildId(guild.getIdLong())
					.cachedIconURL(guild.getIconUrl())
					.cachedName(guild.getName())
					.guildLevelingProfile(new LevelingModel())
					.build();
			guildRepository.save(guildModel);
		} else if (updateGuildInformation(guildModel, guild)) {
			guildRepository.save(guildModel);
		}

		return guildModel;
	}


	public boolean updateGuildInformation(GuildModel guildModel, Guild guild) {
		boolean changes = false;
		if (!Objects.equals(guildModel.getCachedName(), guild.getName())) {
			guildModel.setCachedName(guild.getName());
			changes = true;
		}
		if (!Objects.equals(guildModel.getCachedIconURL(), guild.getIconUrl())) {
			guildModel.setCachedIconURL(guild.getIconUrl());
			changes = true;
		}
		return changes;
	}

}
