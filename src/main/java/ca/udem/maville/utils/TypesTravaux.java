package ca.udem.maville.utils;

public enum TypesTravaux {
    travauxRoutiers("Travaux Routiers"),
    travauxGazOuElectricite("Travaux Gaz Ou Electricité"),
    constructionOuRenovation("Construction Ou Rénovation"),
    entretienPaysager("Entretien Paysager"),
    travauxLiesAuxTransportsEnCommun("Travaux Liés Aux Transports En Commun"),
    travauxDeSignalisationEtEclairage("Travaux De Signalisation Et Eclairage"),
    travauxSouterrains("Travaux Souterrains"),
    travauxResidentiel("Travaux Résidentiel"),
    entretienUrbain("Entretien Urbain"),
    entretienDesReseauxDeTelecommunication("Entretien Des Réseaux de Télécommunication");


    private final String nomLisible;

    TypesTravaux(String nomLisible) {
        this.nomLisible = nomLisible;
    }

    // Accesseur
    public String getLabel() {
        return nomLisible;
    }

    // Une methode pour avoir l'enum à partir du label
    public static TypesTravaux fromLabel(String label) {
        for (TypesTravaux t : TypesTravaux.values()) {
            if (t.getLabel().equals(label)) {
                return t;
            }
        }
        return null;
    }
}
