package ca.udem.maville.server.dao.files;

import ca.udem.maville.server.dao.config.MongoConfig;
import ca.udem.maville.server.models.FicheProbleme;
import dev.morphia.query.filters.Filters;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * La classe ProblemDAO qui représente la couche d'interactions
 * à la base de données pour tout ce qui concerne les fiches problèmes.
 * Elle offre des méthodes statiques utilisées dans {@link ca.udem.maville.server.controllers.ProblemController}
 * et qui agissent sur les documents stockés tout en suivant le model
 * {@link ca.udem.maville.server.models.FicheProbleme}
 */
public class ProblemDAO {


    /**
     * Récupère tous les problèmes routiers enregistrés.
     * @return une liste de tous les problèmes.
     */
    public static List<FicheProbleme> findAll(){
        return MongoConfig.getDatastore()
                .find(FicheProbleme.class)
                .iterator()
                .toList();
    }

    /**
     * Récupère un problème spécifique par son id.
     * @param id représente l'id de la fiche problème recherchée.
     * @return la fiche problème trouvée.
     */
    public FicheProbleme findById(ObjectId id){
        return MongoConfig.getDatastore()
                .find(FicheProbleme.class)
                .filter(Filters.eq("_id", id))
                .first();
    }

    /**
     * Enregistre ou met à jour une fiche problème.
     * @param problem qui représente la fiche problème à enregistrer.
     */
    public static void save(FicheProbleme problem){
        MongoConfig.getDatastore().save(problem);
    }


}
