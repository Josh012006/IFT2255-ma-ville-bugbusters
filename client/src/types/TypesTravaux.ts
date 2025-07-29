/**
 * Une énumération représentant les différents types de travaux.
 */
export type TypeTravaux = "Travaux Routiers" | "Travaux Gaz Ou Electricité" | "Construction Ou Rénovation" |
                    "Entretien Paysager" | "Travaux Liés Aux Transports En Commun" | "Travaux De Signalisation Et Eclairage" |
                    "Travaux Souterrains" | "Travaux Résidentiel" | "Entretien Urbain" | "Entretien Des Réseaux de Télécommunication";

export const TYPE_TRAVAUX = [
    "Travaux Routiers",
    "Travaux Gaz Ou Electricité",
    "Construction Ou Rénovation",
    "Entretien Paysager",
    "Travaux Liés Aux Transports En Commun",
    "Travaux De Signalisation Et Eclairage",
    "Travaux Souterrains",
    "Travaux Résidentiel",
    "Entretien Urbain",
    "Entretien Des Réseaux de Télécommunication",
] as const;
