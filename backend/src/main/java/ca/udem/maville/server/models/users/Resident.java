package ca.udem.maville.server.models.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.morphia.annotations.Entity;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Représente un utilisateur résident dans le système.
 * Hérite de la classe Utilisateur et ajoute des attributs supplémentaires
 * propres aux résidents, tels que l'adresse, le code postal, le quartier
 * et la date de naissance.
 *
 * Cette classe est stockée dans la collection "utilisateurs" de MongoDB
 * en utilisant le mécanisme de discriminateur de Morphia.
 */
@Entity(discriminator = "residents")
public class Resident extends Utilisateur {

    /**
     * Adresse du résident.
     */
    private String adresse;

    /**
     * Code postal de l'adresse du résident.
     */
    private String codePostal;

    /**
     * Quartier où habite le résident.
     */
    private String quartier;

    /**
     * La liste des rues auxquels le résident s'est abonné.
     */
    private List<String> abonnementsRue = new ArrayList<>();

    /**
     * Date de naissance du résident.
     * Sérialisée sous forme de chaîne ISO 8601 dans le JSON.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date dateNaissance;

    /**
     * Constructeur sans argument requis par Morphia.
     */
    public Resident() {}

    /**
     * Constructeur permettant d'initialiser un résident avec tous ses champs.
     *
     * @param id Identifiant MongoDB.
     * @param nom Nom du résident.
     * @param adresseCourriel Adresse courriel du résident.
     * @param abonnementsQuartier Liste des quartiers abonnés.
     * @param abonnementsRue Liste des rues auxquels il s'est abonné.
     * @param adresse Adresse du résident.
     * @param codePostal Code postal.
     * @param quartier Quartier de résidence.
     * @param dateNaissance Date de naissance.
     */
    public Resident(ObjectId id, String nom, String adresseCourriel, ArrayList<String> abonnementsQuartier,
                    ArrayList<String> abonnementsRue, String adresse, String codePostal, String quartier, Date dateNaissance) {
        super(id, nom, adresseCourriel, abonnementsQuartier);
        this.abonnementsRue = abonnementsRue;
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
    public List<String> getAbonnementsRue() { return this.abonnementsRue; }

    // Setters
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public void setQuartier(String quartier) { this.quartier = quartier; }
    public void setDateNaissance(Date dateNaissance) { this.dateNaissance = dateNaissance; }
    public void setAbonnementsRue(List<String> abonnementsRue) { this.abonnementsRue = abonnementsRue; }
}
