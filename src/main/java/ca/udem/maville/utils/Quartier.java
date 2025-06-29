package ca.udem.maville.utils;

public enum Quartier {

    ahuntsicCartierville("Ahuntsic–Cartierville"),
    anjou("Anjou"),
    coteDesNeigesNotreDameDeGrace("Côte-des-Neiges–Notre-Dame-de-Grâce"),
    lachine("Lachine"),
    laSalle("LaSalle"),
    lePlateauMontRoyal("Le Plateau-Mont-Royal"),
    leSudOuest("Le Sud-Ouest"),
    ileBizardSainteGenevieve("L’Île-Bizard–Sainte-Geneviève"),
    mercierHochelagaMaisonneuve("Mercier–Hochelaga-Maisonneuve"),
    montrealNord("Montréal-Nord"),
    outremont("Outremont"),
    pierrefondsRoxboro("Pierrefonds–Roxboro"),
    riviereDesPrairiesPointeAuxTrembles("Rivière-des-Prairies–Pointe-aux-Trembles"),
    rosemontLaPetitePatrie("Rosemont–La Petite-Patrie"),
    saintLaurent("Saint-Laurent"),
    saintLeonard("Saint-Léonard"),
    verdun("Verdun"),
    villeMarie("Ville-Marie"),
    villeraySaintMichelParcExtension("Villeray–Saint-Michel–Parc-Extension");


    private final String nomLisible;

    Quartier(String nomLisible) {
        this.nomLisible = nomLisible;
    }

    // Accesseur
    public String getLabel() {
        return nomLisible;
    }

    // Une methode pour avoir l'enum à partir du label
    public static Quartier fromLabel(String label) {
        for (Quartier q : Quartier.values()) {
            if (q.getLabel().equals(label)) {
                return q;
            }
        }
        return null;
    }
}
