import type { Quartier } from "../../types/Quartier";


/**
 * An interface that represent a user of the app, except the STPM that only has one profile.
 */
export default interface Utilisateur {
    _id?: string,
    nom: string,
    addresseCourriel: string,
    abonnementsQuartier: Quartier[],   // Le champ ajouté pour garder une trace de ses abonnements aux quartiers. Pour le prestataire il faut mettre automatiquement les quartiers qu'il couvre et pour le résident, il faut mettre automatiquement son Quartier.
}