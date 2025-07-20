package ca.udem.maville.server.controllers.users;

import ca.udem.maville.server.dao.files.users.PrestataireDAO;
import ca.udem.maville.server.models.users.Prestataire;
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
 * en tout ce qui concerne les prestataires.
 */
public class PrestataireController {
    public String urlHead;
    public Logger logger;

    public PrestataireController(String urlHead, Logger logger) {
        this.urlHead = urlHead;
        this.logger = logger;
    }

    /**
     * Cette route permet de récupérer tous les prestataires qui pourraient être intéressés
     * par un problème déclaré. Elle a besoin de deux query parameters :
     * Le paramètre quartier contient le nom du quartier où le problème est situé.
     * Le paramètre type contient le nom du type de travail requis.
     * @param ctx qui représente le contexte de la requête
     */
    public void getConcerned(Context ctx) {
        try {
            String quartier = ctx.pathParam("quartier");
            String type = ctx.pathParam("type");

            if(quartier.isEmpty() || type.isEmpty()) {
                ctx.status(400).result("{\"message\": \"Les query parameters quartier et type sont requis.\"}").contentType("application/json");
                return;
            }

            List<Prestataire> concerned = PrestataireDAO.findToNotify(quartier, type);
            List<String> concernedIds = new ArrayList<>();
            for(Prestataire prestataire : concerned) {
                concernedIds.add(prestataire.getId().toHexString());
            }

            ctx.status(200).json(concernedIds).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette fonction permet de récupérer tous les prestataires
     * de la base de données. Il est utile au niveau du choix de profil.
     * @param ctx qui représente le contexte de la requête.
     */
    public void getAll(Context ctx) {
        try {
            List<Prestataire> prestataires = PrestataireDAO.findAll();

            ctx.status(200).json(prestataires).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de récupérer les informations sur un prestataire donné
     * en fonction de son id.
     * @param ctx qui représente le contexte de la requête.
     */
    public void getById(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            Prestataire prestataire = PrestataireDAO.findById(new ObjectId(id));

            if(prestataire == null) {
                ctx.status(404).result("{\"message\": \"Aucun prestataire avec un tel ID retrouvé.\"}").contentType("application/json");
                return;
            }

            // Renvoyer le prestataire
            ctx.status(200).json(prestataire).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de modifier les préférences de notifications (abonnements)
     * d'un prestataire, connaissant son id.
     * Le body doit contenir les champs à modifier avec la nouvelle information.
     * Assurez vous que la nouvelle information a le bon type.
     * Elle remplace complètement les tableaux de la base de donénes par ceux envoyés.
     * @param ctx qui représente le contexte de la requête.
     */
    public void patch(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            Prestataire prestataire = PrestataireDAO.findById(new ObjectId(id));

            if(prestataire == null) {
                ctx.status(404).result("{\"message\": \"Aucun prestataire avec un tel ID retrouvé.\"}").contentType("application/json");
                return;
            }

            ObjectMapper mapper = JavalinJackson.defaultMapper();

            JsonNode json = mapper.readTree(ctx.body());

            JsonNode json1 = json.get("abonnementsQuartier");
            if(json1 != null && json1.isArray()) {
                List<String> abonnementsQuartier = mapper.readerForListOf(String.class).readValue(json1);
                prestataire.setAbonnementsQuartier(abonnementsQuartier);
            }

            JsonNode json2 = json.get("abonnementsType");
            if(json2 != null && json2.isArray()) {
                List<String> abonnementsType = mapper.readerForListOf(String.class).readValue(json2);
                prestataire.setAbonnementsType(abonnementsType);
            }

            PrestataireDAO.save(prestataire);

            ctx.status(200).json(prestataire).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }
}
