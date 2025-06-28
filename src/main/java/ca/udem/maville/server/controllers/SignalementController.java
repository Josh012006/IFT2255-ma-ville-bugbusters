package ca.udem.maville.server.controllers;

import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.server.Database;
import ca.udem.maville.utils.ControllerHelper;
import ca.udem.maville.utils.RandomGeneration;
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

    /**
     * Cette route permet de récupérer tous les signalements d'un résident.
     * Le paramètre de path user représente l'id du résident.
     * @param ctx qui représente le contexte de la requête
     */
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
                    data.add(JsonParser.parseString(signal).getAsJsonObject());
                }
            }

            // Renvoyer les signalements trouvés dans un tableau
            ctx.status(200).json(data).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de créer un nouveau signalement pour un résident.
     * Le body doit contenir toutes informations nécessaires notamment :
     * - typeProbleme: qui représente le type travail requis sous forme Label
     * - quartier: qui représente le quartier affecté. Il est sous forme Label
     * - localisation: ici on s'intéressera principalement à la rue ou à l'adresse du résident
     * - description: la description du problème rencontré
     * - resident: l'id du résident faisant le signalement. Très important
     * Elle s'occupe automatiquement d'assigner les champs id, statut et dateSignalement
     * Elle implémente aussi un traitement automatique du signalement pour créer la fiche problème
     * avec une assignation aléatoire de priorité.
     * {@link #manageSignal(JsonObject)}
     * @param ctx représente le contexte de la requête
     */
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

    /**
     * Cette route permet de récupérer un signalement en particulier à
     * partir de son id.
     * @param ctx représente le contexte de la requête.
     */
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

    /**
     * Cette route permet de modifier seulement partiellement les informations
     * d'un signalement, connaissant son id.
     * Le body doit contenir les champs à modifier avec la nouvelle information.
     * Assurez vous que la nouvelle information a le bon type.
     * Elle nécessite également un queryParameter replace = true | false qui est utile pour les tableaux
     * notamment pour savoir s'il faut juste ajouter les éléments ou remplacer le tableau en entier.
     * La modification n'est possible que si le signalement n'a pas encore été traité.
     * @param ctx qui représente le contexte de la requête.
     */
    public void patch(Context ctx) {
        try {
            // La modification est possible seulement si pas encore traité
            String id = ctx.pathParam("id");
            boolean replace = Boolean.parseBoolean(ctx.queryParam("replace"));

            JsonObject updates = JsonParser.parseString(ctx.body()).getAsJsonObject();
            String strSignal = database.signalements.get(id);

            if (strSignal == null) {
                ctx.status(404).result("{\"message\": \"Signalement non retrouvé.\"}").contentType("application/json");
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

    /**
     * Cette route permet de remplacer complètement un signalement existant
     * par un autre avec de nouvelles informations, connaissant son id.
     * Le body doit contenir le nouveau signalement avec tous les champs présents et ayant le bon type Json.
     * La modification n'est possible que si le signalement n'est pas encore traité.
     * Je précise que l'objet envoyé en body doit vraiment tout contenir. Contrairement à la fonction
     * {@link #create(Context)}, cette fonction ne génère aucune information automatiquement.
     * @param ctx qui représente le contexte de la requête.
     */
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

    /**
     * Cette route permet de supprimer un signalement à partir de son id
     * @param ctx qui représente le contexte de la requête.
     */
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

    /**
     * Une fonction qui après avoir attendu un certain temps (1.5s) attribue une priorité aléatoire
     * au problème. Cette priorité est choisie avec une tendance plus pousée à faible et moyen.
     * Elle crée ensuite une fiche problème. La création de la fiche problème inclut l'envoi d'une notification
     * à tous les résidents ayant fait des signalements en rapport et aussi à tous les prestataires
     * qui pourraient être intéressés par le problème. Voir {@link ProblemController#create(Context)}.
     * @param signal qui représente le nouveau signalement à traiter
     */
    private void manageSignal(JsonObject signal) {

        try {
            // Simule une attente de traitement
            Thread.sleep(1500);

            // Assigner une priorité aléatoire entre 0, 1 et 2 (faible, moyenne, élevée)
            // avec une tendance plus grande à 0 et 1
            int[] priorites = {0, 0, 0, 0, 1, 1, 2};
            int randomPriorite = priorites[RandomGeneration.getRandomUniformInt(0, 7)];

            // Rassembler les informations pour la fiche problème
            JsonObject problemToCreate = new JsonObject();

            problemToCreate.addProperty("typeTravaux", signal.get("typeProbleme").getAsString());
            problemToCreate.addProperty("localisation", signal.get("localisation").getAsString());
            problemToCreate.addProperty("description", signal.get("description").getAsString());
            problemToCreate.addProperty("quartier", signal.get("quartier").getAsString());
            problemToCreate.addProperty("priorite", randomPriorite);

            JsonArray signals = new JsonArray();
            signals.add(signal.get("id").getAsString());
            problemToCreate.add("signalements", signals);
            JsonArray residents = new JsonArray();
            residents.add(signal.get("resident").getAsString());
            problemToCreate.add("residents", residents);


            // Envoyer une requête pour créer une fiche problème
            String problemResponse = UseRequest.sendRequest(this.urlHead + "/probleme", RequestType.POST, problemToCreate.toString());

            if(problemResponse == null) {
                System.out.println("Une erreur est survenue lors de la création de la fiche problème. Réponse nulle.");
                return;
            }

            JsonElement elemProblem = JsonParser.parseString(problemResponse);
            JsonObject jsonProblem = elemProblem.getAsJsonObject();

            int statusCodeProblem = jsonProblem.get("status").getAsInt();
            if (statusCodeProblem != 201) {
                System.out.println("Une erreur est survenue lors de la création de la fiche problème. Message d'erreur: " + jsonProblem.get("data").getAsJsonObject().get("message").getAsString());
                return;
            }

            System.out.println("Fiche problème créée avec succès.");

        } catch (InterruptedException e) {
            // Réinterrompre le thread
            Thread.currentThread().interrupt();
            System.err.println("Le traitement du signalement a été interrompu.");
        } catch (Exception e) {
            System.out.println("Une erreur lors du traitement du signalement.");
            e.printStackTrace();
        }
    }
}






