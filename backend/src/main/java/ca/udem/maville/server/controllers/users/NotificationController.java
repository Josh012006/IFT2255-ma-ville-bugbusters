package ca.udem.maville.server.controllers.users;

import ca.udem.maville.server.dao.files.users.NotificationDAO;
import ca.udem.maville.server.models.users.Notification;
import com.fasterxml.jackson.databind.JsonNode;
import io.javalin.http.Context;

import java.util.List;

import io.javalin.json.JavalinJackson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;


/**
 * La controller qui gère les différentes interactions du client avec le serveur
 * en tout ce qui concerne les notifications.
 */
public class NotificationController {
    public String urlHead;
    public Logger logger;

    public NotificationController( String urlHead, Logger logger) {
        this.urlHead = urlHead;
        this.logger = logger;
    }

    /**
     * Cette route permet de récupérer toutes les notifications d'un utilisateur.
     * Le paramètre de path user contient l'id de l'utilisateur.
     * @param ctx qui représente le contexte de la requête.
     */
    public void getAll(Context ctx) {
        try {
            String idUser = ctx.pathParam("user");

            List<Notification> notifs = NotificationDAO.findUserNotifications(new ObjectId(idUser));

            // Renvoyer les notifications trouvés pour l'utilisateur.
            ctx.status(200).json(notifs).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de créer une nouvelle notification pour un utilisateur.
     * Le body doit contenir les informations suivantes :
     * - user : qui est l'id de l'utilisateur auquel la notification est envoyée.
     * - message: qui représente le message de la notification à envoyer.
     * - url : qui est un champ optionnel qui permet de préciser une url de redirection.
     * @param ctx qui représente le contexte de la requête.
     */
    public void create(Context ctx) {
        try {
            JsonNode json = JavalinJackson.defaultMapper().readTree(ctx.body());

            String message = null;
            if(json.has("message")) {
                message = json.get("message").asText();
            }

            String user = null;
            if(json.has("user")) {
                user = json.get("user").asText();
            }

            if(user == null || message == null) {
                ctx.status(400).result("{\"message\": \"Les champs message et user sont obligatoires.\"}").contentType("application/json");
                return;
            }

            String url = null;
            if(json.has("url")) {
                url = json.get("url").asText();
            }

            Notification newNotif = new Notification(message, new ObjectId(user), url);

            NotificationDAO.save(newNotif);
            
            // Renvoyer la nouvelle notification créée.
            ctx.status(201).json(newNotif).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * C'est une méthode qui permet de marquer la notification comme lue
     * en ayant son id dans le path de la route.
     * @param ctx qui représente le context de la requête.
     */
    public void markAsRead(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            Notification notif = NotificationDAO.findById(new ObjectId(id));

            if(notif == null) {
                ctx.status(404).result("{\"message\": \"Aucune notification avec un tel ID retrouvé.\"}").contentType("application/json");
                return;
            }

            notif.setStatut("lue");

            NotificationDAO.save(notif);

            ctx.status(200).json(notif).contentType("application/json"); // renvoyer l'objet JSON de notification modifiée.
        } catch(Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }
}
