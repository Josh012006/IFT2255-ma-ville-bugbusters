package ca.udem.maville.server.dao.files.users;

import ca.udem.maville.server.dao.config.MongoConfig;
import ca.udem.maville.server.models.users.Resident;
import dev.morphia.query.filters.Filter;
import dev.morphia.query.filters.Filters;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.regex.Pattern;

/**
 * La classe ResidentDAO qui représente la couche d'interactions
 * à la base de données pour tout ce qui concerne les résidents.
 * Elle offre des méthodes statiques utilisées dans {@link ca.udem.maville.server.controllers.users.ResidentController}
 * et qui agissent sur les documents stockés tout en suivant le model
 * {@link ca.udem.maville.server.models.users.Resident}
 */
public class ResidentDAO {

    /**
     * Récupère un résident par son id.
     * @param id représente l'id du résident.
     * @return le résident trouvé.
     */
    public static Resident findById(ObjectId id) {
        return MongoConfig.getDatastore()
                .find(Resident.class)
                .filter(Filters.eq("_id", id))
                .first();
    }

    /**
     * Récupère tous les résidents présents dans la base de données.
     * @return une liste des résidents trouvés.
     */
    public static List<Resident> findAll() {
        return MongoConfig.getDatastore()
                .find(Resident.class)
                .iterator()
                .toList();
    }


    /**
     * Récupère les résidents à notifier par quartier ou rue.
     * @param quartier qui représente le quartier concerné.
     * @param rues qui repésente un tableau des rues affectées.
     * @return une liste des résidents trouvés.
     */
    public static List<Resident> findToNotify(String quartier, String[] rues) {
        Filter[] ruesFilters = new Filter[rues.length];
        for (int i = 0; i < rues.length; i++) {
            ruesFilters[i] = Filters.regex("abonnementsRue", Pattern.compile(".*" + Pattern.quote(rues[i]) + ".*", Pattern.CASE_INSENSITIVE));
        }

        return MongoConfig.getDatastore()
                .find(Resident.class)
                .filter(
                    Filters.or(
                        Filters.eq("abonnementsQuartier", quartier),
                        Filters.or(ruesFilters)
                    )
                )
                .iterator()
                .toList();
    }


    /**
     * Sauvegarde ou met à jour un résident.
     * @param resident qui représente le résident à sauvegarder.
     */
    public static void save(Resident resident) {
        MongoConfig.getDatastore().save(resident);
    }
}
