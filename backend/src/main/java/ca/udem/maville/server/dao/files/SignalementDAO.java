package ca.udem.maville.server.dao.files;

import ca.udem.maville.server.dao.config.MongoConfig;
import ca.udem.maville.server.models.Signalement;
import dev.morphia.query.filters.Filters;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * La classe SignalementDAO qui représente la couche d'interactions
 * à la base de données pour tout ce qui concerne les signalements.
 * Elle offre des méthodes statiques utilisées dans {@link ca.udem.maville.server.controllers.SignalementController}
 * et qui agissent sur les documents stockés tout en suivant le model
 * {@link ca.udem.maville.server.models.Signalement}
 */
public class SignalementDAO {

    /**
     * Récupère un signalement par son id.
     * @param id qui est l'id du signalement recherché.
     * @return le signalement trouvé.
     */
    public static Signalement findById(ObjectId id){
        return MongoConfig.getDatastore()
                .find(Signalement.class)
                .filter(Filters.eq("_id", id))
                .first();
    }


    /**
     * Récupère tous les signalements ayant le statut "en attente".
     * @return la liste des signalements trouvés.
     */
    public static List<Signalement> findAll(){
        return MongoConfig.getDatastore()
                .find(Signalement.class)
                .filter(Filters.eq("statut", "en attente"))
                .iterator()
                .toList();

    }


    /**
     * Récupère les signalements effectués par un résident donné.
     * @param userId qui est l'id du résident.
     * @return la liste des signalements retrouvés.
     */
    public static List<Signalement> findResidentSignalements(ObjectId userId){
        return MongoConfig.getDatastore()
                .find(Signalement.class)
                .filter(Filters.eq("resident", userId))
                .iterator()
                .toList();
    }

    /**
     * Enregistre ou met à jour un signalement.
     * @param signalement qui ets le signalement à enregistrer.
     */
    public static void save(Signalement signalement) {
        MongoConfig.getDatastore().save(signalement);
    }

    /**
     * Supprime un signalement de la base de données par son id.
     * @param id représente l'id du signalement à supprimer.
     */
    public static void delete(ObjectId id) {
        Signalement signalement = findById(id);
        if(signalement != null){
            MongoConfig.getDatastore().delete(signalement);
        }
    }

}
