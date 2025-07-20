package ca.udem.maville.server.controllers.users;

import ca.udem.maville.server.dao.files.users.ResidentDAO;
import ca.udem.maville.server.models.users.Resident;
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
 * en tout ce qui concerne les résidents.
 */
public class ResidentController {

    public String urlHead;
    public Logger logger;


    public ResidentController(String urlHead, Logger logger) {
        this.urlHead = urlHead;
        this.logger = logger;
    }


    /**
     * Cette route permet de récupérer tous les résidents qui pourraient être intéressés
     * par les changements sur un projet. Elle a besoin de deux query parameters :
     * Le paramètre quartier contient le nom du quartier où le problème est situé.
     * Le paramètre rues qui contient les rues affectées par le projet en question séparées bien sûr par des virgules.
     * @param ctx qui représente le contexte de la requête
     */
    public void getConcerned(Context ctx) {
        try {
            String quartier = ctx.queryParam("quartier");
            String rues = ctx.queryParam("rues");

            if(quartier == null || rues == null || quartier.isEmpty() || rues.isEmpty()) {
                ctx.status(400).result("{\"message\": \"Les query parameters quartier et rues sont requis.\"}").contentType("application/json");
                return;
            }

            String[] ruesTab = rues.split(",");
            List<Resident> concerned = ResidentDAO.findToNotify(quartier, ruesTab);

            List<String> concernedIds = new ArrayList<>();
            for(Resident resident : concerned) {
                concernedIds.add(resident.getId().toHexString());
            }

            ctx.status(200).json(concernedIds).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette fonction permet de récupérer tous les résidents
     * de la base de données. Il est utile au niveau du choix de profil.
     * @param ctx qui représente le contexte de la requête.
     */
    public void getAll(Context ctx) {
        try {
            List<Resident> residents = ResidentDAO.findAll();

            ctx.status(200).json(residents).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de récupérer les informations sur un résident donné
     * en fonction de son id.
     * @param ctx qui représente le contexte de la requête.
     */
    public void getById(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            Resident resident = ResidentDAO.findById(new ObjectId(id));

            if(resident == null) {
                ctx.status(404).result("{\"message\": \"Aucun résident avec un tel ID retrouvé.\"}").contentType("application/json");
                return;
            }

            // Renvoyer le résident
            ctx.status(200).json(resident).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de modifier les préférences de notifications (abonnements)
     * d'un résident, connaissant son id.
     * Le body doit contenir les champs à modifier avec la nouvelle information.
     * Assurez vous que la nouvelle information a le bon type.
     * NB: Elle remplace complètement les champs tableaux de la base de données par ceux envoyés.
     * @param ctx qui représente le contexte de la requête.
     */
    public void patch(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            Resident resident = ResidentDAO.findById(new ObjectId(id));

            if(resident == null) {
                ctx.status(404).result("{\"message\": \"Aucun résident avec un tel ID retrouvé.\"}").contentType("application/json");
                return;
            }

            ObjectMapper mapper = JavalinJackson.defaultMapper();

            JsonNode json = mapper.readTree(ctx.body());

            JsonNode json1 = json.get("abonnementsQuartier");
            if(json1 != null && json1.isArray()) {
                List<String> abonnementsQuartier = mapper.readerForListOf(String.class).readValue(json1);
                resident.setAbonnementsQuartier(abonnementsQuartier);
            }

            JsonNode json2 = json.get("abonnementsRue");
            if(json2 != null && json2.isArray()) {
                List<String> abonnementsRue = mapper.readerForListOf(String.class).readValue(json2);
                resident.setAbonnementsRue(abonnementsRue);
            }

            ResidentDAO.save(resident);

            ctx.status(200).json(resident).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }
}
