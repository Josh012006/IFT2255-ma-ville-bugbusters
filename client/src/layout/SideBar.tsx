import type { ReactNode } from "react";
import { useAppSelector } from "../redux/store";


/**
 * Ce composant décrit la sidebar intégrée dans le layout de l'app. Elle s'appuie sur le type d'utilisateur pour déduire 
 * les liens à y montrer. Ne pas oublier d'inclure en bas un lien "Retourner à l'authentification" pour revenir au choix 
 * du type d'utilisateur.
 * @param children il s'agit du composant enfant de notre SideBar qui représente ce qui sera rendu à côté d'elle.
 * @returns ReactNode
 */
export default function SideBar({children} : {children: ReactNode}) {
    const userType : string | null = useAppSelector((state) => state.auth.userType);

    return(
        <main>
            
        </main>
    );
}