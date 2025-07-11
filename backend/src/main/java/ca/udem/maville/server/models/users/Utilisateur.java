package ca.udem.maville.server.models.users;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class representing a user in the system.
 * Contains common user properties such as id, name, email address,
 * and a list of neighborhood subscriptions.
 *
 * This class is stored in the "utilisateurs" MongoDB collection,
 * and supports discriminator for inheritance mapping.
 */
@Entity(value = "utilisateurs", useDiscriminator = true)
public abstract class Utilisateur {

    /**
     * Unique MongoDB identifier for the user.
     */
    @Id
    protected ObjectId id;

    /**
     * Name of the user.
     */
    protected String nom;

    /**
     * Email address of the user.
     */
    protected String adresseCourriel;

    /**
     * List of neighborhood subscriptions for this user.
     */
    protected List<String> abonnementsQuartier = new ArrayList<>();

    /**
     * No-argument constructor required by Morphia.
     */
    public Utilisateur() {}

    /**
     * Constructor to initialize a user with id, name, email,
     * and list of subscribed neighborhoods.
     *
     * @param id MongoDB identifier.
     * @param nom User's name.
     * @param adresseCourriel User's email address.
     * @param abonnementsQuartier List of neighborhood subscriptions.
     */
    public Utilisateur(ObjectId id, String nom, String adresseCourriel, ArrayList<String> abonnementsQuartier) {
        this.id = id;
        this.nom = nom;
        this.adresseCourriel = adresseCourriel;
        this.abonnementsQuartier = abonnementsQuartier;
    }

    // Getters
    public ObjectId getId() { return this.id; }
    public String getNom() { return this.nom; }
    public String getAdresseCourriel() { return this.adresseCourriel; }
    public List<String> getAbonnementsQuartier() { return this.abonnementsQuartier; }

    // Setters
    public void setNom(String nom) { this.nom = nom; }
    public void setAbonnementsQuartier(List<String> abonnementsQuartier) { this.abonnementsQuartier = abonnementsQuartier; }
    public void setAdresseCourriel(String adresseCourriel) { this.adresseCourriel = adresseCourriel; }
}
