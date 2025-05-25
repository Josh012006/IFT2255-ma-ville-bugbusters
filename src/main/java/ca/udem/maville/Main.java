package ca.udem.maville;

import java.util.Scanner;

public class Main {
    
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


    private static void prompt() {System.out.print("Entrez le chiffre correspondant à votre choix et appuyez sur la touche 'Entrer' : ");}

    private static void errorCase(int min, int max) {
        int choice = -1;
        do {
            System.out.print("Veuillez entrer un chiffre valide : ");
            try {
                choice = sc.nextInt();
            } catch (Exception e) {
                errorCase(min, max);
            }
        } while(choice < min || choice > max);
    }
    
    
    public static void main(String[] args) {
        System.out.println(introText);
        System.out.println(motto);

        // Profile choice
        System.out.println("----- Connexion -----\nQuel est votre type de profil ?\n1. Résident\n2. Prestataire\n0. Mettre fin à l'application\n");
        prompt();
        int choice = sc.nextInt();
        

        switch (choice) {
            case 1:
                System.out.println("----- Menu principal -----");
                break;
            case 2:
                System.out.println("----- Menu principal -----");
                break;
            case 0:
                System.exit(0);
                break;
            default:
                errorCase(0, 2);
        }

    }
}