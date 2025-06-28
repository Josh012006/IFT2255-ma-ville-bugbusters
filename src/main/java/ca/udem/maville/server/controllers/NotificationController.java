package ca.udem.maville.server.controllers;

import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.server.Database;
import ca.udem.maville.utils.RequestType;
import ca.udem.maville.utils.UniqueID;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.javalin.http.Context;

import java.time.Instant;

public class NotificationController {
    public Database database;
    public String urlHead;

    public NotificationController(Database database, String urlHead) {
        this.database = database;
        this.urlHead = urlHead;
    }

    /**
     * Cette route permet de récupérer toutes les notifications d'un utilisateur.
     * Le paramètre de path user contient l'id de l'utilisateur.
     * Elle nécessite un queryParameter userType = resident | prestataire qui permet de savoir
     * de quel type d'utilisateur il s'agit.
     * @param ctx qui représente le contexte de la requête.
     */
    public void getAll(Context ctx) {
        try {
            String idUser = ctx.pathParam("user");
            String userType = ctx.queryParam("userType");

            // Récupérer le user
            String responseUser;
            if(userType == null) {
                ctx.status(500).result("{\"message\": \"Paramètre de requête 'userType' nécessaire.\"}").contentType("application/json");
                return;
            } else if (userType.equals("prestataire")) {
                responseUser = UseRequest.sendRequest(this.urlHead + "/prestataire/" + idUser , RequestType.GET, null);
            } else if (userType.equals("resident")) {
                responseUser = UseRequest.sendRequest(this.urlHead + "/resident/" + idUser , RequestType.GET, null);
            } else {
                ctx.status(500).result("{\"message\": \"Paramètre de requête 'userType' ne peut être que soit 'resident' ou 'prestataire'.\"}").contentType("application/json");
                return;
            }

            if(responseUser == null) {
                throw new Exception("Une erreur est survenue lors de la récupération de l'utilisateur. Réponse nulle.");
            }

            JsonElement elemUser = JsonParser.parseString(responseUser);
            JsonObject jsonUser = elemUser.getAsJsonObject();

            int statuscode = jsonUser.get("status").getAsInt();
            if (statuscode == 404) {
                ctx.status(404).result("{\"message\": \"Aucun " + userType + " avec un tel ID retrouvé.\"}").contentType("application/json");
                return;
            } else if(statuscode != 200) {
                throw new Exception("Une erreur est survenue lors de la récupération du prestataire. Message d'erreur: " + jsonUser.get("data").getAsJsonObject().get("message").getAsString());
            }

            JsonObject user = jsonUser.get("data").getAsJsonObject();

            JsonArray notifications = user.get("notifications").getAsJsonArray();

            // Récupérer à partir des ids, chaque notification
            JsonArray data = new JsonArray();
            for (JsonElement notifId : notifications) {
                String notifIdString = notifId.getAsString();
                String notification = database.notifications.get(notifIdString);
                if(notification != null) {
                    data.add(JsonParser.parseString(notification).getAsJsonObject());
                }
            }

            // Renvoyer les candidatures trouvées dans un tableau
            ctx.status(200).json(data).contentType("application/json");


        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de créer une nouvelle notification pour un utilisateur.
     * Le paramètre de path user contient l'id de l'utilisateur.
     * Elle nécessite un queryParameter userType = resident | prestataire qui permet de savoir
     * de quel type d'utilisateur il s'agit.
     * Le body doit contenir les informations suivantes :
     * - message: qui représente le message de la notification à envoyer
     * Elle s'assure automatiquement de créer les champs id et dateNotification
     * @param ctx qui représente le contexte de la requête.
     */
    public void create(Context ctx) {
        try {
            String idUser = ctx.pathParam("user");
            String userType = ctx.queryParam("userType");

            if(userType == null) {
                ctx.status(500).result("{\"message\": \"Paramètre de requête 'userType' nécessaire.\"}").contentType("application/json");
                return;
            } else if (!(userType.equals("prestataire") || userType.equals("resident"))){
                ctx.status(500).result("{\"message\": \"Paramètre de requête 'userType' ne peut être que soit 'resident' ou 'prestataire'.\"}").contentType("application/json");
                return;
            }

            // Le body contient déjà le message donc ajouter l'id et la dateNotification
            JsonObject newNotif = JsonParser.parseString(ctx.body()).getAsJsonObject();
            String notifID = UniqueID.generateUniqueID();
            newNotif.addProperty("id", notifID);
            newNotif.addProperty("dateNotification", Instant.now().toString());

            // Faire le patch pour ajouter la notification pour l'utilisateur concerné
            String toSend = "{\"notifications\": [\"" + notifID + "\"]}";
            String responseUser;

            if(userType.equals("prestataire")) {
                responseUser = UseRequest.sendRequest(this.urlHead + "/prestataire/" + idUser , RequestType.PATCH, toSend);
            } else {
                responseUser = UseRequest.sendRequest(this.urlHead + "/resident/" + idUser, RequestType.PATCH, toSend);
            }

            if(responseUser == null) {
                throw new Exception("Une erreur est survenue lors de l'ajout de la notification pour l'utilisateur. Réponse nulle.");
            }

            JsonElement elemUser = JsonParser.parseString(responseUser);
            JsonObject jsonUser = elemUser.getAsJsonObject();

            int statuscode = jsonUser.get("status").getAsInt();
            if (statuscode == 404) {
                ctx.status(404).result("{\"message\": \"Aucun " + userType + "avec un tel ID retrouvé.\"}").contentType("application/json");
                return;
            } else if(statuscode != 200) {
                throw new Exception("Une erreur est survenue lors de l'ajout de la notification pour l'utilisateur. Message d'erreur: " + jsonUser.get("data").getAsJsonObject().get("message").getAsString());
            }

            // Renvoyer la notification pour marquer le succès
            database.notifications.put(notifID, newNotif.toString());
            ctx.status(201).json(newNotif).contentType("application/json");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }
}
