package me.kxmischesdomi.db.repositories;

import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;
import me.kxmischesdomi.db.Constants;
import me.kxmischesdomi.db.domain.Repository;
import me.kxmischesdomi.db.models.GuildModel;
import me.kxmischesdomi.db.models.MemberModel;
import me.kxmischesdomi.db.models.UserModel;
import xyz.juliandev.easy.annotations.Inject;
import xyz.juliandev.easy.annotations.Named;
import xyz.juliandev.easy.annotations.Singleton;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Singleton
public class GuildRepository extends Repository<GuildModel> {

    @Inject
    protected GuildRepository(@Named(Constants.DATASTORE) Datastore datastore) {
        super(GuildModel.class, datastore);
    }

    public GuildModel findByGuildId(Long id) {
        return this.createQuery()
                .filter(Filters.eq("guildId", id))
                .first();
    }

}
