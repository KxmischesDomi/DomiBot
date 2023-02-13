package me.kxmischesdomi.db.repositories;

import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;
import me.kxmischesdomi.db.Constants;
import me.kxmischesdomi.db.domain.Repository;
import me.kxmischesdomi.db.models.UserModel;
import xyz.juliandev.easy.annotations.Inject;
import xyz.juliandev.easy.annotations.Named;
import xyz.juliandev.easy.annotations.Singleton;

import java.util.UUID;

@Singleton
public class UserRepository extends Repository<UserModel> {

    @Inject
    protected UserRepository(@Named(Constants.DATASTORE) Datastore datastore) {
        super(UserModel.class, datastore);
    }

    public UserModel findByUuid(UUID uuid) {
        return this.createQuery()
                .filter(Filters.eq("uuid", uuid))
                .first();
    }

    public UserModel findByLatestName(String latestName) {
        return this.createQuery()
                .filter(Filters.eq("latestName", latestName))
                .first();
    }

    public UserModel findByXBoxName(String xBoxName) {
        return this.createQuery()
                .filter(Filters.eq("xBoxName", xBoxName))
                .first();
    }
}
