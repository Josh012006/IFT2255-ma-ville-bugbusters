package ca.udem.maville.client.users;
import java.util.Date;

public class Notification {
    private String description;
    private Date dateNotification;
    private TypeNotification typeNotification;
    private TypeTravaux typeTravaux;
    private Quartier quartier;

    public Notification(String description, TypeNotification typeNotification,
                        TypeTravaux typeTravaux, Quartier quartier) {
        this.description = description;
        this.dateNotification = new Date();
        this.typeNotification = typeNotification;
        this.typeTravaux = typeTravaux;
        this.quartier = quartier;
    }

    // Getters
    public String getDescription() { return description; }

    public Date getDateNotification() { return dateNotification; }

    public TypeNotification getTypeNotification() { return typeNotification; }

    public TypeTravaux getTypeTravaux() { return typeTravaux; }

    public Quartier getQuartier() { return quartier; }
}
