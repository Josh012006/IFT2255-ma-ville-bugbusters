package ca.udem.maville.server.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Représente une fiche de problème dans le système.
 * Contient des détails sur le type de travaux nécessaires, la localisation,
 * le quartier, la description, la priorité, le statut, ainsi que les résidents
 * et signalements associés.
 *
 * Cette classe est utilisée pour le stockage dans MongoDB via Morphia
 * et pour la sérialisation JSON via Jackson.
 */
@Entity("problemes")
public class FicheProbleme {

    /**
     * Identifiant unique MongoDB.
     */
    @Id
    private ObjectId id;

    /**
     * Type de travaux requis (ex. : réparation, construction).
     */
    private String typeTravaux;

    /**
     * Localisation précise du problème.
     */
    private String localisation;

    /**
     * Quartier où se situe le problème.
     */
    private String quartier;

    /**
     * Description détaillée du problème.
     */
    private String description;

    /**
     * Niveau de priorité du problème (ex. : faible, moyen, élevé).
     */
    private String priorite;

    /**
     * Liste des identifiants des signalements associés.
     */
    private List<ObjectId> signalements = new ArrayList<>();

    /**
     * Statut actuel de la fiche de problème (ex. : en attente, résolu).
     */
    private String statut;

    /**
     * Liste des identifiants des résidents concernés par ce problème.
     */
    private List<ObjectId> residents = new ArrayList<>();

    /**
     * Date de création de la fiche de problème.
     * Sérialisée au format ISO 8601 dans le JSON.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date createdAt;

    /**
     * Date de dernière mise à jour de la fiche de problème.
     * Sérialisée au format ISO 8601 dans le JSON.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date updatedAt;

    /**
     * Constructeur sans argument requis par Morphia.
     */
    public FicheProbleme() {}

    /**
     * Constructeur permettant d'initialiser une fiche de problème avec les champs principaux.
     *
     * @param id Identifiant MongoDB.
     * @param typeTravaux Type de travaux requis.
     * @param localisation Localisation précise du problème.
     * @param description Description détaillée du problème.
     * @param priorite Niveau de priorité.
     * @param quartier Quartier concerné.
     */
    public FicheProbleme(ObjectId id, String typeTravaux, String localisation, String description, String priorite,
                         String quartier) {
        this.id = id;
        this.typeTravaux = typeTravaux;
        this.quartier = quartier;
        this.localisation = localisation;
        this.description = description;
        this.priorite = priorite;
        this.statut = "en attente";
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Getters
    public String getTypeTravaux() { return this.typeTravaux; }
    public ObjectId getId() { return this.id; }
    public String getQuartier() { return this.quartier; }
    public String getLocalisation() { return this.localisation; }
    public String getDescription() { return this.description; }
    public String getPriorite() { return this.priorite; }
    public String getStatut() { return this.statut; }
    public Date getCreatedAt() { return this.createdAt; }
    public Date getUpdatedAt() { return this.updatedAt; }
    public List<ObjectId> getResidents() { return this.residents; }
    public List<ObjectId> getSignalements() { return this.signalements; }

    // Setters
    public void setTypeTravaux(String typeTravaux) { this.typeTravaux = typeTravaux; }
    public void setQuartier(String quartier) { this.quartier = quartier; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }
    public void setDescription(String description) { this.description = description; }
    public void setPriorite(String priorite) { this.priorite = priorite; }
    public void setStatut(String statut) { this.statut = statut; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    public void setResidents(List<ObjectId> residents) { this.residents = residents; }
    public void setSignalements(List<ObjectId> signalements) { this.signalements = signalements; }

}
