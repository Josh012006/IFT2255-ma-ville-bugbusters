package ca.udem.maville.server.models.users;

import dev.morphia.annotations.Entity;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un utilisateur de type Prestataire dans le système.
 * Hérite de la classe Utilisateur et inclut des attributs supplémentaires tels que
 * le numéro d'entreprise, les quartiers desservis, les types de travaux proposés
 * et les types d'abonnements.
 *
 * Cette classe utilise le mécanisme de discriminateur de Morphia pour le stockage
 * dans MongoDB sous la collection "utilisateurs".
 */
@Entity(discriminator = "prestataires")
public class Prestataire extends Utilisateur {

    /**
     * Numéro d'entreprise du prestataire.
     */
    private String numeroEntreprise;

    /**
     * Liste des quartiers desservis par le prestataire.
     */
    private List<String> quartiers;

    /**
     * Liste des types de travaux proposés par le prestataire.
     */
    private List<String> typesTravaux;

    /**
     * Liste des types d'abonnements du prestataire.
     */
    private List<String> abonnementsType = new ArrayList<>();

    /**
     * Constructeur sans arguments requis par Morphia.
     */
    public Prestataire() {}

    /**
     * Constructeur permettant d'initialiser un prestataire avec tous ses champs.
     *
     * @param id Identifiant MongoDB.
     * @param nom Nom du prestataire.
     * @param adresseCourriel Adresse courriel.
     * @param abonnementsQuartier Liste des quartiers abonnés.
     * @param numeroEntreprise Numéro d'entreprise.
     * @param quartiers Quartiers desservis.
     * @param typesTravaux Types de travaux proposés.
     * @param abonnementsType Types d'abonnements.
     */
    public Prestataire(ObjectId id, String nom, String adresseCourriel, ArrayList<String> abonnementsQuartier,
                       String numeroEntreprise, ArrayList<String> quartiers, ArrayList<String> typesTravaux,
                       ArrayList<String> abonnementsType) {
        super(id, nom, adresseCourriel, abonnementsQuartier);
        this.numeroEntreprise = numeroEntreprise;
        this.quartiers = quartiers;
        this.typesTravaux = typesTravaux;
        this.abonnementsType = abonnementsType;
    }

    // Getters
    public String getNumeroEntreprise() {
        return this.numeroEntreprise;
    }
    public List<String> getQuartiers() {
        return this.quartiers;
    }
    public List<String> getTypesTravaux() {
        return this.typesTravaux;
    }
    public List<String> getAbonnementsType() { return this.abonnementsType; }

    // Setters
    public void setNumeroEntreprise(String numeroEntreprise) { this.numeroEntreprise = numeroEntreprise; }
    public void setQuartiers(List<String> quartiers) { this.quartiers = quartiers; }
    public void setTypesTravaux(List<String> typesTravaux) { this.typesTravaux = typesTravaux; }
    public void setAbonnementsType(List<String> abonnementsType) { this.abonnementsType = abonnementsType; }

}
