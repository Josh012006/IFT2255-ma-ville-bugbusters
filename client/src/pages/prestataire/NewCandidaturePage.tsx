import { useNavigate, useParams } from "react-router-dom";
import { useEffect, useRef, useState, type FormEvent } from "react";
import { Box, MenuItem, TextField, Alert, Stack, Chip, InputAdornment } from "@mui/material";
import { TYPE_TRAVAUX } from "../../types/TypesTravaux";
import Loader from "../../components/Loader";
import { useAppSelector } from "../../redux/store";
import type { Prestataire } from "../../interfaces/users/Prestataire";
import type Candidature from "../../interfaces/Candidature";
import useManualRequest from "../../hooks/UseManualRequest";


/**
 * Cette page permet au prestataire de soumettre une nouvelle candidature pour une fiche problème qu'il a repéré.
 * Il faut donc utiliser un formulaire pour récupérer les informations nécessaires, les valider et les soumettre au backend.
 * Dans la soumission, il y aura un besoin de connaitre l'id de la fiche. C'est cette information qui est contenue dans ficheProbleme
 * récupéré dans le path.
 * @returns ReactNode
 */

export default function NewCandidaturePage() {

    
    const myRef = useRef<HTMLDivElement | null>(null);

    const userInfos = useAppSelector(state =>  state.auth.infos) as Prestataire;

    const ficheProbleme = useParams().problemId;
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(false);
    const [error1, setError1] = useState(false);
    const [success, setSuccess] = useState(false);

    const [titreProjet, setTitreProjet] = useState("");
    const [description, setDescription] = useState("");
    const [typeTravaux, setTypeTravaux] = useState("");
    const [coutEstime, setCoutEstime] = useState("");
    const [dateDebut, setDateDebut] = useState("");
    const [dateFin, setDateFin] = useState("");
    const [ruesAffectees, setRuesAffectees] = useState<string[]>([]);
    const [newRue, setNewRue] = useState("");
    const [error2, setError2] = useState(false);


    const handleAddRue = () => {
        if(newRue !== "") {
            const newList = [...ruesAffectees, newRue];
            setRuesAffectees(newList);
            setNewRue("");
        }
    }



    const { send, result } = useManualRequest();

    const navigate = useNavigate();

    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();
        myRef.current?.scrollIntoView({
            behavior: 'smooth'
        });
        setError1(false);
        setError2(false);

        if(ruesAffectees.length === 0) {
            setError2(true);
            return;
        }
        
        const date1 = new Date(dateDebut);
        const date2 = new Date(dateFin); 

        if(date1 > date2) {
            setError1(true);
            return;
        }

        setLoading(true);

        const candidature = {
            prestataire: userInfos?.id?? "",
            nomPrestataire: userInfos?.nom,
            ficheProbleme: ficheProbleme?? "",
            numeroEntreprise: userInfos?.numeroEntreprise?? "",
            titreProjet,
            description,
            typeTravaux,
            coutEstime: parseFloat(coutEstime),
            dateDebut: date1,
            dateFin: date2,
            ruesAffectees,
        } as Candidature;

        send("/candidature", "POST", JSON.stringify(candidature));
        
    };

    useEffect(() => {
        if(result) {
            if(result.status === 201) {
                setLoading(false);
                setSuccess(true);
                setTimeout(() => {
                    navigate("/prestataire/candidature/list")
                }, 2000);
            } else {
                setLoading(false);
                setError(true);
                console.log("An error occured", result.data);
                setTimeout(() => {
                    window.location.reload();
                }, 2000);
            }   
        }
    }, [navigate, result]);

    return (
        <div ref={myRef} className="d-flex flex-column align-items-center">
            <h1 className="mt-5 mb-3 text-center">Soumettre une candidature</h1>
            <p>Veuillez remplir le formulaire ci-dessous pour soumettre une candidature pour la fiche problème.</p>
            <Box sx={{ width: 420, mx: 6, mt: 4, mb: 6 }}>
                <form onSubmit={handleSubmit}>
                    <div className="my-3">
                        {loading && <Loader />}
                        {error && <Alert severity="error">Un problème est survenu. Veuillez réessayer plus tard.</Alert>}
                        {error1 && <Alert severity="error">La date de début du projet doit venir avant celle de fin.</Alert>}
                        {error2 && <Alert severity="error">Il est nécessaire de préciser les rues affectées.</Alert>}
                        {success && <Alert severity="success">Candidature soumise avec succès. Un agent du STPM traitera votre signalement sous peu.</Alert>}
                    </div>

                    <Stack spacing={2}>

                        <TextField
                            label="Titre du projet"
                            value={titreProjet}
                            onChange={(e) => setTitreProjet(e.target.value)}
                            fullWidth
                            required
                        />

                        <TextField
                            label="Description"
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
                            multiline
                            rows={3}
                            fullWidth
                            required
                        />

                        <TextField
                            select
                            label="Type de travaux"
                            value={typeTravaux}
                            onChange={(e) => setTypeTravaux(e.target.value)}
                            fullWidth
                            required
                        >
                            {TYPE_TRAVAUX.map((option) => (
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
                            required
                        />

                        <TextField
                            label="Date de début"
                            type="date"
                            value={dateDebut}
                            onChange={(e) => setDateDebut(e.target.value)}
                            slotProps={{inputLabel: { shrink: true } }}
                            fullWidth
                            required
                        />

                        <TextField
                            label="Date de fin"
                            type="date"
                            value={dateFin}
                            onChange={(e) => setDateFin(e.target.value)} 
                            slotProps={{inputLabel: { shrink: true } }}
                            fullWidth
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
                            {ruesAffectees.map((rue, index) => {
                                return <Chip className="m-1" key={index} label={rue} onDelete={() => {setRuesAffectees(list => list.filter(elem => elem !== rue))}} />
                            })}
                        </div>

                        <button className="rounded-4 border-0 text-white p-3 my-4 orange" type="submit">Soumettre</button>
                    </Stack>
                </form>
            </Box>
        </div>
    );
    }