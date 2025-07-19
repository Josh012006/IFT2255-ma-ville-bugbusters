package ca.udem.maville.server.dao.files.users;

import ca.udem.maville.server.dao.config.MongoConfig;
import ca.udem.maville.server.models.users.Notification;
import dev.morphia.query.experimental.filters.Filters;
import org.bson.types.ObjectId;

import java.util.List;

public class NotificationDAO {

    /**
     * Récupère toutes les notifications destinées à un utilisateur
     */
    public List<Notification> findUserNotifications(ObjectId userId){
        return MongoConfig.getDatastore()
                .find(Notification.class)
                .filter(Filters.eq("user", userId))
                .iterator()
                .toList();
    }


/**
     * Sauvegarde ou met à jour une notification
     */
    public void save(Notification notication){
        MongoConfig.getDatastore().save(notification);
    }



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
