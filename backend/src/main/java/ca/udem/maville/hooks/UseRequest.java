package ca.udem.maville.hooks;

import ca.udem.maville.utils.RequestType;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;


/**
 * Une classe utilitaire pour faciliter et centraliser la logique d'envoi des requêtes HTTP.
 * Elle permet de faire des requête et de gérer les réponses à travers sa méthode sendRequest.
 */
public final class UseRequest {

    /**
     * Il s'agit d'une méthode pour envoyer une requête à un serveur, n'importe lequel et recevoir sa réponse.
     * Elle permet de centraliser cette logique pour ne pas avoir à la répéter à plusieurs
     * endroits dans le code.
     *
     * @param urlName qui représente le path auquel la requête est envoyée.
     * @param requestMethod qui représente la méthode HTTP de la requête
     * @param body qui est l'information à envoyer au serveur sous forme de chaine JSON
     * @return une chaine JSON avec deux champs:
     *         - status: qui définit le statut de la requête
     *         - data: qui représente l'information ou la réponse renvoyée par le serveur.
     */
    public static String sendRequest(String urlName, RequestType requestMethod, String body) {
        try {
            URI uri = URI.create(urlName);

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json; charset=UTF-8");

            // Choix de la méthode HTTP
            if (requestMethod == RequestType.GET) {
                requestBuilder.GET();
            } else {
                HttpRequest.BodyPublisher bodyPublisher =
                        (body != null) ? HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8)
                                : HttpRequest.BodyPublishers.noBody();

                // Utilise .method(name, bodyPublisher) pour supporter PATCH
                requestBuilder.method(requestMethod.name(), bodyPublisher);
            }

            HttpRequest request = requestBuilder.build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();
            String responseBody = response.body();

            JsonElement data = JsonParser.parseString(responseBody);

            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("status", status);
            responseJson.add("data", data);

            return responseJson.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
