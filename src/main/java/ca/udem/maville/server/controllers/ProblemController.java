package ca.udem.maville.server.controllers;

import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.server.Database;
import ca.udem.maville.utils.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.javalin.http.Context;

import java.time.Instant;
import java.util.ArrayList;

public class ProblemController {

    public Database database;
    public String urlHead;

    public ProblemController(Database database, String urlHead) {
        this.database = database;
        this.urlHead = urlHead;
    }

    /**
     * Cette route permet de récupérer tous les problèmes qui pourraient intéresser
     * un prestataire en particulier.
     * Le paramètre de path user représente l'id du prestataire. Les problèmes retournés sont
     * ceux non encore traités et dont le type de travail et le quartier appartiennent tous deux respectivement
     * aux types de travaux et aux quartiers couverts par le prestataire.
     * @param ctx qui représente le contexte de la requête.
     */
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
                    if(typeTravauxList.contains(ficheObject.get("typeTravaux").getAsString())
                            && quartiersList.contains(ficheObject.get("quartier").getAsString())
                            && !ficheObject.get("statut").getAsString().equals("traité")) {
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

    /**
     * Cette route permet de créer une nouvelle fiche problème pour un signalement déclaré.
     * Le body doit contenir toutes informations nécessaires notamment :
     * - typeTravaux: qui représente le type travail requis sous forme Label
     * - quartier: qui représente le quartier affecté. Il est sous forme Label
     * - localisation: ici on s'intéressera principalement à la rue ou à l'adresse du résident
     * - description: la description du problème rencontré
     * - priorite: Il s'agit d'un entier entre 0 et 2 inclusif généré automatiquement et qui représente le niveau de priorité du problème
     * - signalements: Les ids des signalements liés à la fiche problème
     * - residents: les ids des résidents ayant déclarés le problème. Très important.
     * Elle s'occupe automatiquement d'assigner les champs id, statut et dateCreationFiche
     * Elle inclut l'envoi d'une notification à tous les résidents ayant fait des
     * signalements en rapport et aussi à tous les prestataires
     * qui pourraient être intéressés par le problème.
     * @param ctx représente le contexte de la requête
     */
    public void create(Context ctx) {
        try {
            // Récupérer les informations et ajouter le statut, la date de création et l'id unique
            String rawJson = ctx.body();

            JsonElement element = JsonParser.parseString(rawJson);

            // Vérifie si c'est bien un objet
            if (!element.isJsonObject()) {
                ctx.status(400).result("{\"message\": \"Format JSON invalide.\"}").contentType("application/json");
                return;
            }

            // Modifier l'objet pour ajouter un id et toute information nécessaire
            JsonObject newProblem = element.getAsJsonObject();

            String id = UniqueID.generateUniqueID();

            newProblem.addProperty("id", id);
            newProblem.addProperty("statut", "enAttente");
            newProblem.addProperty("dateCreationFiche", Instant.now().toString());

            // Ajouter la nouvelle fiche problème à la base de données
            database.problemes.put(id, newProblem.toString());

            // Modifier tous les signalements pour les marquer comme traités
            for(JsonElement idSign : newProblem.get("signalements").getAsJsonArray()) {

                String response = UseRequest.sendRequest(this.urlHead + "/signalement/" + idSign.getAsString() + "?replace=false",
                        RequestType.PATCH, "{\"statut\": \"traité\"}");

                if(response == null) {
                    System.out.println("Une erreur est survenue lors du changement de statut du signalement. Réponse nulle.");
                    continue;
                }

                JsonElement json = JsonParser.parseString(response);
                JsonObject jsonObject = json.getAsJsonObject();

                int statusCode = jsonObject.get("status").getAsInt();
                if (statusCode != 200) {
                    System.out.println("Une erreur est survenue lors changement de statut du signalement. Message d'erreur: " + jsonObject.get("data").getAsJsonObject().get("message").getAsString());
                }
            }


            // Envoyer des notifications aux résidents
            for(JsonElement idRes : newProblem.get("residents").getAsJsonArray()) {
                String bodyMessage = "{\"message\": \"Votre signalement a été traité par un agent. Veuillez consulter la liste de vos signalements pour plus d'informations." +
                        " Vous serez à nouveau averti lorsqu'un projet pour le régler aura commencé.\"}";

                String response = UseRequest.sendRequest(this.urlHead + "/notification/" + idRes.getAsString() + "?userType=resident",
                    RequestType.POST, bodyMessage);

                if(response == null) {
                    System.out.println("Une erreur est survenue lors de l'envoi de la notification au résident pour l'avertir du traitement du signalement. Réponse nulle.");
                    continue;
                }

                JsonElement json = JsonParser.parseString(response);
                JsonObject jsonObject = json.getAsJsonObject();

                int statusCode = jsonObject.get("status").getAsInt();
                if (statusCode != 201) {
                    System.out.println("Une erreur est survenue lors de l'envoi de la notification au résident pour l'avertir du traitement du signalement. Message d'erreur: " + jsonObject.get("data").getAsJsonObject().get("message").getAsString());
                }
            }

            // Récupérer tous les prestataires qui pourraient être intéressés
            String responseInterested = UseRequest.sendRequest(this.urlHead + "/prestataire/getInterested/" + Quartier.fromLabel(newProblem.get("quartier").getAsString()) + "/" + TypesTravaux.fromLabel(newProblem.get("typeTravaux").getAsString()), RequestType.GET, null);

            if(responseInterested == null) {
                throw new Exception("Une erreur est survenue lors de la récupération des prestataires intéressés. Réponse nulle.");
            }

            JsonElement jsonInterested = JsonParser.parseString(responseInterested);
            JsonObject jsonObjectInterested = jsonInterested.getAsJsonObject();

            int statusCodeInterested = jsonObjectInterested.get("status").getAsInt();
            if (statusCodeInterested != 200) {
                throw new Exception("Une erreur est survenue lors de la récupération des prestataires intéressés. Message d'erreur: " + jsonObjectInterested.get("data").getAsJsonObject().get("message").getAsString());
            }

            JsonArray interested = jsonObjectInterested.get("data").getAsJsonArray();

            // Envoyer des notifications aux prestataires
            for(JsonElement prestataire : interested) {
                String bodyMessage = "{\"message\": \"Un nouveau problème qui pourrait vous intéresser a été déclaré. " +
                        "Veuillez consulter la liste des problèmes pour plus d'informations.\"}";

                String response = UseRequest.sendRequest(this.urlHead + "/notification/" + prestataire.getAsJsonObject().get("id").getAsString() + "?userType=prestataire",
                        RequestType.POST, bodyMessage);

                if(response == null) {
                    System.out.println("Une erreur est survenue lors de l'envoi de la notification au prestataire pour l'avertir du nouveau problème. Réponse nulle.");
                    continue;
                }

                JsonElement json = JsonParser.parseString(response);
                JsonObject jsonObject = json.getAsJsonObject();

                int statusCode = jsonObject.get("status").getAsInt();
                if (statusCode != 201) {
                    System.out.println("Une erreur est survenue lors de l'envoi de la notification au prestataire pour l'avertir du nouveau problème. Message d'erreur: " + jsonObject.get("data").getAsJsonObject().get("message").getAsString());
                }
            }


            // Renvoyer la fiche problème pour marquer le succès
            ctx.status(201).json(newProblem).contentType("application/json");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de récupérer une fiche problème en particulier à
     * partir de son id.
     * @param ctx représente le contexte de la requête.
     */
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

    /**
     * Cette route permet de modifier seulement partiellement les informations
     * d'une fiche problème, connaissant son id.
     * Le body doit contenir les champs à modifier avec la nouvelle information.
     * Assurez vous que la nouvelle information a le bon type.
     * Elle nécessite également un queryParameter replace = true | false qui est utile pour les tableaux
     * notamment pour savoir s'il faut juste ajouter les éléments ou remplacer le tableau en entier.
     * @param ctx qui représente le contexte de la requête.
     */
    public void patch(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            boolean replace = Boolean.parseBoolean(ctx.queryParam("replace"));

            JsonObject updates = JsonParser.parseString(ctx.body()).getAsJsonObject();
            String strProblem = database.problemes.get(id);
            if(strProblem == null) {
                ctx.status(404).result("{\"message\": \"Fiche problème non retrouvée.\"}").contentType("application/json");
                return;
            }

            JsonObject problem = JsonParser.parseString(strProblem).getAsJsonObject();

            // Appeler la logique de patch
            boolean ok = ControllerHelper.patchLogic(updates, replace, problem, ctx);

            if(!ok) {
                return;
            }

            // Message de succès
            database.problemes.put(id, problem.toString());
            ctx.status(200).json(problem).contentType("application/json");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }
}
