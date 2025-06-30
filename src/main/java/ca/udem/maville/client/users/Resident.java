package ca.udem.maville.client.users;

import java.util.*;

import ca.udem.maville.utils.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ca.udem.maville.client.MaVille;
import ca.udem.maville.client.Signalement;
import ca.udem.maville.hooks.UseRequest;

public class Resident extends Utilisateur {

    private String adresse;
    private String codePostal;
    private ArrayList<String> signalements;
    private ArrayList<String> abonnements;
    private String quartier;
    private Date dateNaissance;

    public Resident(String id, String nom, String adresseCourriel, String adresse, String codePostal, String quartier, Date dateNaissance) {
        super(id, nom, adresseCourriel);
        this.adresse = adresse;
        this.codePostal = codePostal;
        this.quartier = quartier;
        this.dateNaissance = dateNaissance;
        this.signalements = new ArrayList<>();
        this.abonnements = new ArrayList<>();
    }

    // Getters
    public String getAdresse() { return adresse; }

    public String getCodePostal() { return codePostal; }

    public String getQuartier() { return quartier; }

    public Date getDateNaissance() { return dateNaissance; }


    // Setters

    public void addAbonnement(String idProjet) {
        abonnements.add(idProjet);
    }

    public void addSignalement(String idSignalement) {
        signalements.add(idSignalement);
    }


    public void signalerProbleme() {
        
        try {

            Scanner scanner = new Scanner(System.in);

            System.out.println("\nVeuillez choisir le type de problème que vous rencontrez:");
            ArrayList<TypesTravaux> tab = new ArrayList<>(Arrays.asList(TypesTravaux.values()));
            for (TypesTravaux t : tab ) {
                System.out.println( tab.indexOf(t) + ". " + t.getLabel());
            }
            System.out.print("Choix: ");
            String choice = scanner.nextLine();
            int choix = Integer.parseInt(choice);
            String typeProbleme = tab.get(choix).getLabel();


            System.out.println("\nVeuillez choisir le quartier concerné:");
            ArrayList<Quartier> tab1 = new ArrayList<>(Arrays.asList(Quartier.values()));
            for (Quartier s : tab1 ) {
                System.out.println( tab1.indexOf(s) + ". " + s.getLabel());
            }
            System.out.print("Choix: ");
            String choice1 = scanner.nextLine();
            int choix1 = Integer.parseInt(choice1);
            String quartier = tab1.get(choix1).getLabel();

            System.out.println("\nDécrivez le problème rencontrez. Veuillez ne pas retourner à la ligne:");
            String description = scanner.nextLine();

            // Envoyer une requête pour créer un nouveau signalement
            JsonObject newSignal = new JsonObject();

            newSignal.addProperty("typeProbleme", typeProbleme);
            newSignal.addProperty("quartier", quartier);
            newSignal.addProperty("description", description);
            newSignal.addProperty("localisation", this.adresse);
            newSignal.addProperty("resident", this.id);

            String responseSignal = UseRequest.sendRequest(MaVille.urlHead + "/signalement" , RequestType.POST, newSignal.toString());

            if(responseSignal == null){
                System.out.println("\nUne erreur est survenue lors de la création du signalement! Veuillez réessayer plus tard.");
                return;
            }

            JsonElement elemSignal = JsonParser.parseString(responseSignal);
            JsonObject jsonSignal = elemSignal.getAsJsonObject();

            if(jsonSignal.get("status").getAsInt() != 201) {
                System.out.println("\nUne erreur est survenue lors de la création du signalement! Veuillez réessayer plus tard.");
                return;
            }

            System.out.println("\nVotre signalement a bien été reçu. Une notification vous sera envoyée lorsqu'il sera traité par un agent. Merci!");

        } catch (Exception e) {
            System.out.println("\nChoix invalide. Veuillez recommencer la procedure.");
        };

    }

    public void recupererSignalements() {
        
        String responseSignal = UseRequest.sendRequest(MaVille.urlHead + "/signalement/getAll/" + this.id, RequestType.GET, null);

        if(responseSignal == null) {
            System.out.println("\nUne erreur est survenue lors de la récuperation de vos signalements! Veuillez réessayer plus tard.");
            return;
        }

        JsonElement elemSignal = JsonParser.parseString(responseSignal);
        JsonObject jsonSignal = elemSignal.getAsJsonObject();

        int statuscode = jsonSignal.get("status").getAsInt();
        if(statuscode != 200) {
            System.out.println("\nUne erreur est survenue lors de la récupération de vos signalements! Veuillez réessayer plus tard.");
            return;
        }

        JsonArray jsonSignals = jsonSignal.get("data").getAsJsonArray();
        Gson gson = AdaptedGson.getGsonInstance();

        if(jsonSignals.isEmpty()) {
            System.out.println("\nAucun signalement trouvé.");
            return;
        }

        System.out.println("\n----- Liste de vos signalements -----\n");

        for (int i = jsonSignals.size() - 1; i >= 0; i--) {
            JsonElement s = jsonSignals.get(i);
            Signalement signal = gson.fromJson(s.getAsJsonObject(), Signalement.class);

            System.out.println("Type de Problème: " + signal.getTypeProbleme());
            System.out.println("Quartier: " + signal.getQuartier());
            System.out.println("Description: " + signal.getDescription());
            System.out.println("Statut: " + signal.getStatut());
            System.out.println("Date du Signalement: " + DateManagement.formatIsoDate(signal.getDateSignalement().toInstant().toString()) + "\n");
            
        }

    }
  

}
