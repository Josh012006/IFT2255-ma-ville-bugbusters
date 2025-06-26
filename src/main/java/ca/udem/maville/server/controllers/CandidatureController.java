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
        try {
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

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    public void create(Context ctx) {
        try {
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

            // Ajouter la candidature à la liste pour le prestataire concerné
            JsonObject patchBody = new JsonObject();
            JsonArray toAdd = new JsonArray();
            toAdd.add(id);
            patchBody.add("candidatures", toAdd);

            // Envoyer la requête pour modifier la liste des candidatures
            String response = UseRequest.sendRequest(this.urlHead + "/prestataire/" + idPrestataire + "?replace=false", RequestType.PATCH, patchBody.toString());

            if(response == null) {
                throw new Exception("Une erreur est survenue lors de l'ajout de la candidature pour le prestataire. Réponse nulle.");
            }

            JsonElement elemCandidature = JsonParser.parseString(response);
            JsonObject jsonCandidature = elemCandidature.getAsJsonObject();

            int statusCodeProjet = jsonCandidature.get("status").getAsInt();
            if (statusCodeProjet != 200) {
                throw new Exception("Une erreur est survenue lors de l'ajout de la candidature pour le prestataire. Message d'erreur: " + jsonCandidature.get("data").getAsJsonObject().get("message").getAsString());
            }

            // Lancer la logique d'acceptation ou de refus aléatoire d'une candidature
            // en arrière-plan sans bloquer aucun Thread.
            CompletableFuture.runAsync(() -> {
                this.validateCandidature(newCandidature);
            });


            // Renvoyer la candidature avec un message de succès
            ctx.status(201).json(newCandidature).contentType("application/json");

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
                // Rassembler les informations et envoyer une requête pour créer un projet

                JsonObject projectToCreate = new JsonObject();

                projectToCreate.addProperty("statut", "enCours");
                projectToCreate.addProperty("dateDebut", candidature.get("dateDebut").getAsString());
                projectToCreate.addProperty("dateFin", candidature.get("dateFin").getAsString());

                projectToCreate.addProperty("prestataire", candidature.get("prestataire").getAsString());
                projectToCreate.addProperty("ruesAffectees", candidature.get("ruesAffectees").getAsString() );
                projectToCreate.addProperty("typesTravaux", candidature.get("typesTravaux").getAsString());
                projectToCreate.addProperty("cout", candidature.get("coutEstime").getAsString());
                projectToCreate.addProperty("description", candidature.get("description").getAsString());
                projectToCreate.addProperty("titreProjet", candidature.get("titreProjet").getAsString());

                // Récupérer le quartier et les abonnés à partir de la ficheProbleme
                String idFicheProblem = candidature.get("ficheProblem").getAsString();

                if(idFicheProblem == null) {
                    System.out.println("Erreur lors de la création du projet : ID de la fiche problème manquant.");
                    return;
                }

                String ficheResponse = UseRequest.sendRequest(this.urlHead + "/probleme/" + idFicheProblem, RequestType.GET, null);

                if(ficheResponse == null) {
                    System.out.println("Une erreur est survenue lors de la recherche de la fiche problème. Réponse nulle.");
                    return;
                }

                JsonElement elemFiche = JsonParser.parseString(ficheResponse);
                JsonObject jsonFiche = elemFiche.getAsJsonObject();

                int statusCodeFiche = jsonFiche.get("status").getAsInt();
                if (statusCodeFiche != 200) {
                    System.out.println("Une erreur est survenue lors de la recherche de la fiche problème. Message d'erreur: " + jsonFiche.get("data").getAsJsonObject().get("message").getAsString());
                    return;
                }

                JsonObject data = jsonFiche.get("data").getAsJsonObject();

                String quartier = data.get("quartier").getAsString();
                JsonArray abonnes = data.get("residents").getAsJsonArray();

                projectToCreate.addProperty("quartier", quartier);
                projectToCreate.add("abonnes", abonnes);

                // Envoyer la requête pour créer un projet
                String projetResponse = UseRequest.sendRequest(this.urlHead + "/projet", RequestType.POST, projectToCreate.toString());

                if(projetResponse == null) {
                    System.out.println("Une erreur est survenue lors de la création du projet. Réponse nulle.");
                }

                JsonElement elemProjet = JsonParser.parseString(ficheResponse);
                JsonObject jsonProjet = elemProjet.getAsJsonObject();

                int statusCodeProjet = jsonProjet.get("status").getAsInt();
                if (statusCodeProjet != 201) {
                    System.out.println("Une erreur est survenue lors de la création du projet. Message d'erreur: " + jsonProjet.get("data").getAsJsonObject().get("message").getAsString());
                    return;
                }

                JsonObject newProject = jsonProjet.get("data").getAsJsonObject();

                // Envoyer une requête afin de créer une notification pour l'utilisateur concerné
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
                    System.out.println("Une erreur est survenue lors de la création de la notification d'acceptation. Message d'erreur: " + jsonObject.get("data").getAsJsonObject().get("message").getAsString());
                    return;
                }
                else {
                    System.out.println("Notification d'acceptation créee avec succès.");
                }

            } else {
                // Choisir une raison aléatoire de refus
                String[] refusalReasons = {"Soumission financière non compétitive.",
                        "Soumission financière non compétitive.", "Non-conformité aux normes ou aux méthodes imposées.", "Délais de réalisation trop longs.",
                        "Ressources humaines et matérielles inadéquates."};

                String randomRefusalReason = refusalReasons[RandomGeneration.getRandomUniformInt(0, 5)];

                //Envoyer une requête afin de créer une notification pour l'utilisateur concerné
                String response = UseRequest.sendRequest(this.urlHead + "/notification/" + candidature.get("prestataire").getAsString() + "?userType=prestataire",
                        RequestType.POST, "{\"message\": \"Votre candidature pour projet " + candidature.get("titreProjet").getAsString() + " a été refusée pour la " +
                                "raison suivante: " + randomRefusalReason +"\"}");

                if(response == null) {
                    System.out.println("Une erreur est survenue lors de la création de la notification de refus. Réponse nulle.");
                    return;
                }

                JsonElement json = JsonParser.parseString(response);
                JsonObject jsonObject = json.getAsJsonObject();

                int statusCode = jsonObject.get("status").getAsInt();
                if (statusCode != 201) {
                    System.out.println("Une erreur est survenue lors de la création de la notification de refus. Message d'erreur: " + jsonObject.get("data").getAsJsonObject().get("message").getAsString());
                    return;
                }
                else {
                    System.out.println("Notification de refus créée avec succès.");
                }

            }

            System.out.println("Candidature " + id + " a reçu correctement statut: " + candidature.get("statut").getAsString());

        } catch (InterruptedException e) {
            // Réinterrompre le thread
            Thread.currentThread().interrupt();
            System.err.println("Le traitement de validation a été interrompu.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
