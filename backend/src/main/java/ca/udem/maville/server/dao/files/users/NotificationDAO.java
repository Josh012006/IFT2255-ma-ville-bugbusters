package ca.udem.maville.server.dao.files.users;

import ca.udem.maville.server.dao.config.MongoConfig;
import ca.udem.maville.server.models.users.Notification;
import dev.morphia.query.experimental.filters.Filters;
import org.bson.types.ObjectId;

import java.util.List;

public class NotificationDAO {

    // Todo: Une méthode findUserNotifications(ObjectId userId) qui prend l'id d'un utilisateur et
    //  renvoie une liste contenant toutes ses notifications.
    /**
     * Récupère toutes les notifications destinées à un utilisateur
     */
    public List<Notification> findUserNotifications(ObjectId userId){
        return MongoConfig.getDatastore()
                .find(Notification.class)
                .filter(Filters.eq("destinataire", userId))
                .iterator()
                .toList();
    }

    // Todo: Une méthode save(Notification notification) qui prend un élément de type model Notification
    //  et le sauvegarde dans la base de données. Elle ne renvoie rien.

/**
     * Sauvegarde ou met à jour une notification
     */
    public void save(Notification notication){
        MongoConfig.getDatastore().save(notification);
    }

    // Todo: Une méthode findById(ObjectId id) qui renvoie un élément de type model Notification
    //  qui représente la notification avec un tel id dans la base de données.

    /**
     * Récupère une notification par son ID
     */
    public Notification findById(ObjectId id){
        return MongoConfig.getDatastore()
                .find(Notification.class)
                .filter(Filters.eq("_id", id))
                .first();

    }

}
