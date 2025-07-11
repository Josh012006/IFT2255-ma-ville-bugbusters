package ca.udem.maville.server.models.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.morphia.annotations.Entity;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;

/**
 * Represents a Resident user in the system.
 * Inherits from Utilisateur and adds additional attributes specific to residents,
 * such as address, postal code, neighborhood, and date of birth.
 *
 * This class is stored in the "utilisateurs" collection in MongoDB
 * using Morphia's discriminator mechanism.
 */
@Entity(discriminator = "resident")
public class Resident extends Utilisateur {

    /**
     * Resident's street address.
     */
    private String adresse;

    /**
     * Postal code for the resident's address.
     */
    private String codePostal;

    /**
     * Neighborhood where the resident lives.
     */
    private String quartier;

    /**
     * Date of birth of the resident.
     * Serialized as ISO 8601 string in JSON.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date dateNaissance;

    /**
     * No-argument constructor required by Morphia.
     */
    public Resident() {}

    /**
     * Constructor to initialize a Resident with all fields.
     *
     * @param id MongoDB identifier.
     * @param nom Resident's name.
     * @param adresseCourriel Resident's email address.
     * @param abonnementsQuartier List of neighborhood subscriptions.
     * @param adresse Street address.
     * @param codePostal Postal code.
     * @param quartier Neighborhood.
     * @param dateNaissance Date of birth.
     */
    public Resident(ObjectId id, String nom, String adresseCourriel, ArrayList<String> abonnementsQuartier,
                    String adresse, String codePostal, String quartier, Date dateNaissance) {
        super(id, nom, adresseCourriel, abonnementsQuartier);
        this.adresse = adresse;
        this.codePostal = codePostal;
        this.quartier = quartier;
        this.dateNaissance = dateNaissance;
    }

    // Getters
    public String getAdresse() { return this.adresse; }
    public String getCodePostal() { return this.codePostal; }
    public String getQuartier() { return this.quartier; }
    public Date getDateNaissance() { return this.dateNaissance; }

    // Setters
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public void setQuartier(String quartier) { this.quartier = quartier; }
    public void setDateNaissance(Date dateNaissance) { this.dateNaissance = dateNaissance; }
}
