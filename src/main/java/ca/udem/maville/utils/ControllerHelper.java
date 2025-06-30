package ca.udem.maville.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.Map;

public final class ControllerHelper {

    public static boolean sameKeysSameTypes(JsonObject a, JsonObject b) {
        // Vérifier que les deux objets ont les mêmes clés
        if (!a.keySet().equals(b.keySet())) {
            return false;
        }

        // Pour chaque clé, vérifier que les types sont identiques
        for (String clé : a.keySet()) {
            JsonElement va = a.get(clé);
            JsonElement vb = b.get(clé);

            // Vérifie le type JSON
            if (!sameTypeJson(va, vb)) {
                return false;
            }
        }

        return true;
    }

    // Dis si deux champs ont le même type
    public static boolean sameTypeJson(JsonElement a, JsonElement b) {
        return typeJson(a).equals(typeJson(b));
    }

    // Retourne un type sous forme de String : "string", "number", "boolean", "array", "object", "null"
    private static String typeJson(JsonElement e) {
        if (e.isJsonNull()) return "null";
        if (e.isJsonPrimitive()) {
            JsonPrimitive p = e.getAsJsonPrimitive();
            if (p.isString()) return "string";
            if (p.isBoolean()) return "boolean";
            if (p.isNumber()) return "number";
        }
        if (e.isJsonArray()) return "array";
        if (e.isJsonObject()) return "object";
        return "unknown";
    }


    // Enlève les duplicatas
    public static ArrayList<String> removeDuplicates(ArrayList<String> tab) {
        ArrayList<String> result = new ArrayList<>();

        ArrayList<String> seenIDs = new ArrayList<>();

        for (String o : tab) {
            if (!seenIDs.contains(o)) {
                result.add(o);
                seenIDs.add(o);
            }
        }

        return result;

    }


    // Vérifie qu'une string est dans un JsonArray
    public static boolean containsValue(JsonArray array, String valeur) {
        for (JsonElement element : array) {
            if (element.getAsString().equals(valeur)) {
                return true;
            }
        }
        return false;
    }


    // Faire la logique de patch pour le controller. Permet d'éviter la répétition de code
    public static boolean patchLogic(JsonObject updates, boolean replace, JsonObject toPatch, Context ctx) {
        for (Map.Entry<String, JsonElement> entry : updates.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();

            if (value.isJsonArray()) {
                JsonArray nouvelles = value.getAsJsonArray();

                if (replace || !toPatch.has(key)) {
                    // Remplacer complètement
                    toPatch.add(key, nouvelles);
                } else {
                    // Ajouter sans doublons
                    JsonArray existantes = toPatch.getAsJsonArray(key);
                    for (JsonElement elem : nouvelles) {
                        if (!ControllerHelper.containsValue(existantes, elem.getAsString())) {
                            existantes.add(elem);
                        }
                    }
                }

            } else {
                // Champ simple → remplacement direct
                if(!ControllerHelper.sameTypeJson(toPatch.get(key), value)) {
                    ctx.status(400).result("{\"message\": \"Le champ " + key + " envoyé n'a pas le bon type.\"}").contentType("application/json");
                    return false;
                }
                toPatch.add(key, value);
            }
        }
        return true;
    }

}
