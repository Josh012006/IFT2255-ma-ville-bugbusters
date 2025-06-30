package ca.udem.maville.client;

import java.util.Scanner;

import ca.udem.maville.utils.AdaptedGson;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ca.udem.maville.client.users.Prestataire;
import ca.udem.maville.client.users.Resident;
import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.utils.RequestType;

public class MaVille {

    private final static Scanner sc = new Scanner(System.in);

    private final static String introText = "\n" +
            "__/\\\\\\\\____________/\\\\\\\\_________________/\\\\\\________/\\\\\\________/\\\\\\\\\\\\_____/\\\\\\\\\\\\___________________        \n" +
            " _\\/\\\\\\\\\\\\________/\\\\\\\\\\\\________________\\/\\\\\\_______\\/\\\\\\_______\\////\\\\\\____\\////\\\\\\___________________       \n" +
            "  _\\/\\\\\\//\\\\\\____/\\\\\\//\\\\\\________________\\//\\\\\\______/\\\\\\___/\\\\\\____\\/\\\\\\_______\\/\\\\\\___________________      \n" +
            "   _\\/\\\\\\\\///\\\\\\/\\\\\\/_\\/\\\\\\__/\\\\\\\\\\\\\\\\\\_____\\//\\\\\\____/\\\\\\___\\///_____\\/\\\\\\_______\\/\\\\\\________/\\\\\\\\\\\\\\\\__     \n" +
            "    _\\/\\\\\\__\\///\\\\\\/___\\/\\\\\\_\\////////\\\\\\_____\\//\\\\\\__/\\\\\\_____/\\\\\\____\\/\\\\\\_______\\/\\\\\\______/\\\\\\/////\\\\\\_    \n" +
            "     _\\/\\\\\\____\\///_____\\/\\\\\\___/\\\\\\\\\\\\\\\\\\\\_____\\//\\\\\\/\\\\\\_____\\/\\\\\\____\\/\\\\\\_______\\/\\\\\\_____/\\\\\\\\\\\\\\\\\\\\\\__   \n" +
            "      _\\/\\\\\\_____________\\/\\\\\\__/\\\\\\/////\\\\\\______\\//\\\\\\\\\\______\\/\\\\\\____\\/\\\\\\_______\\/\\\\\\____\\//\\\\///////___  \n" +
            "       _\\/\\\\\\_____________\\/\\\\\\_\\//\\\\\\\\\\\\\\\\/\\\\______\\//\\\\\\_______\\/\\\\\\__/\\\\\\\\\\\\\\\\\\__/\\\\\\\\\\\\\\\\\\__\\//\\\\\\\\\\\\\\\\\\\\_ \n" +
            "        _\\///______________\\///___\\////////\\//________\\///________\\///__\\/////////__\\/////////____\\//////////__\n";

    private final static String motto = "                   \"Une ville n’est pas seulement des rues et des immeubles,\n" +
            "                       c’est un espace vivant que vous façonnez chaque jour.\"\n\n                                 Bienvenue dans \uD83C\uDF06 MaVille\n";


    public static final String urlHead = "http://localhost:7070/api";


    public static void demarrer() throws InterruptedException {
        // Faire une attente minme pour s'assurer que les initialisations au niveau du serveur sont faites
        System.out.println("Démarrage de l'application...");
        Thread.sleep(4000);

        System.out.println(introText);
        System.out.println(motto);

        while (true) {
            System.out.println("\n===== Menu principal =====");
            System.out.println("1. Se connecter en tant que Résident");
            System.out.println("2. Se connecter en tant que Prestataire");
            System.out.println("0. Quitter");
            System.out.print("Choix : ");

            String choix = sc.nextLine();
            switch (choix) {
                case "1":
                    menuResident();
                    break;
                case "2":
                    menuPrestataire();
                    break;
                case "0":
                    System.out.println("\nMerci d'avoir utilisé MaVille!");
                    return;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }


    private static void menuResident() {

        // Récupérer le résident
        String responseUser = UseRequest.sendRequest(urlHead + "/resident/7e57d004-2b97-0e7a-b45f-5387367791cd" , RequestType.GET, null);

        if(responseUser == null) {
            System.out.println("Une erreur est survenue! Veuillez réessayer plus tard.");
            return;
        }

        JsonElement elemUser = JsonParser.parseString(responseUser);
        JsonObject jsonUser = elemUser.getAsJsonObject();

        if(jsonUser.get("status").getAsInt() != 200) {
            System.out.println("Une erreur est survenue! Veuillez réessayer plus tard.");
            return;
        }

        Gson gson = AdaptedGson.getGsonInstance();
        Resident resident = gson.fromJson(jsonUser.get("data").getAsJsonObject(), Resident.class);

        while (true) {
            System.out.println("\n===== Menu Résident =====");
            System.out.println("1. Signaler un problème");
            System.out.println("2. Voir mes signalements");
            System.out.println("3. Consulter les projets");
            System.out.println("4. Voir mes notifications");
            System.out.println("0. Retour au menu principal");
            System.out.print("Choix: ");

            String choix = sc.nextLine();

            switch (choix) {
                case "1":
                    System.out.println("\n----- Signalement d'un problème -----\n");
                    resident.signalerProbleme();
                    break;
                case "2":
                    resident.recupererSignalements();
                    break;
                case "3":
                    Projet.consulterProjets();
                    break;
                case "4":
                    resident.consulterNotifications();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }

    private static void menuPrestataire() {

        // Récupérer le prestataire
        String responseUser = UseRequest.sendRequest(urlHead + "/prestataire/a3d78c70b0f84b8dbff1913b5d213e38" , RequestType.GET, null);

        if(responseUser == null) {
            System.out.println("Une erreur est survenue! Veuillez réessayer plus tard.");
            return;
        }

        JsonElement elemUser = JsonParser.parseString(responseUser);
        JsonObject jsonUser = elemUser.getAsJsonObject();

        if(jsonUser.get("status").getAsInt() != 200) {
            System.out.println("Une erreur est survenue! Veuillez réessayer plus tard.");
            return;
        }

        Gson gson = AdaptedGson.getGsonInstance();
        Prestataire prestataire = gson.fromJson(jsonUser.get("data").getAsJsonObject(), Prestataire.class);

        while (true) {
            System.out.println("\n===== Menu Prestataire =====");
            System.out.println("1. Consulter les fiches disponibles concernant mon domaine d'expertise");
            System.out.println("2. Soumettre une candidature");
            System.out.println("3. Voir et modifier un projet");
            System.out.println("4. Voir mes notifications");
            System.out.println("0. Retour au menu principal");
            System.out.print("Choix: ");

            String choix = sc.nextLine();

            switch (choix) {
                case "1":
                    prestataire.voirFichesDisponibles();
                    break;
                case "2":
                    System.out.println("\n----- Soumission de candidature -----\n");
                    prestataire.soumettreCandidature();
                    break;
                case "3":
                    prestataire.modifierProjet();
                    break;
                case "4":
                    prestataire.consulterNotifications();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }

}