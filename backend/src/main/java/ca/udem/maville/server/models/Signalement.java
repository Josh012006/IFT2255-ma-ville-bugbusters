package ca.udem.maville.server.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.PrePersist;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Représente un signalement soumis par un résident concernant un problème.
 * Contient des informations sur le type de problème, la localisation,
 * la description, le statut, le résident associé et les dates de création et de mise à jour.
 *
 * Cette classe est stockée dans la collection MongoDB "signalements"
 * et utilise Jackson pour la sérialisation JSON.
 */
@Entity("signalements")
public class Signalement {

    /**
     * Identifiant unique du signalement.
     */
    @Id
    private ObjectId id;

    /**
     * Quartier où le problème a été signalé.
     */
    private String quartier;

    /**
     * Type de problème signalé.
     */
    private String typeProbleme;

    /**
     * Localisation précise du problème.
     */
    private String localisation;

    /**
     * Description détaillée du problème.
     */
    private String description;

    /**
     * Statut actuel du signalement : "en attente" | "vu" | "traité".
     */
    private String statut = "en attente";

    /**
     * Identifiant du résident ayant soumis le signalement.
     */
    private ObjectId resident;

    /**
     * Date de création du signalement.
     * Sérialisée au format ISO 8601 en JSON.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date createdAt;

    /**
     * Date de dernière mise à jour du signalement.
     * Sérialisée au format ISO 8601 en JSON.
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
     * Constructeur sans argument requis par Morphia.
     */
    public Signalement() {}

    /**
     * Constructeur pour initialiser un signalement avec les champs principaux.
     *
     * @param id Identifiant unique.
     * @param typeProbleme Type de problème.
     * @param localisation Localisation précise.
     * @param description Description détaillée.
     * @param resident Résident ayant soumis le signalement.
     * @param quartier Quartier du problème.
     */
    public Signalement(ObjectId id, String typeProbleme, String localisation, String description, ObjectId resident,
                       String quartier) {
        this.id = id;
        this.typeProbleme = typeProbleme;
        this.quartier = quartier;
        this.localisation = localisation;
        this.description = description;
        this.resident = resident;
        this.statut = "en attente";
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Getters
    public ObjectId getId() {return this.id;}
    public ObjectId getResident() { return this.resident; }
    public String getQuartier() { return this.quartier; }
    public String getTypeProbleme() {
        return this.typeProbleme;
    }
    public String getLocalisation() {
        return this.localisation;
    }
    public String getDescription() {
        return this.description;
    }
    public String getStatut() {
        return this.statut;
    }
    public Date getCreatedAt() { return this.createdAt; }
    public Date getUpdatedAt() { return this.updatedAt; }

    // Setters
    public void setResident(ObjectId resident) { this.resident = resident; }
    public void setQuartier(String quartier) { this.quartier = quartier; }
    public void setTypeProbleme(String typeProbleme) { this.typeProbleme = typeProbleme; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }
    public void setDescription(String description) { this.description = description; }
    public void setStatut(String statut) {
        this.statut = statut;
    }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
