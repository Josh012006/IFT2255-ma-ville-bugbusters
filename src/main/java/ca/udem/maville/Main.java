package ca.udem.maville;


import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.server.Database;
import ca.udem.maville.server.Server;
import ca.udem.maville.utils.RequestType;

public class Main {

    public static void main(String[] args) {
        Database db = new Database();
//        Thread serverThread = new Thread(() -> {
//            // Création et démarrage du serveur Javalin
//            final Server server = new Server();
//            server.start();
//        });
//
//        serverThread.setDaemon(true); // Pour que le thread du serveur s'arrête quand le thread principal s'arrête
//        serverThread.start();
//
//        // Lancer l'application client en ligne de commande ici
//        // Appel à MaVille.java
    }
}