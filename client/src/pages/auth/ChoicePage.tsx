import { Navigate } from "react-router-dom";
import { useAppSelector, type AppDispatch } from "../../redux/store";
import { useEffect, useState } from "react";
import type { Prestataire } from "../../interfaces/users/Prestataire";
import type Resident from "../../interfaces/users/Resident";
import useRequest from "../../hooks/UseRequest";
import MyLink from "../../components/MyLink";
import { useDispatch } from "react-redux";
import { loginInfos } from "../../redux/features/authSlice";
import Loader from "../../components/Loader";
import { Alert } from "@mui/material";

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
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState(false);
    const response = useRequest("/" + userType + "/getAll", "GET");

    useEffect(() => {
        if (response ) {
            if(response.status === 200) {
                setUsers(response.data);
                setLoading(false);
            } else {
                console.log(response.data);
                setLoading(false);
                setError(true);
            }
        }
    }, [response]);


    const dispatch = useDispatch<AppDispatch>();

    const handleInfos = (user: Prestataire | Resident) => {
        dispatch(loginInfos(user));
    }

    if(!userType || userType === "stpm") {
        return <Navigate to="/auth" replace />
    } else {
        return (
            <div className="d-flex flex-column justify-content-around align-items-center">
                <h1 className="m-5">Choix d'utilisateur</h1>
                {loading && <Loader />}
                {error && <Alert severity="error">Un problème est survenu. Veuillez réessayer plus tard.</Alert>}
                {!loading &&<div className="p-3 min-vh-100 grid row">
                    {users.map((user, index) => {
                        if("abonnementsRue" in user) {
                            const resident = user as Resident;
                
                            return <div key={index} className={`col-12 d-flex align-items-center justify-content-center col-lg-4`}>
                                <MyLink key={index} to="/" onClick={() => {handleInfos(resident)}} className={`mycard1 orange text-white rounded-4 p-3 m-5 border `}>
                                    <h5 className="fs-5 fw-bold text-center"> {resident.nom}</h5>
                                    <p><b>Quartier</b> : {resident.quartier}</p>
                                    <p><b>Abonnements quartiers</b> : {resident.abonnementsQuartier.join(", ")}</p>
                                    <p><b>Abonnements rues</b> : {resident.abonnementsRue.join(", ")}</p>
                                </MyLink>
                            </div>
                        } else {
                            const prestataire = user as Prestataire;
                            return <div key={index} className={`col-12 d-flex align-items-center justify-content-center col-lg-4`}>
                                <MyLink to="/" onClick={() => {handleInfos(prestataire)}} className={`mycard1 orange text-white rounded-4 p-3 m-5 border`}>
                                    <h5 className="fs-5 fw-bold text-center">{prestataire.nom}</h5>
                                    <p><b>Quartiers couverts</b> : {prestataire.quartiers.join(", ")}</p>
                                    <p><b>Types de travaux offerts</b> : {prestataire.typesTravaux.join(", ")}</p>
                                    <p><b>Abonnements quartiers</b> : {prestataire.abonnementsQuartier.join(", ")}</p>
                                    <p><b>Abonnements types travaux</b> : {prestataire.abonnementsType.join(", ")}</p>
                                </MyLink>
                            </div>
                        }
                    })}
                </div>}
            </div>
        );
    }
    
}