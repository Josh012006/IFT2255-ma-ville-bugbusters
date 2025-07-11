package ca.udem.maville.server.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a Problem Report (FicheProbleme) in the system.
 * Contains details about the type of work needed, location,
 * neighborhood, description, priority, status, and related residents and reports.
 *
 * This class is used for MongoDB storage with Morphia
 * and JSON serialization via Jackson.
 */
@Entity("problemes")
public class FicheProbleme {

    /**
     * Unique MongoDB identifier.
     */
    @Id
    private ObjectId id;

    /**
     * Type of work required (e.g., repair, construction).
     */
    private String typeTravaux;

    /**
     * Specific location of the problem.
     */
    private String localisation;

    /**
     * Neighborhood where the problem is located.
     */
    private String quartier;

    /**
     * Detailed description of the problem.
     */
    private String description;

    /**
     * Priority level of the problem (e.g., low, medium, high).
     */
    private String priorite;

    /**
     * List of ObjectIds referencing related reports (signalements).
     */
    private List<ObjectId> signalements = new ArrayList<>();

    /**
     * Current status of the problem report (e.g., pending, resolved).
     */
    private String statut;

    /**
     * List of ObjectIds referencing residents concerned by this problem.
     */
    private List<ObjectId> residents = new ArrayList<>();

    /**
     * Date when the problem report was created.
     * Serialized as ISO 8601 string in JSON.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date createdAt;

    /**
     * Date when the problem report was last updated.
     * Serialized as ISO 8601 string in JSON.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date updatedAt;

    /**
     * No-argument constructor required by Morphia.
     */
    public FicheProbleme() {}

    /**
     * Constructor to initialize a problem report with the main fields.
     *
     * @param id MongoDB identifier.
     * @param typeTravaux Type of work required.
     * @param localisation Specific location of the problem.
     * @param description Detailed description of the problem.
     * @param priorite Priority level.
     * @param quartier Neighborhood.
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

