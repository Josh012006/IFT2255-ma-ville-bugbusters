import { Navigate } from "react-router-dom";
import type { Prestataire } from "../../interfaces/users/Prestataire";
import type Resident from "../../interfaces/users/Resident";
import { type AppDispatch, useAppSelector } from "../../redux/store";
import { useState, useEffect, type FormEvent, type ReactNode } from "react";
import { formatDate } from "../../utils/formatDate";
import { Box, Modal, Chip, FormControl, Select, MenuItem, OutlinedInput, InputAdornment, type SelectChangeEvent } from "@mui/material";
import { QUARTIERS, type Quartier } from "../../types/Quartier";
import { TYPE_TRAVAUX, type TypeTravaux } from "../../types/TypesTravaux";
import Loader from "../../components/Loader";
import useManualRequest from "../../hooks/UseManualRequest";
import { useDispatch } from "react-redux";
import { loginInfos } from "../../redux/features/authSlice";


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

    const dispatch = useDispatch<AppDispatch>();

    const [loading, setLoading] = useState(false);


    type BodyType = 
    | { abonnementsQuartier: Quartier[]; abonnementsRue: string[] }
    | { abonnementsQuartier: Quartier[]; abonnementsType: TypeTravaux[] };

    const initial: BodyType =
    userType === "resident"
        ? { abonnementsQuartier: [], abonnementsRue: [] }
        : { abonnementsQuartier: [], abonnementsType: [] };

    const [body, setBody] = useState<BodyType>(initial);

    const { send, result } = useManualRequest();

    const [open, setOpen] = useState<boolean>(false);

    const [listQuartiers, setListQuartiers] = useState(userInfos ? userInfos.abonnementsQuartier : []);
    const [quartierValue, setQuartierValue] = useState("");
    const [listTypes, setListTypes] = useState((userInfos && "abonnementsType" in userInfos) ? (userInfos as Prestataire).abonnementsType : []);
    const [typeValue, setTypeValue] = useState("");
    const [listRues, setListRues] = useState((userInfos && "abonnementsRue" in userInfos) ? (userInfos as Resident).abonnementsRue : []);
    const [rueValue, setRueValue] = useState("");

    useEffect(() => {
        if(userType === "resident") {
            setBody({
                abonnementsQuartier: listQuartiers,
                abonnementsRue: listRues
            });
        } else if(userType === "prestataire") {
            setBody({
                abonnementsQuartier: listQuartiers,
                abonnementsType: listTypes
            });
        }
    }, [listQuartiers, listRues, listTypes, userType]);

    const style = {
        position: 'absolute',
        top: '50%',
        left: '50%',
        transform: 'translate(-50%, -50%)',
        minWidth: 500,
        bgcolor: 'white',
        boxShadow: 24,
        borderRadius: "20px",
        pt: 2,
        px: 4,
        pb: 3,
    };

    const [submitted, setSubmitted] = useState(false);

    const handleSubmit = async (event : FormEvent) => {
        event.preventDefault();

        setSubmitted(true);

    }

    useEffect(() => {
        if(submitted) {
            setLoading(true);

            // Faire la requête pour update les préférences
            send("/" + userType + "/" + userInfos?.id, "PATCH", JSON.stringify(body));
            
            if(result && result.status === 200) {
                setLoading(false);
                dispatch(loginInfos(result.data));
                setOpen(false);
                window.location.reload();
            }
        }

    }, [body, dispatch, result, send, submitted, userInfos?.id, userType])

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
                            return <Chip className="m-1" key={index} label={type} />
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
                            return <Chip className="m-1" key={index} label={rue} />
                        })}
                    </div>
                </>
            );
        }
    }



    const handleClickQuartier = (event : SelectChangeEvent) => {
        setListQuartiers(list => { 
            const newList : Quartier[] = [...list];
            newList.push(event.target.value as Quartier); 
            return newList;
        });
        setQuartierValue("");
    }

    const handleClickType = (event : SelectChangeEvent) => {
        setListTypes(list => { 
            const newList : TypeTravaux[] = [...list];
            newList.push(event.target.value as TypeTravaux); 
            return newList;
        });
        setTypeValue("");
    }

    const handleAddRue = () => {
        if(rueValue !== "") {
            setListRues(list => { 
                const newList : string[] = [...list];
                newList.push(rueValue); 
                return newList;
            });
            
        }  
        setRueValue("");
    }

    function secondAbonnementModal() : ReactNode {
        if(!userInfos)
            return <></>;
        if("numeroEntreprise" in userInfos) {
            userInfos = userInfos as Prestataire;
            return(
                <>
                    <label className="fw-bold">Abonnements types travaux</label>
                    <Select
                        value={typeValue}
                        onChange={handleClickType}
                        displayEmpty
                    >
                        <MenuItem disabled value="">
                            <em>Sélectionnez le type de travail</em>
                        </MenuItem>
                        {TYPE_TRAVAUX.filter(elem => !listTypes.includes(elem)).map((type, index) => {
                            return <MenuItem key={index} value={type}>{type}</MenuItem>
                        })}
                    </Select>
                    <div className="my-1">
                        {listTypes.map((type, index) => {
                            return <Chip className="m-1" key={index} label={type} onDelete={() => {setListTypes(list => list.filter(elem => elem !== type))}} />
                        })}
                    </div>
                </>
            );
        } else {
            userInfos = userInfos as Resident;
            return(
                <>
                    <label className="fw-bold">Abonnements rues</label>
                    <OutlinedInput
                        value={rueValue}
                        placeholder="Entrez la rue"
                        onChange={(event) => setRueValue(event.target.value)}
                        type="text"
                        sx={{ height: '100%', paddingRight: 0, }}
                        endAdornment={
                            <InputAdornment position="end" sx={{ p: 0, m: 0 }}>
                                <div
                                    className="p-1 bg-success d-flex ml-1 rounded-end-1 align-items-center justify-content-center pointer"
                                    style={{ height: '56px', width: "56px", }}
                                    onClick={handleAddRue}
                                >
                                    <img
                                    alt="validation icon"
                                    src="/validate_icon.png"
                                    width="20"
                                    />
                                </div>
                            </InputAdornment>
                        }
                    />
                    <div className="my-1">
                        {listRues.map((rue, index) => {
                            return <Chip className="m-1" key={index} label={rue} onDelete={() => {setListRues(list => list.filter(elem => elem !== rue))}} />
                        })}
                    </div>
                </>
            );
        }
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
                                    return <Chip className="m-1" key={index} label={quartier} />
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
                            <Box sx={{ ...style, display: "flex", flexDirection: "column", justifyContent: "space-around", padding: "20px", width: 400 }}>
                                <div className="w-100 d-flex justify-content-end align-items-center pointer" onClick={() => {setOpen(false)}}>
                                    <img src="/close_icon.png" alt="close icon" width="20"/>
                                </div>
                                <form className="d-flex flex-column justify-content-around">
                                    {loading && <Loader />}
                                    <FormControl>
                                        <div className="my-2 d-flex flex-column">
                                            <label className="fw-bold">Abonnements quartiers</label>
                                            <Select
                                                value={quartierValue}
                                                onChange={handleClickQuartier}
                                                displayEmpty
                                            >
                                                <MenuItem disabled value="">
                                                    <em>Sélectionnez le quartier</em>
                                                </MenuItem>
                                                {QUARTIERS.filter(elem => !listQuartiers.includes(elem)).map((quartier, index) => {
                                                    return <MenuItem key={index} value={quartier}>{quartier}</MenuItem>
                                                })}
                                            </Select>
                                            <div className="my-1">
                                                {listQuartiers.map((quartier, index) => {
                                                    return <Chip className="m-1" key={index} label={quartier} onDelete={() => {setListQuartiers(list => list.filter(elem => elem !== quartier))}} />
                                                })}
                                            </div>
                                        </div>
                                    </FormControl>
                                    <FormControl>
                                        <div className="my-2 d-flex flex-column">
                                            {secondAbonnementModal()}
                                        </div>
                                    </FormControl>
                                    <button type="submit" className="rounded-4 border-0 text-white orange p-3 my-4" onClick={handleSubmit}>Soumettre</button>
                                </form>
                            </Box>
                        </Modal>
                    </div>
                </div>
            </div>
        )
    }
}