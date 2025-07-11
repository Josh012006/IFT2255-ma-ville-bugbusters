package ca.udem.maville.server.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents an application (Candidature) for a construction or renovation project.
 * Contains information about the service provider, project details, schedule,
 * financial estimates, and affected streets.
 *
 * This class is used for storage in MongoDB via Morphia
 * and for JSON serialization/deserialization via Jackson.
 */
@Entity("candidatures")
public class Candidature {

    /**
     * Unique MongoDB identifier.
     */
    @Id
    private ObjectId id;

    /**
     * Current status of the application (e.g., "pending", "accepted", "rejected").
     */
    private String statut;

    /**
     * Identifier of the service provider associated with this application.
     */
    private ObjectId prestataire;

    /**
     * Name of the service provider.
     */
    private String nomPrestataire;

    /**
     * Identifier of the related problem report.
     */
    private ObjectId ficheProbleme;

    /**
     * Business number of the service provider.
     */
    private String numeroEntreprise;

    /**
     * Title of the project for which the application is submitted.
     */
    private String titreProjet;

    /**
     * Detailed description of the project or proposed work.
     */
    private String description;

    /**
     * Type of work to be performed (e.g., "repair", "construction").
     */
    private String typeTravaux;

    /**
     * Scheduled start date of the project.
     * Serialized as ISO 8601 string in JSON.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date dateDebut;

    /**
     * Scheduled end date of the project.
     * Serialized as ISO 8601 string in JSON.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date dateFin;

    /**
     * List of streets affected by the project.
     */
    private List<String> ruesAffectees;

    /**
     * Estimated cost of the project.
     */
    private double coutEstime;

    /**
     * Date when the application was created.
     * Serialized as ISO 8601 string in JSON.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date createdAt;

    /**
     * Date when the application was last updated.
     * Serialized as ISO 8601 string in JSON.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date updatedAt;

    /**
     * No-argument constructor required by Morphia.
     */
    public Candidature() {}

    /**
     * Main constructor to initialize an application.
     *
     * @param id MongoDB identifier of the application.
     * @param ruesAffectees List of affected streets.
     * @param numeroEntreprise Business number of the service provider.
     * @param prestataire Identifier of the service provider.
     * @param nomPrestataire Name of the service provider.
     * @param ficheProbleme Identifier of the related problem report.
     * @param titreProjet Project title.
     * @param description Detailed description of the project.
     * @param typeTravaux Type of work to be performed.
     * @param dateDebut Scheduled start date.
     * @param dateFin Scheduled end date.
     * @param coutEstime Estimated project cost.
     */
    public Candidature(ObjectId id, ArrayList<String> ruesAffectees, String numeroEntreprise, ObjectId prestataire,
                       String nomPrestataire, ObjectId ficheProbleme, String titreProjet, String description,
                       String typeTravaux, Date dateDebut, Date dateFin, double coutEstime) {

        this.statut = "en attente";
        this.prestataire = prestataire;
        this.nomPrestataire = nomPrestataire;
        this.ficheProbleme = ficheProbleme;
        this.numeroEntreprise = numeroEntreprise;
        this.titreProjet = titreProjet;
        this.id = id;
        this.description = description;
        this.typeTravaux = typeTravaux;
        this.ruesAffectees = ruesAffectees;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.coutEstime = coutEstime;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }


    // Getters
    public ObjectId getId() {
        return this.id;
    }
    public String getStatut() {
        return this.statut;
    }
    public ObjectId getPrestataire() {
        return this.prestataire;
    }
    public String getNomPrestataire() {
        return this.nomPrestataire;
    }
    public ObjectId getFicheProbleme() {
        return this.ficheProbleme;
    }
    public String getNumeroEntreprise() {
        return this.numeroEntreprise;
    }
    public String getTitreProjet() {
        return this.titreProjet;
    }
    public String getDescription() {
        return this.description;
    }
    public String getTypeTravaux() {
        return this.typeTravaux;
    }
    public Date getDateDebut() {
        return this.dateDebut;
    }
    public Date getDateFin() {
        return this.dateFin;
    }
    public List<String> getRuesAffectees() {
        return this.ruesAffectees;
    }
    public double getCoutEstime() {
        return this.coutEstime;
    }
    public Date getCreatedAt() {
        return this.createdAt;
    }
    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    // Setters
    public void setStatut(String statut) {
        this.statut = statut;
    }
    public void setPrestataire(ObjectId prestataire) {
        this.prestataire = prestataire;
    }
    public void setNomPrestataire(String nomPrestataire) {
        this.nomPrestataire = nomPrestataire;
    }
    public void setFicheProbleme(ObjectId ficheProbleme) {
        this.ficheProbleme = ficheProbleme;
    }
    public void setNumeroEntreprise(String numeroEntreprise) {
        this.numeroEntreprise = numeroEntreprise;
    }
    public void setTitreProjet(String titreProjet) {
        this.titreProjet = titreProjet;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setTypeTravaux(String typeTravaux) {
        this.typeTravaux = typeTravaux;
    }
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }
    public void setRuesAffectees(List<String> ruesAffectees) {
        this.ruesAffectees = ruesAffectees;
    }
    public void setCoutEstime(double coutEstime) {
        this.coutEstime = coutEstime;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
