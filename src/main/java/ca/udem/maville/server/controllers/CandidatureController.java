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


import java.util.concurrent.CompletableFuture;

public class CandidatureController {
    public Database database;
    public String urlHead;

    public CandidatureController(Database database, String urlHead) {
        this.database = database;
        this.urlHead = urlHead;
    }

    /**
     * Cette route permet de récupérer toutes les candidatures d'un prestaire.
     * Le paramètre de path user représente l'id du prestataire.
     * @param ctx qui représente le contexte de la requête
     */
    public void getAll(Context ctx) {
        try {
            String idUser = ctx.pathParam("user");

            // Envoyer une requête pour récupérer le prestataire

            String responseUser = UseRequest.sendRequest(this.urlHead + "/prestataire/" + idUser , RequestType.GET, null);

            if(responseUser == null) {
                throw new Exception("Une erreur est survenue lors de la récupération du prestataire. Réponse nulle.");
            }

            JsonElement elemUser = JsonParser.parseString(responseUser);
            JsonObject jsonUser = elemUser.getAsJsonObject();

            int statuscode = jsonUser.get("status").getAsInt();
            if (statuscode == 404) {
                ctx.status(404).result("{\"message\": \"Aucun prestataire avec un tel ID retrouvé.\"}").contentType("application/json");
                return;
            } else if(statuscode != 200) {
                throw new Exception("Une erreur est survenue lors de la récupération du prestataire. Message d'erreur: " + jsonUser.get("data").getAsJsonObject().get("message").getAsString());
            }

            JsonObject prestataire = jsonUser.get("data").getAsJsonObject();

            JsonArray candidatures = prestataire.get("candidatures").getAsJsonArray();

            JsonArray data = new JsonArray();
            // Récupérer à partir des ids, chaque candidature
            for (JsonElement candidatureId : candidatures) {
                String candidatureIdString = candidatureId.getAsString();
                String candidature = database.candidatures.get(candidatureIdString);
                if(candidature != null) {
                    data.add(JsonParser.parseString(candidature).getAsJsonObject());
                }
            }

            // Renvoyer les candidatures trouvées dans un tableau
            ctx.status(200).json(data).contentType("application/json");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }


    /**
     * Cette route permet de créer une nouvelle candidature pour un prestataire.
     * Le body doit contenir toutes informations nécessaires notamment :
     * - prestataire: qui est l'id du prestataire qui dépose la candidature. Très important.
     * - ficheProbleme: qui est l'id de la fiche problème concernée
     * - numeroEntreprise: qui représente le numéro d'entreprise du prestataire
     * - titreProjet: qui est le titre du projet proposé
     * - description: La description du projet proposé
     * - typeTravaux: qui représente le type travail à réaliser sous forme Label
     * - coutEstime: Le cout proposé pour le projet
     * - dateDebut: qui doit être sous format ISO
     * - dateFin: qui doit être sous format ISO
     * - ruesAffectees: Les rues affectées par les travaux sous forme de String
     * Elle s'occupe automatiquement d'assigner les champs id, statut et dateSoumission
     * Elle implémente aussi une validation aléatoire de la candidature à travers la méthode
     * {@link #validateCandidature(JsonObject)}
     * @param ctx représente le contexte de la requête
     */
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
            newCandidature.addProperty("dateSoumission", Instant.now().toString());


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

            int statusCodeCandidature = jsonCandidature.get("status").getAsInt();
            if (statusCodeCandidature != 200) {
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


    /**
     * Cette route permet de récupérer une candidature en particulier à
     * partir de son id.
     * @param ctx représente le contexte de la requête.
     */
    public void get(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            String strCandidature = database.candidatures.get(id);

            if (strCandidature == null) {
                ctx.status(404).result("{\"message\": \"Candidature non retrouvée.\"}").contentType("application/json");
                return;
            }

            JsonObject jsonCandidature = JsonParser.parseString(strCandidature).getAsJsonObject();

            // Renvoyer la candidature
            ctx.status(200).json(jsonCandidature).contentType("application/json");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }


    /**
     * Cette route permet de modifier seulement partiellement les informations
     * d'une candidature, connaissant son id.
     * Le body doit contenir les champs à modifier avec la nouvelle information.
     * Assurez vous que la nouvelle information a le bon type.
     * Elle nécessite également un queryParameter replace = true | false qui est utile pour les tableaux
     * notamment pour savoir s'il faut juste ajouter les éléments ou remplacer le tableau en entier.
     * La modification n'est possible que si la candidature n'a pas encore été traitée.
     * @param ctx qui représente le contexte de la requête.
     */
    public void patch(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            boolean replace = Boolean.parseBoolean(ctx.queryParam("replace"));

            JsonObject updates = JsonParser.parseString(ctx.body()).getAsJsonObject();
            String strCandidature = database.candidatures.get(id);

            if (strCandidature == null) {
                ctx.status(404).result("{\"message\": \"Candidature non retrouvée.\"}").contentType("application/json");
                return;
            }

            JsonObject candidature = JsonParser.parseString(strCandidature).getAsJsonObject();

            // La modification est possible seulement si pas encore traité
            if(!candidature.get("statut").getAsString().equals("enAttente")) {
                ctx.status(400).result("{\"message\": \"Impossible de modifier une candidature déjà traitée. Veuillez vérifier vos notifications.\"}").contentType("application/json");
                return;
            }

            // Appeler la logique de patch
            boolean ok = ControllerHelper.patchLogic(updates, replace, candidature, ctx);

            if(!ok) {
                return;
            }

            // Message de succès
            database.candidatures.put(id, candidature.toString());
            ctx.status(200).json(candidature).contentType("application/json");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }


    /**
     * Cette route permet de remplacer complètement une candidature existante
     * par une autre avec de nouvelles informations, connaissant son id.
     * Le body doit contenir la nouvelle candidature avec tous les champs présents et ayant le bon type Json.
     * La modification n'est possible que si la candidature n'est pas encore traitées.
     * Je précise que l'objet envoyé en body doit vraiment tout contenir. Contrairement à la fonction
     * {@link #create(Context)}, cette fonction ne génère aucune information automatiquement.
     * @param ctx qui représente le contexte de la requête.
     */
    public void update(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            String strCandidature = database.candidatures.get(id);

            if (strCandidature == null) {
                ctx.status(404).result("{\"message\": \"Candidature non retrouvée.\"}").contentType("application/json");
                return;
            }

            // Traiter la nouvelle candidature
            String rawJson = ctx.body();

            JsonElement element = JsonParser.parseString(rawJson);

            // Vérifie si c'est bien un objet
            if (!element.isJsonObject()) {
                ctx.status(400).result("{\"message\": \"Format JSON invalide.\"}").contentType("application/json");
                return;
            }

            // Vérifier que toutes les entrées sont là
            JsonObject newCandidature = element.getAsJsonObject();

            JsonObject actualCandidature = JsonParser.parseString(strCandidature).getAsJsonObject();

            // La modification est possible seulement si pas encore traité
            if(!actualCandidature.get("statut").getAsString().equals("enAttente")) {
                ctx.status(400).result("{\"message\": \"Impossible de modifier une candidature déjà traitée. Veuillez vérifier vos notifications.\"}").contentType("application/json");
                return;
            }

            if(!ControllerHelper.sameKeysSameTypes(actualCandidature, newCandidature)) {
                ctx.status(400).result("{\"message\": \"Format d'objet ne correspondant pas à celui d'une candidature. Vérifiez que les champs envoyés sont corrects et que les types sont bons.\"}").contentType("application/json");
                return;
            }

            // M'assurer que l'id reste le même
            JsonObject updatedCandidature = newCandidature;
            updatedCandidature.addProperty("id", id);

            database.candidatures.put(id, updatedCandidature.toString());

            // Renvoyer le nouvel objet
            ctx.status(200).json(updatedCandidature).contentType("application/json");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }


    /**
     * Cette route permet de supprimer une candidature à partir de son id
     * @param ctx qui représente le contexte de la requête.
     */
    public void delete(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            String strCandidature = database.candidatures.get(id);

            if (strCandidature == null) {
                ctx.status(404).result("{\"message\": \"Candidature non retrouvée.\"}").contentType("application/json");
                return;
            }

            database.candidatures.remove(id);

            // Renvoyer la réponse de succès
            ctx.status(200).result("{\"message\": \"Suppression réalisée avec succès.\"}").contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }


    /**
     * Une fonction qui après avoir attendue un certain temps (1.5s) valide ou refuse la candidature.
     * En cas d'acceptation, elle crée un nouveau projet. La création du projet inclut l'envoi des notifications
     * aux résidents ayant signalé le problème et au prestataire ayant proposé la candidature. {@link ProjectController#create(Context)}
     * En cas de refus, une raison aléatoire de refus est choisie et une notification explicative est envoyée
     * au prestataire.
     * @param candidature qui représente la nouvelle candidature à valider
     */
    private void validateCandidature(JsonObject candidature) {
        try {
            // Simule une attente de traitement
            Thread.sleep(1500);

            // Logique de validation aléatoire
            boolean accepted = Math.random() < 0.85;
            candidature.addProperty("statut", accepted ? "acceptée" : "refusée");

            // Mise à jour dans la "base de données"
            String id = candidature.get("id").getAsString();
            database.candidatures.put(id, candidature.toString());

            if (accepted) {
                // Rassembler les informations et envoyer une requête pour créer un projet

                JsonObject projectToCreate = new JsonObject();

                projectToCreate.addProperty("dateDebut", candidature.get("dateDebut").getAsString());
                projectToCreate.addProperty("dateFin", candidature.get("dateFin").getAsString());

                projectToCreate.addProperty("prestataire", candidature.get("prestataire").getAsString());
                projectToCreate.addProperty("ruesAffectees", candidature.get("ruesAffectees").getAsString() );
                projectToCreate.addProperty("typeTravaux", candidature.get("typeTravaux").getAsString());
                projectToCreate.addProperty("cout", candidature.get("coutEstime").getAsString());
                projectToCreate.addProperty("description", candidature.get("description").getAsString());
                projectToCreate.addProperty("titreProjet", candidature.get("titreProjet").getAsString());

                // Récupérer le quartier et les abonnés à partir de la ficheProbleme
                String idFicheProblem = candidature.get("ficheProbleme").getAsString();

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
                projectToCreate.addProperty("quartier", quartier);



                // Former les abonnés à partir de ceux qui étaient déjà abonnés à la fiche problème mais aussi
                // à partir de tous les résidents du quartier. Sans duplicata.
                JsonArray abonnesSignal = data.get("residents").getAsJsonArray();

                // Récupérer les résidents du quartier
                String responseRegionResidents = UseRequest.sendRequest(this.urlHead + "/resident/getByRegion/" + Quartier.fromLabel(quartier), RequestType.GET, null);

                if(responseRegionResidents == null) {
                    System.out.println("Une erreur est survenue lors de la récupération des résidents de la région. Réponse nulle.");
                    return;
                }

                JsonElement jsonRegionResidents = JsonParser.parseString(responseRegionResidents);
                JsonObject jsonObjectRegionResidents = jsonRegionResidents.getAsJsonObject();

                int statusCodeRegionResidents = jsonObjectRegionResidents.get("status").getAsInt();
                if (statusCodeRegionResidents != 200) {
                    System.out.println("Une erreur est survenue lors de la récupération des résidents de la région. Message d'erreur: " + jsonObjectRegionResidents.get("data").getAsJsonObject().get("message").getAsString());
                    return;
                }

                JsonArray regionResidents = jsonObjectRegionResidents.get("data").getAsJsonArray();

                // Regrouper les potentiels abonnés
                ArrayList<JsonObject> people = new ArrayList<>();

                for (JsonElement element : abonnesSignal) {
                    people.add(element.getAsJsonObject());
                }
                for (JsonElement element : regionResidents) {
                    people.add(element.getAsJsonObject());
                }

                // Enlever les duplicatas
                ArrayList<JsonObject> abonnesList = ControllerHelper.removeDuplicates(people);

                // Enregistrer l'information sur le projet
                JsonArray abonnesJsonArray = new JsonArray();
                for (JsonObject abonne : abonnesList) {
                    abonnesJsonArray.add(abonne.get("id").getAsString());
                }
                projectToCreate.add("abonnes", abonnesJsonArray);



                // Envoyer la requête pour créer un projet
                String projetResponse = UseRequest.sendRequest(this.urlHead + "/projet", RequestType.POST, projectToCreate.toString());

                if(projetResponse == null) {
                    System.out.println("Une erreur est survenue lors de la création du projet. Réponse nulle.");
                    return;
                }

                JsonElement elemProjet = JsonParser.parseString(projetResponse);
                JsonObject jsonProjet = elemProjet.getAsJsonObject();

                int statusCodeProjet = jsonProjet.get("status").getAsInt();
                if (statusCodeProjet != 201) {
                    System.out.println("Une erreur est survenue lors de la création du projet. Message d'erreur: " + jsonProjet.get("data").getAsJsonObject().get("message").getAsString());
                    return;
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
            System.out.println("Une erreur lors de la validation de la candidature.");
            e.printStackTrace();
        }
    }
}
