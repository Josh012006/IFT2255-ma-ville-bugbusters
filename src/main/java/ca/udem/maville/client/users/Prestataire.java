package ca.udem.maville.client.users;

import java.util.ArrayList;

import ca.udem.maville.client.Candidature;

public class Prestataire extends Utilisateur{

    private String numeroEntreprise;
    private ArrayList<String> quartiers;
    private ArrayList<String> typesTravaux;
    private ArrayList<String> candidatures;

    public Prestataire(String id,String nom, String adresseCourriel, String numeroEntreprise,
                       ArrayList<String> quartiers, ArrayList<String> typesTravaux) {
        super(id,nom, adresseCourriel);
        this.numeroEntreprise = numeroEntreprise;
        this.quartiers = quartiers;
        this.typesTravaux = typesTravaux;
        this.candidatures = new ArrayList<>();
    }
    

    public ArrayList<Candidature> getCandidatures() {
        return candidatures; // Chercher les candidatures au niveau du Backend
    }

    public String getNumeroEntreprise() {
        return numeroEntreprise;
    }

    public ArrayList<String> getQuartiers() {
        return quartiers;
    }

    public ArrayList<String> getTypesTravaux() {
        return typesTravaux;
    }
}
