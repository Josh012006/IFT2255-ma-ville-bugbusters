package ca.udem.maville.client.users;

import java.util.ArrayList;
import java.util.List;

import ca.udem.maville.client.Candidature;

public class Prestataire extends Utilisateur{
    private String numeroEntreprise;
    private List<Quartier> quartiers;
    private List<TypeTravaux> typesTravaux;
    private List<Candidature> candidatures;

    public Prestataire(String nom, String email, String numeroEntreprise,
                       List<Quartier> quartiers, List<TypeTravaux> typesTravaux) {
        super(nom, email);
        this.numeroEntreprise = numeroEntreprise;
        this.quartiers = quartiers;
        this.typesTravaux = typesTravaux;
        this.candidatures = new ArrayList<>();
    }

    public void soumettreCandidature(Candidature c) {
        candidatures.add(c);
    }

    public void modifierCandidature(Candidature c) {
        // À adapter selon ton besoin, ici c’est symbolique
    }

    public void annulerCandidature(Candidature c) {
        candidatures.remove(c);
    }

    public List<Candidature> getCandidatures() {
        return candidatures;
    }

    public String getNumeroEntreprise() {
        return numeroEntreprise;
    }

    public List<Quartier> getQuartiers() {
        return quartiers;
    }

    public List<TypeTravaux> getTypesTravaux() {
        return typesTravaux;
    }
}
