package ca.udem.maville.client.users;
import java.util.Date;

public class Notification {
    private String id;
    private String message;
    private Date dateNotification;
   

    public Notification(String message, String id) {
        this.id = id;
        this.message = message;
        this.dateNotification = new Date();
    }

    // Getters

    public String getID() { return id;}

    public String getMessage() { return message; }

    public Date getDateNotification() { return dateNotification; }

   
}
