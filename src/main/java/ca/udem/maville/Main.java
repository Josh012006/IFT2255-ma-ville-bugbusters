package ca.udem.maville;

import ca.udem.maville.client.MaVille;
import ca.udem.maville.server.Server;

public class Main {

    public static void main(String[] args) {
       try {
           // Création d'une instance de serveur
           final Server server = new Server();

           Thread serverThread = new Thread(() -> {
               // Démarrage du serveur Javalin
               server.start();
           });

           serverThread.setDaemon(true); // Pour que le thread du serveur s'arrête quand le thread principal s'arrête
           serverThread.start();

           // Lancer l'application client en ligne de commande ici
           // Appel à MaVille.java
           MaVille.demarrer();


           // Arrêter le serveur après le client
           if (server != null) {
               server.stop();
           }


       } catch (Exception e) {
           e.printStackTrace();
       }

    }
}