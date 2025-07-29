import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {Button,Card,CardContent,Typography,Modal,TextField,Box,CardActions,} from "@mui/material";
import useRequest from "../hooks/useRequest";
import type { Candidature } from "../types/Candidature";
import { Grid } from "@mui/material";

/**
 * Cette page permet d'afficher une candidature d'un prestataire avec ses détails.
 * Il faut donc récupérer les informations depuis le backend avec l'id.
 * Le prestataire a aussi la possibilité de modifier ou de supprimer la candidature tant que son
 * statut n'est pas marquée comme 'vue' par le STPM.
 * @returns ReactNode
 */ 


const modalStyle = {
    position: "absolute" as const,
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    width: 500,
    bgcolor: "background.paper",
    p: 4,
    borderRadius: 2,
};

export default function PrestataireCandidaturePage() {
    const candidatureId = useParams().id;
    const [candidatures, setCandidatures] = useState<Candidature[]>([]);
    const [expandedId, setExpandedId] = useState<string | null>(null);
    const [showModal, setShowModal] = useState(false);
    const [formData, setFormData] = useState({
        titreProjet: "",
        description: "",
        dateDebut: "",
        dateFin: "",
    });
    const [selectedCandidatureId, setSelectedCandidatureId] = useState<string | null>(null);  

    // Recuperation des candidatures
    useEffect(() => {
    async function fetchCandidatures() {
        const res = await useRequest("/candidatures/prestataire", "GET");
        if (res.status === 200) {
            setCandidatures(res.data);
        }
    }

    fetchCandidatures();
  }, []);

    // Ouvrir ou refermer une card
    const handleExpand = (id: string) => {
        setExpandedId(expandedId === id ? null : id);
    }; 

    // Ouvrir la modale de modification et préremplir les champs
    const handleOpenModal = (cand: Candidature) => {
        setSelectedCandidatureId(cand.id!);
        setFormData({
        titreProjet: cand.titreProjet,
        description: cand.description,
        dateDebut: cand.dateDebut?.substring(0, 10) || "",
        dateFin: cand.dateFin?.substring(0, 10) || "",
        });
        setShowModal(true);
    };

    // Modification des champs de la modale
    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    // Modification d'une candidature
    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        const res = await useRequest(`/candidature/${selectedCandidatureId}`, "PUT", formData);
        if (res.status === 200) {
        setCandidatures((prev) =>
            prev.map((cand) =>
            cand.id === selectedCandidatureId
                ? { ...cand, ...formData }
                : cand
            )
        );
        setShowModal(false);
        }
    };

    // Suppression d'une candidature
    const handleDelete = async (id: string) => {
        const confirm = window.confirm("Supprimer cette candidature ?");
        if (confirm) {
        const res = await useRequest(`/candidature/${id}`, "DELETE");
        if (res.status === 200) {
            setCandidatures((prev) => prev.filter((c) => c.id !== id));
        }
        }
    };

    return (
        <div style={{ padding: "2rem" }}>
        <Typography variant="h4" gutterBottom>Mes candidatures</Typography>

        <Grid container spacing={2}>
            {candidatures.map((cand) => (
            <Grid item xs={12} sm={6} md={4} key={cand.id}>
                <Card>
                <CardContent>
                    <Typography variant="h6">{cand.titreProjet}</Typography>
                    <Button onClick={() => handleExpand(cand.id!)} size="small">
                    {expandedId === cand.id ? "Voir moins" : "Voir plus"}
                    </Button>

                    {expandedId === cand.id && (
                    <div style={{ marginTop: "1rem" }}>
                        <Typography><strong>Description:</strong> {cand.description}</Typography>
                        <Typography><strong>Dates:</strong> {new Date(cand.dateDebut).toLocaleDateString()} ➜ {new Date(cand.dateFin).toLocaleDateString()}</Typography>
                        <Typography><strong>Type de travaux:</strong> {cand.typeTravaux}</Typography>
                        <Typography><strong>Coût estimé:</strong> {cand.coutEstime} $</Typography>
                        <Typography><strong>Statut:</strong> {cand.statut}</Typography>

                        {cand.statut === "en attente" && (
                        <CardActions>
                            <Button variant="contained" onClick={() => handleOpenModal(cand)}>
                            Modifier
                            </Button>
                            <Button variant="outlined" color="error" onClick={() => handleDelete(cand.id!)}>
                            Supprimer
                            </Button>
                        </CardActions>
                        )}
                    </div>
                    )}
                </CardContent>
                </Card>
            </Grid>
            ))}
        </Grid>

        <Modal open={showModal} onClose={() => setShowModal(false)}>
            <Box sx={modalStyle}>
            <Typography variant="h6" gutterBottom>Modifier la candidature</Typography>
            <form onSubmit={handleSubmit}>
                <TextField
                fullWidth
                label="Titre du projet"
                name="titreProjet"
                value={formData.titreProjet}
                onChange={handleChange}
                margin="normal"
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
                />
                <TextField
                fullWidth
                label="Date début"
                name="dateDebut"
                type="date"
                value={formData.dateDebut}
                onChange={handleChange}
                InputLabelProps={{ shrink: true }}
                margin="normal"
                />
                <TextField
                fullWidth
                label="Date fin"
                name="dateFin"
                type="date"
                value={formData.dateFin}
                onChange={handleChange}
                InputLabelProps={{ shrink: true }}
                margin="normal"
                />
                <div style={{ marginTop: "1rem" }}>
                <Button type="submit" variant="contained">Enregistrer</Button>
                <Button onClick={() => setShowModal(false)} style={{ marginLeft: "1rem" }}>
                    Annuler
                </Button>
                </div>
            </form>
            </Box>
        </Modal>
        </div>
  );
}
