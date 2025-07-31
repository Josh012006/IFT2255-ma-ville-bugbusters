package ca.udem.maville.server.dao.files.users;

import ca.udem.maville.server.dao.config.MongoConfig;
import ca.udem.maville.server.models.users.Notification;
import dev.morphia.query.filters.Filters;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * La classe NotificationDAO qui représente la couche d'interactions
 * à la base de données pour tout ce qui concerne les notifications.
 * Elle offre des méthodes statiques utilisées dans {@link ca.udem.maville.server.controllers.users.NotificationController}
 * et qui agissent sur les documents stockés tout en suivant le model
 * {@link ca.udem.maville.server.models.users.Notification}
 */
public class NotificationDAO {

    /**
     * Récupère toutes les notifications destinées à un utilisateur.
     * @param userId il s'agit de l'id de l'utilisateur.
     * @return la liste des notifications trouvées.
     */
    public static List<Notification> findUserNotifications(ObjectId userId){
        return MongoConfig.getDatastore()
                .find(Notification.class)
                .filter(Filters.eq("user", userId))
                .iterator()
                .toList();
    }

    /**
     * Cette fonction permet de vérifier si un utilisateur a au moins une notification non lue.
     * @param userId qui représente l'id de l'utilisateur
     * @return un booléen qui dit s'il a au moins une notification.
     */
    public static boolean hasNotifications(ObjectId userId) {
        return MongoConfig.getDatastore().find(Notification.class)
                .filter(Filters.and(
                    Filters.eq("user", userId),
                    Filters.eq("statut", "non lue")
                ))
                .iterator()
                .hasNext();
    }


    /**
     * Sauvegarde ou met à jour une notification.
     * @param notification qui représente la notification.
     */
    public static void save(Notification notification){
        MongoConfig.getDatastore().save(notification);
    }



    /**
     * Récupère une notification par son id.
     * @param id qui est l'id de la notification à récupérer.
     * @return la notification trouvée.
     */
    public static Notification findById(ObjectId id){
        return MongoConfig.getDatastore()
                .find(Notification.class)
                .filter(Filters.eq("_id", id))
                .first();

    }

    public static void deleteByUrl(String url){
        List<Notification> found = MongoConfig.getDatastore()
                .find(Notification.class)
                .filter(Filters.eq("url", url))
                .iterator()
                .toList();
        if(!found.isEmpty()) {
            for(Notification notification : found){
                MongoConfig.getDatastore().delete(notification);
            }
        }
    }

}
