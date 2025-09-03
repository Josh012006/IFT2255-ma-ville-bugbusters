package ca.udem.maville.server.dao.config;

import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * La classe de configuration de la base de données.
 */
public class MongoConfig {

    private static final Datastore datastore;
    private static final String mongoUri;

    static {
        // Essaie de charger le .env en local
        Dotenv dotenv = null;
        try {
            dotenv = Dotenv.configure()
                    .directory("backend")
                    .ignoreIfMissing()  // ✅ ne crashe pas si le fichier n’existe pas
                    .load();
        } catch (Exception e) {
            System.out.println("No .env file found, falling back to system environment variables.");
        }

        // Si .env existe → prend MONGO_URI depuis .env
        // Sinon → prend la variable d’environnement système (prod/Koyeb)
        if (dotenv != null && dotenv.get("MONGO_URI") != null) {
            mongoUri = dotenv.get("MONGO_URI");
        } else {
            mongoUri = System.getenv("MONGO_URI");
        }

        if (mongoUri == null) {
            throw new RuntimeException("MONGO_URI is not set in .env or environment variables.");
        }

        datastore = Morphia.createDatastore(
                MongoClients.create(mongoUri),
                "maville"
        );
    }

    public static Datastore getDatastore() {
        return datastore;
    }
}
