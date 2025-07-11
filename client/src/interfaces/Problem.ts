import type { Quartier } from "../types/Quartier";
import type { TypeTravaux } from "../types/TypesTravaux";


/**
 * An interface representing a problem created by the STPM agent.
 */
export default interface Problem {
    _id?: string,
    typeTravaux: TypeTravaux,
    localisation: string,
    quartier: Quartier,
    description: string,
    priorite: "faible" | "moyenne" | "élevée",
    signalements: string[],
    statut: "en attente" | "traitée",
    residents: string[],
    createdAt?: Date,
    updatedAt?: Date,
}