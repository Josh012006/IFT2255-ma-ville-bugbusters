import { useNavigate } from "react-router-dom";
import { useEffect, useState, type FormEvent } from "react";
import { Box, MenuItem, TextField, Alert } from "@mui/material";
import { TYPE_TRAVAUX } from "../../types/TypesTravaux";
import { QUARTIERS } from "../../types/Quartier";
import Loader from "../../components/Loader";
import { useAppSelector } from "../../redux/store";
import type Resident from "../../interfaces/users/Resident";
import type Signalement from "../../interfaces/Signalement";
import useManualRequest from "../../hooks/UseManualRequest";

/**
 * Cette page permet au résident de faire un nouveau signalement.
 * Elle contient principalement un formulaire avec les informations demandées et avlidées avant d'envoyer une requête
 * au serveur avec les informations afin de créer le nouveau signalement.
 * @returns ReactNode
 */
export default function NewSignalementPage() {
    const navigate = useNavigate();
    const userInfos = useAppSelector(state => state.auth.infos) as Resident;
    const { send, result } = useManualRequest();

    const [loading, setLoading] = useState(false);
    const [success, setSuccess] = useState(false);
    const [error, setError] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");

    const [typeProbleme, setTypeProbleme] = useState("");
    const [description, setDescription] = useState("");
    const [localisation, setLocalisation] = useState("");
    const [quartier, setQuartier] = useState("");

    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();

        if (typeProbleme === "" || description === "" || localisation === "" || quartier === "") {
            setError(true);
            setErrorMessage("Veuillez remplir tous les champs obligatoires.");
            return;
        }

        setLoading(true);
        setError(false);

        const signalement = {
            resident: userInfos?.id ?? "",
            typeProbleme,
            description,
            localisation,
            quartier,
        } as Signalement;

        send("/signalement", "POST", JSON.stringify(signalement));
    };

    useEffect(() => {
        if (result) {
            if (result.status === 201) {
                setLoading(false);
                setSuccess(true);
                setTimeout(() => {
                    navigate("/dashboard");
                }, 1500);
            } else {
                setLoading(false);
                setError(true);
                setErrorMessage("Une erreur est survenue lors de la création du signalement.");
            }
        }
    }, [navigate, result]);

    return (
        <Box sx={{ p: 4, maxWidth: 700, margin: "auto" }}>
            {loading && <Loader />}
            <h2>Signaler un nouveau problème</h2>
            <p>Veuillez remplir le formulaire ci-dessous pour signaler un nouveau problème dans votre quartier.</p>

            <form onSubmit={handleSubmit}>
                <Box sx={{ display: "flex", flexDirection: "column", gap: 3, mt: 4 }}>
                    {error && <Alert severity="error">{errorMessage}</Alert>}
                    {success && <Alert severity="success">Signalement envoyé avec succès ! Vous serez redirigé.</Alert>}

                    <TextField
                        select
                        label="Type de problème"
                        value={typeProbleme}
                        onChange={(e) => setTypeProbleme(e.target.value)}
                        required
                    >
                        {TYPE_TRAVAUX.map((option) => (
                            <MenuItem key={option} value={option}>
                                {option}
                            </MenuItem>
                        ))}
                    </TextField>

                    <TextField
                        label="Localisation (ex: en face du 244 rue Sherbrooke)"
                        variant="outlined"
                        fullWidth
                        value={localisation}
                        onChange={(e) => setLocalisation(e.target.value)}
                        required
                    />

                    <TextField
                        select
                        label="Quartier"
                        value={quartier}
                        onChange={(e) => setQuartier(e.target.value)}
                        required
                    >
                        {QUARTIERS.map((option) => (
                            <MenuItem key={option} value={option}>
                                {option}
                            </MenuItem>
                        ))}
                    </TextField>

                    <TextField
                        label="Description détaillée du problème"
                        variant="outlined"
                        fullWidth
                        multiline
                        rows={4}
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        required
                    />

                    <button type="submit" className="btn btn-primary" disabled={loading || success}>
                        Envoyer le signalement
                    </button>
                </Box>
            </form>
        </Box>
    );
}