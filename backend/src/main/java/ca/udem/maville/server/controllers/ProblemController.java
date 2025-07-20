package ca.udem.maville.server.controllers;

import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.server.dao.files.ProblemDAO;
import ca.udem.maville.server.models.Candidature;
import ca.udem.maville.server.models.FicheProbleme;
import ca.udem.maville.utils.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.javalin.http.Context;

import io.javalin.json.JavalinJackson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;

import java.time.Instant;
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
     * Elle s'occupe automatiquement d'assigner les champs id, statut et dateCreationFiche
     * Elle inclut l'envoi d'une notification à tous les résidents ayant fait des
     * signalements en rapport et aussi à tous les prestataires
     * qui pourraient être intéressés par le problème.
     * @param ctx représente le contexte de la requête.
     */
    public void create(Context ctx) {
        try {
            // Récupérer les informations sur le nouveau problème
            FicheProbleme newProblem = ctx.bodyAsClass(FicheProbleme.class);

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
     * Le body doit contenir le champ resident qui représente l'id du résident ayant fais le nouveau
     * signalement et le champ signalement qui représente l'id de son signalement.
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

            JsonNode json = JavalinJackson.defaultMapper().readTree(ctx.body());

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

            ctx.status(200).json(probleme).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }
}
