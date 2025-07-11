import type { Quartier } from "../types/Quartier";
import type { TypeTravaux } from "../types/TypesTravaux";


/**
 * An interface that represents a Project.
 */
export default interface Projet {
    _id?: string,
    ruesAffectees: string,
    abonnes: string[],
    titreProjet: string,
    description: string,
    typeTravaux: TypeTravaux,
    statut: "en cours" | "annulé" | "suspendu" | "terminé",
    dateDebut: Date,
    dateFin: Date,
    ficheProbleme: string,
    prestataire: string,
    nomPrestataire: string,
    quartier: Quartier,
    cout: number,
    priorite: "faible" | "moyenne" | "élevée",
    nbRapports: number,
    createdAt?: Date,
    updatedAt?: Date,
}