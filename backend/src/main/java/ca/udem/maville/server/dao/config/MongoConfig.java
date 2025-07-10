package ca.udem.maville.server.dao.config;

import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

public class MongoConfig {
    private static final Datastore datastore;
    private static final String serverUrl = "mongodb://localhost:27017";

    static {
        datastore = Morphia.createDatastore(
                MongoClients.create(serverUrl),
                "database"
        );
    }

    public static Datastore getDatastore() {
        return datastore;
    }
}
