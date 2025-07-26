import type { ReactNode } from "react";
import { type AppDispatch, useAppSelector } from "../redux/store";
import MyLink from "../components/MyLink";
import { useDispatch } from "react-redux";
import { loginInfos, loginType } from "../redux/features/authSlice";


/**
 * Ce composant décrit la sidebar intégrée dans le layout de l'app. Elle s'appuie sur le type d'utilisateur pour déduire 
 * les liens à y montrer. Ne pas oublier d'inclure en bas un lien "Retourner à l'authentification" pour revenir au choix 
 * du type d'utilisateur.
 * @param children il s'agit du composant enfant de notre SideBar qui représente ce qui sera rendu à côté d'elle.
 * @returns ReactNode
 */
export default function SideBar({children, show} : {children: ReactNode, show: boolean}) {
    const userType : string | null = useAppSelector((state) => state.auth.userType);

    const dispatch = useDispatch<AppDispatch>();

    const handleLogout = () => {
        dispatch(loginInfos(null));
        dispatch(loginType(null));
    }

    return(
        <main className="row">
            <aside className={`${show? "d-block" : "d-none"} col-12 col-lg-2 d-lg-block h-100 z-1 z-lg-0 bg-light d-flex flex-column justify-content-center align-items-center`}>
                {userType}
                <MyLink to="/auth" className="" onClick={handleLogout}>Revenir à l'authentification</MyLink>
            </aside>
            <div className="col-12 col-lg-10 z-0">{ children }</div>
        </main>
    );
}