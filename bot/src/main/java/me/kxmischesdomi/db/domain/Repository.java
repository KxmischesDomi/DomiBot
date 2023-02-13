package me.kxmischesdomi.db.domain;

import com.mongodb.client.result.DeleteResult;
import dev.morphia.Datastore;
import dev.morphia.query.Query;
import dev.morphia.query.filters.Filters;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public abstract class Repository<ValueType extends Model> {

    private final Class<ValueType> entityClazz;

    private final Datastore datastore;

    protected Repository(Class<ValueType> entityClazz, Datastore datastore) {
        this.entityClazz = entityClazz;
        this.datastore = datastore;
    }

    protected Datastore getDatastore() {
        return this.datastore;
    }

    public ValueType save(ValueType value) {
        return this.datastore.save(value);
    }

    public boolean delete(ValueType value) {
        return deleteById(value.getId());
    }

    public boolean deleteById(ObjectId id) {
        DeleteResult deleteResult = this.datastore.find(this.entityClazz).filter(Filters.eq("id", id)).delete();
        return deleteResult.getDeletedCount() == 1;
    }

    public Optional<ValueType> findById(ObjectId id) {
        return this.datastore.find(this.entityClazz).filter(Filters.eq("id", id)).stream().findFirst();
    }

    public List<ValueType> findAll() {
        return this.createQuery().stream().toList();
    }

    public Query<ValueType> createQuery() {
        return this.datastore.find(this.entityClazz);
    }
}
