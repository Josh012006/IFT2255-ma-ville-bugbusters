package ca.udem.maville.client;
import java.util.ArrayList;
import java.util.Date;


public class FicheProbleme {

    private String typeTravaux;
    private String localisation;
    private String quartier;
    private String description;
    private int priorite;
    private ArrayList<String> signalements;
    private String statut;
    private String id;
    private Date dateCreationFiche;
    private ArrayList<String> residents;

    // Constructeur
    public FicheProbleme(String id, String typeTravaux, String localisation, String description, int priorite, String quartier) {
        this.typeTravaux = typeTravaux;
        this.quartier = quartier;
        this.id = id;
        this.localisation = localisation;
        this.description = description;
        this.priorite = priorite;
        this.statut = "enAttente";
        this.signalements = new ArrayList<>();
        this.residents = new ArrayList<>();
        this.dateCreationFiche = new Date();
    }



    // Getters
    public String getTypeTravaux() { return typeTravaux; }

    public String getID() { return id; }

    public String getQuartier() { return quartier; }

    public String getLocalisation() { return localisation; }

    public String getDescription() { return description; }
  
    public int getPriorite() { return priorite; }

    public String getStatut() { return statut; }

    public Date getDateCreationFiche() { return dateCreationFiche; }

    // Setters
    public void addSignalement(String signalement) {
        signalements.add(signalement);
    }

    public void addResident(String resident) {
        residents.add(resident);
    }
}

