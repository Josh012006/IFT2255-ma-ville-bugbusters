package ca.udem.maville.server.controllers;

import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.server.Database;
import ca.udem.maville.utils.ControllerHelper;
import ca.udem.maville.utils.RequestType;
import ca.udem.maville.utils.UniqueID;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.javalin.http.Context;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

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
            // Récupérer les informations sur le nouveau signalement
            String rawJson = ctx.body();

            JsonElement element = JsonParser.parseString(rawJson);

            // Vérifie si c'est bien un objet
            if (!element.isJsonObject()) {
                ctx.status(400).result("{\"message\": \"Format JSON invalide.\"}").contentType("application/json");
                return;
            }

            // Modifier l'objet pour ajouter un id et toute information nécessaire
            JsonObject newSignal = element.getAsJsonObject();

            String idResident = newSignal.has("resident") ? newSignal.get("resident").getAsString() : null;
            if (idResident == null) {
                ctx.status(400).result("{\"message\": \"ID du résident manquant!\"}").contentType("application/json");
                return;
            }
            String id = UniqueID.generateUniqueID();

            newSignal.addProperty("id", id);
            newSignal.addProperty("statut", "enAttente");
            newSignal.addProperty("dateSignalement", Instant.now().toString());

            // Ajouter le nouveau signalement à la base de données
            database.signalements.put(id, newSignal.toString());

            // Ajouter le signalement à la liste pour le résident concerné
            JsonObject patchBody = new JsonObject();
            JsonArray toAdd = new JsonArray();
            toAdd.add(id);
            patchBody.add("signalements", toAdd);

            // Envoyer la requête pour modifier la liste des signalements
            String response = UseRequest.sendRequest(this.urlHead + "/resident/" + idResident + "?replace=false", RequestType.PATCH, patchBody.toString());

            if(response == null) {
                throw new Exception("Une erreur est survenue lors de l'ajout du signalement pour le résident. Réponse nulle.");
            }

            JsonElement elemSignal = JsonParser.parseString(response);
            JsonObject jsonSignal = elemSignal.getAsJsonObject();

            int statusCodeSignal = jsonSignal.get("status").getAsInt();
            if (statusCodeSignal != 200) {
                throw new Exception("Une erreur est survenue lors de l'ajout du signalement pour le résident. Message d'erreur: " + jsonSignal.get("data").getAsJsonObject().get("message").getAsString());
            }

            // Lancer la logique traitement du signalement et d'attribution de priorité aléatoire
            // en arrière-plan sans bloquer aucun Thread.
            CompletableFuture.runAsync(() -> {
                this.manageSignal(newSignal);
            });


            // Renvoyer le signalement avec un message de succès
            ctx.status(201).json(newSignal).contentType("application/json");

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

    // Une fonction qui après avoir attendu un certain temps attribue une priorité aléatoire
    // au problème et crée une fiche problème
    private void manageSignal(JsonObject signal) {
        // Todo: Assigner une priorité aléatoire

        // Todo: Envoyer une requête pour créer une ficheProblème
    }
}





