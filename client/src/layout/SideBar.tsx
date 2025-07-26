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
        <main className="w-100">
            <div className="row w-100 h-100">
                <aside className={`${show? "d-flex" : "d-none"} p-0 text-white orange col-12 col-lg-2 d-lg-flex h-100 z-1 z-lg-0 flex-column justify-content-between align-items-center sticky-top`}>
                    <div className="d-flex flex-column align-items-center w-100">
                        {userType === "resident" && <>
                            <MyLink to="/resident/projet/list" className="text-white border-bottom border-1 border-white w-100 d-block p-3 text-center orange-clair">Projets de Travaux</MyLink>
                            <MyLink to="/resident/signalement" className="text-white border-bottom border-1 border-white w-100 d-block p-3 text-center orange-clair">Signalements</MyLink>
                        </>}
                        {userType === "prestataire" && <>
                            <MyLink to="/prestataire/projet/list" className="text-white border-bottom border-1 border-white w-100 d-block p-3 text-center orange-clair">Projets</MyLink>
                            <MyLink to="/prestataire/candidature/list" className="text-white border-bottom border-1 border-white w-100 d-block p-3 text-center orange-clair">Candidatures</MyLink>
                            <MyLink to="/prestataire/probleme/list" className="text-white border-bottom border-1 border-white w-100 d-block p-3 text-center orange-clair">Fiches problèmes</MyLink>
                        </>}
                        {userType === "stpm" && <>
                            <MyLink to="/stpm/signalement/list" className="text-white border-bottom border-1 border-white w-100 d-block p-3 text-center orange-clair">Gérer les signalements</MyLink>
                            <MyLink to="/stpm/candidature/list" className="text-white border-bottom border-1 border-white w-100 d-block p-3 text-center orange-clair">Gérer les candidatures</MyLink>
                        </>}
                    </div>
                    <MyLink to="/auth" className="text-white border-top border-1 border-white w-100 d-block p-3 text-center orange-clair" onClick={handleLogout}>Revenir à l'authentification</MyLink>
                </aside>
                <div className="col-12 col-lg-10 z-0 m-0 p-0 w-100">{ children }</div>
            </div>
        </main>
    );
}