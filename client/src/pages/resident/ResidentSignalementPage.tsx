import { useNavigate, useParams } from "react-router-dom";
import { useEffect, useState, type FormEvent } from "react";
import { Box, MenuItem, TextField, Alert, Typography } from "@mui/material";
import { TYPE_TRAVAUX } from "../../types/TypesTravaux";
import { QUARTIERS } from "../../types/Quartier";
import Loader from "../../components/Loader";
import useRequest from "../../hooks/UseRequest";
import useManualRequest from "../../hooks/UseManualRequest";
import type Signalement from "../../interfaces/Signalement";



/**
 * Cette page permet d'afficher les détails d'un signalement du résident. Il faut donc récupérer les informations depuis le backend.
 * Le résident à la possibilité de modifier ou de supprimer un signalement tantq u'il n'est pas encore marqué comme
 * statut 'vu' par le STPM.
 * @returns ReactNode
 */
export default function ResidentSignalementPage() {
    const navigate = useNavigate();
    const { id } = useParams();

    const res = useRequest(`/signalement/${id}?stpm=false`, "GET");
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(false);
    const [signalement, setSignalement] = useState<Signalement | null>(null);

    const { send: sendUpdate, result: updateResult } = useManualRequest();
    const [updateLoading, setUpdateLoading] = useState(false);
    const [success, setSuccess] = useState(false);
    const [updateError, setUpdateError] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");

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

    useEffect(() => {
        if (updateResult) {
            setUpdateLoading(false);
            if (updateResult.status === 200) {
                setSuccess(true);
                setTimeout(() => navigate("/dashboard"), 1500);
            } else {
                setUpdateError(true);
                setErrorMessage("Une erreur est survenue lors de la mise à jour.");
            }
        }
    }, [updateResult, navigate]);

    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();
        setUpdateLoading(true);
        setUpdateError(false);

        const form = e.target as HTMLFormElement;
        const formData = new FormData(form);
        const updatedSignalement = Object.fromEntries(formData.entries());

        await sendUpdate(`/signalement/${id}`, "PATCH", JSON.stringify(updatedSignalement));
    };

    const canBeModified = signalement?.statut === "en attente";

    if (loading) return <Loader />;
    if (error || !signalement) return <Alert severity="error">Impossible de charger les détails du signalement.</Alert>;

    return (
        <Box sx={{ p: 4, maxWidth: 700, margin: "auto" }}>
            {updateLoading && <Loader />}
            <Typography variant="h4" gutterBottom>Détails du Signalement</Typography>
            
            {!canBeModified && (
                <Alert severity="info" sx={{ mb: 2 }}>
                    Ce signalement a été traité par la STPM et ne peut plus être modifié.
                </Alert>
            )}

            <form onSubmit={handleSubmit}>
                <Box sx={{ display: "flex", flexDirection: "column", gap: 3, mt: 2 }}>
                    {updateError && <Alert severity="error">{errorMessage}</Alert>}
                    {success && <Alert severity="success">Signalement mis à jour ! Vous serez redirigé.</Alert>}

                    <TextField
                        select
                        label="Type de problème"
                        name="typeProbleme"
                        defaultValue={signalement.typeProbleme}
                        disabled={!canBeModified}
                        required
                    >
                        {TYPE_TRAVAUX.map((option) => (
                            <MenuItem key={option} value={option}>{option}</MenuItem>
                        ))}
                    </TextField>

                    <TextField
                        label="Localisation"
                        name="localisation"
                        defaultValue={signalement.localisation}
                        disabled={!canBeModified}
                        required
                    />

                    <TextField
                        select
                        label="Quartier"
                        name="quartier"
                        defaultValue={signalement.quartier}
                        disabled={!canBeModified}
                        required
                    >
                        {QUARTIERS.map((option) => (
                            <MenuItem key={option} value={option}>{option}</MenuItem>
                        ))}
                    </TextField>

                    <TextField
                        label="Description"
                        name="description"
                        multiline
                        rows={4}
                        defaultValue={signalement.description}
                        disabled={!canBeModified}
                        required
                    />

                    {canBeModified && (
                        <button type="submit" className="btn btn-primary" disabled={updateLoading || success}>
                            Mettre à jour le signalement
                        </button>
                    )}
                </Box>
            </form>
        </Box>
    );
}