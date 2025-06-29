package ca.udem.maville.client;
import java.util.Date;

import ca.udem.maville.client.users.Resident;

public class Signalement {
    private String id;
    private String quartier;
    private String typeProbleme;
    private String localisation;
    private String description;
    private String statut ;
    private String resident;
    private Date dateSignalement;

    // Constructeur
    public Signalement(String typeProbleme, String localisation, String description, String resident, Date dateSignalement, String quartier,String id) {
        this.typeProbleme = typeProbleme;
        this.quartier = quartier;
        this.id = id;
        this.localisation = localisation;
        this.description = description;
        this.resident = resident;
        this.dateSignalement = dateSignalement;
        this.statut = "enAttente";
    }


    public String getID() {return id;}

    public String getQuartier() {return quartier;}

    public String getTypeProbleme() {
        return typeProbleme;
    }

    public String getLocalisation() {
        return localisation;
    }

    public String getDescription() {
        return description;
    }

    public String getStatut() {
        return statut;
    }

    public Date getDateSignalement() {
        return dateSignalement;
    }
}
