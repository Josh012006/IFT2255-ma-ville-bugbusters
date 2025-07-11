package ca.udem.maville.server.dao.config;

import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

/**
 * La classe de configuration de la base de données. Permet de s'y connecter facilement dans les
 * DAO files.
 */
public class MongoConfig {
    private static final Datastore datastore;
    private static final String serverUrl = "mongodb://localhost:27017";

    static {
        datastore = Morphia.createDatastore(
                MongoClients.create(serverUrl),
                "maville"
        );
    }

    public static Datastore getDatastore() {
        return datastore;
    }
}
