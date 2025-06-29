package ca.udem.maville.client.users;
import java.util.List;

public interface Notifiable {
    void recevoirNotification(Notification notif);
    List<Notification> consulterNotifications();
}
    

