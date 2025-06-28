package ca.udem.maville.server.controllers;

import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.server.Database;
import ca.udem.maville.utils.ControllerHelper;
import ca.udem.maville.utils.DateManagement;
import ca.udem.maville.utils.RequestType;
import ca.udem.maville.utils.UniqueID;
import com.google.gson.JsonArray;
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

    /**
     * Cette route permet de récupérer tous les travaux prévus.
     * @param ctx qui représente le contexte de la requête
     */
    public void getAll(Context ctx) {
        try {
            JsonArray allProjects = new JsonArray();
            for(String strProject: database.projets.values()) {
                allProjects.add(JsonParser.parseString(strProject).getAsJsonObject());
            }

            // Envoyer le message de succès
            ctx.status(200).json(allProjects).contentType("application/json");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de récupérer tous les projets d'un prestataire en particulier.
     * Le paramètre de path user précise l'id du prestataire.
     * @param ctx qui représente le contexte de la requête
     */
    public void getByPrestataire(Context ctx) {
        try {

            String idUser = ctx.pathParam("user");

            JsonArray presProjects = new JsonArray();
            for(String strProject: database.projets.values()) {
                JsonObject myProject = JsonParser.parseString(strProject).getAsJsonObject();
                if(myProject.get("prestataire").getAsString().equals(idUser)) {
                    presProjects.add(myProject);
                }
            }

            // Envoyer le message de succès
            ctx.status(200).json(presProjects).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de créer un nouveau projet pour un prestataire donné.
     * Le body doit contenir toutes informations nécessaires notamment :
     * - prestataire: qui est l'id du prestataire auquel le projet appartient
     * - ficheProbleme: qui est l'id de la fiche problème concernée
     * - quartier: qui représente le quartier affecté par les travaux
     * - titreProjet: qui est le titre du projet
     * - description: La description du projet
     * - typeTravaux: qui représente le type travail à réaliser sous forme Label
     * - cout: Le cout du projet
     * - dateDebut: qui doit être sous format ISO
     * - dateFin: qui doit être sous format ISO
     * - ruesAffectees: Les rues affectées par les travaux sous forme de String
     * - abonnes: Qui représente tous les résidents abonnés au projet
     * Elle s'occupe automatiquement d'assigner les champs id et statut
     * Elle inclut l'envoi des notifications aux résidents ayant signalé le problème et au
     * prestataire ayant proposé la candidature.
     * @param ctx qui représente le contexte de la requête.
     */
    public void create(Context ctx) {
        try {

            // Récupérer les informations et ajouter le statut et l'id unique
            String rawJson = ctx.body();

            JsonElement element = JsonParser.parseString(rawJson);

            // Vérifie si c'est bien un objet
            if (!element.isJsonObject()) {
                ctx.status(400).result("{\"message\": \"Format JSON invalide.\"}").contentType("application/json");
                return;
            }

            // Modifier l'objet pour ajouter un id et toute information nécessaire
            JsonObject newProject = element.getAsJsonObject();

            String id = UniqueID.generateUniqueID();

            newProject.addProperty("id", id);
            newProject.addProperty("statut", "enCours");

            // Ajouter le nouveau projet à la base de données
            database.projets.put(id, newProject.toString());

            // Marquer la fiche comme traitée
            String responsePatch = UseRequest.sendRequest(this.urlHead + "/probleme/" + newProject.get("ficheProbleme").getAsString() + "?replace=false",
                    RequestType.PATCH, "{\"statut\": \"traité\"}");

            if(responsePatch == null) {
                throw new Exception("Une erreur est survenue lors du changement de statut de la fiche problème. Réponse nulle.");
            }

            JsonElement jsonPatch = JsonParser.parseString(responsePatch);
            JsonObject jsonObjectPatch = jsonPatch.getAsJsonObject();

            int statusCodePatch = jsonObjectPatch.get("status").getAsInt();
            if (statusCodePatch != 200) {
                throw  new Exception("Une erreur est survenue lors changement de statut de la fiche problème signalement. Message d'erreur: " + jsonObjectPatch.get("data").getAsJsonObject().get("message").getAsString());
            }


            // Envoyer une requête afin de créer une notification pour le prestataire concerné
            String responsePrestataire = UseRequest.sendRequest(this.urlHead + "/notification/" + newProject.get("prestataire").getAsString() + "?userType=prestataire",
                    RequestType.POST, "{\"message\": \"Votre candidature pour projet " + newProject.get("titreProjet").getAsString() + " a été acceptée. Veuillez consulter" +
                            " votre liste des projets pour plus d'informations.\"}");

            if(responsePrestataire == null) {
                throw new Exception("Une erreur est survenue lors de la création de la notification de création de projet pour le prestataire. Réponse nulle.");
            }

            JsonElement jsonPrestataire = JsonParser.parseString(responsePrestataire);
            JsonObject jsonObjectPrestataire = jsonPrestataire.getAsJsonObject();

            int statusCodePrestataire = jsonObjectPrestataire.get("status").getAsInt();
            if (statusCodePrestataire != 201) {
                throw new Exception("Une erreur est survenue lors de la création de la notification de création de projet pour le prestataire. Message d'erreur: " + jsonObjectPrestataire.get("data").getAsJsonObject().get("message").getAsString());
            }


            // Faire une boucle et envoyer une notification à chacun des abonnés. On ne se préoccupe pas
            // que ça marche pour tout le monde.

            String bodyResidentsNotif = "{\"message\": \"De nouveaux travaux sont prévus dans votre quartier entre le " + DateManagement.formatIsoDate(newProject.get("dateDebut").getAsString()) + " et le" +
                    DateManagement.formatIsoDate(newProject.get("dateFin").getAsString()) + ". Les rues affectées sont les suivantes: " + newProject.get("ruesAffectees").getAsString() + ". Merci de nous faire confiance.\"}";

            for(JsonElement personElement : newProject.get("abonnes").getAsJsonArray()) {
                JsonObject person = personElement.getAsJsonObject();
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

            // Renvoyer le projet pour marquer le succès
            ctx.status(201).json(newProject).contentType("application/json");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de récupérer un projet en particulier
     * grâce à son id.
     * @param ctx représente le contexte de la requête.
     */
    public void get(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            String strProjet = database.projets.get(id);

            if (strProjet == null) {
                ctx.status(404).result("{\"message\": \"Projet non retrouvé.\"}").contentType("application/json");
                return;
            }

            JsonObject jsonProjet = JsonParser.parseString(strProjet).getAsJsonObject();

            // Renvoyer le projet
            ctx.status(200).json(jsonProjet).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de modifier seulement partiellement les informations
     * d'un projet, connaissant son id.
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
            String strProjet = database.projets.get(id);
            if(strProjet == null) {
                ctx.status(404).result("{\"message\": \"Projet non retrouvé.\"}").contentType("application/json");
                return;
            }

            JsonObject projet = JsonParser.parseString(strProjet).getAsJsonObject();

            // Appeler la logique de patch
            boolean ok = ControllerHelper.patchLogic(updates, replace, projet, ctx);

            if(!ok) {
                return;
            }

            // Message de succès
            database.projets.put(id, projet.toString());
            ctx.status(200).json(projet).contentType("application/json");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de remplacer complètement un projet existant
     * par un autre avec de nouvelles informations, connaissant son id.
     * Le body doit contenir le nouveau projet avec tous les champs présents et ayant le bon type Json.
     * Je précise que l'objet envoyé en body doit vraiment tout contenir. Contrairement à la fonction
     * {@link #create(Context)}, cette fonction ne génère aucune information automatiquement.
     * @param ctx qui représente le contexte de la requête.
     */
    public void update(Context ctx) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de supprimer un projet à partir de son id
     * @param ctx qui représente le contexte de la requête.
     */
    public void delete(Context ctx) {
        try {

            String id = ctx.pathParam("id");

            String strProjet = database.projets.get(id);

            if (strProjet == null) {
                ctx.status(404).result("{\"message\": \"Projet non retrouvé.\"}").contentType("application/json");
                return;
            }

            // Traiter le nouveau projet
            String rawJson = ctx.body();

            JsonElement element = JsonParser.parseString(rawJson);

            // Vérifie si c'est bien un objet
            if (!element.isJsonObject()) {
                ctx.status(400).result("{\"message\": \"Format JSON invalide.\"}").contentType("application/json");
                return;
            }

            // Vérifier que toutes les entrées sont là
            JsonObject newProjet = element.getAsJsonObject();

            JsonObject actualProjet = JsonParser.parseString(strProjet).getAsJsonObject();

            if(!ControllerHelper.sameKeysSameTypes(actualProjet, newProjet)) {
                ctx.status(400).result("{\"message\": \"Format d'objet ne correspondant pas à celui d'un projet. Vérifiez que les champs envoyés sont corrects et que les types sont bons.\"}").contentType("application/json");
                return;
            }

            // M'assurer que l'id reste le même
            JsonObject updatedProjet = newProjet;
            updatedProjet.addProperty("id", id);

            database.projets.put(id, updatedProjet.toString());

            // Renvoyer le nouvel objet
            ctx.status(200).json(updatedProjet).contentType("application/json");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }
}
