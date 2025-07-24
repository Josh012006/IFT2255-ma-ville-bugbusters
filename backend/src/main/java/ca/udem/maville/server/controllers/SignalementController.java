package ca.udem.maville.server.controllers;

import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.server.dao.files.SignalementDAO;
import ca.udem.maville.server.models.Signalement;
import ca.udem.maville.utils.RequestType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.json.JavalinJackson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;

import java.util.List;


/**
 * La controller qui gère les différentes interactions du client avec le serveur
 * en tout ce qui concerne les signalements.
 */
public class SignalementController {

    public String urlHead;
    public Logger logger;


    public SignalementController(String urlHead, Logger logger) {
        this.urlHead = urlHead;
        this.logger = logger;
    }

    /**
     * Cette route permet de récupérer tous les signalements non traités présents dans la base
     * de données.
     * @param ctx qui représente le contexte de la requête
     */
    public void getAll(Context ctx) {
        try {
            List<Signalement> signalements = SignalementDAO.findAll();

            // Renvoyer les signalements trouvés.
            ctx.status(200).json(signalements).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de créer un nouveau signalement pour un résident.
     * Le body doit contenir toutes informations nécessaires notamment :
     * - typeProbleme: qui représente le type travail requis.
     * - quartier: qui représente le quartier affecté.
     * - localisation: ici on s'intéressera principalement à la rue ou à l'adresse du résident
     * - description: la description du problème rencontré
     * - resident: l'id du résident faisant le signalement. Très important
     * Elle s'occupe automatiquement d'assigner les champs id et statut.
     * Elle implémente aussi un envoi de notification au STPM.
     * @param ctx représente le contexte de la requête
     */
    public void create(Context ctx) {
        try {
            // Récupérer les informations du nouveau signalement.
            Signalement newSignalement = ctx.bodyAsClass(Signalement.class);

            // S'assurer que le statut est bon avant de le sauvegarder
            newSignalement.setStatut("en attente");
            SignalementDAO.save(newSignalement);

            // Envoyer une notification au STPM
            String body = "{" +
                    "\"message\": \"Un nouveau signalement a été créé par un résident.\"," +
                    "\"user\": \"507f1f77bcf86cd799439011\"," +
                    "\"url\": \"/signalement/" + newSignalement.getId() + "\"" + // Todo: Vérifier l'url une fois l'interface finie.
                    "}";
            String response = UseRequest.sendRequest(urlHead + "/notification", RequestType.POST, body);

            ObjectMapper mapper = JavalinJackson.defaultMapper();
            JsonNode json = mapper.readTree(response);

            if(json.get("status").asInt() != 201) {
                JsonNode data = json.get("data");
                throw new Exception(data.get("message").asText());
            }

            // Renvoyer le signalement avec un message de succès
            ctx.status(201).json(newSignalement).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de récupérer un signalement en particulier à
     * partir de son id.
     * @param ctx représente le contexte de la requête.
     */
    public void getById(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            Signalement signalement = SignalementDAO.findById(new ObjectId(id));

            if (signalement == null) {
                ctx.status(404).result("{\"message\": \"Aucun signalement avec un tel ID trouvé.\"}").contentType("application/json");
                return;
            }

            // Renvoyer le signalement trouvé
            ctx.status(200).json(signalement).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de récupérer tous les signalements d'un résident en particulier.
     * Le path parameter user contient l'id du résident.
     * @param ctx qui représente le contexte de la requête.
     */
    public void getByResident(Context ctx) {
        try {
            String userId = ctx.pathParam("user");

            List<Signalement> signalements = SignalementDAO.findResidentSignalements(new ObjectId(userId));

            ctx.status(200).json(signalements).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de modifier seulement partiellement les informations
     * d'un signalement, connaissant son id.
     * Le body doit contenir les champs à modifier avec la nouvelle information.
     * Assurez vous que la nouvelle information a le bon type.
     * La modification n'est possible que si le signalement n'a pas encore été vu ou traité.
     * NB: Elle remplace complètement les champs tableaux de la base de données par ceux envoyés.
     * @param ctx qui représente le contexte de la requête.
     */
    public void patch(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            // La modification est possible seulement si pas encore traité ou vu
            Signalement signalement = SignalementDAO.findById(new ObjectId(id));

            if(signalement == null) {
                ctx.status(404).result("{\"message\": \"Aucun signalement avec un tel ID trouvée.\"}").contentType("application/json");
                return;
            }

            if(!signalement.getStatut().equals("en attente")) {
                ctx.status(403).result("{\"message\": \"Ce signalement a déjà été vu par le STPM. Vous ne pouvez pas le modifier.\"}").contentType("application/json");
                return;
            }

            Signalement modifiedSignalement = ctx.bodyAsClass(Signalement.class);

            SignalementDAO.save(modifiedSignalement);

            ctx.status(200).json(modifiedSignalement).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de supprimer un signalement à partir de son id
     * @param ctx qui représente le contexte de la requête.
     */
    public void delete(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            SignalementDAO.delete(new ObjectId(id));

            // Renvoyer la réponse de succès
            ctx.status(200).result("{\"message\": \"Suppression réalisée avec succès.\"}").contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route marque un signalement comme vu par le STPM pour empêcher des modifications
     * par les résidents.
     * @param ctx qui représente le contexte de la requête.
     */
    public void markAsSeen(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            Signalement signalement = SignalementDAO.findById(new ObjectId(id));

            if(signalement == null) {
                ctx.status(404).result("{\"message\": \"Aucun signalement avec un tel ID trouvée.\"}").contentType("application/json");
                return;
            }

            signalement.setStatut("vu");
            SignalementDAO.save(signalement);

            // Renvoyer la réponse de succès
            ctx.status(200).json(signalement).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de marquer un signalement comme traité.
     * Elle requiert un query parameter treated qui est un booléen
     * informant si un projet existe déjà pour le signalement.
     * @param ctx qui représente le contexte de la requête.
     */
    public void markAsProcessed(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            boolean treated = Boolean.parseBoolean(ctx.queryParam("treated"));

            Signalement signalement = SignalementDAO.findById(new ObjectId(id));

            if(signalement == null) {
                ctx.status(404).result("{\"message\": \"Aucun signalement avec un tel ID trouvée.\"}").contentType("application/json");
                return;
            }

            signalement.setStatut("traité");
            SignalementDAO.save(signalement);

            // Envoyer une notification au résident.
            String message = (treated) ? "Un projet existe déjà pour régler ce problème. Veuillez consulter la" +
                    " liste des projets récents pour plus d'informations." : "Vous serez notifié lorsqu'un projet pour le résoudre aura été créé.";
            String body = "{" +
                    "\"message\": \"Votre signalement à été traité par le STPM. " + message + "\"," +
                    "\"user\": \"" + signalement.getResident() + "\"," +
                    "\"url\": \"/signalement/" + signalement.getId() + "\"" +     // Todo: Vérifier une fois que l'interface est finie
                    "}";
            String response = UseRequest.sendRequest(urlHead + "/notification", RequestType.POST, body);

            ObjectMapper mapper = JavalinJackson.defaultMapper();
            JsonNode json = mapper.readTree(response);

            if(json.get("status").asInt() != 201) {
                JsonNode data = json.get("data");
                throw new Exception(data.get("message").asText());
            }

            // Renvoyer la réponse de succès
            ctx.status(200).json(signalement).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }
}






