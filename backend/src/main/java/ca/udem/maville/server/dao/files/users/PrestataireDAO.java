package ca.udem.maville.server.dao.files.users;

import ca.udem.maville.server.dao.config.MongoConfig;
import ca.udem.maville.server.models.users.Prestataire;
import dev.morphia.query.experimental.filters.Filters;
import dev.morphia.query.experimental.filters.Filter;
import org.bson.types.ObjectId;

import java.util.List;

public class PrestataireDAO {


    /**
     * Récupère un prestataire par son ID
     */
    public Prestataire findById(ObjectId id){
        return MongoConfig.getDatastore()
                .find(Prestataire.class)
                .filter(Filters.eq("_id", id))
                .first();
    }

    
    /**
     * Récupère tous les prestataires
     */
    public List<Prestataire> findAll() {
        return MongoConfig.getDatastore()
                .find(Prestataire.class)
                .iterator()
                .toList();
    }


    /**
     * Récupère tous les prestataires à notifier selon le quartier ou le type de travaux
     */
    public List<Prestataire> findToNotify(String quartier, String typeTravail) {
        return MongoConfig.getDatastore()
                .find(Prestataire.class)
                .filter(
                    Filters.or(
                        Filters.eq("abonnementsQuartier", quartier),
                        Filters.eq("abonnementsType", typeTravail)
                    )
                )
                .iterator()
                .toList();
    }


    /**
     * Sauvegarde ou met à jour un prestataire
     */
    public void save(Prestataire prestataire) {
        MongoConfig.getDatastore().save(prestataire);
    }
}
