package ca.udem.maville.client;
import java.time.LocalDate;
import java.time.LocalTime;

import ca.udem.maville.client.users.Prestataire;

public class Candidature {
    private LocalDate dateSoumission;
    private LocalTime heureSoumission;
    private StatutCandidature statut;
    private Prestataire prestataire;
    private FicheProbleme ficheProbleme;
    private String numEntreprise;
    private String titreProjet;
    private String descriptionProjet;
    private TypeTravaux[] typesTravaux;
    private String lieu;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private double coutEstime;

    // Constructeur
    public Candidature(Prestataire prestataire, FicheProbleme ficheProbleme, String titreProjet, String descriptionProjet,
                       TypeTravaux[] typesTravaux, String lieu, LocalDate dateDebut, LocalDate dateFin, double coutEstime) {
        this.dateSoumission = LocalDate.now();
        this.heureSoumission = LocalTime.now();
        this.statut = StatutCandidature.enAttente;
        this.prestataire = prestataire;
        this.ficheProbleme = ficheProbleme;
        this.numEntreprise = prestataire.getNumeroEntreprise();
        this.titreProjet = titreProjet;
        this.descriptionProjet = descriptionProjet;
        this.typesTravaux = typesTravaux;
        this.lieu = lieu;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.coutEstime = coutEstime;
    }

    // Méthodes
    public void annuler() {
        this.statut = StatutCandidature.refusee;
    }

    public boolean validerSoumission() {
        return statut == StatutCandidature.enAttente;
    }

    public void changerStatut(StatutCandidature nouveau) {
        this.statut = nouveau;
    }

    // Getters
    public LocalDate getDateSoumission() { return dateSoumission; }

    public LocalTime getHeureSoumission() { return heureSoumission; }

    public StatutCandidature getStatut() { return statut; }

    public Prestataire getPrestataire() { return prestataire; }

    public FicheProbleme getFicheProbleme() { return ficheProbleme; }

    public String getTitreProjet() { return titreProjet; }

    public String getDescriptionProjet() { return descriptionProjet; }

    public TypeTravaux[] getTypesTravaux() { return typesTravaux; }

    public String getLieu() { return lieu; }

    public LocalDate getDateDebut() { return dateDebut; }

    public LocalDate getDateFin() { return dateFin; }

    public double getCoutEstime() { return coutEstime; }

    public String getNumEntreprise() { return numEntreprise; }

}
