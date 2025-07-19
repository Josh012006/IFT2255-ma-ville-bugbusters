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

    // Todo: Une méthode findAll() qui renvoie une liste de tous les résidents dans la base de
    //  données.

    /**
     * Récupère tous les résidents
     */
    public List<Resident> findAll() {
        return MongoConfig.getDatastore()
                .find(Resident.class)
                .iterator()
                .toList();
    }


    // Todo: Une méthode findToNotify(String quartier, String[] rues) qui renvoie tous les résidents
    //  de la base de données qui ont le quartier présent dans leur liste de quartiers auxquels ils sont interessés
    //  ou bien au moins l'une des rues présente dans leur liste de rues auxquelles ils sont abonnés.

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

    // Todo: Une méthode save(Resident resident) qui permet de sauvegarder un résident dans la base de données.

    /**
     * Sauvegarde ou met à jour un résident
     */
    public void save(Resident resident) {
        MongoConfig.getDatastore().save(resident);
    }
}
