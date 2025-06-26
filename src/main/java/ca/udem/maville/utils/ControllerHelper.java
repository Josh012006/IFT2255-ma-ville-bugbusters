package ca.udem.maville.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

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
}
