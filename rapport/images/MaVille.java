//import java.util.Scanner;
import java.util.*;

public class MaVille {
    private List<Probleme> problemes = new ArrayList<>();
    private List<Projet> projets = new ArrayList<>();
    private List<String> notifications = new ArrayList<>();

    public static void main (String[] args) {
        MaVille app = new MaVille();
        app.initialiserDonnees();
        app.lancer();
    }

    public void initialiserDonnees() {
        problemes.add(new Probleme("Nid de poule rue Ontario", "Plateau", "pierre@exemple.com"));
        problemes.add(new Probleme("Trottoir fissuré", "Villeray", "luc@exemple.com"));
        problemes.add(new Probleme("Feu de circulation brisé", "Hochelaga", "samira@exemple.com"));

    }

    public void lancer(){
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Bienvenue dans MaVille !");
            System.out.println("1. Resident");
            System.out.println("2. Prestataire");
            System.out.println("3. Quitter");
            String choix = scanner.nextLine();

            switch (choix) {
                case "1":
                    menuResident(scanner);
                    break;
                case "2":
                    menuPrestataire(scanner);
                    break;
                case "3":
                    System.out.println("Merci d'avoir utilisé MaVille");
                    return;
            
                default:
                System.out.println("Choix invalide.");
                    
            }
            
        }
    }
    
    public void menuResident(Scanner scanner){
        while (true){
            System.out.println("\n=== Menu Résident ===");
            System.out.println("1. Consulter les travaux");
            System.out.println("2. Rechercher des travaux");
            System.out.println("3. S’abonner à une rue/quartier");
            System.out.println("4. Voir les notifications");
            System.out.println("5. Signaler un problème");
            System.out.println("6. Retour");

            String choix = scanner.nextLine();
            switch (choix) {
                case "1":afficherTravaux(); break;
                case "2": rechercherTravaux(scanner); break;
                case "3": abonnementNotification(scanner); break;
                case "4": voirNotifications(); break;
                case "5": signalerProbleme(scanner); break;
                case "6": return;
                default: System.out.println("Choix invalide.");
                    
                   
            }
        }
    }

    public void menuPrestataire(Scanner scanner) {
        while (true) {
            System.out.println("\n=== Menu Prestataire ===");
            System.out.println("1. Consulter problèmes signalés");
            System.out.println("2. Soumettre un projet");
            System.out.println("3. Modifier un projet existant");
            System.out.println("4. Retour");

            String choix = scanner.nextLine();
            switch (choix) {
                case "1": afficherProblemes(); break;
                case "2": soumettreProjet(scanner); break;
                case "3": modifierProjet(scanner); break;
                case "4": return;
                default: System.out.println("Choix invalide.");
            }
        }
    }

    public void afficherTravaux() {
        if (projets.isEmpty()) {
            System.out.println("Aucun projet pour le moment.");
        } else {
            for (Projet p : projets) {
                System.out.println(p);
            }
        }
    }

    public void rechercherTravaux(Scanner scanner) {
        System.out.print("Entrer le quartier ou type de travaux à rechercher : ");
        String critere = scanner.nextLine().toLowerCase();
        for (Projet p : projets) {
            if (p.getQuartier().toLowerCase().contains(critere) || p.getType().toLowerCase().contains(critere)) {
                System.out.println(p);
            }
        }
    }

    public void abonnementNotification(Scanner scanner) {
        System.out.print("Entrez le nom de la rue ou quartier à suivre : ");
        String abonnement = scanner.nextLine();
        notifications.add("Abonnement activé pour : " + abonnement);
        System.out.println("Vous serez notifié pour les changements liés à : " + abonnement);
    }

    public void voirNotifications() {
        if (notifications.isEmpty()) {
            System.out.println("Aucune notification pour le moment.");
        } else {
            for (String notif : notifications) {
                System.out.println(notif);
            }
        }
    }

    public void signalerProbleme(Scanner scanner) {
        System.out.print("Décrivez le problème : ");
        String description = scanner.nextLine();
        System.out.print("Entrez le quartier : ");
        String quartier = scanner.nextLine();
        System.out.print("Entrez votre courriel : ");
        String courriel = scanner.nextLine();

        Probleme nouveau = new Probleme(description, quartier, courriel);
        problemes.add(nouveau);
        System.out.println("Problème signalé avec succès !");
    }

    public void afficherProblemes() {
        if (problemes.isEmpty()) {
            System.out.println("Aucun problème à afficher.");
        } else {
            for (int i = 0; i < problemes.size(); i++) {
                System.out.println("#" + (i + 1) + " - " + problemes.get(i));
            }
        }
    }

    public void soumettreProjet(Scanner scanner) {
        System.out.print("Titre du projet : ");
        String titre = scanner.nextLine();
        System.out.print("Quartier : ");
        String quartier = scanner.nextLine();
        System.out.print("Type de travaux : ");
        String type = scanner.nextLine();
        System.out.print("Date début (AAAA-MM-JJ) : ");
        String debut = scanner.nextLine();
        System.out.print("Date fin (AAAA-MM-JJ) : ");
        String fin = scanner.nextLine();
        System.out.print("Coût estimé : ");
        double cout = Double.parseDouble(scanner.nextLine());

        Projet projet = new Projet(titre, quartier, type, debut, fin, cout);
        projets.add(projet);
        System.out.println("Projet soumis avec succès.");
    }

    public void modifierProjet(Scanner scanner) {
        afficherTravaux();
        System.out.print("Sélectionnez le numéro du projet à modifier : ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;
        if (index >= 0 && index < projets.size()) {
            Projet p = projets.get(index);
            System.out.print("Nouvelle description : ");
            p.setTitre(scanner.nextLine());
            System.out.print("Nouvelle date de fin : ");
            p.setDateFin(scanner.nextLine());
            System.out.print("Nouveau statut (En cours / Suspendu / Terminé) : ");
            p.setStatut(scanner.nextLine());
            System.out.println("Projet mis à jour.");
        } else {
            System.out.println("Projet invalide.");
        }
    }
}

class Probleme {
    private String description;
    private String quartier;
    private String email;

    public Probleme(String description, String quartier, String email) {
        this.description = description;
        this.quartier = quartier;
        this.email = email;
    }

    public String toString() {
        return quartier + " - " + description + " (" + email + ")";
    }
}

class Projet {
    private String titre;
    private String quartier;
    private String type;
    private String dateDebut;
    private String dateFin;
    private double cout;
    private String statut = "En attente";

    public Projet(String titre, String quartier, String type, String dateDebut, String dateFin, double cout) {
        this.titre = titre;
        this.quartier = quartier;
        this.type = type;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.cout = cout;
    }

    public String toString() {
        return titre + " | " + quartier + " | " + type + " | " + dateDebut + " → " + dateFin + " | " + statut + " | $" + cout;
    }

    public String getQuartier() { return quartier; }
    public String getType() { return type; }
    public void setTitre(String titre) { this.titre = titre; }
    public void setDateFin(String df) { this.dateFin = df; }
    public void setStatut(String s) { this.statut = s; }




}
