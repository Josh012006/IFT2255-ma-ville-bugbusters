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
    abonnementsRue: string[], // On garde une trace des chaines de caractères entrées par le résident
}