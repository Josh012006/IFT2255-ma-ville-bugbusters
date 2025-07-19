package ca.udem.maville.server.dao.files;

import ca.udem.maville.server.dao.config.MongoConfig;
import ca.udem.maville.server.models.Projet;
import dev.morphia.query.experimental.filters.Filters;
import org.bson.types.ObjectId;

import java.util.List;


public class ProjetDAO {

    /**
     * Récupère un projet par son ID
     */
    public Projet findById(ObjectId id){
        return MongoConfig.getDatastore()
                .find(Projet.class)
                .filter(Filters.eq("_id", id))
                .first();
    }

     /**
     * Récupère tous les projets
     */
    public List<Projet> findAll(){
        return MongoConfig.getDatastore()
                .find(Project.class)
                .iterator()
                .toList();
    }


    /**
     * Récupère les projets appartenant à un prestataire donné
     */
    public List<Projet> findPrestataireProjet(ObjectId userId){
        return MongoConfig.getDatastore()
                .find(Project.class)
                .filter(Filters.eq("prestataire", userId))
                .iterator()
                .toList();
    }

    /**
     * Sauvegarde ou met à jour un projet
     */
    public void save (Projet projet){
        MongoConfig.getDatastore().save(projet);
    }

}
