package ca.udem.maville.client;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import ca.udem.maville.StatutCandidature;
import ca.udem.maville.client.users.Prestataire;

public class Candidature {
    private String id;
    private Date dateSoumission;
    private String statut;
    private String prestataire;
    private String ficheProbleme;
    private String numeroEntreprise;
    private String titreProjet;
    private String description;
    private String typeTravaux;
    private Date dateDebut;
    private Date dateFin;
    private String ruesAffectees;
    private double coutEstime;

    // Constructeur
    public Candidature(String id,String ruesAffectees,String numeroEntreprise,String prestataire, String ficheProbleme, String titreProjet, String description,
                       String typeTravaux, Date dateDebut,Date dateSoumission, Date dateFin, double coutEstime) {
        this.dateSoumission = dateSoumission;
        this.statut = "enAttente";
        this.prestataire = prestataire;
        this.ficheProbleme = ficheProbleme;
        this.numeroEntreprise = numeroEntreprise;
        this.titreProjet = titreProjet;
        this.id = id;
        this.description = description;
        this.typeTravaux = typeTravaux;
        this.ruesAffectees = ruesAffectees;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.coutEstime = coutEstime;
    }

   

    // Getters
    public Date getDateSoumission() { return dateSoumission; }

    public String getStatut() { return statut; }

    public Prestataire getPrestataire() { return prestataire; } //recup

    public FicheProbleme getFicheProbleme() { return ficheProbleme; } // recup

    public String getTitreProjet() { return titreProjet; }

    public String getDescription() { return description; }

    public String getRuesAffectees() { return ruesAffectees; }

    public String getTypeTravaux() { return typeTravaux; }


    public Date getDateDebut() { return dateDebut; }

    public Date getDateFin() { return dateFin; }

    public double getCoutEstime() { return coutEstime; }

    public String getNumeroEntreprise() { return numeroEntreprise; }

}
