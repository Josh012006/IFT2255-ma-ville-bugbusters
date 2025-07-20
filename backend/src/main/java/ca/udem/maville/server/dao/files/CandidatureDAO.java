package ca.udem.maville.server.dao.files;

import ca.udem.maville.server.dao.config.MongoConfig;
import ca.udem.maville.server.models.Candidature;
import dev.morphia.query.filters.Filters;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * La classe CandidatureDAO qui représente la couche d'interactions
 * à la base de données pour tout ce qui concerne les candidatures.
 * Elle offre des méthodes statiques utilisées dans {@link ca.udem.maville.server.controllers.CandidatureController}
 * et qui agissent sur les documents stockés tout en suivant le model
 * {@link ca.udem.maville.server.models.Candidature}
 */
public class CandidatureDAO {

    /**
     * Récupère toutes les candidatures dont le statut est "en attente".
     * @return une liste des candidatures trouvées.
     */
    public static List<Candidature> findAll(){
        return MongoConfig.getDatastore()
                .find(Candidature.class)
                .filter(Filters.eq("statut","en attente"))
                .iterator()
                .toList();
    }

    /**
     * Récupère une candidature à partir de son id.
     * @param id qui représente l'id de la candidature.
     * @return un objet de type Candidature.
     */
    public static Candidature findById(ObjectId id){
        return MongoConfig.getDatastore()
                .find(Candidature.class)
                .filter(Filters.eq("_id", id))
                .first();
    }

    /**
     * Récupère toutes les candidatures soumises par un prestataire donné.
     * @param userId qui représente l'id du prestataire.
     * @return une liste des candidatures trouvées.
     */
    public static List<Candidature> findPrestataireCandidatures(ObjectId userId){
        return MongoConfig.getDatastore()
                .find(Candidature.class)
                .filter(Filters.eq("prestataire", userId))
                .iterator()
                .toList();
    }

    /**
     * Sauvegarde ou met à jour une candidature.
     * @param candidature qui est la candidature à sauvegarder.
     */
    public static void save(Candidature candidature){
        MongoConfig.getDatastore().save(candidature);
    }

     /**
     * Supprime une candidature par son id.
      * @param id qui est l'id de la candidature à supprimer.
     */
    public static void delete(ObjectId id){
        Candidature candidature = findById(id);
        if (candidature != null){
            MongoConfig.getDatastore().delete(candidature);
        }
    }

 


}
