package ca.udem.maville.server.models.users;

import dev.morphia.annotations.Entity;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Service Provider (Prestataire) user in the system.
 * Inherits from Utilisateur and includes additional attributes such as
 * business number, neighborhoods served, types of work offered,
 * and subscription types.
 *
 * This class uses Morphia's discriminator mechanism for MongoDB storage
 * under the "utilisateurs" collection.
 */
@Entity(discriminator = "prestataire")
public class Prestataire extends Utilisateur {

    /**
     * Business number of the service provider.
     */
    private String numeroEntreprise;

    /**
     * List of neighborhoods served by the service provider.
     */
    private List<String> quartiers;

    /**
     * List of types of work the service provider offers.
     */
    private List<String> typesTravaux;

    /**
     * List of subscription types for the service provider.
     */
    private List<String> abonnementsType = new ArrayList<>();

    /**
     * No-argument constructor required by Morphia.
     */
    public Prestataire() {}

    /**
     * Constructor to initialize a service provider with all fields.
     *
     * @param id MongoDB identifier.
     * @param nom Service provider's name.
     * @param adresseCourriel Email address.
     * @param abonnementsQuartier List of subscribed neighborhoods.
     * @param numeroEntreprise Business number.
     * @param quartiers Neighborhoods served.
     * @param typesTravaux Types of work offered.
     * @param abonnementsType Subscription types.
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
