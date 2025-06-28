package ca.udem.maville.server.controllers;

import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.server.Database;
import ca.udem.maville.utils.DateManagement;
import ca.udem.maville.utils.RequestType;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.javalin.http.Context;

public class ProjectController {

    public Database database;
    public String urlHead;

    public ProjectController(Database database, String urlHead) {
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

    public void getByPrestataire(Context ctx) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    public void getByType(Context ctx) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    public void getByRegion(Context ctx) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    public void create(Context ctx) {
        try {
            // Envoyer une notification aux résidents
            // Ne pas oublier de prendre des informations sur la ficheProbleme (voir candidature)

            JsonObject newProject = jsonProjet.get("data").getAsJsonObject();

            // Todo: Penser à déplacer ça aussi au niveau de la création de projet
            //  Envoyer une requête afin de créer une notification pour le prestataire concerné
            String response = UseRequest.sendRequest(this.urlHead + "/notification/" + candidature.get("prestataire").getAsString() + "?userType=prestataire",
                    RequestType.POST, "{\"message\": \"Votre candidature pour projet " + newProject.get("titreProjet").getAsString() + " a été acceptée. Veuillez consulter" +
                            " votre liste des projets pour plus d'informations.\"}");

            if(response == null) {
                System.out.println("Une erreur est survenue lors de la création de la notification d'acceptation. Réponse nulle.");
                return;
            }

            JsonElement json = JsonParser.parseString(response);
            JsonObject jsonObject = json.getAsJsonObject();

            int statusCode = jsonObject.get("status").getAsInt();
            if (statusCode != 201) {
                // Todo: Changer les messages
                System.out.println("Une erreur est survenue lors de la création de la notification d'acceptation. Message d'erreur: " + jsonObject.get("data").getAsJsonObject().get("message").getAsString());
                return;
            } else {
                System.out.println("Notification d'acceptation créee avec succès.");
            }


            // Todo: Déplacer cette logique dans la partie de création
            //  Faire une boucle et envoyer une notification à chacun des abonnés. On ne se préoccupe pas que ça marche pour tout
            //  le monde.

            String bodyResidentsNotif = "{\"message\": \"De nouveaux travaux sont prévus dans votre quartier entre le " + DateManagement.formatIsoDate(newProject.get("dateDebut").getAsString()) + " et le" +
                    DateManagement.formatIsoDate(newProject.get("dateFin").getAsString()) + ". Les rues affectées sont les suivantes: " + newProject.get("ruesAffectees").getAsString() + ". Merci de nous faire confiance.\"}";

            for(JsonObject person : abonnesList) {
                String resIndividual = UseRequest.sendRequest(this.urlHead + "/notification/" + person.get("id").getAsString() + "?userType=resident",
                        RequestType.POST, bodyResidentsNotif);

                if(resIndividual == null) {
                    System.out.println("Une erreur est survenue lors de l'envoi de la notification à chaque résidents pour les avertir du projet. Réponse nulle.");
                    continue;
                }

                JsonElement jsonIndividual = JsonParser.parseString(resIndividual);
                JsonObject jsonObjectIndividual = jsonIndividual.getAsJsonObject();

                int statusCodeIndividual = jsonObjectIndividual.get("status").getAsInt();
                if (statusCodeIndividual != 201) {
                    System.out.println("Une erreur est survenue lors de l'envoi de la notification au résident pour l'avertir du projet. Message d'erreur: " + jsonObjectIndividual.get("data").getAsJsonObject().get("message").getAsString());
                }

            }

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

    public void delete(Context ctx) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }
}
