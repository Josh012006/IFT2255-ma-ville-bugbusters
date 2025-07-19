package ca.udem.maville.server.dao.files;

import ca.udem.maville.server.dao.config.MongoConfig;
import ca.udem.maville.server.models.Signalement;
import dev.morphia.query.experimental.filters.Filters;
import org.bson.types.ObjectId;

import java.util.List;

public class SignalementDAO {

    // Todo: Une méthode findById(ObjectId id) qui renvoie un élément de type model Signalement dont l'id est
    //  celui donné.
    /**
     * Récupère un signalement par son ID
     */
    public Signalement findById(ObjectId id){
        return MongoConfig.getDatastore()
                .find(Signalement.class)
                .filter(Filters.eq("_id", id))
                .first();
    }

    // Todo: Une méthode findAll() qui renvoie la liste de tous les signalements de la base de données ayant un statut
    //  "en attente".
    /**
     * Récupère tous les signalements ayant le statut "en attente"
     */
    public List<Signalement> findAll(){
        return MongoConfig.getDatastore()
                .find(Signalement.class)
                .filter(Filters.eq("statut", "en attente"))
                .iterator()
                .toList();

    }

    // Todo: Une méthode findResidentSignalements(ObjectId userId) qui renvoie la liste de tous les signalements de la
    //  base de données appartenant au résident dont l'id est userId. Autrement dit, leur champ resident porte la valeur userId.
    /**
     * Récupère les signalements effectués par un résident donné
     */
    public List<Signalement> findResidentSignalements(ObjectId userId){
        return MongoConfig.getDatastore()
                .find(Signalement.class)
                .filter(Filters.eq("resident.id", userId))
                .iterator()
                .toList();
    }

    // Todo: Une méthode save(Signalement signalement) qui enregistre un signalement dans la base de données.
     /**
     * Enregistre ou met à jour un signalement
     */
    public void save(Signalement signalement) {
        MongoConfig.getDatastore().save(signalement);
    }

}
