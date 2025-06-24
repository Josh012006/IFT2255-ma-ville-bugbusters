package ca.udem.maville.client;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FicheProbleme {
    private TypeTravaux typeTravaux;
    private String localisation;
    private String description;
    private PrioriteProbleme priorite;
    private List<Signalement> signalements;
    private StatutProbleme statut;
    private int nombreSignalement;
    private Date dateCreationFiche;
    private List<Candidature> candidatures;

    // Constructeur
    public FicheProbleme(TypeTravaux typeTravaux, String localisation, String description, PrioriteProbleme priorite) {
        this.typeTravaux = typeTravaux;
        this.localisation = localisation;
        this.description = description;
        this.priorite = priorite;
        this.statut = StatutProbleme.enAttente;
        this.signalements = new ArrayList<>();
        this.candidatures = new ArrayList<>();
        this.nombreSignalement = 0;
        this.dateCreationFiche = new Date();
    }

    // Ajouter un signalement
    public void ajouterSignalement(Signalement s) {
        this.signalements.add(s);
        this.nombreSignalement++;
        s.setFicheProblemeAssociee(this);
    }

    // Changer le statut
    public void changerStatut(StatutProbleme nouveauStatut) {
        this.statut = nouveauStatut;
    }

    // Ajouter une candidature
    public void soumettreCandidature(Candidature c) {
        this.candidatures.add(c);
    }

    // Getters
    public TypeTravaux getTypeTravaux() { return typeTravaux; }

    public String getLocalisation() { return localisation; }

    public String getDescription() { return description; }

    public PrioriteProbleme getPriorite() { return priorite; }

    public List<Signalement> getSignalements() { return signalements; }

    public StatutProbleme getStatut() { return statut; }

    public int getNombreSignalement() { return nombreSignalement; }

    public Date getDateCreationFiche() { return dateCreationFiche; }

    public List<Candidature> getCandidatures() { return candidatures; }
}

