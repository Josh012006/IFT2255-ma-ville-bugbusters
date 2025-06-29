package ca.udem.maville.client.users;

import java.sql.Date;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ca.udem.maville.client.MaVille;
import ca.udem.maville.client.Projet;
import ca.udem.maville.client.Signalement;
import ca.udem.maville.hooks.UseRequest;
import ca.udem.maville.utils.DateManagement;
import ca.udem.maville.utils.Quartier;
import ca.udem.maville.utils.RequestType;
import ca.udem.maville.utils.TypesTravaux;

public class Resident extends Utilisateur {
    private String adresse;
    private String codePostal;
    private ArrayList<String> signalements;
    private ArrayList<String> abonnements;
    private String quartier;
    private Date dateNaissance;

    public Resident(String id,String nom, String adresseCourriel, String adresse, String codePostal, String quartier, Date dateNaissance) {
        super(id,nom,adresseCourriel);
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

    public ArrayList<Projet> getAbonnement() { return abonnements; } // Recup Projet du Backend 

    public ArrayList<Signalement> getSignalements() { return signalements; } // Recup signalement du Backend 


    /**
     * 
     */
    public void signalerProbleme (){
        
        try {

            Scanner scanner = new Scanner(System.in);

            System.out.println("Veuillez choisir le Type de problème que vous rencontrez:");
        
            ArrayList<TypesTravaux> tab = new ArrayList<>(Arrays.asList(TypesTravaux.values()));

            for (TypesTravaux t :tab ){
                System.out.println( tab.indexOf(t) + ". " + t.getLabel());
            }
            System.out.print("Choix : ");
            
            String choice = scanner.nextLine();
            int choix = Integer.parseInt(choice);
           
            String typeProbleme = tab.get(choix).getLabel();

            System.out.println("Veuillez choisir le Quartier Concerné:");
        
            ArrayList<Quartier> tab1 = new ArrayList<>(Arrays.asList(Quartier.values()));

            for (Quartier s :tab1 ){
                System.out.println( tab1.indexOf(s) + ". " + s.getLabel());
            }
            System.out.print("Choix : ");
            
            String choice1 = scanner.nextLine();
            int choix1 = Integer.parseInt(choice1);
           
            String quartier = tab1.get(choix1).getLabel();

            System.out.println("Decrivez le problème rencontrez:");
            String description = scanner.nextLine();

            // Envoyer une requête pour creer un nouveau signalement

            JsonObject newSignal = new JsonObject();

            newSignal.addProperty("typeProbleme", typeProbleme);
            newSignal.addProperty("quartier", quartier);
            newSignal.addProperty("description", description);
            newSignal.addProperty("localisation", this.adresse);
            newSignal.addProperty("resident", this.id);

            String responseSignal = UseRequest.sendRequest(MaVille.urlHead + "/signalement" , RequestType.POST, newSignal.toString());

            if(responseSignal == null) {
                   
            System.out.println("Une erreur est survenue lors de la creation du signalement. Réponse nulle.");
            }

            JsonElement elemSignal = JsonParser.parseString(responseSignal);
            JsonObject jsonSignal = elemSignal.getAsJsonObject();

            int statuscode = jsonSignal.get("status").getAsInt();
            if (statuscode == 404) {
                System.out.println("Utilisateur non trouver lors de la creation du signalement");

            } else if(statuscode != 200) {
                System.out.println("Une erreur est survenue lors de la creation du signalement. Message d'erreur: " + jsonSignal.get("data").getAsJsonObject().get("message").getAsString());
            }

            System.out.println("Votre signalement a bien été reçu. Une notification vous sera envoyé lorsqu'il sera traité par un agent. Merci!");

            

        } catch (Exception e) {
            System.out.println("Choix Invalide: Veuillez recommencer la procedure");
            return;
        };


    }

    public void recupererSignalements(){
        
        String responseSignal = UseRequest.sendRequest(MaVille.urlHead + "/signalement" + this.id, RequestType.GET, null);

        if(responseSignal == null) {
                
        System.out.println("Une erreur est survenue lors de la recuperation des signalements.On le garde.");
        }

        JsonElement elemSignal = JsonParser.parseString(responseSignal);
        JsonObject jsonSignal = elemSignal.getAsJsonObject();

        int statuscode = jsonSignal.get("status").getAsInt();
        if (statuscode == 404) {
            System.out.println("Utilisateur non trouver lors de la recherche");

        } else if(statuscode != 200) {
            System.out.println("Une erreur est survenue lors de la recherche. Message d'erreur: " + jsonSignal.get("data").getAsJsonObject().get("message").getAsString());
        }

        JsonArray jsonSignals = jsonSignal.get("data").getAsJsonArray();
        Gson gson = new Gson();
        for (JsonElement s : jsonSignals){
            
            Signalement signal = gson.fromJson(s.getAsJsonObject(), Signalement.class);
            System.out.println("Type de Problème: " + signal.getTypeProbleme());
            System.out.println("Quartier: " + signal.getQuartier());
            System.out.println("Description: " + signal.getDescription());
            System.out.println("Statut: " + signal.getStatut());
            System.out.println("Date du Signalement: " + DateManagement.formatIsoDate(signal.getDateSignalement().toString()) + "\n");
            
        }
    



    }
  

}
