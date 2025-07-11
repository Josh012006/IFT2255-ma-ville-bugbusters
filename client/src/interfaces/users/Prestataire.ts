
import type { Quartier } from "../../types/Quartier";
import type { TypeTravaux } from "../../types/TypesTravaux";
import type Utilisateur from "./Utilisateur";

/**
 * Une interface pour représenter un Prestataire. Elle hérite de
 * l'interface {@link Utilisateur}
 */
export interface Prestataire extends Utilisateur {
    numeroEntreprise: string,
    quartiers: Quartier[],
    typesTravaux: TypeTravaux[],
    abonnementsType: TypeTravaux[],        // Le nouveau champ ajouté pour garder la trace des abonnements qu'il a fait. On doit y mettre automatiquement les types qu'il couvre
}