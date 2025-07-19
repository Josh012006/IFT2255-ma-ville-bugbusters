package ca.udem.maville.server.dao.files.users;

import ca.udem.maville.server.dao.config.MongoConfig;
import ca.udem.maville.server.models.users.Prestataire;
import dev.morphia.query.experimental.filters.Filters;
import dev.morphia.query.experimental.filters.Filter;
import org.bson.types.ObjectId;

import java.util.List;

public class PrestataireDAO {

    // Todo: Une méthode findById(ObjectId id) qui renvoie un élément de type model Prestataire
    //  qui représente le prestataire avec un tel id dans la base de données.
    /**
     * Récupère un prestataire par son ID
     */
    public Prestataire findById(ObjectId id){
        return MongoConfig.getDatastore()
                .find(Prestataire.class)
                .filter(Filters.eq("_id", id))
                .first();
    }

    // Todo: Une méthode findAll() qui renvoie une liste de tous les prestataires dans la base de
    //  données.
    /**
     * Récupère tous les prestataires
     */
    public List<Prestataire> findAll() {
        return MongoConfig.getDatastore()
                .find(Prestataire.class)
                .iterator()
                .toList();
    }

    // Todo: Une méthode findToNotify(String quartier, String typeTravail) qui renvoie tous les prestataires
    //  de la base de données qui ont le quartier présent dans leur liste de quartiers couverts ou bien le typeTravail
    //  présent dans leur liste de types de travaux couverts.

    /**
     * Récupère tous les prestataires à notifier selon le quartier ou le type de travaux
     */
    public List<Prestataire> findToNotify(String quartier, String typeTravail) {
        return MongoConfig.getDatastore()
                .find(Prestataire.class)
                .filter(
                    Filters.or(
                        Filters.eq("quartiersCouverts", quartier),
                        Filters.eq("typesTravauxCouverts", typeTravail)
                    )
                )
                .iterator()
                .toList();
    }

    // Todo: Une méthode save(Prestataire prestataire) qui permet de sauvegarder un prestataire dans la base de données.

    /**
     * Sauvegarde ou met à jour un prestataire
     */
    public void save(Prestataire prestataire) {
        MongoConfig.getDatastore().save(prestataire);
    }
}
