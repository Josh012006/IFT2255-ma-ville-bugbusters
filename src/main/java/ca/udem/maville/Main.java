package ca.udem.maville;

import ca.udem.maville.server.Server;

public class Main {

    public static void main(String[] args) {
        Thread serverThread = new Thread(() -> {
            // Création et démarrage du serveur Javalin
            final Server server = new Server();
            server.start();
        });

        serverThread.setDaemon(true); // Pour que le thread du serveur s'arrête quand le thread principal s'arrête
        serverThread.start();

        // Lancer l'application client en ligne de commande ici
        // Appel à MaVille.java

    }
}