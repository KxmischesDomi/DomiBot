package me.kxmischesdomi.db.modules;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import me.kxmischesdomi.db.Constants;
import me.kxmischesdomi.db.config.MorphiaConfig;
import org.bson.UuidRepresentation;
import xyz.juliandev.easy.annotations.Named;
import xyz.juliandev.easy.annotations.Provides;
import xyz.juliandev.easy.module.AbstractModule;

import java.util.Collections;

public class CommonModule extends AbstractModule {

    private final MorphiaConfig morphiaConfig;

    public CommonModule(MorphiaConfig morphiaConfig) {
        this.morphiaConfig = morphiaConfig;
    }

    @Provides
    @Named(Constants.DATASTORE)
    public Datastore provideDatastore() {

        ServerAddress serverAddress = new ServerAddress(this.morphiaConfig.databaseHost(), this.morphiaConfig.databasePort());

        MongoCredential credential = MongoCredential.createCredential(this.morphiaConfig.databaseUser(), this.morphiaConfig.databaseName(), this.morphiaConfig.databasePassword().toCharArray());

        MongoClient mongoClient = MongoClients.create(MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                        builder.hosts(Collections.singletonList(serverAddress)))
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .credential(credential)
                .build());

        Datastore datastore = Morphia.createDatastore(mongoClient, this.morphiaConfig.databaseName());

        datastore.getMapper().mapPackage(Constants.COMMON_MODEL_PACKAGE);

        datastore.ensureIndexes();

        return datastore;
    }


    @Provides
    public MorphiaConfig provideConfig() {
        return this.morphiaConfig;
    }

}