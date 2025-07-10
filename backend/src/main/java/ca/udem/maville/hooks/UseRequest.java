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

public final class UseRequest {

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
