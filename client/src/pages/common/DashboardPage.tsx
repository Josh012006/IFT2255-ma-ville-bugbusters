import MyLink from "../../components/MyLink";
import type { Prestataire } from "../../interfaces/users/Prestataire";
import type Resident from "../../interfaces/users/Resident";
import { useAppSelector } from "../../redux/store";


/**
 * Cette page affiche le dashboard en fonction du type d'utilisateur avec les liens et les possibilités.
 * @returns ReactNode
 */
export default function DashboardPage() {
    const userType : string | null = useAppSelector((state) => state.auth.userType);
    const userInfos : Prestataire | Resident | null = useAppSelector((state) => state.auth.infos);

    return (
        <div className="d-flex flex-column align-items-center justify-content-between text-center h-100">
            <div className="d-flex flex-column align-items-center text-center w-100">
                <div>
                    <h1 className="mt-5 mb-3">Bienvenue {userInfos? userInfos.nom : "STPM"}</h1>
                    <h5>Vous êtes connecté en tant que <i><b>{(userType === "resident")? "Résident" : (userType === "prestataire")? "Prestataire" : "Agent STPM"}</b></i>.</h5>
                    <p className="m-5 mb-2">Que voulez-vous faire ?</p>
                </div>
                <div className="d-flex flex-column flex-lg-row w-100 justify-content-around">
                    {userType === "resident" &&
                        <>
                            <div className="d-flex flex-column align-items-center my-3">
                                <MyLink to="/resident/projet/list" className="mycard2 orange text-white rounded-4 p-2 m-2 border d-flex align-items-center justify-content-center">
                                    <img width="50" src="/projet.png" alt="projets" />
                                    <h6 className="mx-3">Projets de Travaux</h6>
                                </MyLink>
                                <span className="fw-bold myspan">Voir les travaux à venir et les travaux en cours</span>
                            </div>
                            <div className="d-flex flex-column align-items-center my-3">
                                <MyLink to="/resident/signalement" className="mycard2 orange text-white rounded-4 p-2 m-2 border d-flex align-items-center justify-content-center">
                                    <img width="50" src="/signalement.png" alt="signalements" />
                                    <h6 className="mx-3">Signalements</h6>
                                </MyLink>
                                <span className="fw-bold myspan">Voir et modifier vos signalements</span>
                            </div>
                        </>
                    }
                    {userType === "prestataire" &&
                        <>
                            <div className="d-flex flex-column align-items-center my-3">
                                <MyLink to="/prestataire/projet/list" className="mycard2 orange text-white rounded-4 p-2 m-2 border d-flex align-items-center justify-content-center">
                                    <img width="50" src="/projet.png" alt="projets" />
                                    <h6 className="mx-3">Projets</h6>
                                </MyLink>
                                <span className="fw-bold myspan">Voir vos projets et les mettre à jour</span>
                            </div>
                            <div className="d-flex flex-column align-items-center my-3">
                                <MyLink to="/prestataire/candidature/list" className="mycard2 orange text-white rounded-4 p-2 m-2 border d-flex align-items-center justify-content-center">
                                    <img width="50" src="/candidature.png" alt="candidatures" />
                                    <h6 className="mx-3">Candidatures</h6>
                                </MyLink>
                                <span className="fw-bold myspan">Voir et modifier vos candidatures</span>
                            </div>
                            <div className="d-flex flex-column align-items-center my-3">
                                <MyLink to="/prestataire/probleme/list" className="mycard2 orange text-white rounded-4 p-2 m-2 border d-flex align-items-center justify-content-center">
                                    <img width="50" src="/probleme.png" alt="problemes" />
                                    <h6 className="mx-3">Fiches problèmes</h6>
                                </MyLink>
                                <span className="fw-bold myspan">Voir les fiches problèmes et soumettre une candidature</span>
                            </div>
                        </>
                    }
                    {userType === "stpm" &&
                        <>
                            <div className="d-flex flex-column align-items-center my-3">
                                <MyLink to="/stpm/signalement/list" className="mycard2 orange text-white rounded-4 p-2 m-2 border d-flex align-items-center justify-content-center">
                                    <img width="50" src="/signalement.png" alt="signalements" />
                                    <h6 className="mx-3">Gérer les signalements</h6>
                                </MyLink>
                                <span className="fw-bold myspan">Voir les nouveaux signalements et affecter une priorité</span>
                            </div>
                            <div className="d-flex flex-column align-items-center my-3">
                                <MyLink to="/stpm/candidature/list" className="mycard2 orange text-white rounded-4 p-2 m-2 border d-flex align-items-center justify-content-center">
                                    <img width="50" src="/candidature.png" alt="candidatures" />
                                    <h6 className="mx-3">Gérer les candidatures</h6>
                                </MyLink>
                                <span className="fw-bold myspan">Voir et traiter les nouvelles candidatures</span>
                            </div>
                        </>
                    }
                </div>
            </div>
            <p className="m-4">Vous pouvez toujours accéder à toutes vos pages en vous dirigeant vers la sidebar</p>
        </div>
    );
}