import { useNavigate, useParams } from "react-router-dom";
import useRequest from "../../hooks/UseRequest";
import type Candidature from "../../interfaces/Candidature";
import { useEffect, useState } from "react";
import Loader from "../../components/Loader";
import { formatDate } from "../../utils/formatDate";
import { boxStyle } from "../../types/boxStyle";
import { Alert, Box, Divider, Modal, TextField } from "@mui/material";
import useManualRequest from "../../hooks/UseManualRequest";


/**
 * Cette pas est la page de traitement d'une candidature. Il faut récupérer les informations de la candidature 
 * à partir de son id et les afficher. Ensuite le STPM a le droit de l'accepter ou de la refuser. S'il la refuse, 
 * il faut lui demander une raison.
 * Une fois une décision prise, il faut faire un appel au backend pour marquer la décision.
 * @returns ReactNode
 */
export default function ManageCandidaturePage() {

    const navigate = useNavigate();

    const [candidature, setCandidature] = useState<Candidature | null>(null);
    const [loading, setLoading] = useState(true);
    const [error1, setError1] = useState(false);

    const candidatureId = useParams().id;

    const response = useRequest("/candidature/" + candidatureId + "?stpm=true", "GET");

    useEffect(() => {
        if(response) {
            if(response.status === 200) {
                setCandidature(response.data);
                setLoading(false);
            } else {
                console.log(response.data);
                setLoading(false);
                setError1(true);
            }
        }
    }, [response]);



    const [show, setShow] = useState<"accept" | "reject" | "">("");
    const [disabledAccept, setDisabledAccept] = useState(false);
    const [disabledReject, setDisabledReject] = useState(false);

    const [error, setError] = useState(false);
    const [success, setSuccess] = useState(false);

    const { send, result } = useManualRequest();


    // Gérer l'acceptation
    const [open, setOpen] = useState(false);
    const [loading1, setLoading1] = useState(false);

    const handleAccept = async () => {
        setDisabledReject(true);
        setLoading1(true);

        const body = {
            candidature : candidature?.id?? ""
        }

        await send("/projet", "POST" , JSON.stringify(body));

        setLoading1(false);
    }


    const [reason, setReason] = useState("");
    const [loading2, setLoading2] = useState(false);

    const handleReject = async () => {
        setDisabledAccept(true);
        setLoading2(true);

        const body = {
            raison : reason
        }

        await send("/candidature/markAsRejected/" + candidature?.id, "PATCH" , JSON.stringify(body));

        setLoading2(false);
    }



    useEffect(() => {
        // Set le succès ou l'échec et rediriger ou reload
        if(result) {
            if(result.status === 201 || result.status === 200) {
                setSuccess(true);
                setTimeout(() => {
                    navigate("/stpm/candidature/list");
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
            <h1 className="mt-5 mb-3 text-center">Candidature</h1>
            {loading && <Loader />}
            {error1 && <Alert severity="error">Un problème est survenu. Veuillez réessayer plus tard.</Alert>}
            {!loading && candidature && <div>
                <div className="mt-5 mb-3 d-flex flex-column align-items-center">
                    <h5 className="fw-bold my-3">Projet proposé par {candidature.nomPrestataire} - {candidature.numeroEntreprise}</h5>
                    <p><b>Titre du projet</b> : {candidature.titreProjet}</p>
                    <p><b>Type de travaux requis</b> : {candidature.typeTravaux}</p>
                    <p><b>Période de réalisation</b> : {formatDate(candidature.dateDebut, true)} - {formatDate(candidature.dateFin, true)}</p>
                    <p><b>Rues affectées</b> : {candidature.ruesAffectees.join(", ")}</p>
                    <p><b>Coût estimé</b> : {candidature.coutEstime} $</p>
                    <p><b>Date de candidature</b> : {candidature.createdAt? formatDate(candidature.createdAt): ""}</p>
                    <p className="my-2 mx-4 mx-lg-5"><b>Description du projet</b> : {candidature.description}</p>
                </div>
                <div className="d-flex flex-column flex-lg-row justify-content-around align-items-center">
                    <button disabled={disabledAccept} type="button" className="rounded-3 border-0 text-white bg-success p-2 my-2 disabled" onClick={() => {setShow("accept"); setOpen(true)}}>Accepter</button>
                    <button disabled={disabledReject} type="button" className="rounded-3 border-0 text-white bg-danger p-2 my-2 disabled" onClick={() => {setShow("reject")}}>Refuser</button>
                </div>
                {show !== "" && <Divider className="my-3" />}
                {show === "accept" && <div>
                    <Modal
                        open={open}
                        onClose={() => {setOpen(false)}}
                    >
                        <Box sx={{ ...boxStyle, display: "flex", flexDirection: "column", justifyContent: "space-around", padding: "20px", width: 400 }}>
                            <div className="w-100 d-flex justify-content-end align-items-center pointer" onClick={() => {setOpen(false)}}>
                                <img src="/close_icon.png" alt="close icon" width="20"/>
                            </div>
                            <p>Confirmez-vous l'opération ?</p>
                            <button type="button" className="rounded-4 border-0 text-white orange p-3 my-4" onClick={handleAccept}>Confirmer</button>
                            <div className="my-2">
                                {loading1 && <Loader />}
                                {success && <Alert severity="success">Opération réalisée avec succès.</Alert>}
                                {error && <Alert severity="error">Un problème est survenu. Veuillez réessayer plus tard.</Alert>}
                            </div>
                        </Box>
                    </Modal>
                </div>}
                {show === "reject" && <div className="d-flex flex-column align-items-center">
                    <label className="fw-bold my-1">Raison de refus</label>
                    <TextField placeholder="Entrez une raison de refus" sx={{ minWidth: "400px" }} value={reason} onChange={(event) => {setReason(event?.target.value)}}  />
                    <button disabled={reason === ""} type="button" className="rounded-4 border-0 text-white orange p-3 my-4" onClick={handleReject}>Confirmer</button>
                    <div className="my-2">
                        {loading2 && <Loader />}
                        {success && <Alert severity="success">Opération réalisée avec succès.</Alert>}
                        {error && <Alert severity="error">Un problème est survenu. Veuillez réessayer plus tard.</Alert>}
                    </div>
                </div>}
            </div>}
        </div>
    );
}