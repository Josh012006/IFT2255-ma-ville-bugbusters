package ca.udem.maville.server.controllers;

import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.server.Database;
import ca.udem.maville.utils.ControllerHelper;
import ca.udem.maville.utils.RequestType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.javalin.http.Context;

public class SignalementController {

    public Database database;
    public String urlHead;

    public SignalementController(Database database, String urlHead) {
        this.database = database;
        this.urlHead = urlHead;
    }

    public void getAll(Context ctx) {
        try {
            String idUser = ctx.pathParam("user");

            // Récupérer le user
            String responseUser = UseRequest.sendRequest(this.urlHead + "/resident/" + idUser , RequestType.GET, null);

            if(responseUser == null) {
                throw new Exception("Une erreur est survenue lors de la récupération de l'utilisateur. Réponse nulle.");
            }

            JsonElement elemUser = JsonParser.parseString(responseUser);
            JsonObject jsonUser = elemUser.getAsJsonObject();

            int statuscode = jsonUser.get("status").getAsInt();
            if (statuscode == 404) {
                ctx.status(404).result("{\"message\": \"Aucun résident avec un tel ID retrouvé.\"}").contentType("application/json");
                return;
            } else if(statuscode != 200) {
                throw new Exception("Une erreur est survenue lors de la récupération du prestataire. Message d'erreur: " + jsonUser.get("data").getAsJsonObject().get("message").getAsString());
            }

            JsonObject user = jsonUser.get("data").getAsJsonObject();

            JsonArray signals = user.get("signalements").getAsJsonArray();

            // Récupérer à partir des ids, chaque signalement
            JsonArray data = new JsonArray();
            for (JsonElement signalId : signals) {
                String signalIdString = signalId.getAsString();
                String signal = database.signalements.get(signalIdString);
                if(signal != null) {
                    data.add(signal);
                }
            }

            // Renvoyer les signalements trouvés dans un tableau
            ctx.status(200).json(data).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    public void create(Context ctx) {
        try {
            // Todo: Ne pas oublier d'ajouter aux signalements du résident et d'envoyer une notification de traitement.
            //  Ensuite, dans un Thread séparé, assigner une prorité aléatoire et créer une ficheProblème et envoyer le message aux prestataires qui peuvent être
            //  intéressés.

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    public void get(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            String strSignal = database.signalements.get(id);

            if (strSignal == null) {
                ctx.status(404).result("{\"message\": \"Signalement non retrouvé.\"}").contentType("application/json");
                return;
            }

            JsonObject jsonSignal = JsonParser.parseString(strSignal).getAsJsonObject();

            // Renvoyer le résident
            ctx.status(200).json(jsonSignal).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    public void patch(Context ctx) {
        try {
            // La modification est possible seulement si pas encore traité
            String id = ctx.pathParam("id");
            boolean replace = Boolean.parseBoolean(ctx.queryParam("replace"));

            JsonObject updates = JsonParser.parseString(ctx.body()).getAsJsonObject();
            String strSignal = database.signalements.get(id);

            if (strSignal == null) {
                ctx.status(404).result("{\"message\": \"Signalement non retrouvée.\"}").contentType("application/json");
                return;
            }

            JsonObject signal = JsonParser.parseString(strSignal).getAsJsonObject();

            // La modification est possible seulement si pas encore traité
            if(!signal.get("statut").getAsString().equals("enAttente")) {
                ctx.status(400).result("{\"message\": \"Impossible de modifier un signalement déjà traité. Veuillez vérifier vos notifications.\"}").contentType("application/json");
                return;
            }

            // Appeler la logique de patch
            boolean ok = ControllerHelper.patchLogic(updates, replace, signal, ctx);

            if(!ok) {
                return;
            }

            // Message de succès
            database.signalements.put(id, signal.toString());
            ctx.status(200).json(signal).contentType("application/json");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    public void update(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            String strSignal = database.signalements.get(id);

            if (strSignal == null) {
                ctx.status(404).result("{\"message\": \"Signalement non retrouvé.\"}").contentType("application/json");
                return;
            }

            // Traiter le nouveau signalement
            String rawJson = ctx.body();

            JsonElement element = JsonParser.parseString(rawJson);

            // Vérifie si c'est bien un objet
            if (!element.isJsonObject()) {
                ctx.status(400).result("{\"message\": \"Format JSON invalide.\"}").contentType("application/json");
                return;
            }

            // Vérifier que toutes les entrées sont là
            JsonObject newSignal = element.getAsJsonObject();

            JsonObject actualSignal = JsonParser.parseString(strSignal).getAsJsonObject();

            // La modification est possible seulement si pas encore traité
            if(!actualSignal.get("statut").getAsString().equals("enAttente")) {
                ctx.status(400).result("{\"message\": \"Impossible de modifier un signalement déjà traité. Veuillez vérifier vos notifications.\"}").contentType("application/json");
                return;
            }

            if(!ControllerHelper.sameKeysSameTypes(actualSignal, newSignal)) {
                ctx.status(400).result("{\"message\": \"Format d'objet ne correspondant pas à celui d'un signalement. Vérifiez que les champs envoyés sont corrects et que les types sont bons.\"}").contentType("application/json");
                return;
            }

            // M'assurer que l'id reste le même
            JsonObject updatedSignal = newSignal;
            updatedSignal.addProperty("id", id);

            database.signalements.put(id, updatedSignal.toString());

            // Renvoyer le nouvel objet
            ctx.status(200).json(updatedSignal).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    public void delete(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            String strSignal = database.signalements.get(id);

            if (strSignal == null) {
                ctx.status(404).result("{\"message\": \"Signalement non retrouvé.\"}").contentType("application/json");
                return;
            }

            database.signalements.remove(id);

            // Renvoyer la réponse de succès
            ctx.status(200).result("{\"message\": \"Suppression réalisée avec succès.\"}").contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }
}
