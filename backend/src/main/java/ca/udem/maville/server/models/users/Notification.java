package ca.udem.maville.server.models.users;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

/**
 * Represents a notification sent to a user.
 * This class is mapped to the "notifications" collection in the database.
 */
@Entity("notifications")
public class Notification {

    @Id
    private ObjectId id;

    private ObjectId user;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date updatedAt;

    /**
     * Default constructor required for Morphia and Jackson.
     */
    public Notification() {}

    /**
     * Creates a new notification with the given message and user.
     * Automatically sets the creation and update timestamps.
     *
     * @param message The notification message.
     * @param user The ObjectId of the user receiving the notification.
     */
    public Notification(String message, ObjectId user) {
        this.user = user;
        this.message = message;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Getters
    public ObjectId getId() { return this.id; }
    public ObjectId getUser() { return this.user; }
    public String getMessage() { return this.message; }
    public Date getCreatedAt() { return this.createdAt; }
    public Date getUpdatedAt() { return this.updatedAt; }

    // Setters
    public void setUser(ObjectId user) { this.user = user; }
    public void setMessage(String message) { this.message = message; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

}
