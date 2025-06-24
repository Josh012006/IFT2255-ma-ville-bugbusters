package ca.udem.maville;


import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.utils.RequestType;

public class Main {

    public static void main(String[] args) {
//        String testUrl = "https://donnees.montreal.ca/api/3/action/datastore_search?resource_id=cc41b532-f12d-40fb-9f55-eb58c9a2b12b";
//        String response = UseRequest.sendRequest(testUrl, RequestType.GET, null);
//
//        System.out.println(response);
        Thread serverThread = new Thread(() -> {
            // Créer le serveur Javalin ou autre backend ici
            // Instanciation de Server.java
        });

        serverThread.setDaemon(true); // Pour que le thread du serveur s'arrête quand le thread principal s'arrête
        serverThread.start();

        // Lancer l'application client en ligne de commande ici
        // Appel à MaVille.java
    }
}