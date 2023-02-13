package me.kxmischesdomi.db.repositories;

import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;
import me.kxmischesdomi.db.Constants;
import me.kxmischesdomi.db.domain.Repository;
import me.kxmischesdomi.db.models.MemberModel;
import xyz.juliandev.easy.annotations.Inject;
import xyz.juliandev.easy.annotations.Named;
import xyz.juliandev.easy.annotations.Singleton;

import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Singleton
public class MemberRepository extends Repository<MemberModel> {

    @Inject
    protected MemberRepository(@Named(Constants.DATASTORE) Datastore datastore) {
        super(MemberModel.class, datastore);
    }

    public MemberModel findByGuildAndUserId(long guildId, long userId) {
        return this.createQuery()
                .filter(Filters.and(Filters.eq("userId", userId), Filters.eq("guildId", guildId)))
                .first();
    }

    public List<MemberModel> findByGuildId(long guildId) {
        return this.createQuery()
                .filter(Filters.eq("guildId", guildId))
                .stream().toList();
    }

    public List<MemberModel> findByUserId(long userId) {
        return this.createQuery()
                .filter(Filters.eq("userId", userId))
                .stream().toList();
    }

}
