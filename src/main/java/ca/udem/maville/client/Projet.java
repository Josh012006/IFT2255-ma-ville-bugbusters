package ca.udem.maville.client;

import java.time.LocalDate;

import ca.udem.maville.client.users.Prestataire;

public class Projet {
    private String titre;
    private String description;
    private TypeTravaux typeTravaux;
    private StatutProjet statut;
    private LocalDate dateDebut;
    private LocalDate dateFinPrevue;
    private LocalDate dateFinEffective;
    private FicheProbleme ficheProbleme;
    private Prestataire prestataire;

    // Constructeur
    public Projet(String titre, String description, TypeTravaux typeTravaux, LocalDate dateDebut, LocalDate dateFinPrevue,
                  FicheProbleme ficheProbleme, Prestataire prestataire) {
        this.titre = titre;
        this.description = description;
        this.typeTravaux = typeTravaux;
        this.dateDebut = dateDebut;
        this.dateFinPrevue = dateFinPrevue;
        this.ficheProbleme = ficheProbleme;
        this.prestataire = prestataire;
        this.statut = StatutProjet.enCours;
    }

    // Méthodes
    public void mettreAJour(String desc, LocalDate dateFin, StatutProjet statut) {
        this.description = desc;
        this.dateFinEffective = dateFin;
        this.statut = statut;
    }

    // Getters
    public String getTitre() { return titre; }

    public String getDescription() { return description; }

    public TypeTravaux getTypeTravaux() { return typeTravaux; }

    public StatutProjet getStatut() { return statut; }

    public LocalDate getDateDebut() { return dateDebut; }

    public LocalDate getDateFinPrevue() { return dateFinPrevue; }

    public LocalDate getDateFinEffective() { return dateFinEffective; }

    public FicheProbleme getFicheProbleme() { return ficheProbleme; }

    public Prestataire getPrestataire() { return prestataire; }
}
