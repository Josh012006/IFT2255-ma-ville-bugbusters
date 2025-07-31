import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Typography, Modal, TextField, Box, Alert,} from "@mui/material";
import useRequest from "../../hooks/UseRequest";
import type Candidature from "../../interfaces/Candidature";
import Loader from "../../components/Loader";
import { dateToInputValue, formatDate } from "../../utils/formatDate";
import { boxStyle } from "../../types/boxStyle";
import useManualRequest from "../../hooks/UseManualRequest";
import { useAppSelector } from "../../redux/store";



/**
 * Cette page permet d'afficher une candidature d'un prestataire avec ses détails.
 * Il faut donc récupérer les informations depuis le backend avec l'id.
 * Le prestataire a aussi la possibilité de modifier ou de supprimer la candidature tant que son
 * statut n'est pas marquée comme 'vue' par le STPM.
 * @returns ReactNode
 */ 
export default function PrestataireCandidaturePage() {

    const navigate = useNavigate();
    const candidatureId = useParams().id;

    const userInfos = useAppSelector(state => state.auth.infos);

    const res = useRequest("/candidature/" + (candidatureId?? "") + "?stpm=false", "GET");
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(false);

    const [loading1, setLoading1] = useState(false);
    const [error1, setError1] = useState(false);
    const [error2, setError2] = useState(false);
    const [success1, setSuccess1] = useState(false);

    const [candidature, setCandidature] = useState<Candidature | null>(null);
    const [open1, setOpen1] = useState(false);
    const [open2, setOpen2] = useState(false);


    const [formData, setFormData] = useState({
        titreProjet: "",
        description: "",
        dateDebut: "",
        dateFin: "",
    });

    const { send, result } = useManualRequest();


    // Recuperation des candidatures
    useEffect(() => {
        if(res) {
            if (res.status === 200) {
                setLoading(false);
                setCandidature(res.data);
            } else {
                console.log(res.data);
                setLoading(false);
                setError(true);
            }
        }
    }, [res]);


    // Ouvrir la modale de modification et préremplir les champs
    const handleOpenModal = () => {
        setFormData({
            titreProjet: candidature?.titreProjet?? "",
            description: candidature?.description?? "",
            dateDebut: dateToInputValue(candidature?.dateDebut?? new Date()),
            dateFin: dateToInputValue(candidature?.dateFin?? new Date()),
        })
        setOpen1(true);
    };

    // Modification des champs de la modal
    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    // Modification d'une candidature
    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        setError2(false);

        const body = candidature as Candidature;
        body.titreProjet = formData.titreProjet;
        body.description = formData.description;
        const date1 = new Date(formData.dateDebut);
        const date2 = new Date(formData.dateFin);

        if(date1 > date2) {
            setError2(true);
            return;
        }
        body.dateDebut = date1;
        body.dateFin = date2;

        setLoading1(true);

        await send(`/candidature/${candidatureId}`, "PATCH", JSON.stringify(body)); 
    };

    // Suppression d'une candidature
    const handleDelete = async () => {
        setLoading1(true);

        await send(`/candidature/${candidatureId}`, "DELETE");
    };

    useEffect(() => {
        if(result) {
            if(result.status === 200) {
                setLoading1(false);
                setSuccess1(true);
                setTimeout(() => {
                    if(open1) {
                        window.location.reload();
                    } else if(open2) {
                        navigate("/prestataire/candidature/list");
                    }
                }, 1500);
            } else {
                console.log("An error occured:", result.data);
                setLoading1(false);
                setError1(true);
                setTimeout(() => {
                    window.location.reload();
                }, 1500);
            }
            
            
        }
    }, [navigate, open1, open2, result]);

    return (
        <div>
            <h1 className="mt-5 mb-3 text-center">Candidature</h1>
            {loading && <Loader />}
            {error && <Alert severity="error">Un problème est survenu. Veuillez réessayer plus tard.</Alert>}
            {!loading && candidature && candidature.prestataire === userInfos?.id &&<div>

                <div className="mt-3">
                    <div className="mt-5 mb-3 d-flex flex-column align-items-center">
                        <p><b>Titre du projet</b> : {candidature.titreProjet}</p>
                        <p><b>Type de travaux requis</b> : {candidature.typeTravaux}</p>
                        <p><b>Période de réalisation</b> : {formatDate(candidature.dateDebut, true)} - {formatDate(candidature.dateFin, true)}</p>
                        <p><b>Rues affectées</b> : {candidature.ruesAffectees.join(", ")}</p>
                        <p><b>Coût estimé</b> : {candidature.coutEstime} $</p>
                        <p><b>Statut</b> : {candidature.statut}</p>
                        <p className="my-2 mx-4 mx-lg-5"><b>Description du projet</b> : {candidature.description}</p>
                    </div>
                    {candidature.statut !== "en attente" && <p className="text-center">Votre candidature {(candidature.statut === "vue")? "est en cours de traitement par un agent." : "a déjà été traitée."}</p>}
                    {candidature.statut === "en attente" && (
                        <div className="d-flex flex-column flex-lg-row justify-content-around align-items-center">
                            <button className="rounded-4 border-0 text-white p-3 my-4 orange" type="button" onClick={handleOpenModal}>Modifier</button>
                            <button className="rounded-4 border-0 text-white p-3 my-4 bg-danger" type="button" onClick={() => setOpen2(true)}>Supprimer</button>
                        </div>
                    )}
                </div>

                <Modal
                    open={open2}
                    onClose={() => {setOpen2(false)}}
                >
                    <Box sx={{ ...boxStyle, display: "flex", flexDirection: "column", justifyContent: "space-around", padding: "20px", width: 400 }}>
                        <div className="w-100 d-flex justify-content-end align-items-center pointer" onClick={() => {setOpen2(false)}}>
                            <img src="/close_icon.png" alt="close icon" width="20"/>
                        </div>
                        <p>Confirmez-vous l'opération ?</p>
                        <button type="button" className="rounded-4 border-0 text-white orange p-3 my-4" onClick={() => {handleDelete()}}>Confirmer</button>
                        <div className="my-2">
                            {loading1 && <Loader />}
                            {success1 && <Alert severity="success">Opération réalisée avec succès.</Alert>}
                            {error1 && <Alert severity="error">Un problème est survenu. Veuillez réessayer plus tard.</Alert>}
                        </div>
                    </Box>
                </Modal>

                <Modal open={open1} onClose={() => setOpen1(false)}>
                    <Box sx={{ ...boxStyle, display: "flex", flexDirection: "column", justifyContent: "space-around", padding: "20px", width: 400 }}>
                    <div className="w-100 d-flex justify-content-end align-items-center pointer" onClick={() => {setOpen1(false)}}>
                        <img src="/close_icon.png" alt="close icon" width="20"/>
                    </div>
                    <Typography variant="h6" gutterBottom>Modifier la candidature</Typography>
                    <form onSubmit={handleSubmit}>
                        <div className="my-2">
                            {loading1 && <Loader />}
                            {success1 && <Alert severity="success">Opération réalisée avec succès.</Alert>}
                            {error1 && <Alert severity="error">Un problème est survenu. Veuillez réessayer plus tard.</Alert>}
                            {error2 && <Alert severity="error">La date de début du projet doit venir avant celle de fin.</Alert>}
                        </div>
                        <TextField
                            fullWidth
                            label="Titre du projet"
                            name="titreProjet"
                            value={formData.titreProjet}
                            onChange={handleChange}
                            margin="normal"
                            required
                        />
                        <TextField
                            fullWidth
                            label="Description"
                            name="description"
                            multiline
                            rows={3}
                            value={formData.description}
                            onChange={handleChange}
                            margin="normal"
                            required
                        />
                        <TextField
                            fullWidth
                            label="Date début"
                            name="dateDebut"
                            type="date"
                            value={formData.dateDebut}
                            onChange={handleChange}
                            slotProps={{inputLabel: { shrink: true } }}
                            margin="normal"
                            required
                        />
                        <TextField
                            fullWidth
                            label="Date fin"
                            name="dateFin"
                            type="date"
                            value={formData.dateFin}
                            onChange={handleChange}
                            slotProps={{inputLabel: { shrink: true } }}
                            margin="normal"
                            required
                        />

                        <div className="d-flex justify-content-center align-items-center">
                            <button className="rounded-4 border-0 text-white p-3 my-4 orange" type="submit">Confirmer</button>
                        </div>
                    </form>
                    </Box>
                </Modal>
            </div>}
        </div>
  );
}
