import type { Quartier } from "../../types/Quartier";
import type Utilisateur from "./Utilisateur";


/**
 * Une interface pour représenter un Résident. Elle hérite de
 * l'interface {@link Utilisateur}
 */
export default interface Resident extends Utilisateur {
    adresse: string,
    codePostal: string,
    quartier: Quartier,
    dateNaissance: Date,
    // abonnementsRue: Rues[], // En parler davantage après
}