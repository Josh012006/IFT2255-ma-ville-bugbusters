package ca.udem.maville.server.controllers;

import ca.udem.maville.server.Database;
import ca.udem.maville.utils.ControllerHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.javalin.http.Context;

import java.util.Map;

public class ResidentController {

    public Database database;
    public String urlHead;

    public ResidentController(Database database, String urlHead) {
        this.database = database;
        this.urlHead = urlHead;
    }

    public void get(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            String strResident = database.residents.get(id);

            if (strResident == null) {
                ctx.status(404).result("{\"message\": \"Résident non retrouvé.\"}").contentType("application/json");
                return;
            }

            JsonObject jsonResident = JsonParser.parseString(strResident).getAsJsonObject();

            // Renvoyer le résident
            ctx.status(200).json(jsonResident).contentType("application/json");

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
            String strResident = database.residents.get(id);
            if(strResident == null) {
                ctx.status(404).result("{\"message\": \"Résident non retrouvé.\"}").contentType("application/json");
                return;
            }

            JsonObject resident = JsonParser.parseString(strResident).getAsJsonObject();

            for (Map.Entry<String, JsonElement> entry : updates.entrySet()) {
                String key = entry.getKey();
                JsonElement value = entry.getValue();

                if (value.isJsonArray()) {
                    JsonArray nouvelles = value.getAsJsonArray();

                    if (replace || !resident.has(key)) {
                        // Remplacer complètement
                        resident.add(key, nouvelles);
                    } else {
                        // Ajouter sans doublons
                        JsonArray existantes = resident.getAsJsonArray(key);
                        for (JsonElement elem : nouvelles) {
                            if (!existantes.contains(elem)) {
                                existantes.add(elem);
                            }
                        }
                    }

                } else {
                    // Champ simple → remplacement direct
                    if(!ControllerHelper.sameTypeJson(resident.get(key), value)) {
                        ctx.status(400).result("{\"message\": \"Le champ " + key + " envoyé n'a pas le bon type.\"}").contentType("application/json");
                    }
                    resident.add(key, value);
                }
            }

            // Message de succès
            database.residents.put(id, resident.toString());
            ctx.status(200).json(resident).contentType("application/json");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    public void update(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            String strResident = database.residents.get(id);

            if (strResident == null) {
                ctx.status(404).result("{\"message\": \"Résident non retrouvé.\"}").contentType("application/json");
                return;
            }

            // Traiter le nouveau résident
            String rawJson = ctx.body();

            JsonElement element = JsonParser.parseString(rawJson);

            // Vérifie si c'est bien un objet
            if (!element.isJsonObject()) {
                ctx.status(400).result("{\"message\": \"Format JSON invalide.\"}").contentType("application/json");
                return;
            }

            // Vérifier que toutes les entrées sont là
            JsonObject newResident = element.getAsJsonObject();

            JsonObject actualResident = JsonParser.parseString(strResident).getAsJsonObject();

            if(!ControllerHelper.sameKeysSameTypes(actualResident, newResident)) {
                ctx.status(400).result("{\"message\": \"Format d'objet ne correspondant pas à celui d'un résident. Vérifiez que les champs envoyés sont corrects et que les types sont bons.\"}").contentType("application/json");
                return;
            }

            // M'assurer que l'id reste le même
            JsonObject updatedResident = newResident;
            updatedResident.addProperty("id", id);

            database.residents.put(id, updatedResident.toString());

            // Renvoyer le nouvel objet
            ctx.status(200).json(updatedResident).contentType("application/json");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }
}
