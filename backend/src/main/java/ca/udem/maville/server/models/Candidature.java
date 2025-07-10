package ca.udem.maville.client;

import java.util.*;

public class Candidature {

    private String id;
    private Date dateSoumission;
    private String statut;
    private String prestataire;
    private String nomPrestataire;
    private String ficheProbleme;
    private String numeroEntreprise;
    private String titreProjet;
    private String description;
    private String typeTravaux;
    private Date dateDebut;
    private Date dateFin;
    private String ruesAffectees;
    private double coutEstime;
    private String quartier;

    // Constructeur
    public Candidature(String id, String ruesAffectees, String numeroEntreprise, String prestataire, String nomPrestataire, String ficheProbleme, String titreProjet, String description,
                       String typeTravaux, Date dateDebut, Date dateFin, double coutEstime, String quartier) {

        this.dateSoumission = new Date();
        this.statut = "enAttente";
        this.prestataire = prestataire;
        this.nomPrestataire = nomPrestataire;
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
        this.quartier = quartier;
    }
   

    // Getters
    public Date getDateSoumission() { return dateSoumission; }

    public String getNomPrestataire() { return nomPrestataire; }

    public String getPrestataire() {
        return prestataire;
    }

    public String getFicheProbleme() {
        return ficheProbleme;
    }

    public String getID() {
        return id;
    }

    public String getQuartier() {
        return quartier;
    }

    public String getStatut() { return statut; }

    public String getTitreProjet() { return titreProjet; }

    public String getDescription() { return description; }

    public String getRuesAffectees() { return ruesAffectees; }

    public String getTypeTravaux() { return typeTravaux; }

    public Date getDateDebut() { return dateDebut; }

    public Date getDateFin() { return dateFin; }

    public double getCoutEstime() { return coutEstime; }

    public String getNumeroEntreprise() { return numeroEntreprise; }



    // Setters
    public void setStatut(String statut) { this.statut = statut; }


}
