import { useAppSelector } from "../../redux/store";

/**
 * La page de choix du profil. Elle est réservée uniquement aux prestataires ou aux résidents.
 * Donc il faut faire la vérification et l'authentification. Pour cela, en fonction du userType, il faut chercher
 * tous les résidents ou les prestataires et les afficher pour permettre de faire un choix.
 * Ne pas manquer d'update le redux store après.
 * @returns ReactNode
 */
export default function ChoicePage() {
    const userType : string | null = useAppSelector((state) => state.auth.userType);
    
    return (
        <div>

        </div>
    );
}