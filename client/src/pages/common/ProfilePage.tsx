import type { Prestataire } from "../../interfaces/users/Prestataire";
import type Resident from "../../interfaces/users/Resident";
import { useAppSelector } from "../../redux/store";


/**
 * Il s'agit de la page de profil prévue uniquement pour les résidents et les prestataires. Elle permet d'afficher les informations 
 * de l'utilisateur et particulièrement ses préférences de notifications qu'il peut modifier.
 * Ne pas oublier d'envoyer une requête pour modifier les préférences si cela arrive et 
 * d'update aussi le redux store après.
 * @returns ReactNode
 */
export default function ProfilePage() {
    const userType : string | null = useAppSelector((state) => state.auth.userType);
    const userInfos : Prestataire | Resident | null = useAppSelector((state) => state.auth.infos);

    return(
        <div>

        </div>
    )
}