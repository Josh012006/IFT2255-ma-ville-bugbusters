package ca.udem.maville;

import ca.udem.maville.server.Server;

import java.util.TimeZone;

public class Main {

    public static void main(String[] args) {

        // Définir le réseau horaire dans toute l'application
        TimeZone.setDefault(TimeZone.getTimeZone("America/Toronto"));

        // On lance juste le serveur
        final Server server = new Server();
        Thread serverThread = new Thread(() -> {
            try {
                server.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "javalin-server");
        serverThread.start();
    }
}
