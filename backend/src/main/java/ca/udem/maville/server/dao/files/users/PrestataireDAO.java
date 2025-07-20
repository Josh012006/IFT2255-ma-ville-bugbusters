package ca.udem.maville.server.dao.files.users;

import ca.udem.maville.server.dao.config.MongoConfig;
import ca.udem.maville.server.models.users.Prestataire;
import dev.morphia.query.filters.Filters;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * La classe PrestataireDAO qui représente la couche d'interactions
 * à la base de données pour tout ce qui concerne les prestataires.
 * Elle offre des méthodes statiques utilisées dans {@link ca.udem.maville.server.controllers.users.PrestataireController}
 * et qui agissent sur les documents stockés tout en suivant le model
 * {@link ca.udem.maville.server.models.users.Prestataire}
 */
public class PrestataireDAO {


    /**
     * Récupère un prestataire par son id.
     * @param id qui représente l'id du prestataire recherché.
     * @return le prestataire trouvé.
     */
    public static Prestataire findById(ObjectId id){
        return MongoConfig.getDatastore()
                .find(Prestataire.class)
                .filter(Filters.eq("_id", id))
                .first();
    }

    
    /**
     * Récupère tous les prestataires.
     * @return la liste des prestatatires trouvés.
     */
    public static List<Prestataire> findAll() {
        return MongoConfig.getDatastore()
                .find(Prestataire.class)
                .iterator()
                .toList();
    }


    /**
     * Récupère tous les prestataires à notifier selon le quartier ou le type de travaux.
     * @param quartier représente le quartier concerné.
     * @param typeTravail représente le type de travail concerné.
     * @return la liste des prestataires trouvés.
     */
    public static List<Prestataire> findToNotify(String quartier, String typeTravail) {
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
     * Sauvegarde ou met à jour un prestataire.
     * @param prestataire qui représente le prestataire.
     */
    public static void save(Prestataire prestataire) {
        MongoConfig.getDatastore().save(prestataire);
    }
}
