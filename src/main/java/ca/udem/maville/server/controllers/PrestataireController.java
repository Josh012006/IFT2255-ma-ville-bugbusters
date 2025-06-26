package ca.udem.maville.server.controllers;

import ca.udem.maville.server.Database;
import io.javalin.http.Context;

public class PrestataireController {
    public Database database;
    public String urlHead;

    public PrestataireController(Database database, String urlHead) {
        this.database = database;
        this.urlHead = urlHead;
    }

    public void getAll(Context ctx) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    public void get(Context ctx) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    public void patch(Context ctx) {
        try {
            // Logique minimale de patch. Il faudra encore récupérer les informations dans le body
            // On suivra le template suivant:

//        app.patch("/prestataires/:id", ctx -> {
//            String id = ctx.pathParam("id");
//            boolean replace = Boolean.parseBoolean(ctx.queryParam("replace", "false"));
//
//            JsonObject updates = JsonParser.parseString(ctx.body()).getAsJsonObject();
//            JsonObject prestataire = JsonParser.parseString(database.prestataires.get(id)).getAsJsonObject();
//
//            for (Map.Entry<String, JsonElement> entry : updates.entrySet()) {
//                String key = entry.getKey();
//                JsonElement value = entry.getValue();
//
//                if (value.isJsonArray()) {
//                    JsonArray nouvelles = value.getAsJsonArray();
//
//                    if (replace || !prestataire.has(key)) {
//                        // Remplacer complètement
//                        prestataire.add(key, nouvelles);
//                    } else {
//                        // Ajouter sans doublons
//                        JsonArray existantes = prestataire.getAsJsonArray(key);
//                        for (JsonElement elem : nouvelles) {
//                            if (!existantes.contains(elem)) {
//                                existantes.add(elem);
//                            }
//                        }
//                    }
//                } else {
//                    // Champ simple → remplacement direct
//                    prestataire.add(key, value);
//                }
//            }
//
//            database.prestataires.put(id, prestataire.toString());
//            ctx.status(204); // No Content
//        });


            //        String strPrestataire = database.prestataires.get(idPrestataire);
//
//        if (strPrestataire == null) {
//            // Ça serait bizarre qu'il n'existe pas parce qu'il faudrait qu'il existe pour
//            // pouvoir déposer une candidature.
//            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
//            return;
//        }
//
//        JsonElement elemPrestataire = JsonParser.parseString(strPrestataire);
//        JsonObject prestataire = elemPrestataire.getAsJsonObject();
//
//        prestataire.get("candidatures").getAsJsonArray().add(id);
//
//        // Enregistrer la modification au prestataire
//        database.prestataires.put(idPrestataire, prestataire.toString());
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    public void update(Context ctx) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }
}
