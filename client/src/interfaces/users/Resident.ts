import type { Quartier } from "../../types/Quartier";
import type Utilisateur from "./Utilisateur";


/**
  * An interface that represents the Resident. It is a subtype of the Utilisateur interface.
 */
export default interface Resident extends Utilisateur {
    adresse: string,
    codePostal: string,
    quartier: Quartier,
    dateNaissance: Date,
    // abonnementsRue: Rues[], // En parler davantage après
}