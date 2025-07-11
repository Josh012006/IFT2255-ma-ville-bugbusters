package ca.udem.maville.server.controllers.users;

import ca.udem.maville.utils.ControllerHelper;
import ca.udem.maville.utils.Quartier;
import ca.udem.maville.utils.TypesTravaux;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.javalin.http.Context;

import org.slf4j.Logger;


public class PrestataireController {
    public String urlHead;
    public Logger logger;

    public PrestataireController(String urlHead, Logger logger) {
        this.urlHead = urlHead;
        this.logger = logger;
    }

    /**
     * Cette route permet de récupérer tous les prestataires qui pourraient être intéressés
     * par un problème déclaré.
     * Le paramètre de path region contient le nom du quartier où le problème est situé. Il doit sous forme de l'enum.
     * Le paramètre de path type contient le nom du type de travail requis. Il doit être sous forme enum.
     * @param ctx qui représente le contexte de la requête
     */
    public void getInterested(Context ctx) {
        // Todo: Changer la logique de getInterested pour match les abonnements
        try {
            String regionParam = ctx.pathParam("region");
            String typeParam = ctx.pathParam("type");

            if (regionParam.isEmpty()) {
                ctx.status(400).result("{\"message\": \"Le quartier est nécessaire. Veuillez le préciser dans le path.\"}").contentType("application/json");
                return;
            }

            if (typeParam.isEmpty()) {
                ctx.status(400).result("{\"message\": \"Le type de travail est nécessaire. Veuillez le préciser dans le path.\"}").contentType("application/json");
                return;
            }

            Quartier regionEnum = Quartier.valueOf(regionParam);
            String region = regionEnum.getLabel();
            TypesTravaux typeEnum = TypesTravaux.valueOf(typeParam);
            String type = typeEnum.getLabel();

            JsonArray jsonPrestataires = new JsonArray();

            for(String strPrestataire : database.prestataires.values()) {
                if(strPrestataire != null) {
                    JsonObject jsonPrestataire = JsonParser.parseString(strPrestataire).getAsJsonObject();
                    if(ControllerHelper.containsValue(jsonPrestataire.get("quartiers").getAsJsonArray(), region)
                            || ControllerHelper.containsValue(jsonPrestataire.get("typesTravaux").getAsJsonArray(), type)) {
                        jsonPrestataires.add(jsonPrestataire);
                    }
                }
            }

            // Renvoyer les prestataires
            ctx.status(200).json(jsonPrestataires).contentType("application/json");

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            ctx.status(404).result("{\"message\": \"Quartier ou type travail inconnu.\"}").contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de récupérer les informations sur un prestataire donné
     * en fonction de son id.
     * @param ctx qui représente le contexte de la requête.
     */
    public void get(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            String strPrestataire = database.prestataires.get(id);

            if (strPrestataire == null) {
                ctx.status(404).result("{\"message\": \"Prestataire non retrouvé.\"}").contentType("application/json");
                return;
            }

            JsonObject jsonPrestataire = JsonParser.parseString(strPrestataire).getAsJsonObject();

            // Renvoyer le prestataire
            ctx.status(200).json(jsonPrestataire).contentType("application/json");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de modifier seulement partiellement les informations
     * d'un prestataire, connaissant son id.
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
            String strPrestataire = database.prestataires.get(id);

            if(strPrestataire == null) {
                ctx.status(404).result("{\"message\": \"Prestataire non retrouvé.\"}").contentType("application/json");
                return;
            }

            JsonObject prestataire = JsonParser.parseString(strPrestataire).getAsJsonObject();

            // Appeler la logique de patch
            boolean ok = ControllerHelper.patchLogic(updates, replace, prestataire, ctx);

            if(!ok) {
                return;
            }

            // Message de succès
            database.prestataires.put(id, prestataire.toString());
            ctx.status(200).json(prestataire).contentType("application/json");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }
    /**
     * Cette route permet de remplacer complètement un prestataire enregistré
     * par un autre avec de nouvelles informations, connaissant son id.
     * Le body doit contenir le nouveau prestataire avec tous les champs présents et ayant le bon type Json.
     * Je précise que l'objet envoyé en body doit vraiment tout contenir.
     * @param ctx qui représente le contexte de la requête.
     */
    public void update(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            String strPrestataire = database.prestataires.get(id);

            if (strPrestataire == null) {
                ctx.status(404).result("{\"message\": \"Prestataire non retrouvé.\"}").contentType("application/json");
                return;
            }

            // Traiter le nouveau prestataire
            String rawJson = ctx.body();

            JsonElement element = JsonParser.parseString(rawJson);

            // Vérifie si c'est bien un objet
            if (!element.isJsonObject()) {
                ctx.status(400).result("{\"message\": \"Format JSON invalide.\"}").contentType("application/json");
                return;
            }

            // Vérifier que toutes les entrées sont là
            JsonObject newPrestataire = element.getAsJsonObject();

            JsonObject actualPrestataire = JsonParser.parseString(strPrestataire).getAsJsonObject();

            if(!ControllerHelper.sameKeysSameTypes(actualPrestataire, newPrestataire)) {
                ctx.status(400).result("{\"message\": \"Format d'objet ne correspondant pas à celui d'un prestataire. Vérifiez que les champs envoyés sont corrects et que les types sont bons.\"}").contentType("application/json");
                return;
            }

            // M'assurer que l'id reste le même
            JsonObject updatedPrestataire = newPrestataire;
            updatedPrestataire.addProperty("id", id);

            database.prestataires.put(id, updatedPrestataire.toString());

            // Renvoyer le nouvel objet
            ctx.status(200).json(updatedPrestataire).contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }
}
