package ca.udem.maville.client.users;

import java.util.ArrayList;
import java.util.List;

import ca.udem.maville.client.Projet;
import ca.udem.maville.client.Signalement;

public class Resident extends Utilisateur {
    private String adresse;
    private String codePostal;
    private List<Signalement> signalements;
    private List<Projet> abonnements;

    public Resident(String nom, String email, String adresse, String codePostal) {
        super(nom, email);
        this.adresse = adresse;
        this.codePostal = codePostal;
        this.signalements = new ArrayList<>();
        this.abonnements = new ArrayList<>();
    }

    public void signalerProbleme(Signalement s) {
        signalements.add(s);
    }

    public List<Signalement> consulterSignalements() {
        return signalements;
    }

    public List<Projet> consulterProjets() {
        return abonnements;
    }

    public void ajouterAbonnement(Projet projet) {
        abonnements.add(projet);
    }

    // Getters
    public String getAdresse() { return adresse; }

    public String getCodePostal() { return codePostal; }

    public List<Signalement> getSignalements() { return signalements; }

}
