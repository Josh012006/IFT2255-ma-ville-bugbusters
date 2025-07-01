package ca.udem.maville;

import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.server.Server;
import ca.udem.maville.utils.RequestType;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    static Server server;
    static final int PORT = 7070;
    static final String BASE_URL = "http://localhost:" + PORT + "/api";

    @BeforeAll
    public static void startServer() {
        server = new Server(PORT);
        server.start();
    }

    @AfterAll
    public static void stopServer() {
        server.stop();
    }

    // Tests du controller des residents (Test Cyreanne)
    @Test
    public void testResidentGet1() {
        // Tester que le statut est 404 lorsque l'id n'existe pas
        String response = UseRequest.sendRequest(BASE_URL + "/resident/non_existant_id", RequestType.GET, null);
        assertNotNull(response);
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        assertEquals(404, jsonObject.get("status").getAsInt());
    }
    @Test
    public void testResidentGet2() {
        // Tester que la base de données initialise bien le résident
        String response = UseRequest.sendRequest(BASE_URL + "/resident/7e57d004-2b97-0e7a-b45f-5387367791cd", RequestType.GET, null);
        assertNotNull(response);
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        assertEquals(200, jsonObject.get("status").getAsInt());
        assertEquals("7e57d004-2b97-0e7a-b45f-5387367791cd", jsonObject.get("data").getAsJsonObject().get("id").getAsString());
    }
    @Test
    public void testResidentGetByRegion() {
        // Tester la récupération des résidents par région
        String response = UseRequest.sendRequest(BASE_URL + "/resident/getByRegion/villeMarie", RequestType.GET, null);
        assertNotNull(response);
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        assertEquals(200, jsonObject.get("status").getAsInt());
        assertTrue(jsonObject.get("data").isJsonArray());
    }





    // Tests du controller des prestataires (Test Lallia)
    @Test
    public void testPrestataireGet1() {
        // Tester que le statut est 404 lorsque l'id n'existe pas
        String response = UseRequest.sendRequest(BASE_URL + "/prestataire/non_existant_id", RequestType.GET, null);
        assertNotNull(response);
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        assertEquals(404, jsonObject.get("status").getAsInt());
    }
    @Test
    public void testPrestataireGet2() {
        // Tester que la base de données initialise bien le prestataire
        String response = UseRequest.sendRequest(BASE_URL + "/prestataire/a3d78c70b0f84b8dbff1913b5d213e38", RequestType.GET, null);
        assertNotNull(response);
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        assertEquals(200, jsonObject.get("status").getAsInt());
        assertEquals("a3d78c70b0f84b8dbff1913b5d213e38", jsonObject.get("data").getAsJsonObject().get("id").getAsString());
    }
    @Test
    public void testPrestataireGetInterested() {
        // Tester la récupération des prestataires intéressés par un qurtier ou un type de travail
        String response = UseRequest.sendRequest(BASE_URL + "/prestataire/getInterested/villeMarie/travauxRoutiers", RequestType.GET, null);
        assertNotNull(response);
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        assertEquals(200, jsonObject.get("status").getAsInt());
        assertTrue(jsonObject.get("data").isJsonArray());
    }





    // Tests du controller des notifications (Test Roxanne)
    @Test
    public void testNotificationCreate1() {
        // Tester que le statut est 404 lorsque l'id n'existe pas
        String response = UseRequest.sendRequest(BASE_URL + "/notification/non_existant_id?userType=resident", RequestType.POST, "{\"message\": \"Ma notification.\"}");
        assertNotNull(response);
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        assertEquals(404, jsonObject.get("status").getAsInt());
    }
    @Test
    public void testNotificationCreate2() {
        // Tester que la notification est bien créée
        String response = UseRequest.sendRequest(BASE_URL + "/notification/7e57d004-2b97-0e7a-b45f-5387367791cd?userType=resident", RequestType.POST, "{\"message\": \"Ma notification.\"}");
        assertNotNull(response);
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        assertEquals(201, jsonObject.get("status").getAsInt());
        assertEquals("Ma notification.", jsonObject.get("data").getAsJsonObject().get("message").getAsString());
    }
    @Test
    public void testNotificationGet() {
        // Tester que les notifications sont récupérées
        String response = UseRequest.sendRequest(BASE_URL + "/notification/getAll/7e57d004-2b97-0e7a-b45f-5387367791cd?userType=resident", RequestType.GET, null);
        assertNotNull(response);
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        assertEquals(200, jsonObject.get("status").getAsInt());
        assertTrue(jsonObject.get("data").isJsonArray());
    }





    // Tests du controller des projets (Test Zachary)
    @Test
    public void testProjetGetAll() {
        // Tester que les projets sont bien initialisés à partir de l'API de Montréal
        String response = UseRequest.sendRequest(BASE_URL + "/projet/getAll", RequestType.GET, null);
        assertNotNull(response);
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        assertEquals(200, jsonObject.get("status").getAsInt());
        assertTrue(jsonObject.get("data").isJsonArray());
        assertFalse(jsonObject.get("data").getAsJsonArray().isEmpty());
    }
    @Test
    public void testProjetGetByPrestataire1() {
        // Tester qu'un prestataire inconnu n'a pas de projet
        String response = UseRequest.sendRequest(BASE_URL + "/projet/getByPrestataire/non_existant_id", RequestType.GET, null);
        assertNotNull(response);
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        assertEquals(200, jsonObject.get("status").getAsInt());
        assertTrue(jsonObject.get("data").getAsJsonArray().isEmpty());
    }

    @Test
    public void testProjetGetByPrestataire2() {
        // Tester que les projets de notre prestataire sont bien initialisés
        String response = UseRequest.sendRequest(BASE_URL + "/projet/getByPrestataire/a3d78c70b0f84b8dbff1913b5d213e38", RequestType.GET, null);
        assertNotNull(response);
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        assertEquals(200, jsonObject.get("status").getAsInt());
        assertTrue(jsonObject.get("data").isJsonArray());
        assertFalse(jsonObject.get("data").getAsJsonArray().isEmpty());
    }





    // Tests du signalements (Test Josué)
    @Test
    public void testSignalementGetAll1() {
        // Tester que les signalements n'existe pas pour un résident non existant
        String response = UseRequest.sendRequest(BASE_URL + "/signalement/getAll/non_existant_id", RequestType.GET, null);
        assertNotNull(response);
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        assertEquals(404, jsonObject.get("status").getAsInt());
    }
    @Test
    public void testSignalementGetAll2() {
        // Tester que les signalements de notre résident sont bien initialisées
        String response = UseRequest.sendRequest(BASE_URL + "/signalement/getAll/7e57d004-2b97-0e7a-b45f-5387367791cd", RequestType.GET, null);
        assertNotNull(response);
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        assertEquals(200, jsonObject.get("status").getAsInt());
        assertTrue(jsonObject.get("data").isJsonArray());
        assertFalse(jsonObject.get("data").getAsJsonArray().isEmpty());
    }
    @Test
    public void testSignalementPatch() {
        // Tester que les changements aux signalements fonctionnent
        String response = UseRequest.sendRequest(BASE_URL + "/signalement/b9e45c5f-72ac-4e8c-b38e-36f22c92db3e", RequestType.PATCH, "{\"statut\": \"traité\"}");
        assertNotNull(response);
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        assertEquals(200, jsonObject.get("status").getAsInt());
        assertEquals("traité", jsonObject.get("data").getAsJsonObject().get("statut").getAsString());
    }
}