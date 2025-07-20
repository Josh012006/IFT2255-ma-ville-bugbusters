package ca.udem.maville.server.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.PrePersist;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Représente une candidature pour un projet de construction ou de rénovation.
 * Contient des informations sur le prestataire, les détails du projet, le calendrier,
 * les estimations financières et les rues concernées.
 *
 * Cette classe est utilisée pour le stockage dans MongoDB via Morphia
 * et pour la sérialisation/désérialisation JSON via Jackson.
 */
@Entity("candidatures")
public class Candidature {

    /**
     * Identifiant unique MongoDB.
     */
    @Id
    private ObjectId id;

    /**
     * Statut actuel de la candidature : "en attente" | "vue" | "acceptée" | "refusée".
     */
    private String statut = "en attente";

    /**
     * Identifiant du prestataire associé à cette candidature.
     */
    private ObjectId prestataire;

    /**
     * Nom du prestataire.
     */
    private String nomPrestataire;

    /**
     * Identifiant du rapport de problème associé.
     */
    private ObjectId ficheProbleme;

    /**
     * Numéro d'entreprise du prestataire.
     */
    private String numeroEntreprise;

    /**
     * Titre du projet pour lequel la candidature est soumise.
     */
    private String titreProjet;

    /**
     * Description détaillée du projet ou des travaux proposés.
     */
    private String description;

    /**
     * Type de travaux à réaliser (ex. : "réparation", "construction").
     */
    private String typeTravaux;

    /**
     * Date de début prévue du projet.
     * Sérialisée au format ISO 8601 dans le JSON.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date dateDebut;

    /**
     * Date de fin prévue du projet.
     * Sérialisée au format ISO 8601 dans le JSON.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date dateFin;

    /**
     * Liste des rues affectées par le projet.
     */
    private List<String> ruesAffectees;

    /**
     * Coût estimé du projet.
     */
    private double coutEstime;

    /**
     * Date de création de la candidature.
     * Sérialisée au format ISO 8601 dans le JSON.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date createdAt;

    /**
     * Date de dernière mise à jour de la candidature.
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
     * Constructeur sans argument requis par Morphia.
     */
    public Candidature() {}

    /**
     * Constructeur principal pour initialiser une candidature.
     *
     * @param id Identifiant MongoDB de la candidature.
     * @param ruesAffectees Liste des rues affectées.
     * @param numeroEntreprise Numéro d'entreprise du prestataire.
     * @param prestataire Identifiant du prestataire.
     * @param nomPrestataire Nom du prestataire.
     * @param ficheProbleme Identifiant du rapport de problème associé.
     * @param titreProjet Titre du projet.
     * @param description Description détaillée du projet.
     * @param typeTravaux Type de travaux à réaliser.
     * @param dateDebut Date de début prévue.
     * @param dateFin Date de fin prévue.
     * @param coutEstime Coût estimé du projet.
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
    public ObjectId getId() { return this.id; }
    public String getStatut() { return this.statut; }
    public ObjectId getPrestataire() { return this.prestataire; }
    public String getNomPrestataire() { return this.nomPrestataire; }
    public ObjectId getFicheProbleme() { return this.ficheProbleme; }
    public String getNumeroEntreprise() { return this.numeroEntreprise; }
    public String getTitreProjet() { return this.titreProjet; }
    public String getDescription() { return this.description; }
    public String getTypeTravaux() { return this.typeTravaux; }
    public Date getDateDebut() { return this.dateDebut; }
    public Date getDateFin() { return this.dateFin; }
    public List<String> getRuesAffectees() { return this.ruesAffectees; }
    public double getCoutEstime() { return this.coutEstime; }
    public Date getCreatedAt() { return this.createdAt; }
    public Date getUpdatedAt() { return this.updatedAt; }

    // Setters
    public void setStatut(String statut) { this.statut = statut; }
    public void setPrestataire(ObjectId prestataire) { this.prestataire = prestataire; }
    public void setNomPrestataire(String nomPrestataire) { this.nomPrestataire = nomPrestataire; }
    public void setFicheProbleme(ObjectId ficheProbleme) { this.ficheProbleme = ficheProbleme; }
    public void setNumeroEntreprise(String numeroEntreprise) { this.numeroEntreprise = numeroEntreprise; }
    public void setTitreProjet(String titreProjet) { this.titreProjet = titreProjet; }
    public void setDescription(String description) { this.description = description; }
    public void setTypeTravaux(String typeTravaux) { this.typeTravaux = typeTravaux; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }
    public void setRuesAffectees(List<String> ruesAffectees) { this.ruesAffectees = ruesAffectees; }
    public void setCoutEstime(double coutEstime) { this.coutEstime = coutEstime; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
