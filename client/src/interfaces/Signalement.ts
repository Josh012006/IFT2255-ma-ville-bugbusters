import type { Quartier } from "../types/Quartier";
import type { TypeTravaux } from "../types/TypesTravaux";

/**
 * An interface that represents a Resident's alert.
 */
export default interface Signalement {
    _id?: string,
    typeProbleme: TypeTravaux,
    localisation: string,
    description: string,
    statut: "en attente" | "traité",
    resident: string,
    quartier: Quartier,
    createdAt?: Date,
    updatedAt?: Date,
}