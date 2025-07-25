import { Navigate } from "react-router-dom";
import { useAppSelector } from "../../redux/store";
import { useEffect, useState } from "react";
import type { Prestataire } from "../../interfaces/users/Prestataire";
import type Resident from "../../interfaces/users/Resident";
import { useRequest } from "../../hooks/UseRequest";

/**
 * La page de choix du profil. Elle est réservée uniquement aux prestataires ou aux résidents.
 * Donc il faut faire la vérification et l'authentification. Pour cela, en fonction du userType, il faut chercher
 * tous les résidents ou les prestataires et les afficher pour permettre de faire un choix.
 * Ne pas manquer d'update le redux store après.
 * @returns ReactNode
 */
export default function ChoicePage() {
    const userType : string | null = useAppSelector((state) => state.auth.userType);

    // Requête au backend
    const [users, setUsers] = useState<Prestataire[] | Resident[]>([]);
    const response = useRequest("/" + userType + "/getAll", "GET");

    if(response) {
        if(response.status === 200) {
            setUsers(response.data);
        }
    }

    if(!userType) {
        return <Navigate to="/auth" replace />
    } else {
        return (
            <div>

            </div>
        );
    }
    
}