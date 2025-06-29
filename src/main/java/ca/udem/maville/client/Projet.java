package ca.udem.maville.client;

import java.util.*;

import ca.udem.maville.StatutProjet;
import ca.udem.maville.client.users.Prestataire;

public class Projet {
    private String id;
    private String ruesAffectees;
    private ArrayList<String> abonnes;
    private String titreProjet;
    private String description;
    private String typeTravaux;
    private String statut;
    private Date dateDebut;
    private Date dateFin;
    private String ficheProbleme;
    private String prestataire;
    private String quartier;
    private double cout;

    // Constructeur
    public Projet(String id,String titreProjet,String ruesAffectees, String description, String typeTravaux,Date dateDebut, Date dateFin,
                  String ficheProbleme, String prestataire, String quartier, double cout) {
        this.id = id;
        this.ruesAffectees = ruesAffectees;
        this.abonnes = new ArrayList<>();
        this.titreProjet = titreProjet;
        this.description = description;
        this.typeTravaux = typeTravaux;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.ficheProbleme = ficheProbleme;
        this.quartier = quartier;
        this.cout = cout;
        this.prestataire = prestataire;
        this.statut = "enCours";
    }

   

    // Getters
    public String getTitreProjet() { return titreProjet; }

    public double getCout() { return cout; }

    public String getID() { return id; }

    public String getRuesAffectees() { return ruesAffectees; }

    public String getQuartier() { return quartier; }

    public String getDescription() { return description; }

    public String getTypeTravaux() { return typeTravaux; }

    public String getStatut() { return statut; }

    public Date getDateDebut() { return dateDebut; }

    public Date getDateFin() { return dateFin; }

    public String getFicheProbleme() { return ficheProbleme; }

    public ArrayList<Resident> getAbonnes() { return abonnes; }

    public Prestataire getPrestataire() { return prestataire; }
}
