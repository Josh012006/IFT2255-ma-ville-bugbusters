import type { Quartier } from "../../types/Quartier";


/**
 * Une interface pour représenter un utilisateur quelconque de l'application, sauf le STPM qui 
 * a un profil déjà initialisé et non changeable.
 */
export default interface Utilisateur {
    id?: string,
    nom: string,
    adresseCourriel: string,
    abonnementsQuartier: Quartier[],   // Le champ ajouté pour garder une trace de ses abonnements aux quartiers. Pour le prestataire il faut mettre automatiquement les quartiers qu'il couvre et pour le résident, il faut mettre automatiquement son Quartier.
}