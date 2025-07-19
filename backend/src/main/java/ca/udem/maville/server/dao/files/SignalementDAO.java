package ca.udem.maville.server.dao.files;

import ca.udem.maville.server.dao.config.MongoConfig;
import ca.udem.maville.server.models.Signalement;
import dev.morphia.query.experimental.filters.Filters;
import org.bson.types.ObjectId;

import java.util.List;

public class SignalementDAO {

    /**
     * Récupère un signalement par son ID
     */
    public Signalement findById(ObjectId id){
        return MongoConfig.getDatastore()
                .find(Signalement.class)
                .filter(Filters.eq("_id", id))
                .first();
    }

    
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

    
    /**
     * Récupère les signalements effectués par un résident donné
     */
    public List<Signalement> findResidentSignalements(ObjectId userId){
        return MongoConfig.getDatastore()
                .find(Signalement.class)
                .filter(Filters.eq("resident", userId))
                .iterator()
                .toList();
    }

     /**
     * Enregistre ou met à jour un signalement
     */
    public void save(Signalement signalement) {
        MongoConfig.getDatastore().save(signalement);
    }

}
