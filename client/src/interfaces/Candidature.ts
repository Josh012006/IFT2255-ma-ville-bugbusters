import type { TypeTravaux } from "../types/TypesTravaux";


/**
 * An interface representing a Prestataire's candidature.
 */
export default interface Candidature {
    id?: string,
    statut: "en attente" | "acceptée" | "refusée",
    prestataire: string,
    ficheProbleme: string,
    numeroEntreprise: string,
    titreProjet: string,
    description: string,
    typeTravaux: TypeTravaux,
    dateDebut: Date,
    dateFin: Date,
    ruesAffectees: string[],
    coutEstime: number,
    nomPrestataire: string,
    createdAt?: Date,
    updatedAt?: Date,
}