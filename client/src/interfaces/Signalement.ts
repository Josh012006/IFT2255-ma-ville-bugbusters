import type { Quartier } from "../types/Quartier";
import type { TypeTravaux } from "../types/TypesTravaux";

/**
 * Une interface pour représneter un Signalement d'un Résident.
 * Elle garde une trace du Résident concerné à travers son champ 
 * resident.
 */
export default interface Signalement {
    id?: string,
    typeProbleme: TypeTravaux,
    localisation: string,
    description: string,
    statut: "en attente" | "vu" | "traité",
    resident: string,
    quartier: Quartier,
    createdAt?: Date,
    updatedAt?: Date,
}