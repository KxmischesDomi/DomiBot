package me.kxmischesdomi.db.repositories;

import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;
import me.kxmischesdomi.db.Constants;
import me.kxmischesdomi.db.domain.Repository;
import me.kxmischesdomi.db.models.UserModel;
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

    public UserModel findByUserId(long id) {
        return this.createQuery()
                .filter(Filters.eq("userId", id))
                .first();
    }

    public UserModel findByAccessToken(String accessToken) {
        return this.createQuery()
                .filter(Filters.exists("webClientModel"))
                .filter(Filters.eq("webClientModel.accessToken", accessToken))
                .first();
    }

}
