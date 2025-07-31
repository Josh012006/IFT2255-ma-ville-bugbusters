import { useNavigate, useParams } from "react-router-dom";
import useRequest from "../../hooks/UseRequest";
import { useEffect, useState } from "react";
import type Signalement from "../../interfaces/Signalement";
import Loader from "../../components/Loader";
import type Problem from "../../interfaces/Problem";
import { Alert, Box, Divider, List, ListItem, ListItemText, Modal } from "@mui/material";
import { boxStyle } from "../../types/boxStyle";
import { formatDate } from "../../utils/formatDate";
import useManualRequest from "../../hooks/UseManualRequest";


/**
 * Cette page est al page de traitement du signalement par le STPM. Elle inclut la récupération individuelle
 * de la notification grâce à signalementId. Après récupération des informations, il faut les afficher.
 * Le STPM a deux possibilités. La première est de choisir une priorité et confirmer. Une fois cela fait, 
 * il faut envoyer une requête pour créer une fiche problème.
 * La deuxième possibilité est de reconnaitre que ce signalement est déjà en lien avec un problème créé et lui lier le signalement.
 * Pour cette deuxième option, il faudra qu'on filtre les problèmes existants et faire un filtrage par description,
 * par quartier et par ville pour trouver des similitudes. Ensuite les proposer au STPM pour qu'il associe un au signelement.
 * Une requête dera ensuite envoyée pour marquer l'opération.
 * @returns ReactNode
 */
export default function ManageSignalementPage() {

    const navigate = useNavigate();

    const signalementId = useParams().id;

    const [signalement, setSignalement] = useState<Signalement | null>(null);
    const [loading, setLoading] = useState<boolean>(true);
    const [error1, setError1] = useState(false);
    const [loading1, setLoading1] = useState<boolean>(true);

    const [similarProblems, setSimilarProblems] = useState<Problem[]>([]);

    const response = useRequest("/signalement/" + signalementId + "?stpm=true", "GET");
    const response1 = useRequest("/probleme/getSimilar?quartier=" + signalement?.quartier.replace(" ", "+") + "&type=" + signalement?.typeProbleme.replace(" ", "+"), "POST", JSON.stringify({description : signalement?.description ?? ""}));


    useEffect(() => {
        if(response) {
            if(response.status === 200) {
                setSignalement(response.data);
                setLoading(false);
            } else {
                console.log(response.data);
                setLoading(false);
                setError1(true);
            }
        }
    }, [response]);

    useEffect(() => {
        if(response && response1) {
            if(response1.status === 200) {
                setSimilarProblems(response1.data);
                setLoading1(false);
            } else {
                console.log(response1.data);
                setLoading1(false);
                setError1(true);
            }
        }
    }, [response, response1]);




    const [show, setShow] = useState<"lier" | "priorite" | "">("");
    const [success, setSuccess] = useState(false);
    const [error, setError] = useState(false);

    const { send, result } = useManualRequest();


    // Gérer l'attribution de priorité
    const [priorite, setPriorite] = useState<"faible" | "moyenne" | "élevée">("faible");
    const [loading2, setLoading2] = useState(false);
    const [disabled1, setDisabled1] = useState(false);

    const handlePriority = async () => {
        setLoading2(true);
        setDisabled2(true);

        const body = {
            typeTravaux: signalement?.typeProbleme,
            quartier: signalement?.quartier,
            localisation: signalement?.localisation,
            description: signalement?.description,
            priorite: priorite,
            signalements: [signalement?.id?? ""],
            residents: [signalement?.resident]
        }

        await send("/probleme", "POST" , JSON.stringify(body));

        setLoading2(false);
    }


    // Gérer le cas où il choisi de lier
    const [chosenToLink, setChosenToLink] = useState<Problem | null>(null);
    const [open, setOpen] = useState(false);
    const [disabled2, setDisabled2] = useState(false);

    const handleLink = async () => {
        setLoading2(true);
        setDisabled1(true);

        const body = {
            resident: signalement?.resident,
            signalement: signalement?.id
        }

        await send("/probleme/addExisting/" + (chosenToLink?.id ?? ""), "PATCH", JSON.stringify(body));

        setLoading2(false);
    }

    useEffect(() => {
        // Set le succès ou l'échec et rediriger ou reload
        if(result) {
            if(result.status === 201 || result.status === 200) {
                setSuccess(true);
                setTimeout(() => {
                    navigate("/stpm/signalement/list");
                }, 1500);
            } else {
                console.log("An error occured:", result.data);
                setError(true);
                setTimeout(() => {
                    window.location.reload();
                }, 1500);
            }
        }
    }, [navigate, result]);



    return(
        <div>
            <h1 className="mt-5 mb-3 text-center">Signalement</h1>
            {(loading || loading1) && <Loader />}
            {error1 && <Alert severity="error">Un problème est survenu. Veuillez réessayer plus tard.</Alert>}
            {!loading && !loading1 && signalement && <div>
                <div className="mt-5 mb-3 d-flex flex-column align-items-center">
                    <p><b>Type de problème</b> : {signalement.typeProbleme}</p>
                    <p><b>Quartier</b> : {signalement.quartier}</p>
                    <p><b>Localisation précise</b> : {signalement.localisation}</p>
                    <p><b>Date de signalement</b> : {signalement.createdAt? formatDate(signalement.createdAt): ""}</p>
                    <p className="my-2 mx-4"><b>Description</b> : {signalement.description}</p>
                </div>
                {signalement.statut && signalement.statut === "traité" && <p className="text-center">Signalement déjà traité</p>}
                {signalement.statut && signalement.statut !== "traité" && <>
                    <div className="d-flex flex-column flex-lg-row justify-content-around align-items-center">
                        <button disabled={disabled2} type="button" className="rounded-3 border-0 text-white orange p-2 my-2 disabled" onClick={() => {setShow("lier")}}>Lier à un problème déjà existant</button>
                        <button disabled={disabled1} type="button" className="rounded-3 border-0 text-white orange p-2 my-2 disabled" onClick={() => {setShow("priorite")}}>Affecter une priorité</button>
                    </div>
                    {show !== "" && <Divider className="my-3" />}
                    {show === "priorite" && <div className="d-flex flex-column align-items-center"> 
                        <div className="d-flex justify-content-center align-items-center w-100">
                            <span onClick={() => {setPriorite("faible")}} className={`${(priorite === "faible")? "bg-secondary text-white" : "bg-white text-black"} p-2 pointer rounded-start-2 border-end`}>Faible</span>
                            <span onClick={() => {setPriorite("moyenne")}} className={`${(priorite === "moyenne")? "bg-secondary text-white" : "bg-white text-black"} p-2 pointer`}>Moyenne</span>
                            <span onClick={() => {setPriorite("élevée")}} className={`${(priorite === "élevée")? "bg-secondary text-white" : "bg-white text-black"} p-2 pointer rounded-end-2 border-start`}>Élevée</span>
                        </div>
                        <button type="button" className="rounded-4 border-0 text-white orange p-3 my-4" onClick={handlePriority}>Confirmer</button>
                        <div className="my-2">
                            {loading2 && <Loader />}
                            {success && <Alert severity="success">Opération réalisée avec succès.</Alert>}
                            {error && <Alert severity="error">Un problème est survenu. Veuillez réessayer plus tard.</Alert>}
                        </div>
                    </div>}
                    {show === "lier" && <div>
                        <h5 className="mt-5 mb-3 text-center">Fiches problèmes existantes et similaires au signalement</h5>
                        {similarProblems.length === 0 && <p className="mb-4 text-center fw-bold">Aucune fiche problème existante traitant du sujet du signalement.</p>}
                        {similarProblems.length !== 0 && 
                        <>
                            <p className="mb-4 text-center">Cliquez sur une d'entre elles pour confirmer lui lier le signalement</p>
                            <List>
                                {similarProblems.map((problem, index) => {
                                    return <div key={index} onClick={() => {setChosenToLink(problem); setOpen(true);}}>
                                        <Divider component="li" />
                                        <ListItem className="d-flex align-items-center hover-white pointer">
                                            <ListItemText
                                                primary={<p><b>{problem.typeTravaux}</b> dans le quartier <b>{problem.quartier}</b></p>}
                                                secondary={<span>{problem.description}</span>}
                                            />
                                        </ListItem>
                                    </div>
                                })}
                            </List>
                        </>}
                        <Modal
                            open={open}
                            onClose={() => {setOpen(false)}}
                        >
                            <Box sx={{ ...boxStyle, display: "flex", flexDirection: "column", justifyContent: "space-around", padding: "20px", width: 400 }}>
                                <div className="w-100 d-flex justify-content-end align-items-center pointer" onClick={() => {setOpen(false)}}>
                                    <img src="/close_icon.png" alt="close icon" width="20"/>
                                </div>
                                <p>Confirmez-vous l'opération ?</p>
                                <button type="button" className="rounded-4 border-0 text-white orange p-3 my-4" onClick={handleLink}>Confirmer</button>
                                <div className="my-2">
                                    {loading2 && <Loader />}
                                    {success && <Alert severity="success">Opération réalisée avec succès.</Alert>}
                                    {error && <Alert severity="error">Un problème est survenu. Veuillez réessayer plus tard.</Alert>}
                                </div>
                            </Box>
                        </Modal>
                    </div>}
                </>}
            </div>}
        </div>
    );

}