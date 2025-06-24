package ca.udem.maville.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ca.udem.maville.client.users.Prestataire;
import ca.udem.maville.client.users.PrioriteProbleme;
import ca.udem.maville.client.users.Quartier;
import ca.udem.maville.client.users.Resident;
import ca.udem.maville.client.users.TypeTravaux;

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


            private static final List<Resident> residents = new ArrayList<>();
            private static final List<Prestataire> prestataires = new ArrayList<>();
            private static final List<FicheProbleme> fiches = new ArrayList<>();
            private static final List<Projet> projets = new ArrayList<>();
        
            public static void main(String[] args) {
                System.out.println(introText);
                System.out.println(motto);
                initialiserDonnees();
                menuPrincipal();
            }
        
            private static void menuPrincipal() {
                while (true) {
                    System.out.println("\n=== Menu principal ===");
                    System.out.println("1. Se connecter en tant que Résident");
                    System.out.println("2. Se connecter en tant que Prestataire");
                    System.out.println("3. Se connecter en tant qu’Agent STPM");
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
                        case "3":
                            menuAgent();
                            break;
                        case "0":
                            System.out.println("Merci d'avoir utilisé MaVille !");
                            return;
                        default:
                            System.out.println("Choix invalide. Réessayez.");
                    }
                }
            }
        
            private static void menuResident() {
                while (true) {
                    System.out.println("\n[Menu Résident]");
                    System.out.println("1. Signaler un problème");
                    System.out.println("2. Voir mes signalements");
                    System.out.println("3. Consulter les projets");
                    System.out.println("4. Voir mes notifications");
                    System.out.println("0. Retour au menu principal");
                    System.out.print("Choix : ");
                    String choix = sc.nextLine();
                    switch (choix) {
                        case "1":
                            System.out.println("[Simulation] Signaler un problème...");
                            break;
                        case "2":
                            System.out.println("[Simulation] Liste de vos signalements...");
                            break;
                        case "3":
                            System.out.println("[Simulation] Liste des projets en cours...");
                            break;
                        case "4":
                            System.out.println("[Simulation] Vos notifications...");
                            break;
                        case "0":
                            return;
                        default:
                            System.out.println("Choix invalide. Réessayez.");
                    }
                }
            }
        
            private static void menuPrestataire() {
                while (true) {
                    System.out.println("\n[Menu Prestataire]");
                    System.out.println("1. Consulter les fiches disponibles");
                    System.out.println("2. Soumettre une candidature");
                    System.out.println("3. Modifier un projet");
                    System.out.println("4. Voir mes notifications");
                    System.out.println("0. Retour au menu principal");
                    System.out.print("Choix : ");
                    String choix = sc.nextLine();
                    switch (choix) {
                        case "1":
                            System.out.println("[Simulation] Liste des fiches disponibles...");
                            break;
                        case "2":
                            System.out.println("[Simulation] Soumission de candidature...");
                            break;
                        case "3":
                            System.out.println("[Simulation] Modification de projet...");
                            break;
                        case "4":
                            System.out.println("[Simulation] Vos notifications...");
                            break;
                        case "0":
                            return;
                        default:
                            System.out.println("Choix invalide. Réessayez.");
                    }
                }
            }
        
        /*   private static void menuAgent() {
        while (true) {
            System.out.println("\n[Menu Agent STPM]");
            System.out.println("1. Voir toutes les fiches problèmes");
            System.out.println("2. Valider/refuser des candidatures");
            System.out.println("3. Attribuer des priorités aléatoires");
            System.out.println("0. Retour au menu principal");
            System.out.print("Choix : ");
            String choix = sc.nextLine();
            switch (choix) {
                case "1":
                    for (FicheProbleme fiche : fiches) {
                        System.out.println("\n- Fiche: " + fiche.getDescription() + " (Priorité: " + fiche.getPriorite() + ", Statut: " + fiche.getStatut() + ")");
                    }
                    break;
                case "2":
                    for (FicheProbleme fiche : fiches) {
                        for (Candidature c : fiche.getCandidatures()) {
                            System.out.println("\nCandidature: " + c.getTitreProjet() + " | Prestataire: " + c.getNumEntreprise() + " | Statut: " + c.getStatut());
                            if (c.getStatut() == StatutCandidature.enAttente) {
                                System.out.print("Accepter (A) / Refuser (R) ? ");
                                String reponse = sc.nextLine();
                                if (reponse.equalsIgnoreCase("A")) {
                                    c.changerStatut(StatutCandidature.acceptee);
                                    System.out.println("✔ Acceptée");
                                } else if (reponse.equalsIgnoreCase("R")) {
                                    c.changerStatut(StatutCandidature.refusee);
                                    System.out.println("✘ Refusée");
                                } else {
                                    System.out.println("Réponse invalide. Ignorée.");
                                }
                            }
                        }
                    }
                    break;
                case "3":
                    Random rand = new Random();
                    PrioriteProbleme[] niveaux = PrioriteProbleme.values();
                    for (FicheProbleme fiche : fiches) {
                        PrioriteProbleme p = niveaux[rand.nextInt(niveaux.length)];
                        fiche.changerPriorite(p);
                        System.out.println("Fiche: " + fiche.getDescription() + " ➝ Nouvelle priorité: " + p);
                    }
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Choix invalide. Réessayez.");
            }
        }
    }
        
            private static void initialiserDonnees() {
                residents.add(new Resident("Alice", "alice@example.com", "123 rue Ontario", "H2X 1Y4"));
                residents.add(new Resident("Bob", "bob@example.com", "456 avenue du Parc", "H2W 2N2"));
                residents.add(new Resident("Clara", "clara@example.com", "789 boul. Saint-Laurent", "H2Y 3Z3"));
        
                prestataires.add(new Prestataire("Entreprise ABC", "abc@travaux.com", "NE123456",
                        List.of(Quartier.VILLE_MARIE, Quartier.LE_PLATEAU_MONT_ROYAL),
                        List.of(TypeTravaux.TRAVAUX_ROUTIERS)));
        
                prestataires.add(new Prestataire("Groupe BTP", "btp@chantier.ca", "NE654321",
                        List.of(Quartier.LASALLE, Quartier.ROSEMONT_LA_PETITE_PATRIE),
                        List.of(TypeTravaux.CONSTRUCTION_RENOVATION)));
        
                prestataires.add(new Prestataire("Signal Pro", "signal@eclairage.net", "NE777777",
                        List.of(Quartier.SAINT_LAURENT),
                        List.of(TypeTravaux.SIGNALISATION_ECLAIRAGE)));*/

          //  }
}