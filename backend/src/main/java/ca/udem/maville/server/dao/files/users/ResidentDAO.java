package ca.udem.maville.server.dao.files.users;

import ca.udem.maville.server.dao.config.MongoConfig;
import ca.udem.maville.server.models.users.Resident;
import dev.morphia.query.experimental.filters.Filters;
import dev.morphia.query.experimental.filters.Filter;
import org.bson.types.ObjectId;

import java.util.List;

public class ResidentDAO {

    // Todo: Une méthode findById(ObjectId id) qui renvoie un élément de type model Resident
    //  qui représente le résident avec un tel id dans la base de données.

     /**
     * Récupère un résident par son ID
     */
    public Resident findById(ObjectId id) {
        return MongoConfig.getDatastore()
                .find(Resident.class)
                .filter(Filters.eq("_id", id))
                .first();
    }

    /**
     * Récupère tous les résidents
     */
    public List<Resident> findAll() {
        return MongoConfig.getDatastore()
                .find(Resident.class)
                .iterator()
                .toList();
    }



     /**
     * Récupère les résidents à notifier par quartier ou rue
     */
    public List<Resident> findToNotify(String quartier, String[] rues) {
        Filter quartierFilter = Filters.eq("abonnementsQuartiers", quartier);
        Filter[] ruesFilters = new Filter[rues.length];
        for (int i = 0; i < rues.length; i++) {
            ruesFilters[i] = Filters.eq("abonnementsRues", rues[i]);
        }

        return MongoConfig.getDatastore()
                .find(Resident.class)
                .filter(
                    Filters.or(
                        quartierFilter,
                        Filters.or(ruesFilters)
                    )
                )
                .iterator()
                .toList();
    }


    /**
     * Sauvegarde ou met à jour un résident
     */
    public void save(Resident resident) {
        MongoConfig.getDatastore().save(resident);
    }
}
