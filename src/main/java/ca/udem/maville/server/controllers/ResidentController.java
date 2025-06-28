package ca.udem.maville.server.controllers;

import ca.udem.maville.server.Database;
import ca.udem.maville.utils.ControllerHelper;
import ca.udem.maville.utils.Quartier;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.javalin.http.Context;

import java.util.Map;

public class ResidentController {

    public Database database;
    public String urlHead;

    public ResidentController(Database database, String urlHead) {
        this.database = database;
        this.urlHead = urlHead;
    }


    /**
     * Cette route permet de récupérer tous les résidents d'un quartier en particulier.
     * Le paramètre de path region contient le nom du quartier. Il doit sous forme de l'enum.
     * @param ctx qui représente le contexte de la requête
     */
    public void getByRegion(Context ctx) {
        try {
            String regionParam = ctx.pathParam("region");

            if (regionParam.isEmpty()) {
                ctx.status(400).result("{\"message\": \"Le quartier est nécessaire. Veuillez le préciser dans le path.\"}").contentType("application/json");
                return;
            }

            Quartier regionEnum = Quartier.valueOf(regionParam);
            String region = regionEnum.getLabel();

            JsonArray jsonResidents = new JsonArray();

            for(String strResident : database.residents.values()) {
                if(strResident != null) {
                    JsonObject jsonResident = JsonParser.parseString(strResident).getAsJsonObject();
                    if(jsonResident.get("quartier").getAsString().equals(region)) {
                        jsonResidents.add(jsonResident);
                    }
                }
            }

            // Renvoyer les résidents
            ctx.status(200).json(jsonResidents).contentType("application/json");

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            ctx.status(404).result("{\"message\": \"Quartier inconnu.\"}").contentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de récupérer les informations sur un résident donné
     * en fonction de son id.
     * @param ctx qui représente le contexte de la requête.
     */
    public void get(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            String strResident = database.residents.get(id);

            if (strResident == null) {
                ctx.status(404).result("{\"message\": \"Résident non retrouvé.\"}").contentType("application/json");
                return;
            }

            JsonObject jsonResident = JsonParser.parseString(strResident).getAsJsonObject();

            // Renvoyer le résident
            ctx.status(200).json(jsonResident).contentType("application/json");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de modifier seulement partiellement les informations
     * d'un résident, connaissant son id.
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
            String strResident = database.residents.get(id);
            if(strResident == null) {
                ctx.status(404).result("{\"message\": \"Résident non retrouvé.\"}").contentType("application/json");
                return;
            }

            JsonObject resident = JsonParser.parseString(strResident).getAsJsonObject();

            // Appeler la logique de patch
            boolean ok = ControllerHelper.patchLogic(updates, replace, resident, ctx);

            if(!ok) {
                return;
            }

            // Message de succès
            database.residents.put(id, resident.toString());
            ctx.status(200).json(resident).contentType("application/json");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }

    /**
     * Cette route permet de remplacer complètement un résident enregistré
     * par un autre avec de nouvelles informations, connaissant son id.
     * Le body doit contenir le nouveau résident avec tous les champs présents et ayant le bon type Json.
     * Je précise que l'objet envoyé en body doit vraiment tout contenir.
     * @param ctx qui représente le contexte de la requête.
     */
    public void update(Context ctx) {
        try {
            String id = ctx.pathParam("id");

            String strResident = database.residents.get(id);

            if (strResident == null) {
                ctx.status(404).result("{\"message\": \"Résident non retrouvé.\"}").contentType("application/json");
                return;
            }

            // Traiter le nouveau résident
            String rawJson = ctx.body();

            JsonElement element = JsonParser.parseString(rawJson);

            // Vérifie si c'est bien un objet
            if (!element.isJsonObject()) {
                ctx.status(400).result("{\"message\": \"Format JSON invalide.\"}").contentType("application/json");
                return;
            }

            // Vérifier que toutes les entrées sont là
            JsonObject newResident = element.getAsJsonObject();

            JsonObject actualResident = JsonParser.parseString(strResident).getAsJsonObject();

            if(!ControllerHelper.sameKeysSameTypes(actualResident, newResident)) {
                ctx.status(400).result("{\"message\": \"Format d'objet ne correspondant pas à celui d'un résident. Vérifiez que les champs envoyés sont corrects et que les types sont bons.\"}").contentType("application/json");
                return;
            }

            // M'assurer que l'id reste le même
            JsonObject updatedResident = newResident;
            updatedResident.addProperty("id", id);

            database.residents.put(id, updatedResident.toString());

            // Renvoyer le nouvel objet
            ctx.status(200).json(updatedResident).contentType("application/json");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"message\": \"Une erreur est interne survenue! Veuillez réessayer plus tard.\"}").contentType("application/json");
        }
    }
}
