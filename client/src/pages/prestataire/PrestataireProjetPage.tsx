import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import type  Projet  from "../../interfaces/Projet";
import { Alert, Modal, Box, TextField, MenuItem, InputAdornment, Chip, Typography } from "@mui/material";
import useRequest from "../../hooks/UseRequest";
import Loader from "../../components/Loader";
import { dateToInputValue, formatDate } from "../../utils/formatDate";
import useManualRequest from "../../hooks/UseManualRequest";
import { boxStyle } from "../../types/boxStyle";
import { useAppSelector } from "../../redux/store";

type typeStatus = "en cours" | "annulé" | "suspendu" | "terminé";




/**
 * Cette page permet de visualiser un projet d'un prestataire. 
 * Il faut donc chercher le projet depuis le backend en utilisant l'id.
 * Le prestataire peut aussi modifier certaines informations du projets, notamment les dates 
 * de fin et de début, la description ou le statut.
 * @returns ReactNode
 */
export default function PrestataireProjetPage() {

    const { id: projetId } = useParams();

    const userInfos = useAppSelector(state => state.auth.infos);
    const navigate = useNavigate();

    const [projet, setProjet] = useState<Projet | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(false);

    const response = useRequest("/projet/" + projetId, "GET");

    useEffect(() => {
        if(response) {
            if(response.status === 200) {
                setProjet(response.data);
                setLoading(false);
            } else {
                console.log(response.data);
                setLoading(false);
                setError(true);
            }
        }
    }, [response]);


    const [open, setOpen] = useState(false);
    const [loading1, setLoading1] = useState(false);
    const [error1, setError1] = useState(false);
    const [error2, setError2] = useState(false);
    const [error3, setError3] = useState(false);
    const [success1, setSuccess1] = useState(false);

    const [newRue, setNewRue] = useState("");
    const [formData, setFormData] = useState<{statut: string, ruesAffectees: string[], description: string, dateDebut: string, dateFin: string}>({
        statut: "",
        ruesAffectees: [],
        description: "",
        dateDebut: "",
        dateFin: "",
    });

    const { send, result } = useManualRequest();

    // Ouvrir la modale de modification et préremplir les champs
    const handleOpenModal = () => {
        setFormData({
            statut: projet?.statut?? "",
            ruesAffectees: projet?.ruesAffectees?? [],
            description: projet?.description?? "",
            dateDebut: dateToInputValue(projet?.dateDebut?? new Date()),
            dateFin: dateToInputValue(projet?.dateFin?? new Date()),
        })
        setOpen(true);
    };

    // Modification des champs de la modal
    const handleAddRue = () => {
        if(newRue !== "") {
            const newList = [...formData.ruesAffectees, newRue];
            setFormData({...formData, ruesAffectees: newList});
            setNewRue("");
        }
    }

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    // Modification d'une candidature
    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        setError2(false);
        setError3(false);

        const body = projet as Projet;
        body.description = formData.description;

        body.statut = formData.statut as typeStatus;

        if(formData.ruesAffectees.length === 0) {
            setError3(true);
            return;
        }
        body.ruesAffectees = formData.ruesAffectees;


        const date1 = new Date(formData.dateDebut);
        const date2 = new Date(formData.dateFin);

        if(date1 > date2) {
            setError2(true);
            return;
        }
        body.dateDebut = date1;
        body.dateFin = date2;

        setLoading1(true);

        await send(`/projet/${projetId}`, "PATCH", JSON.stringify(body));
    };

    useEffect(() => {
        if(result) {
            if(result.status === 200) {
                setLoading1(false);
                setSuccess1(true);
            } else {
                console.log("An error occured:", result.data);
                setLoading1(false);
                setError1(true); 
            }
            setTimeout(() => {
                window.location.reload();
            }, 1500);
        }
    }, [navigate, result]);


    return (
        <div>
            <h1 className="mt-5 mb-3 text-center">Projet</h1>
            {loading && <Loader />}
            {error && <Alert severity="error">Un problème est survenu. Veuillez réessayer plus tard.</Alert>}
            {!loading && projet && projet.prestataire === userInfos?.id && <>
                <div className="mt-5 mb-3 mx-2 d-flex flex-column align-items-center">
                    <p><b>Titre du projet</b> : {projet.titreProjet}</p>
                    <p><b>Type de travaux requis</b> : {projet.typeTravaux}</p>
                    <p><b>Quartier</b> : {projet.quartier}</p>
                    <p><b>Période de réalisation prévue</b> : {formatDate(projet.dateDebut, true)} - {formatDate(projet.dateFin, true)}</p>
                    <p><b>Rues affectées</b> : {projet.ruesAffectees.join(", ")}</p>
                    <br />
                    <p><b>Priorité</b> : {projet.priorite}</p>
                    <p><b>Nombre de rapports</b> : {projet.nbRapports}</p>
                    <p><b>Coût</b> : {projet.cout} $</p>
                    <p><b>Statut</b> : {projet.statut}</p>
                    <br/>
                    <p className="my-2 mx-4 mx-lg-5"><b>Description du projet</b> : {projet.description}</p>
                </div>
                <div className="d-flex justify-content-center align-items-center m-2"><button className="rounded-4 border-0 text-white p-3 my-4 orange" type="button" onClick={handleOpenModal}>Modifier les informations du projet</button></div>
                <Modal open={open} onClose={() => setOpen(false)}>
                    <Box sx={{ ...boxStyle, display: "flex", flexDirection: "column", justifyContent: "space-around", padding: "20px", width: 400 }}>
                        <div className="w-100 d-flex justify-content-end align-items-center pointer" onClick={() => {setOpen(false)}}>
                            <img src="/close_icon.png" alt="close icon" width="20"/>
                        </div>
                        <Typography variant="h6" gutterBottom>Modifier le projet</Typography>
                        <form onSubmit={handleSubmit}>
                            <div className="my-2">
                                {loading1 && <Loader />}
                                {success1 && <Alert severity="success">Opération réalisée avec succès.</Alert>}
                                {error1 && <Alert severity="error">Un problème est survenu. Veuillez réessayer plus tard.</Alert>}
                                {error2 && <Alert severity="error">La date de début du projet doit venir avant celle de fin.</Alert>}
                                {error3 && <Alert severity="error">Il est nécessaire de préciser les rues affectées.</Alert>}
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
                            <TextField
                                select
                                label="Statut"
                                value={formData.statut}
                                name="statut"
                                onChange={handleChange}
                                fullWidth
                                required
                            >
                                <MenuItem value="en cours">En cours</MenuItem>
                                <MenuItem value="annulé">Annulé</MenuItem>
                                <MenuItem value="suspendu">Suspendu</MenuItem>
                                <MenuItem value="terminé">Terminé</MenuItem>
                            </TextField>
                            
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

                            <TextField
                                label="Rues affectées"
                                type="text"
                                value={newRue}
                                onChange={(e) => {setNewRue(e?.target.value);}}
                                fullWidth
                                sx={{ height: '100%', pr: 0 }}
                                slotProps={{
                                    input: {
                                        style : {paddingRight: 0},
                                        endAdornment : (
                                            <InputAdornment position="end" sx={{ p: 0, m: 0 }}>
                                                <div
                                                    className="p-1 bg-success d-flex ml-1 rounded-end-1 align-items-center justify-content-center pointer adornment"
                                                    onClick={handleAddRue}
                                                >
                                                    <img
                                                    alt="validation icon"
                                                    src="/validate_icon.png"
                                                    width="20"
                                                    />
                                                </div>
                                            </InputAdornment>
                                        )
                                    }
                                }}
                            />
                            <div className="my-1">
                                {formData.ruesAffectees.map((rue, index) => {
                                    return <Chip className="m-1" key={index} label={rue} onDelete={() => {const newList = formData.ruesAffectees.filter(elem => elem !== rue); setFormData({...formData, ruesAffectees: newList});}} />
                                })}
                            </div>

                            <div className="d-flex justify-content-center align-items-center">
                                <button className="rounded-4 border-0 text-white p-3 my-4 orange" type="submit">Confirmer</button>
                            </div>
                        </form>
                    </Box>
                </Modal>
            </>}
        </div>
    );
}