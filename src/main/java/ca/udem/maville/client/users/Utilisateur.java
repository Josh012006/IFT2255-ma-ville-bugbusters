package ca.udem.maville.client.users;
import java.util.*;
public abstract class Utilisateur implements Notifiable {

    protected String nom;
    protected String adresseCourriel;
    protected List<Notification> notifications;

    public Utilisateur(String nom, String adresseCourriel) {
        this.nom = nom;
        this.adresseCourriel = adresseCourriel;
        this.notifications = new ArrayList<>();
    }

    // Interface Notifiable
    @Override
    public void recevoirNotification(Notification notif) {
        notifications.add(notif);
    }

    @Override
    public List<Notification> consulterNotifications() {
        return notifications;
    }

    // Getters
    public String getNom() { return nom; }
    public String getAdresseCourriel() { return adresseCourriel; }
}
