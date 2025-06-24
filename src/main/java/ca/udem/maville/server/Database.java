package ca.udem.maville.server;

import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.utils.RequestType;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

public class Database {

    public final Map<String, String> residents = new HashMap<>();
    public final Map<String, String> prestataires = new HashMap<>();

    public final Map<String, String> candidatures = new HashMap<>();
    public final Map<String, String> signalements = new HashMap<>();
    public final Map<String, String> problemes = new HashMap<>();

    public final Map<String, String> projets = new HashMap<>();

    public final Map<String, String> notifications = new HashMap<>();



    public Database() {
        initialize();
    }

    private void initialize() {
        // Todo : Ajouter 3 résidents, 3 prestataires, 3 problèmes et 3 candidatures à des projets de travaux.

        // Récupérer les travaux en cours et les ajouter aux projets
        String publicApi = "https://donnees.montreal.ca/api/3/action/datastore_search?resource_id=cc41b532-f12d-40fb-9f55-eb58c9a2b12b";
        String response = UseRequest.sendRequest(publicApi, RequestType.GET, null);

        JsonElement json = JsonParser.parseString(response);
        JsonObject jsonObject = json.getAsJsonObject();

        int statusCode = jsonObject.get("status").getAsInt();
        if (statusCode == 200) {
            JsonObject data = jsonObject.get("data").getAsJsonObject();
            // Todo: traiter du résultat pour l'ajouter aux projets

        }
    }
}
