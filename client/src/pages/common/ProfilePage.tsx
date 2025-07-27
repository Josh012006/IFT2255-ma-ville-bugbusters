import { Navigate } from "react-router-dom";
import type { Prestataire } from "../../interfaces/users/Prestataire";
import type Resident from "../../interfaces/users/Resident";
import { useAppSelector } from "../../redux/store";
import { useState, type FormEvent, type ReactNode } from "react";
import { formatDate } from "../../utils/formatDate";
import { Box, Modal, Chip, FormControl, Select, MenuItem, type SelectChangeEvent } from "@mui/material";
import { QUARTIERS, type Quartier } from "../../types/Quartier";
import { TYPE_TRAVAUX } from "../../types/TypesTravaux";
import Loader from "../../components/Loader";


/**
 * Il s'agit de la page de profil prévue uniquement pour les résidents et les prestataires. Elle permet d'afficher les informations 
 * de l'utilisateur et particulièrement ses préférences de notifications qu'il peut modifier.
 * Ne pas oublier d'envoyer une requête pour modifier les préférences si cela arrive et 
 * d'update aussi le redux store après.
 * @returns ReactNode
 */
export default function ProfilePage() {
    const userType : string | null = useAppSelector((state) => state.auth.userType);
    let userInfos : Prestataire | Resident | null = useAppSelector((state) => state.auth.infos);

    const [loading, setLoading] = useState(false);


    const [open, setOpen] = useState<boolean>(false);

    const [listQuartiers, setListQuartiers] = useState(userInfos ? userInfos.abonnementsQuartier : []);
    const [listTypes, setListTypes] = useState((userInfos && "abonnementsType" in userInfos) ? (userInfos as Prestataire).abonnementsType : []);
    const [listRues, setListRues] = useState((userInfos && "abonnementsRue" in userInfos) ? (userInfos as Resident).abonnementsRue : []);


    const handleSubmit = (event : FormEvent) => {
        event.preventDefault();

        setLoading(true);

        // Faire la requête


        setLoading(false);

    }

    function presentationRest() : ReactNode {
        if(!userInfos)
            return <></>;
        if("numeroEntreprise" in userInfos) {
            userInfos = userInfos as Prestataire;
            return(
                <>
                    <p className="mb-1"><b>Numero d'entreprise</b> : {userInfos.numeroEntreprise}</p>
                    <p className="mb-1"><b>Quartiers couverts</b> : {userInfos.quartiers.join(", ")}</p>
                    <p className="mb-1"><b>Types de travaux acceptés</b> : {userInfos.typesTravaux.join(", ")}</p>
                </>
            );
        } else {
            userInfos = userInfos as Resident;
            return(
                <>
                    <p className="mb-1"><b>Date de naissance</b> : {formatDate(userInfos.dateNaissance, false)}</p>
                    <p className="mb-1"><b>Adresse</b> : {userInfos.adresse}, {userInfos.codePostal}, MONTREAL, QC</p>
                </>
            );
        }
    }

    function secondAbonnement() : ReactNode {
        if(!userInfos)
            return <></>;
        if("numeroEntreprise" in userInfos) {
            userInfos = userInfos as Prestataire;
            return(
                <>
                    <div className="d-flex justify-content-start align-items-center mb-1">
                        <span className="fw-bold">Abonnements types de travaux</span>
                        <span className="rounded-circle orange text-white small-notice">{userInfos.abonnementsType.length}</span>
                    </div>
                    <div>
                        {userInfos.abonnementsType.map((type, index) => {
                            return <Chip key={index} label={type} />
                        })}
                    </div>
                </>
            );
        } else {
            userInfos = userInfos as Resident;
            return(
                <>
                    <div className="d-flex justify-content-start align-items-center mb-1">
                        <span className="fw-bold">Abonnements rues</span>
                        <span className="rounded-circle orange text-white small-notice">{userInfos.abonnementsRue.length}</span>
                    </div>
                    <div>
                        {userInfos.abonnementsRue.map((rue, index) => {
                            return <Chip key={index} label={rue} />
                        })}
                    </div>
                </>
            );
        }
    }



    const handleClickQuartier = (event : SelectChangeEvent) => {
        setListQuartiers(list => { list.push(event.target.value as Quartier); return list})
    }

    

    if(!userType || userType === "stpm" || !userInfos) {
        return <Navigate to="/dashboard" replace />
    } else {
        return(
            <div className="p-5">
                <div>
                    <h1 className="mt-5">{userInfos.nom}</h1>
                    <p>{userInfos.adresseCourriel}</p>
                    <div className="my-3 d-flex flex-column justify-content-center">{presentationRest()}</div>
                    <div className="mt-4">
                        <div className="my-2">
                            <div className="d-flex justify-content-start align-items-center mb-1">
                                <span className="fw-bold">Abonnements quartiers</span>
                                <span className="rounded-circle orange text-white small-notice">{userInfos.abonnementsQuartier.length}</span>
                            </div>
                            <div>
                                {userInfos.abonnementsQuartier.map((quartier, index) => {
                                    return <Chip key={index} label={quartier} />
                                })}
                            </div>
                        </div>
                        <div className="my-2">
                            {secondAbonnement()}
                        </div>
                        <button type="button" className="rounded-4 border-0 text-white orange p-3 my-4" onClick={() => {setOpen(true)}}>Modifier les préférences de notifications</button>
                        <Modal
                            open={open}
                            onClose={() => {setOpen(false)}}
                        >
                            <Box sx={{ display: "flex", flexDirection: "column", justifyContent: "space-around", padding: "20px", width: 400 }}>
                                <div className="w-100 d-flex justify-content-end align-items-center">
                                    <img src="/close_icon.png" alt="close icon" width="10"/>
                                </div>
                                <FormControl>
                                    {loading && <Loader />}
                                    <div className="my-2">
                                        <label className="fw-bold">Abonnements quartiers</label>
                                        <Select
                                            onChange={handleClickQuartier}
                                            displayEmpty
                                            inputProps={{ 'aria-label': 'Without label' }}
                                            >
                                            {QUARTIERS.filter(elem => !(elem in listQuartiers)).map((quartier, index) => {
                                                return <MenuItem key={index} value={quartier}>{quartier}</MenuItem>
                                            })}
                                        </Select>
                                        <div>
                                            {listQuartiers.map((quartier, index) => {
                                                return <Chip key={index} label={quartier} onDelete={() => {setListQuartiers(list => list.filter(elem => elem !== quartier))}} />
                                            })}
                                        </div>
                                    </div>
                                    <div className="my-2">
                                        {secondAbonnementModal()}
                                    </div>
                                    <button type="submit" className="rounded-4 border-0 text-white orange p-3 my-4" onClick={handleSubmit}>Soumettre</button>
                                </FormControl>
                            </Box>
                        </Modal>
                    </div>
                </div>
            </div>
        )
    }
}