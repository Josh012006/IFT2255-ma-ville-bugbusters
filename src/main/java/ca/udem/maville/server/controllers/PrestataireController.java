package ca.udem.maville.server.controllers;

import ca.udem.maville.server.Database;
import ca.udem.maville.utils.ControllerHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.javalin.http.Context;

import java.util.Map;

public class PrestataireController {
    public Database database;
    public String urlHead;

    public PrestataireController(Database database, String urlHead) {
        this.database = database;
        this.urlHead = urlHead;
    }

    public void get(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            String strPrestataire = database.prestataires.get(id);

            if (strPrestataire == null) {
                ctx.status(404).result("{\"message\": \"Prestataire non retrouvé.\"}").contentType("application/json");
                return;
            }

            JsonObject jsonPrestataire = JsonParser.parseString(strPrestataire).getAsJsonObject();

            // Renvoyer le prestataire
            ctx.status(200).json(jsonPrestataire).contentType("application/json");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    public void patch(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            boolean replace = Boolean.parseBoolean(ctx.queryParam("replace"));

            JsonObject updates = JsonParser.parseString(ctx.body()).getAsJsonObject();
            String strPrestataire = database.prestataires.get(id);
            if(strPrestataire == null) {
                ctx.status(404).result("{\"message\": \"Prestataire non retrouvé.\"}").contentType("application/json");
                return;
            }

            JsonObject prestataire = JsonParser.parseString(strPrestataire).getAsJsonObject();

            for (Map.Entry<String, JsonElement> entry : updates.entrySet()) {
                String key = entry.getKey();
                JsonElement value = entry.getValue();

                if (value.isJsonArray()) {
                    JsonArray nouvelles = value.getAsJsonArray();

                    if (replace || !prestataire.has(key)) {
                        // Remplacer complètement
                        prestataire.add(key, nouvelles);
                    } else {
                        // Ajouter sans doublons
                        JsonArray existantes = prestataire.getAsJsonArray(key);
                        for (JsonElement elem : nouvelles) {
                            if (!existantes.contains(elem)) {
                                existantes.add(elem);
                            }
                        }
                    }

                } else {
                    // Champ simple → remplacement direct
                    if(!ControllerHelper.sameTypeJson(prestataire.get(key), value)) {
                        ctx.status(400).result("{\"message\": \"Le champ " + key + " envoyé n'a pas le bon type.\"}").contentType("application/json");
                    }
                    prestataire.add(key, value);
                }
            }

            // Message de succès
            database.prestataires.put(id, prestataire.toString());
            ctx.status(200).json(prestataire).contentType("application/json");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    public void update(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            String strPrestataire = database.prestataires.get(id);

            if (strPrestataire == null) {
                ctx.status(404).result("{\"message\": \"Prestataire non retrouvé.\"}").contentType("application/json");
                return;
            }

            // Traiter le nouveau prestataire
            String rawJson = ctx.body();

            JsonElement element = JsonParser.parseString(rawJson);

            // Vérifie si c'est bien un objet
            if (!element.isJsonObject()) {
                ctx.status(400).result("{\"message\": \"Format JSON invalide.\"}").contentType("application/json");
                return;
            }

            // Vérifier que toutes les entrées sont là
            JsonObject newPrestataire = element.getAsJsonObject();

            JsonObject actualPrestataire = JsonParser.parseString(strPrestataire).getAsJsonObject();

            if(!ControllerHelper.sameKeysSameTypes(actualPrestataire, newPrestataire)) {
                ctx.status(400).result("{\"message\": \"Format d'objet ne correspondant pas à celui d'un prestataire. Vérifiez que les champs envoyés sont corrects et que les types sont bons.\"}").contentType("application/json");
                return;
            }

            // M'assurer que l'id reste le même
            JsonObject updatedPrestataire = newPrestataire;
            updatedPrestataire.addProperty("id", id);

            database.prestataires.put(id, updatedPrestataire.toString());

            // Renvoyer le nouvel objet
            ctx.status(200).json(updatedPrestataire).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }
}
