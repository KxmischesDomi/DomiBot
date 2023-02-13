package me.kxmischesdomi.db.repositories;

import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;
import me.kxmischesdomi.db.Constants;
import me.kxmischesdomi.db.domain.Repository;
import me.kxmischesdomi.db.models.LevelingModel;
import me.kxmischesdomi.db.models.UserModel;
import net.dv8tion.jda.api.entities.User;
import xyz.juliandev.easy.annotations.Inject;
import xyz.juliandev.easy.annotations.Named;
import xyz.juliandev.easy.annotations.Singleton;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Singleton
public class UserRepository extends Repository<UserModel> {

    @Inject
    protected UserRepository(@Named(Constants.DATASTORE) Datastore datastore) {
        super(UserModel.class, datastore);
    }

    public UserModel findOrCreateUser(User user) {
        UserModel userModel = this.createQuery()
                .filter(Filters.eq("userId", user.getIdLong()))
                .first();
        if (userModel == null) {
            userModel = UserModel.builder()
                    .userId(user.getIdLong())
                    .cachedName(user.getName())
                    .cachedDiscriminator(user.getDiscriminator())
                    .cachedAvatarURL(user.getAvatarUrl())
                    .globalLevelingProfile(new LevelingModel())
                    .build();
            save(userModel);
        }

        return userModel;
    }

    public UserModel findByUserId(Long id) {
        return this.createQuery()
                .filter(Filters.eq("userId", id))
                .first();
    }

}
