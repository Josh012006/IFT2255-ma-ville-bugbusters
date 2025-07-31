import { useNavigate, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { Box, TextField, Alert, Typography, Modal } from "@mui/material";
import Loader from "../../components/Loader";
import useRequest from "../../hooks/UseRequest";
import useManualRequest from "../../hooks/UseManualRequest";
import type Signalement from "../../interfaces/Signalement";
import { boxStyle } from "../../types/boxStyle";
import { useAppSelector } from "../../redux/store";



/**
 * Cette page permet d'afficher les détails d'un signalement du résident. Il faut donc récupérer les informations depuis le backend.
 * Le résident à la possibilité de modifier ou de supprimer un signalement tantq u'il n'est pas encore marqué comme
 * statut 'vu' par le STPM.
 * @returns ReactNode
 */
export default function ResidentSignalementPage() {
    const navigate = useNavigate();

    const userInfos = useAppSelector(state => state.auth.infos);
    const { id:signalementId } = useParams();

    const res = useRequest(`/signalement/${signalementId}?stpm=false`, "GET");
    const [loading, setLoading] = useState(true);
    const [loading1, setLoading1] = useState(false);
    const [error, setError] = useState(false);
    const [error1, setError1] = useState(false);
    const [signalement, setSignalement] = useState<Signalement | null>(null);

    const { send, result } = useManualRequest();
    const [success, setSuccess] = useState(false);

    const [open1, setOpen1] = useState(false);
    const [open2, setOpen2] = useState(false);


    const [formData, setFormData] = useState({
        description: "",
    });

    useEffect(() => {
        if(res) {
            if (res.status === 200) {
                setLoading(false);
                setSignalement(res.data);
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
            description: signalement?.description?? ""
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

        const body = signalement as Signalement;
        body.description = formData.description;

        setLoading1(true);

        await send(`/signalement/${signalementId}`, "PATCH", JSON.stringify(body)); 
    };

    // Suppression d'une candidature
    const handleDelete = async () => {
        setLoading1(true);

        await send(`/signalement/${signalementId}`, "DELETE");
    };

    useEffect(() => {
        if(result) {
            if(result.status === 200) {
                setLoading1(false);
                setSuccess(true);
                setTimeout(() => {
                    if(open1) {
                        window.location.reload();
                    } else if(open2) {
                        navigate("/resident/signalement/list");
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
            <h1 className="mt-5 mb-3 text-center">Signalement</h1>
            {loading && <Loader />}
            {error && <Alert severity="error">Un problème est survenu. Veuillez réessayer plus tard.</Alert>}
            {!loading && signalement && signalement.resident === userInfos?.id && <div>

                <div className="mt-3">
                    <div className="mt-5 mb-3 mx-2 d-flex flex-column align-items-center">
                        <p><b>Type de problème</b> : {signalement.typeProbleme}</p>
                        <p><b>Quartier</b> : {signalement.quartier}</p>
                        <p><b>Localisation précise</b> : {signalement.localisation}</p>
                        <p className="my-2 mx-4"><b>Description</b> : {signalement.description}</p>
                    </div>
                    {signalement.statut !== "en attente" && <p className="text-center">Votre signalement {(signalement.statut === "vu")? "est en cours de traitement par un agent." : "a déjà été traitée."}</p>}
                    {signalement.statut === "en attente" && (
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
                            {success && <Alert severity="success">Opération réalisée avec succès.</Alert>}
                            {error1 && <Alert severity="error">Un problème est survenu. Veuillez réessayer plus tard.</Alert>}
                        </div>
                    </Box>
                </Modal>

                <Modal open={open1} onClose={() => setOpen1(false)}>
                    <Box sx={{ ...boxStyle, display: "flex", flexDirection: "column", justifyContent: "space-around", padding: "20px", width: 400 }}>
                    <div className="w-100 d-flex justify-content-end align-items-center pointer" onClick={() => {setOpen1(false)}}>
                        <img src="/close_icon.png" alt="close icon" width="20"/>
                    </div>
                    <Typography variant="h6" gutterBottom>Modifier le signalement</Typography>
                    <form onSubmit={handleSubmit}>
                        <div className="my-2">
                            {loading1 && <Loader />}
                            {success && <Alert severity="success">Opération réalisée avec succès.</Alert>}
                            {error1 && <Alert severity="error">Un problème est survenu. Veuillez réessayer plus tard.</Alert>}
                        </div>
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