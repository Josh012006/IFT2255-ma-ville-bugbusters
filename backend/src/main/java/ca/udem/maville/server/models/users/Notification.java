package ca.udem.maville.server.models.users;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

/**
 * Représente une notification envoyée à un utilisateur.
 * Cette classe est liée à la collection "notifications" dans la base de données.
 */
@Entity("notifications")
public class Notification {

    @Id
    private ObjectId id;

    private ObjectId user;
    private String message;
    private String statut;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date updatedAt;

    /**
     * Constructeur par défaut requis pour Morphia et Jackson.
     */
    public Notification() {}

    /**
     * Crée une nouvelle notification avec le message et l’utilisateur spécifiés.
     * Définit automatiquement les dates de création et de mise à jour.
     *
     * @param message Le message de la notification.
     * @param user L’ObjectId de l’utilisateur recevant la notification.
     */
    public Notification(String message, ObjectId user) {
        this.user = user;
        this.message = message;
        this.statut = "non lue";
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Getters
    public ObjectId getId() { return this.id; }
    public ObjectId getUser() { return this.user; }
    public String getMessage() { return this.message; }
    public Date getCreatedAt() { return this.createdAt; }
    public Date getUpdatedAt() { return this.updatedAt; }
    public String getStatut() { return this.statut; }

    // Setters
    public void setUser(ObjectId user) { this.user = user; }
    public void setMessage(String message) { this.message = message; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    public void setStatut(String statut) { this.statut = statut; }

}
