package ca.udem.maville.server.dao.files;

import ca.udem.maville.server.dao.config.MongoConfig;
import ca.udem.maville.server.models.FicheProbleme;
import dev.morphia.query.experimental.filters.Filters;
import org.bson.types.ObjectId;

import java.util.List;

import java.util.List;

public class CandidatureDAO {

    /**
     * Récupère toutes les candidatures dont le statut est "en attente"
     */
    public List<Candidature> findAll(){
        return MongoConfig.getDatastore()
                .find(Candidature.class)
                .filter(Filters.eq("statut","en attente"))
                .iterator()
                .toList();
    }

    /**
     * Récupère une candidature par son identifiant
     */
    public Candidature findById(ObjectId id){
        return MongoConfig.getDatastore()
                .find(Candidature.class)
                .filter(Filters.eq("_id", id))
                .first();
    }

     /**
     * Récupère toutes les candidatures soumises par un prestataire donné
     */
    public List<Candidature> findPrestataireCandidatures(ObjectId userId){
        return MongoConfig.getDatastore()
                .find(Candidature.class)
                .filter(Filters.eq("prestataire", userId))
                .iterator()
                .toList();
    }

    /**
     * Sauvegarde ou met à jour une candidature
     */
    public void save(Candidature candidature){
        MongoConfig.getDatastore().save(candidature);
    }

     /**
     * Supprime une candidature par son ID
     */
    public void delete(ObjectId id){
        Candidature candidature = findById(id);
        if (candidature !=null){
            MongoConfig.getDatastore().delete(candidature);
        }
    }

 


}
