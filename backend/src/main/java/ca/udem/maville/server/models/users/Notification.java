package ca.udem.maville.server.models.users;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.PrePersist;
import org.bson.types.ObjectId;

/**
 * Représente une notification envoyée à un utilisateur.
 * Cette classe est liée à la collection "notifications" dans la base de données.
 */
@Entity("notifications")
public class Notification {

    /**
     * Identifiant unique de la notification.
     */
    @Id
    private ObjectId id;

    /**
     * Identifiant MongoDB de l'utilisateur concerné.
     */
    private ObjectId user;
    /**
     * Le message de la notifiaction.
     */
    private String message;
    /**
     * Le statut de la notification : "lue" | "non lue".
     */
    private String statut = "non lue";
    /**
     * Une url au cas où il y a un besoin de redirection.
     * vers une autre page côté client.
     */
    private String url;

    /**
     * Date de création de la notification.
     * Sérialisée au format ISO 8601 dans le JSON.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date createdAt;

    /**
     * Date de dernière mise à jour de la notification.
     * Sérialisée au format ISO 8601 dans le JSON.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date updatedAt;

    /**
     * Un hook pour mettre createdAt et updatedAt à jour automatiquement.
     */
    @PrePersist
    public void prePersist() {
        Date now = new Date();
        if (createdAt == null) {
            createdAt = now; // Il est mise à jour une seule fois seulement
        }
        updatedAt = now; // Toujours mis à jour à chaque save
    }

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
     * @param url Url optionnel à inclure pour une redirection vers une page spécifique côté client.
     */
    public Notification(String message, ObjectId user, String url) {
        this.user = user;
        this.message = message;
        this.url = url;
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
    public String getUrl() { return this.url; }

    // Setters
    public void setUser(ObjectId user) { this.user = user; }
    public void setMessage(String message) { this.message = message; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    public void setStatut(String statut) { this.statut = statut; }
    public void setUrl(String url) { this.url = url; }

}
