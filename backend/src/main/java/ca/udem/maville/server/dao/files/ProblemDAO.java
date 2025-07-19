package ca.udem.maville.server.dao.files;

import ca.udem.maville.server.dao.config.MongoConfig;
import ca.udem.maville.server.models.FicheProbleme;
import dev.morphia.query.experimental.filters.Filters;
import org.bson.types.ObjectId;

import java.util.List;

public class ProblemDAO {

    // Todo: Une méthode findAll() qui renvoie une liste de tous les problèmes présents dans la base de données.
     /**
     * Récupère tous les problèmes routiers enregistrés
     */
    public List<FicheProbleme> findAll(){
        return MongoConfig.getDatastore()
                .find(FicheProbleme.class)
                .iterator()
                .toList();
    }

    // Todo: Une méthode findById(ObjectId id) qui renvoie un objet de type model FicheProbleme et qui représente
    //  le problème de la base de données ayant l'id donné.
    /**
     * Récupère un problème spécifique par son identifiant
     */
    public FicheProbleme findById(ObjectId id){
        return MongoConfig.getDatastore()
                .find(FicheProbleme.class)
                .filter(Filters.eq("_id", id))
                .first();
    }

    // Todo: Une méthode save(FicheProbleme problem) qui enregistre la fiche problème dans la base de données.
    /**
     * Enregistre ou met à jour une fiche problème
     */
    public void save(FicheProbleme problem){
        MongoConfig.getDatastore().save(problem);
    }


}
