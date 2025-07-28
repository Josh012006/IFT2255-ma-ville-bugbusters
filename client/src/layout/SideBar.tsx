import { type AppDispatch, useAppSelector } from "../redux/store";
import MyLink from "../components/MyLink";
import { useDispatch } from "react-redux";
import { loginInfos, loginType } from "../redux/features/authSlice";


/**
 * Ce composant décrit la sidebar intégrée dans le layout de l'app. Elle s'appuie sur le type d'utilisateur pour déduire 
 * les liens à y montrer. Ne pas oublier d'inclure en bas un lien "Retourner à l'authentification" pour revenir au choix 
 * du type d'utilisateur.
 * @param show permet sur petit écran de dire si on doit la montrer ou non
 * @returns ReactNode
 */
export default function SideBar({show, setShow} : {show: boolean, setShow: React.Dispatch<React.SetStateAction<boolean>>}) {
    const userType : string | null = useAppSelector((state) => state.auth.userType);

    const dispatch = useDispatch<AppDispatch>();

    const handleLogout = () => {
        dispatch(loginInfos(null));
        dispatch(loginType(null));
    }

    return(
        <aside className={`${show? "d-flex" : "d-none"} position-absolute w-100 h-100 position-lg-static p-0 text-white orange d-lg-flex z-3 z-lg-0 flex-column justify-content-between align-items-center`}>
            <div className="d-flex flex-column align-items-center w-100">
                <div className="m-2 w-100 p-2 d-flex justify-content-between align-items-center">
                    <img height="55" src="/udem.png" alt="le logo de l'udem" className="m-2" />
                    <img height="35" src="/tab2.png" alt="sidebar icon" className="m-2 pointer d-block d-lg-none" onClick={() => {setShow(false)}} />
                </div>
                <MyLink onClick={() => {setShow(false)}} to="/" className="text-white border-top border-bottom border-1 border-white w-100 d-block p-3 text-center orange-clair">Dashboard</MyLink>
                {userType === "resident" && <>
                    <MyLink onClick={() => {setShow(false)}} to="/resident/projet/list" className="text-white border-bottom border-1 border-white w-100 d-block p-3 text-center orange-clair">Projets de Travaux</MyLink>
                    <MyLink onClick={() => {setShow(false)}} to="/resident/signalement" className="text-white border-bottom border-1 border-white w-100 d-block p-3 text-center orange-clair">Signalements</MyLink>
                </>}
                {userType === "prestataire" && <>
                    <MyLink onClick={() => {setShow(false)}} to="/prestataire/projet/list" className="text-white border-bottom border-1 border-white w-100 d-block p-3 text-center orange-clair">Projets</MyLink>
                    <MyLink onClick={() => {setShow(false)}} to="/prestataire/candidature/list" className="text-white border-bottom border-1 border-white w-100 d-block p-3 text-center orange-clair">Candidatures</MyLink>
                    <MyLink onClick={() => {setShow(false)}} to="/prestataire/probleme/list" className="text-white border-bottom border-1 border-white w-100 d-block p-3 text-center orange-clair">Fiches problèmes</MyLink>
                </>}
                {userType === "stpm" && <>
                    <MyLink onClick={() => {setShow(false)}} to="/stpm/signalement/list" className="text-white border-bottom border-1 border-white w-100 d-block p-3 text-center orange-clair">Gérer les signalements</MyLink>
                    <MyLink onClick={() => {setShow(false)}} to="/stpm/candidature/list" className="text-white border-bottom border-1 border-white w-100 d-block p-3 text-center orange-clair">Gérer les candidatures</MyLink>
                </>}
            </div>
            <MyLink to="/auth" className="text-white border-top mt-5 border-1 border-white w-100 d-block p-3 text-center orange-clair" onClick={handleLogout}>Revenir à l'authentification</MyLink>
        </aside>
    );
}