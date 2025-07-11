package ca.udem.maville.server.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Represents a public works project in the system.
 * Contains all the data about the project, including location, type of work, status,
 * associated company, related problem sheet, and lists of affected streets and subscribers.
 *
 * This class is mapped to the "projets" collection in MongoDB
 * and serialized as JSON via Jackson.
 */
@Entity("projets")
public class Projet {

    /**
     * Unique identifier of the project.
     */
    @Id
    private ObjectId id;

    /**
     * List of streets affected by this project.
     */
    private List<String> ruesAffectees;

    /**
     * List of subscribers (users interested in updates) for this project.
     */
    private List<ObjectId> abonnes = new ArrayList<>();

    /**
     * Title of the project.
     */
    private String titreProjet;

    /**
     * Description of the project.
     */
    private String description;

    /**
     * Type of work involved in the project.
     */
    private String typeTravaux;

    /**
     * Current status of the project (e.g., in progress, completed).
     */
    private String statut;

    /**
     * Start date of the project, serialized in ISO 8601 format.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date dateDebut;

    /**
     * End date of the project, serialized in ISO 8601 format.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date dateFin;

    /**
     * Reference to the associated problem sheet.
     */
    private ObjectId ficheProbleme;

    /**
     * Reference to the company (service provider) executing the project.
     */
    private ObjectId prestataire;

    /**
     * Name of the service provider company.
     */
    private String nomPrestataire;

    /**
     * Neighborhood where the project takes place.
     */
    private String quartier;

    /**
     * Estimated cost of the project.
     */
    private double cout;

    /**
     * Priority level of the project.
     */
    private String priorite;

    /**
     * Number of reports (feedback or issues) related to this project.
     */
    private int nbRapports;

    /**
     * Creation date of the project, serialized in ISO 8601 format.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date createdAt;

    /**
     * Last modification date of the project, serialized in ISO 8601 format.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date updatedAt;

    /**
     * No-argument constructor required by Morphia.
     */
    public Projet() {}

    /**
     * Constructor to initialize a project with its main fields.
     *
     * @param id Unique identifier.
     * @param titreProjet Title of the project.
     * @param ruesAffectees List of affected streets.
     * @param description Description of the project.
     * @param typeTravaux Type of work.
     * @param dateDebut Start date.
     * @param dateFin End date.
     * @param ficheProbleme Reference to the related problem sheet.
     * @param prestataire Reference to the service provider.
     * @param nomPrestataire Name of the service provider.
     * @param quartier Neighborhood of the project.
     * @param cout Estimated cost.
     * @param priorite Priority level.
     */
    public Projet(ObjectId id, String titreProjet, List<String> ruesAffectees, String description, String typeTravaux,
                  Date dateDebut, Date dateFin, ObjectId ficheProbleme, ObjectId prestataire, String nomPrestataire,
                  String quartier, double cout, String priorite) {
        this.id = id;
        this.ruesAffectees = ruesAffectees;
        this.abonnes = new ArrayList<>();
        this.titreProjet = titreProjet;
        this.description = description;
        this.typeTravaux = typeTravaux;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.ficheProbleme = ficheProbleme;
        this.quartier = quartier;
        this.cout = cout;
        this.prestataire = prestataire;
        this.nomPrestataire = nomPrestataire;
        this.statut = "en cours";
        this.priorite = priorite;
        this.nbRapports = 0;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

   

    // Getters
    public ObjectId getId() { return this.id; }
    public String getTitreProjet() { return this.titreProjet; }
    public String getNomPrestataire() { return this.nomPrestataire; }
    public double getCout() { return this.cout; }
    public List<ObjectId> getAbonnes() { return this.abonnes; }
    public ObjectId getPrestataire() { return this.prestataire; }
    public List<String> getRuesAffectees() { return this.ruesAffectees; }
    public String getQuartier() { return this.quartier; }
    public String getDescription() { return this.description; }
    public String getTypeTravaux() { return this.typeTravaux; }
    public String getStatut() { return this.statut; }
    public Date getDateDebut() { return this.dateDebut; }
    public Date getDateFin() { return this.dateFin; }
    public ObjectId getFicheProbleme() { return this.ficheProbleme; }
    public String getPriorite() { return this.priorite; }
    public int getNbRapports() {
        return this.nbRapports;
    }
    public Date getCreatedAt() { return this.createdAt; }
    public Date getUpdatedAt() { return this.updatedAt; }

    // Setters
    public void setTitreProjet(String titreProjet) { this.titreProjet = titreProjet; }
    public void setNomPrestataire(String nomPrestataire) { this.nomPrestataire = nomPrestataire; }
    public void setCout(double cout) { this.cout = cout; }
    public void setAbonnes(List<ObjectId> abonnes) { this.abonnes = abonnes; }
    public void setPrestataire(ObjectId prestataire) { this.prestataire = prestataire; }
    public void setRuesAffectees(List<String> ruesAffectees) { this.ruesAffectees = ruesAffectees; }
    public void setQuartier(String quartier) { this.quartier = quartier; }
    public void setDescription(String description) { this.description = description; }
    public void setTypeTravaux(String typeTravaux) { this.typeTravaux = typeTravaux; }
    public void setStatut(String statut) { this.statut = statut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }
    public void setFicheProbleme(ObjectId ficheProbleme) { this.ficheProbleme = ficheProbleme; }
    public void setPriorite(String priorite) { this.priorite = priorite; }
    public void setNbRapports(int nbRapports) { this.nbRapports = nbRapports; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

}
