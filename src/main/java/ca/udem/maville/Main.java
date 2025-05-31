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

    private static int ask(int min, int max) {
        prompt();
        int choice = sc.nextInt();

        while(choice < min || choice > max) {
            System.out.print("Veuillez entrer un chiffre valide : ");
            try {
                choice = sc.nextInt();
            } catch (Exception e) {
                return ask(min, max);
            }
        }

        return choice;
    }



    private static void menu(String profile) {
        if (profile.equals("resident")) {
            System.out.println("\n----- Menu principal -----");
            System.out.println(
                    "1. Voir tous les projets en cours\n" +
                    "2. Signaler un problème\n" +
                    "3. Voir les notifications\n" +
                    "0. Mettre fin à l'application\n"
            );

            int choice = ask(0, 3);

            switch (choice) {
                case 0 :
                    System.exit(0);
                    break;
                default :
                    System.exit(0);
                    break;
            }
        }
        else if (profile.equals("provider")) {
            System.out.println("\n----- Menu principal -----");
            System.out.println(
                    "1. Projets\n" +
                    "2. Offres\n" +
                    "3. Voir les notifications\n" +
                    "0. Mettre fin à l'application\n"
            );

            int choice = ask(0, 3);

            switch (choice) {
                case 1 :
                    System.out.println("\n----- Section Projets -----");
                    System.out.println(
                            "1. Voir tous les projets en cours\n" +
                            "2. Projets personnels\n" +
                            "0. Retourner au menu principal\n"
                    );

                    int choice1 = ask(0, 2);

                    switch (choice1) {
                        case 1 :
                            System.exit(0);
                            break;
                        case 2 :
                            System.out.println("\n----- Section Projets personnels -----");
                            System.out.println(
                                    "1. Consulter un projet\n" +
                                    "2. Modifier les informations d'un projet\n" +
                                    "0. Retourner au menu principal\n"
                            );

                            int choice2 = ask(0, 2);

                            switch (choice2) {
                                case 0 :
                                    menu("provider");
                                    break;
                                default :
                                    System.exit(0);
                                    break;
                            }
                            break;
                        case 0 :
                            menu("provider");
                            break;
                    }

                    break;
                case 2 :
                    System.out.println("\n----- Section Offres -----");
                    System.out.println(
                            "1. Voir toutes les offres\n" +
                            "2. Déposer une nouvelle candidature\n" +
                            "3. Consulter l'état d'une candidature\n" +
                            "4. Modifier les informations d'une candidature\n" +
                            "5. Supprimer une candidature\n" +
                            "0. Retourner au menu principal\n"
                    );

                    int choice2 = ask(0, 5);
                    switch (choice2) {
                        case 0 :
                            menu("provider");
                            break;
                        default :
                            System.exit(0);
                            break;
                    }
                default:
                    System.exit(0);
            }
        }
    }
    
    
    public static void main(String[] args) {
        System.out.println(introText);
        System.out.println(motto);

        // Profile choice
        System.out.println("\n----- Connexion -----");
        System.out.println("Quel est votre type de profil ?\n" +
                "1. Résident\n" +
                "2. Prestataire\n" +
                "0. Mettre fin à l'application\n"
        );

        int choice = ask(0, 2);
        

        switch (choice) {
            case 1:
                menu("resident");
                break;
            case 2:
                menu("provider");
                break;
            case 0:
                System.exit(0);
                break;
        }

    }
}