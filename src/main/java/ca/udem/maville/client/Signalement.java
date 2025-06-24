package ca.udem.maville.client;
import java.util.Date;

import ca.udem.maville.client.users.Resident;

public class Signalement {
    private TypeTravaux[] typeProbleme;
    private String localisation;
    private String description;
    private StatutSignalement statut ;
    private Resident[] residents;
    private FicheProbleme ficheProblemeAssociee;
    private Date dateSignalement;

    // Constructeur
    public Signalement(TypeTravaux[] typeProbleme, String localisation, String description, Resident[] residents, Date dateSignalement) {
        this.typeProbleme = typeProbleme;
        this.localisation = localisation;
        this.description = description;
        this.residents = residents;
        this.dateSignalement = dateSignalement;
        this.statut = StatutSignalement.enAttente;
}

// Méthodes
public void setFicheProblemeAssociee(FicheProbleme fiche) {
    this.ficheProblemeAssociee = fiche;
}

public void changerStatut(StatutSignalement nouveauStatut) {
    this.statut = nouveauStatut;
}

public FicheProbleme getFicheProblemeAssociee() {
    return ficheProblemeAssociee;
}

public TypeTravaux[] getTypeProbleme() {
    return typeProbleme;
}

public String getLocalisation() {
    return localisation;
}

public String getDescription() {
    return description;
}

public StatutSignalement getStatut() {
    return statut;
}

public Resident[] getResidents() {
    return residents;
}

public Date getDateSignalement() {
    return dateSignalement;
}
}
