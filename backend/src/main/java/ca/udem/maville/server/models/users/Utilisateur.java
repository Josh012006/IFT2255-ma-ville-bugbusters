package ca.udem.maville.server.models.users;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe de base abstraite représentant un utilisateur dans le système.
 * Contient les propriétés communes aux utilisateurs telles que l'identifiant, le nom, l'adresse courriel
 * et la liste des abonnements aux quartiers.
 *
 * Cette classe est stockée dans la collection "utilisateurs" de MongoDB,
 * et utilise le discriminateur pour la gestion de l'héritage.
 */
@Entity(value = "utilisateurs", useDiscriminator = true)
public abstract class Utilisateur {

    /**
     * Identifiant unique MongoDB de l'utilisateur.
     */
    @Id
    protected ObjectId id;

    /**
     * Nom de l'utilisateur.
     */
    protected String nom;

    /**
     * Adresse courriel de l'utilisateur.
     */
    protected String adresseCourriel;

    /**
     * Liste des abonnements aux quartiers pour cet utilisateur.
     */
    protected List<String> abonnementsQuartier = new ArrayList<>();

    /**
     * Constructeur sans argument requis par Morphia.
     */
    public Utilisateur() {}

    /**
     * Constructeur permettant d'initialiser un utilisateur avec son identifiant, son nom, son courriel
     * et sa liste de quartiers abonnés.
     *
     * @param id Identifiant MongoDB.
     * @param nom Nom de l'utilisateur.
     * @param adresseCourriel Adresse courriel de l'utilisateur.
     * @param abonnementsQuartier Liste des quartiers abonnés.
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
