import type { Quartier } from "../types/Quartier";
import type { TypeTravaux } from "../types/TypesTravaux";


/**
 * Une interface pour représenter une fiche problème créée par un agent du STPM.
 * Elle garde une trace des résidents qui ont faits des signalements 
 * similaires à travers le champ residents.
 */
export default interface Problem {
    id?: string,
    typeTravaux: TypeTravaux,
    localisation: string,
    quartier: Quartier,
    description: string,
    priorite: "faible" | "moyenne" | "élevée",
    signalements: string[],
    statut?: "en attente" | "traitée",
    residents: string[],
    createdAt?: Date,
    updatedAt?: Date,
}
