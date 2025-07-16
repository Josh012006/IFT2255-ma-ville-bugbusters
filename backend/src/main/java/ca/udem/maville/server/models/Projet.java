package ca.udem.maville.server.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.PrePersist;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Représente un projet de travaux publics dans le système.
 * Contient toutes les données relatives au projet, y compris la localisation,
 * le type de travaux, le statut, l'entreprise associée, la fiche problème liée,
 * ainsi que les listes des rues affectées et des abonnés.
 *
 * Cette classe est mappée à la collection "projets" dans MongoDB
 * et sérialisée en JSON via Jackson.
 */
@Entity("projets")
public class Projet {

    /**
     * Identifiant unique du projet.
     */
    @Id
    private ObjectId id;

    /**
     * Liste des rues affectées par ce projet.
     */
    private List<String> ruesAffectees;

    /**
     * Liste des abonnés (utilisateurs intéressés par les mises à jour) pour ce projet.
     */
    private List<ObjectId> abonnes = new ArrayList<>();

    /**
     * Titre du projet.
     */
    private String titreProjet;

    /**
     * Description du projet.
     */
    private String description;

    /**
     * Type de travaux impliqués dans le projet.
     */
    private String typeTravaux;

    /**
     * Statut actuel du projet (ex. : en cours, terminé).
     */
    private String statut;

    /**
     * Date de début du projet, sérialisée au format ISO 8601.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date dateDebut;

    /**
     * Date de fin du projet, sérialisée au format ISO 8601.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date dateFin;

    /**
     * Référence à la fiche problème associée.
     */
    private ObjectId ficheProbleme;

    /**
     * Référence à l'entreprise (prestataire) réalisant le projet.
     */
    private ObjectId prestataire;

    /**
     * Nom de l'entreprise prestataire.
     */
    private String nomPrestataire;

    /**
     * Quartier où le projet se déroule.
     */
    private String quartier;

    /**
     * Coût estimé du projet.
     */
    private double cout;

    /**
     * Niveau de priorité du projet.
     */
    private String priorite;

    /**
     * Nombre de rapports (retours ou problèmes) liés à ce projet.
     */
    private int nbRapports;

    /**
     * Date de création du projet, sérialisée au format ISO 8601.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date createdAt;

    /**
     * Date de dernière modification du projet, sérialisée au format ISO 8601.
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
    public Projet() {}

    /**
     * Constructeur permettant d'initialiser un projet avec ses champs principaux.
     *
     * @param id Identifiant unique.
     * @param titreProjet Titre du projet.
     * @param ruesAffectees Liste des rues affectées.
     * @param description Description du projet.
     * @param typeTravaux Type de travaux.
     * @param dateDebut Date de début.
     * @param dateFin Date de fin.
     * @param ficheProbleme Référence à la fiche problème associée.
     * @param prestataire Référence au prestataire.
     * @param nomPrestataire Nom du prestataire.
     * @param quartier Quartier du projet.
     * @param cout Coût estimé.
     * @param priorite Niveau de priorité.
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
    public int getNbRapports() { return this.nbRapports; }
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
