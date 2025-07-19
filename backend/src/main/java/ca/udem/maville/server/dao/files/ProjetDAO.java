package ca.udem.maville.server.dao.files;

import ca.udem.maville.server.dao.config.MongoConfig;
import ca.udem.maville.server.models.Projet;
import dev.morphia.query.filters.Filters;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * La classe ProjetDAO qui représente la couche d'interactions
 * à la base de données pour tout ce qui concerne les projets.
 * Elle offre des méthodes statiques utilisées dans {@link ca.udem.maville.server.controllers.ProjetController}
 * et qui agissent sur les documents stockés tout en suivant le model
 * {@link ca.udem.maville.server.models.Projet}
 */
public class ProjetDAO {

    /**
     * Récupère un projet par son id.
     * @param id qui est l'id du projet recherché.
     * @return le projet trouvé.
     */
    public static Projet findById(ObjectId id){
        return MongoConfig.getDatastore()
                .find(Projet.class)
                .filter(Filters.eq("_id", id))
                .first();
    }

    /**
     * Récupère tous les projets de travaux présents dans la base de données.
     * @return la liste des projets trouvés.
     */
    public static List<Projet> findAll(){
        return MongoConfig.getDatastore()
                .find(Projet.class)
                .iterator()
                .toList();
    }


    /**
     * Récupère les projets appartenant à un prestataire donné.
     * @param userId qui représente l'id du prestataire.
     * @return la liste des projets concernés.
     */
    public static List<Projet> findPrestataireProjet(ObjectId userId){
        return MongoConfig.getDatastore()
                .find(Projet.class)
                .filter(Filters.eq("prestataire", userId))
                .iterator()
                .toList();
    }

    /**
     * Sauvegarde ou met à jour un projet.
     * @param projet qui représente le projet à enregistrer.
     */
    public static void save (Projet projet){
        MongoConfig.getDatastore().save(projet);
    }

}
