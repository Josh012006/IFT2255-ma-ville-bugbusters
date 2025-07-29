import { useParams } from "react-router-dom";
import { useState } from "react";
import {Box,Button,MenuItem,TextField,Typography,Alert,Stack} from "@mui/material";
/**
 * Cette page permet au prestataire de soumettre une nouvelle candidature pour une fiche problème qu'il a repéré.
 * Il faut donc utiliser un formulaire pour récupérer les informations nécessaires, les valider et les soumettre au backend.
 * Dans la soumission, il y aura un besoin de connaitre l'id de la fiche. C'est cette information qui est contenue dans ficheProbleme
 * récupéré dans le path.
 * @returns ReactNode
 */

    export default function NewCandidaturePage() {
    const ficheProbleme = useParams().problemId;

    const [prestataire, setPrestataire] = useState("");
    const [nomPrestataire, setNomPrestataire] = useState("");
    const [numeroEntreprise, setNumeroEntreprise] = useState("");
    const [titreProjet, setTitreProjet] = useState("");
    const [description, setDescription] = useState("");
    const [typeTravaux, setTypeTravaux] = useState("");
    const [coutEstime, setCoutEstime] = useState("");
    const [dateDebut, setDateDebut] = useState("");
    const [dateFin, setDateFin] = useState("");
    const [ruesAffectees, setRuesAffectees] = useState("");
    const [erreur, setErreur] = useState("");
    const [message, setMessage] = useState("");

    const handleSubmit = (e) => {
        e.preventDefault();

        if (!prestataire || !nomPrestataire ||!ficheProbleme ||!numeroEntreprise ||!titreProjet ||!description ||
            !typeTravaux ||!coutEstime ||!dateDebut ||!dateFin ||!ruesAffectees
        ) {
        setErreur("Tous les champs sont requis.");
        return;
        }

        const candidature = {
        prestataire,
        nomPrestataire,
        ficheProbleme,
        numeroEntreprise,
        titreProjet,
        description,
        typeTravaux,
        coutEstime: parseFloat(coutEstime),
        dateDebut: new Date(dateDebut).toISOString(),
        dateFin: new Date(dateFin).toISOString(),
        ruesAffectees,
        };

        fetch("http://localhost:7070/candidatures", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(candidature),
        })
        .then((response) => response.json())
        .then(() => {
            setPrestataire("");
            setNomPrestataire("");
            setNumeroEntreprise("");
            setTitreProjet("");
            setDescription("");
            setTypeTravaux("");
            setCoutEstime("");
            setDateDebut("");
            setDateFin("");
            setRuesAffectees("");
            setErreur("");
            setMessage("Votre candidature a été envoyée avec succès !");
        })
        .catch(() => {
            setErreur("Une erreur est survenue lors de l'envoi de la candidature.");
        });
    };

    return (
        <Box sx={{ maxWidth: 600, mx: "auto", mt: 4 }}>
        <Typography variant="h5" gutterBottom>
            Soumettre une candidature pour la fiche {ficheProbleme}
        </Typography>

        <form onSubmit={handleSubmit}>
            <Stack spacing={2}>
            <TextField
                label="Prestataire"
                value={prestataire}
                onChange={(e) => setPrestataire(e.target.value)}
                fullWidth
            />

            <TextField
                label="Nom de l'entreprise"
                value={nomPrestataire}
                onChange={(e) => setNomPrestataire(e.target.value)}
                fullWidth
            />

            <TextField
                label="Numéro d'entreprise"
                value={numeroEntreprise}
                onChange={(e) => setNumeroEntreprise(e.target.value)}
                fullWidth
            />

            <TextField
                label="Titre du projet"
                value={titreProjet}
                onChange={(e) => setTitreProjet(e.target.value)}
                fullWidth
            />

            <TextField
                label="Description"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                multiline
                rows={3}
                fullWidth
            />

            <TextField
                select
                label="Type de travaux"
                value={typeTravaux}
                onChange={(e) => setTypeTravaux(e.target.value)}
                fullWidth
            >
                {[
                "Travaux routiers",
                "Travaux de gaz ou électricité",
                "Construction ou rénovation",
                "Entretien paysager",
                "Travaux liés aux transports en commun",
                "Travaux de signalisation et éclairage",
                "Travaux souterrains",
                "Travaux résidentiel",
                "Entretien urbain",
                "Entretien des réseaux de télécommunication",
                ].map((option) => (
                <MenuItem key={option} value={option}>
                    {option}
                </MenuItem>
                ))}
            </TextField>

            <TextField
                label="Coût estimé"
                type="number"
                value={coutEstime}
                onChange={(e) => setCoutEstime(e.target.value)}
                fullWidth
            />

            <TextField
                label="Date de début"
                type="date"
                value={dateDebut}
                onChange={(e) => setDateDebut(e.target.value)}
                InputLabelProps={{ shrink: true }}
                fullWidth
            />

            <TextField
                label="Date de fin"
                type="date"
                value={dateFin}
                onChange={(e) => setDateFin(e.target.value)}
                InputLabelProps={{ shrink: true }}
                fullWidth
            />

            <TextField
                label="Rues affectées"
                value={ruesAffectees}
                onChange={(e) => setRuesAffectees(e.target.value)}
                fullWidth
            />

            {erreur && <Alert severity="error">{erreur}</Alert>}
            {message && <Alert severity="success">{message}</Alert>}

            <Button type="submit" variant="contained">
                Soumettre
            </Button>
            </Stack>
        </form>
        </Box>
    );
    }