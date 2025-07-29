import { Navigate, Outlet } from "react-router-dom";
import { useAppSelector } from "../../redux/store";


/**
 * Il s'agit d'un garde de route qui empêche une personne d'accéder aux routes qui ne sont pas autorisées pour son type d'utilisateur.
 * Elle permet une sécurité accrue.
 * @param role qui est une chaîne de caractère représentant le rôle attendu 
 * @returns ReactNode
 */
export default function RequireAuth({role} : {role: string}) {
    const userType : string | null = useAppSelector((state) => state.auth.userType);

    if(userType === role) {
        return <Outlet />
    } else {
        return <Navigate to="/auth" replace />
    }

}