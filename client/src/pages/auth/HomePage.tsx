import { useEffect, useState } from "react";
import { loginType } from "../../redux/features/authSlice";
import { useDispatch } from "react-redux";
import type { AppDispatch } from "../../redux/store";
import MyLink from "../../components/MyLink";


/**
 * La page initiale que l'utilisateur voit lorsqu'il entre dans l'application.
 * @returns ReactNode
 */
function HomePage() {

    const [showCase, setShowCase] = useState<boolean>(true);

    const dispatch = useDispatch<AppDispatch>();

    useEffect(() => {
        setTimeout(() => {
            setShowCase(false);
        }, 8000);
    }, []);

    const handleUserType = (type : string) => {
        dispatch(loginType(type));
    }
    
    return (
        <>
            {showCase && 
            <main className="d-flex flex-column justify-content-around align-items-center text-center min-vh-100 p-3 fade-in-out intro">
                <h1 className="fs-1">Bienvenue dans l'application MaVille &#x1F306;!</h1>
                <p className="fs-3 fw-bold dancing-script">Une ville n’est pas seulement des rues et des immeubles, c’est un espace vivant que vous façonnez chaque jour.</p>
                <img src="/montreal.png" height="30" alt="Logo de la ville de Montréal" />
            </main>}

            {!showCase && 
            <main className="d-flex flex-column justify-content-center align-items-center text-center min-vh-100">
                <h1 className="mt-5 mb-150">Choix du type d'utilisateur</h1>
                <div className="d-flex flex-column flex-lg-row justify-content-around align-items-center">
                    <MyLink to="/auth/choix" className="mycard orange text-white rounded-4 p-3 m-5 border d-flex align-items-center justify-content-center" onClick={() => {handleUserType("resident")}}>
                        <img width="64" src="/resident.png" alt="résident" />
                        <h3 className="fs-5 mx-2">Résident</h3>
                    </MyLink>
                    <MyLink to="/auth/choix" className="mycard orange text-white rounded-4 p-3 m-5 d-flex border align-items-center justify-content-center" onClick={() => {handleUserType("prestataire")}}>
                        <img width="50" src="/prestataire.png" alt="prestataire" />
                        <h3 className="fs-5 mx-2">Prestataire</h3>
                    </MyLink>
                    <MyLink to="/dashboard" className="mycard orange text-white rounded-4 p-3 m-5 d-flex border align-items-center justify-content-center" onClick={() => {handleUserType("stpm")}}>
                        <img width="50" src="/stpm.png" alt="Stpm agent" />
                        <h3 className="fs-5 mx-2">STPM</h3>
                    </MyLink>
                </div>
            </main>}
        </>
    );
}

export default HomePage;