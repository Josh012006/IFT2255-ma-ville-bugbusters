package ca.udem.maville.server.controllers;

import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.server.Database;
import ca.udem.maville.utils.RandomGeneration;
import ca.udem.maville.utils.RequestType;
import ca.udem.maville.utils.UniqueID;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class CandidatureController {
    public Database database;
    public String urlHead;

    public CandidatureController(Database database, String urlHead) {
        this.database = database;
        this.urlHead = urlHead;
    }

    // Une méthod qui récupère toutes les candidatures d'un prestataire en utilisant son id.
    public void getAll(Context ctx) {
        String idUser = ctx.pathParam("user");

        // Récupérer le prestataire
        String user = database.prestataires.get(idUser);

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

        // Ajouter la nouvelle candidature à la base de données
        database.candidatures.put(id, newCandidature.toString());

        // Todo: Ajouter la candidature à la liste pour le prestataire concerné
//        String strPrestataire = database.prestataires.get(idPrestataire);
//
//        if (strPrestataire == null) {
//            // Ça serait bizarre qu'il n'existe pas parce qu'il faudrait qu'il existe pour
//            // pouvoir déposer une candidature.
//            ctx.status(505).result("{\"message\": \"Une erreur est survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
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


        // Lancer la logique d'acceptation ou de refus aléatoire d'une candidature
        // en arrière-plan sans bloquer aucun Thread.
        CompletableFuture.runAsync(() -> {
            this.validateCandidature(newCandidature);
        });


        // Renvoyer la candidature comme succès
        ctx.status(201).json(newCandidature).contentType("application/json");
    }

    public void get(Context ctx) {

    }

    public void patch(Context ctx) {

    }

    public void update(Context ctx) {

    }

    public void delete(Context ctx) {

    }

    // Une fonction qui après avoir attendue un certain temps valide ou refuse la candidature.
    // Elle crée un projet si nécessaire et envoie une notification pour préciser au prestataire la décision prise.
    private void validateCandidature(JsonObject candidature) {
        try {
            // Simule une attente de traitement (ex: 3 secondes)
            Thread.sleep(2500);

            // Logique de validation aléatoire
            boolean accepted = Math.random() < 0.85;
            candidature.addProperty("statut", accepted ? "acceptée" : "refusée");

            // Mise à jour dans la "base de données"
            String id = candidature.get("id").getAsString();
            database.candidatures.put(id, candidature.toString());

            if (accepted) {
                // Todo: Rassembler les informations et envoyer une requête pour créer un projet

                // Todo: Envoyer une requête pour créer une notification: "Votre candidature pour titreProjet a été acceptée.
                //  Veuillez consulter votre liste des projets"
            } else {
                // Choisir une raison aléatoire de refus
                String[] refusalReasons = {"Soumission financière non compétitive.",
                        "Soumission financière non compétitive.", "Non-conformité aux normes ou aux méthodes imposées.", "Délais de réalisation trop longs.",
                        "Ressources humaines et matérielles inadéquates."};

                String randomRefusalReason = refusalReasons[RandomGeneration.getRandomUniformInt(0, 5)];

                // Envoyer une requête afin de créer une notification pour l'utilisateur concerné
                String response = UseRequest.sendRequest(this.urlHead + "/notification/" + candidature.get("prestataire").getAsString() + "?userType=prestataire",
                        RequestType.POST, "{\"message\": \""+ randomRefusalReason +"\"}");

                if(response == null) {
                    System.out.println("Une erreur est survenue lors de la création de la notification de refus. Réponse nulle.");
                    return;
                }

                JsonElement json = JsonParser.parseString(response);
                JsonObject jsonObject = json.getAsJsonObject();

                int statusCode = jsonObject.get("status").getAsInt();
                if (statusCode != 201) {
                    System.out.println("Une erreur est survenue lors de la création de la notification de refus. Message d'erreur: " + jsonObject.get("message").getAsString());
                    return;
                }
                else {
                    System.out.println("Notification de refus créee avec succès.");
                }
            }

            System.out.println("Candidature " + id + " a reçu correctement statut: " + candidature.get("statut").getAsString());

        } catch (InterruptedException e) {
            // Réinterrompre le thread
            Thread.currentThread().interrupt();
            System.err.println("Le traitement de validation a été interrompu.");
        }
        // Todo: Ne pas oublier d'envoyer une notification avec une raison aléatoire en cas de refus
    }
}
