package ca.udem.maville;


import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.utils.RequestType;
import server.Database;
import utils.JsonStorage;
import client.users.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialiser les données
        Database.initialize(); // vide ou avec seedData()
        JsonStorage.loadDatabase("database.json");

        // Lancer l'application (menu principal est dans MaVille)
        MaVille app = new MaVille();  // ← tu dois avoir une méthode public dans MaVille
        app.demarrer();               // ← méthode qui affiche le menu principal

        // Sauvegarder en quittant
        JsonStorage.saveDatabase("database.json");
        System.out.println("✅ Données sauvegardées. À bientôt !");
    }
}
