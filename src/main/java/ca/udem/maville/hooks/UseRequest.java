package ca.udem.maville.hooks;

import ca.udem.maville.utils.RequestType;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public final class UseRequest {
    public static String sendRequest(String urlName, RequestType requestMethod, String body) {
        try {
            URI uri = new URI(urlName);
            URL url = uri.toURL();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestMethod.name());
            conn.setDoOutput(true); // Important pour autoriser l'envoi de données

            if (requestMethod != RequestType.GET && body != null) {
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = body.getBytes("UTF-8");
                    os.write(input, 0, input.length);
                }
            }

            //Getting the response code
            int responseCode = conn.getResponseCode();

            //Getting the response body
            StringBuilder responseBuilder = getStringBuilder(conn, responseCode);
            String responseString = responseBuilder.toString();

            int status = responseCode;
            JsonElement data = JsonParser.parseString(responseString);

            // Création du nouvel objet JSON de réponse
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("status", status);
            responseJson.add("data", data);     // Ajoute l'objet JSON "data" directement

            // Pour obtenir la chaîne JSON finale
            String jsonResponseString = responseJson.toString();

            return jsonResponseString;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @NotNull
    private static StringBuilder getStringBuilder(HttpURLConnection conn, int responseCode) throws IOException {
        InputStream responseBody = null;

        if(responseCode != HttpURLConnection.HTTP_OK) {
            responseBody = conn.getErrorStream();
        }
        else {
            responseBody = conn.getInputStream();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody, StandardCharsets.UTF_8));
        StringBuilder responseBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            responseBuilder.append(line);
        }
        return responseBuilder;
    }
}
