package ca.udem.maville.server.controllers;

import ca.udem.maville.server.Database;
import ca.udem.maville.utils.UniqueID;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.javalin.http.Context;

public class CandidatureController {
    public Database database;

    public CandidatureController(Database database) {
        this.database = database;
    }

    // Une méthod qui récupère toutes les candidatures d'un prestataire en utilisant son id.
    public void getAll(Context ctx) {
        String id = ctx.pathParam("id");

        // Récupérer le prestataire
        String user = database.prestataires.get(id);

        if (user == null) {
            // Renvoyer un message d'erreur avec un code not found
            ctx.status(404).result("{\"message\": \"Aucun prestataire avec un tel ID retrouvé.\"}").contentType("application/json");
            return;
        }

        // Récupérer la liste des ids de ses candidatures
        JsonElement element = JsonParser.parseString(user);
        JsonObject prestataire = element.getAsJsonObject();

        JsonArray candidatures = prestataire.get("candidatures").getAsJsonArray();

        JsonArray data = new JsonArray();
        // Récupérer à partir des ids, chaque candidature
        for (JsonElement candidatureId : candidatures) {
            String candidatureIdString = candidatureId.getAsString();
            String candidature = database.candidatures.get(candidatureIdString);
            data.add(candidature);
        }

        // Renvoyer les candidatures trouvées dans un tableau
        ctx.status(200).json(data).contentType("application/json");


    }

    public void create(Context ctx) {
        // Récupérer les informations sur la nouvelle candidature
        String rawJson = ctx.body();

        JsonElement element = JsonParser.parseString(rawJson);

        // Vérifie si c'est bien un objet
        if (!element.isJsonObject()) {
            ctx.status(400).result("{\"message\": \"Format JSON invalide.\"}").contentType("application/json");
            return;
        }

        // Modifier l'objet pour ajouter un id et toute information nécessaire
        JsonObject newCandidature = element.getAsJsonObject();

        String idPrestataire = newCandidature.has("prestataire") ? newCandidature.get("prestataire").getAsString() : null;
        if (idPrestataire == null) {
            ctx.status(400).result("{\"message\": \"ID du prestataire manquant!\"}").contentType("application/json");
            return;
        }
        String id = UniqueID.generateUniqueID();

        newCandidature.addProperty("id", id);
        newCandidature.addProperty("statut", "enAttente");

        // Todo: Ajouter la candidature à la liste pour le prestataire concerné


        // Todo: Faire la logique d'acceptation ou de rejet aléatoire de la candidature par le serveur dans un autre Thread
        // Todo: Ne pas oublier d'envoyer une notification avec une raison aléatoire en cas de refus
        // Todo: Faire un petit moment avant de le faire pour simuler une attente

        // Renvoyer le message de succès
        ctx.status(201).result("{\"message\": \"Candidature créée avec succès.\"}").contentType("application/json");
    }

    public void get(Context ctx) {

    }

    public void patch(Context ctx) {

    }

    public void update(Context ctx) {

    }

    public void delete(Context ctx) {

    }
}
