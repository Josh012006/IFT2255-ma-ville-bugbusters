import type { TypeTravaux } from "../types/TypesTravaux";


/**
 * Une interface pour représenter la candidature d'un Prestataire pour 
 * un projet. Elle garde une trace du prestataire dans le champ prestataire.
 */
export default interface Candidature {
    id?: string,
    statut: "en attente" | "vue" | "acceptée" | "refusée",
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