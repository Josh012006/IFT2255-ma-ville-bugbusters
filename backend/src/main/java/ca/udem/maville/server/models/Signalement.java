package ca.udem.maville.server.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.morphia.annotations.Entity;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Represents a Report (Signalement) submitted by a resident regarding an issue.
 * Contains information about the problem type, location, description,
 * status, related resident, and timestamps.
 *
 * This class is stored in the "signalements" MongoDB collection
 * and uses Jackson for JSON serialization.
 */
@Entity("signalements")
public class Signalement {

    /**
     * Unique identifier of the report.
     */
    private ObjectId id;

    /**
     * Neighborhood where the problem is reported.
     */
    private String quartier;

    /**
     * Type of problem reported.
     */
    private String typeProbleme;

    /**
     * Specific location of the problem.
     */
    private String localisation;

    /**
     * Detailed description of the problem.
     */
    private String description;

    /**
     * Current status of the report (e.g., pending, resolved).
     */
    private String statut;

    /**
     * Identifier of the resident who submitted the report.
     */
    private ObjectId resident;

    /**
     * Date when the report was created.
     * Serialized as ISO 8601 string in JSON.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date createdAt;

    /**
     * Date when the report was last updated.
     * Serialized as ISO 8601 string in JSON.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date updatedAt;

    /**
     * No-argument constructor required by Morphia.
     */
    public Signalement() {}

    /**
     * Constructor to initialize a report with main fields.
     *
     * @param id Unique identifier.
     * @param typeProbleme Type of problem.
     * @param localisation Specific location.
     * @param description Detailed description.
     * @param resident Resident who submitted the report.
     * @param quartier Neighborhood of the problem.
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
