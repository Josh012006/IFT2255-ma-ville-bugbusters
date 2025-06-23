import java.util.Scanner;

public class MaVille {
    piblic static void main (String[] args) {
        MaVille app = new MaVille();
        app.lancer()
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
            System.out.printl("\n=== Menu Résident ===");
            System.out.println("1. Consulter les travaux");
            System.out.println("2. Rechercher des travaux");
            System.out.println("3. S’abonner à une rue/quartier");
            System.out.println("4. Voir les notifications");
            System.out.println("5. Signaler un problème");
            System.out.println("6. Retour");

            String choix = scanner.nextLine()
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


}
