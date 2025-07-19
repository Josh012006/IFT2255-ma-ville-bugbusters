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

    // Todo: Une méthode findAll() qui renvoie une liste de tous les projets de travaux situés dans la base de données.
     /**
     * Récupère tous les projets
     */
    public List<Projet> findAll(){
        return MongoConfig.getDatastore()
                .find(Project.class)
                .iterator()
                .toList();
    }


    // Todo: Une méthode findPrestataireProjet(ObjectId userId) qui renvoie une liste de tous les projets appartenant
    //  au prestataire dont l'id est userId. C'est-à-dire que leur champ prestataire porte la valeur userId.
    /**
     * Récupère les projets appartenant à un prestataire donné
     */
    public List<Projet> findPrestataireProjet(ObjectId userId){
        return MongoConfig.getDatastore()
                .find(Project.class)
                .filter(Filters.eq("prestataire.id", userId))
                .iterator()
                .toList();
    }

    // Todo: Une méthode save(Projet projet) qui enregistre un projet dans la base de données.
    /**
     * Sauvegarde ou met à jour un projet
     */
    public void save (Projet projet){
        MongoConfig.getDatastore().save(projet);
    }

}
