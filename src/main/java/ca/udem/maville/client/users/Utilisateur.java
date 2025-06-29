package ca.udem.maville.client.users;
import java.util.*;

public abstract class Utilisateur  {

    protected String id ;
    protected String nom;
    protected String adresseCourriel;
    protected ArrayList<String> notifications; 

    public Utilisateur(String id, String nom, String adresseCourriel) {
        this.id = id;
        this.nom = nom;
        this.adresseCourriel = adresseCourriel;
        this.notifications = new ArrayList<>();
    }

   // recup aux backend
    public ArrayList<Notification> consulterNotifications() {
        return notifications;
    }

    // Getters
    public String getID() { return id;}
    public String getNom() { return nom; }
    public String getAdresseCourriel() { return adresseCourriel; }
}
