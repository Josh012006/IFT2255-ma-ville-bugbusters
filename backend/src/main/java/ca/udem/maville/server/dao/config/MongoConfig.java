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
    private static final String serverUrl = "mongodb+srv://josuesmjrmongan:ssdYqr1XI8hM7XA2@dm3.8s6gipw.mongodb.net/?retryWrites=true&w=majority&appName=DM3";

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
