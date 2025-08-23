package ca.udem.maville.server.dao.config;

import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import io.github.cdimascio.dotenv.Dotenv;

import static ca.udem.maville.server.Server.logger;

/**
 * La classe de configuration de la base de données. Permet de s'y connecter facilement dans les
 * DAO files.
 */
public class MongoConfig {

    private static final Dotenv dotenv = Dotenv.configure()
                                            .directory("backend")
                                            .load();

    private static final Datastore datastore;
    private static final String mongoUri = dotenv.get("MONGO_URI");



    static {
        datastore = Morphia.createDatastore(
                MongoClients.create(mongoUri),
                "maville"
        );
    }

    public static Datastore getDatastore() {
        return datastore;
    }
}