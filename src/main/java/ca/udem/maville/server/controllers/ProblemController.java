package ca.udem.maville.server.controllers;

import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.server.Database;
import ca.udem.maville.utils.RequestType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.javalin.http.Context;

import java.util.ArrayList;

public class ProblemController {

    public Database database;
    public String urlHead;

    public ProblemController(Database database, String urlHead) {
        this.database = database;
        this.urlHead = urlHead;
    }

    public void getAll(Context ctx) {
        try {
            String idUser = ctx.pathParam("user");

            // Récupérer le user
            String responseUser = UseRequest.sendRequest(this.urlHead + "/prestataire/" + idUser , RequestType.GET, null);

            if(responseUser == null) {
                throw new Exception("Une erreur est survenue lors de la récupération du prestataire. Réponse nulle.");
            }

            JsonElement elemUser = JsonParser.parseString(responseUser);
            JsonObject jsonUser = elemUser.getAsJsonObject();

            int statuscode = jsonUser.get("status").getAsInt();
            if(statuscode == 404) {
                ctx.status(404).result("{\"message\": \"Aucun prestataire avec un tel ID retrouvé.\"}").contentType("application/json");
                return;
            } else if(statuscode != 200) {
                throw new Exception("Une erreur est survenue lors de la récupération du prestataire. Message d'erreur: " + jsonUser.get("data").getAsJsonObject().get("message").getAsString());
            }

            JsonObject user = jsonUser.get("data").getAsJsonObject();

            JsonArray typesTravaux = user.get("typesTravaux").getAsJsonArray();
            JsonArray quartiers = user.get("quartiers").getAsJsonArray();

            ArrayList<String> typeTravauxList = new ArrayList<>();
            for(JsonElement type : typesTravaux) {
                typeTravauxList.add(type.getAsString());
            }
            ArrayList<String> quartiersList = new ArrayList<>();
            for(JsonElement quartier : quartiers) {
                quartiersList.add(quartier.getAsString());
            }

            // Récupérer les problemes qui touchent un des quartiers et qui sont un des types de travaux
            JsonArray data = new JsonArray();

            for(String fiche : database.problemes.values()) {
                if(fiche != null) {
                    JsonObject ficheObject = JsonParser.parseString(fiche).getAsJsonObject();
                    if(typeTravauxList.contains(ficheObject.get("typeTravaux").getAsString()) && quartiersList.contains(ficheObject.get("quartier").getAsString())) {
                        data.add(ficheObject);
                    }
                }
            }


            // Renvoyer les fiches problèmes trouvées dans un tableau
            ctx.status(200).json(data).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    public void create(Context ctx) {
        try {
            // S'assurer de la création du statut date et id
            // Ne pas manquer d'envoyer une notification aux prestataires intéressés et une notification de traitement au résident
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    public void get(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            String strProbleme = database.problemes.get(id);

            if (strProbleme == null) {
                ctx.status(404).result("{\"message\": \"Fiche problème non retrouvée.\"}").contentType("application/json");
                return;
            }

            JsonObject jsonProbleme = JsonParser.parseString(strProbleme).getAsJsonObject();

            // Renvoyer la fiche problème
            ctx.status(200).json(jsonProbleme).contentType("application/json");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }
}
