package ca.udem.maville.server.controllers;

import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.server.dao.files.ProblemDAO;
import ca.udem.maville.server.models.FicheProbleme;
import ca.udem.maville.utils.RequestType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;

import io.javalin.json.JavalinJackson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * La controller qui gère les différentes interactions du client avec le serveur
 * en tout ce qui concerne les fiches problèmes.
 */
public class ProblemController {

    public String urlHead;
    public Logger logger;


    public ProblemController(String urlHead, Logger logger) {
        this.urlHead = urlHead;
        this.logger = logger;
    }

    /**
     * Cette route permet de récupérer tous les problèmes présents dans la base de
     * données.
     * @param ctx qui représente le contexte de la requête.
     */
    public void getAll(Context ctx) {
        try {

            List<FicheProbleme> problems = ProblemDAO.findAll();

            // Renvoyer les fiches problèmes trouvées
            ctx.status(200).json(problems).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de créer une nouvelle fiche problème pour un signalement déclaré.
     * Le body doit contenir toutes informations nécessaires notamment :
     * - typeTravaux: qui représente le type travail requis sous forme Label
     * - quartier: qui représente le quartier affecté. Il est sous forme Label
     * - localisation: ici on s'intéressera principalement à la rue ou à l'adresse du résident
     * - description: la description du problème rencontré
     * - priorite: la priorité accordée au problème par le STPM.
     * - signalements: Les ids des signalements liés à la fiche problème
     * - residents: les ids des résidents ayant déclarés le problème. Très important.
     * Elle s'occupe automatiquement d'assigner les champs id et statut.
     * Elle inclut de marquer tous les signalements comme traités, ce qui envoie des notifications aux résidents.
     * Elle inclut aussi l'envoi d'une notification à tous les prestataires pouvant être intéressés.
     * qui pourraient être intéressés par le problème.
     * @param ctx représente le contexte de la requête.
     */
    public void create(Context ctx) {
        try {
            // Récupérer les informations sur le nouveau problème
            FicheProbleme newProblem = ctx.bodyAsClass(FicheProbleme.class);

            // S'assurer que le statut est le bon avant de sauvegarder
            newProblem.setStatut("en attente");
            ProblemDAO.save(newProblem);

            ObjectMapper mapper = JavalinJackson.defaultMapper();

            // Marquer les signalements comme traités.
            List<ObjectId> signalements = newProblem.getSignalements();
            for(ObjectId id : signalements) {
                String response = UseRequest.sendRequest(urlHead + "/signalement/markAsProcessed/" + id.toHexString() + "?treated=false", RequestType.PATCH, null);
                JsonNode json = mapper.readTree(response);
                if(json.get("status").asInt() != 200) {
                    JsonNode data = json.get("data");
                    logger.error(data.get("message").asText());
                }
            }

            // Envoyer des notifications aux prestataires intéressés.
            String quartier = newProblem.getQuartier().replace(" ", "+");
            String type = newProblem.getTypeTravaux().replace(" ", "+");

            String response = UseRequest.sendRequest(urlHead + "/prestataire/getConcerned?quartier=" + quartier + "&type=" + type, RequestType.GET, null);
            JsonNode json = mapper.readTree(response);
            JsonNode data = json.get("data");
            if(json.get("status").asInt() != 200) {
                throw new Exception(data.get("message").asText());
            }

            if(!data.isArray()) {
                throw new Exception("Les prestataires intéressés n'est pas un tableau.");
            }

            for(JsonNode element : data) {
                String id = element.asText();
                String body = "{" +
                        "\"message\": \"Une nouvelle fiche problème qui pourrait vous intéresser a été créée.\"," +
                        "\"user\": \"" + id + "\"," +
                        "\"url\": \"/prestataire/probleme/" + newProblem.getId() + "\"" +
                        "}";
                String response1 = UseRequest.sendRequest(urlHead + "/notification", RequestType.POST, body);

                JsonNode json1 = mapper.readTree(response1);

                if(json1.get("status").asInt() != 201) {
                    JsonNode info = json1.get("data");
                    logger.error(info.get("message").asText());
                }
            }

            // Renvoyer la fiche problème pour marquer le succès
            ctx.status(201).json(newProblem).contentType("application/json");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de récupérer une fiche problème en particulier à
     * partir de son id.
     * @param ctx représente le contexte de la requête.
     */
    public void getById(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            FicheProbleme probleme = ProblemDAO.findById(new ObjectId(id));

            if (probleme == null) {
                ctx.status(404).result("{\"message\": \"Aucune fiche problème avec un tel ID retrouvée.\"}").contentType("application/json");
                return;
            }

            // Renvoyer la fiche problème
            ctx.status(200).json(probleme).contentType("application/json");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette méthode permet d'ajouter un résident à la liste des résidents du problème
     * et un signalement à la liste des signalement du problème.
     * Utile lorsqu'un problème à déjà été créé pour un signalement.
     * Le body doit contenir le champ resident qui représente l'id du résident ayant fait le nouveau
     * signalement et le champ signalement qui représente l'id de son signalement.
     * Elle inclut de marquer le signalement comme traité, ce qui envoie une notification au résident.
     * @param ctx qui représente le contexte de la requête.
     */
    public void addExisting(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            FicheProbleme probleme = ProblemDAO.findById(new ObjectId(id));

            if (probleme == null) {
                ctx.status(404).result("{\"message\": \"Aucune fiche problème avec un tel ID retrouvée.\"}").contentType("application/json");
                return;
            }

            ObjectMapper mapper = JavalinJackson.defaultMapper();
            JsonNode json = mapper.readTree(ctx.body());

            if(!json.has("resident") || !json.has("signalement")) {
                ctx.status(400).result("{\"message\": \"Les champs resident et signalement sont obligatoires.\"}").contentType("application/json");
                return;
            }

            List<ObjectId> signalements = probleme.getSignalements();
            signalements.add(new ObjectId(json.get("signalement").asText()));
            probleme.setSignalements(signalements);

            List<ObjectId> residents = probleme.getResidents();
            residents.add(new ObjectId(json.get("resident").asText()));
            probleme.setResidents(residents);

            ProblemDAO.save(probleme);

            // Marquer le signalement comme traité.
            String response1 = UseRequest.sendRequest(urlHead + "/signalement/markAsProcessed/" + json.get("signalement").asText() + "?treated=true", RequestType.PATCH, null);
            JsonNode json1 = mapper.readTree(response1);
            JsonNode data1 = json.get("data");
            if(json1.get("status").asInt() != 200) {
                throw new Exception(data1.get("message").asText());
            }

            ctx.status(200).json(probleme).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de récupérer les id de tous ceux qui ont déclarés le problème.
     * @param ctx qui représente le contexte de la requête.
     */
    public void getReporters(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            FicheProbleme problem = ProblemDAO.findById(new ObjectId(id));

            if(problem == null) {
                ctx.status(404).result("{\"message\": \"Aucune fiche problème avec un tel ID trouvée.\"}").contentType("application/json");
                return;
            }

            List<ObjectId> reporters = problem.getResidents();
            List<String> reportersIds = new ArrayList<>();

            for(ObjectId userId : reporters) {
                reportersIds.add(userId.toHexString());
            }

            ctx.status(200).json(reportersIds).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de marquer une fiche problème comme traitée.
     * @param ctx qui représente le contexte de la requête.
     */
    public void markAsProcessed(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            FicheProbleme problem = ProblemDAO.findById(new ObjectId(id));

            if(problem == null) {
                ctx.status(404).result("{\"message\": \"Aucune fiche problème avec un tel ID trouvée.\"}").contentType("application/json");
                return;
            }

            problem.setStatut("traitée");
            ProblemDAO.save(problem);

            // Renvoyer la réponse de succès
            ctx.status(200).json(problem).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }
}
