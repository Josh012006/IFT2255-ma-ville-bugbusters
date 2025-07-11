
import type { Quartier } from "../../types/Quartier";
import type { TypeTravaux } from "../../types/TypesTravaux";
import type Utilisateur from "./Utilisateur";

/**
 * An interface that represents the Prestataire. It is a subtype of the Utilisateur interface.
 */
export interface Prestataire extends Utilisateur {
    numeroEntreprise: string,
    quartiers: Quartier[],
    typesTravaux: TypeTravaux[],
    abonnementsType: TypeTravaux[],        // Le nouveau champ ajouté pour garder la trace des abonnements qu'il a fait. On doit y mettre automatiquement les types qu'il couvre
}