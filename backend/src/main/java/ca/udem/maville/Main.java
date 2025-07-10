package ca.udem.maville;

import ca.udem.maville.server.Server;

public class Main {

    public static void main(String[] args) {
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
