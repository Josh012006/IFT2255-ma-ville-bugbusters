import { useState, useEffect } from "react";
import useRequest from "../../hooks/UseRequest";
import type Candidature from "../../interfaces/Candidature";
import { useAppSelector } from "../../redux/store";
import Loader from "../../components/Loader";
import { Alert, Divider, List, ListItem, ListItemText } from "@mui/material";
import MyLink from "../../components/MyLink";
import MyPagination from "../../components/MyPagination";
import { formatDate } from "../../utils/formatDate";





/**
 * Cette page permet au prestataire de visualiser toutes se candidatures. Il faut donc les rechercher depuis le backend.
 * Lorsqu'on clique sur l'une d'entre elles, on est redirigé vers la page de visualisation individuelle de la candidature.
 * @returns ReactNode
 */
export default function PrestataireCandidaturesPage() {

    const prestataireId = useAppSelector((state) => state.auth.infos)?.id;

    const [candidatures, setCandidatures] = useState<Candidature[]>([]);
    const [paginatedCandidatures, setPaginatedCandidatures] = useState<Candidature[]>([]);


    // Requête au backend

    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState(false);
    const response = useRequest("/candidature/getByPrestataire/" + (prestataireId ?? ""), "GET");

    useEffect(() => {
        if(response) {
            if (response.status === 200) {
                setCandidatures(response.data.reverse());
                setLoading(false);
            }  else {
                console.log(response.data);
                setLoading(false);
                setError(true);
            }
        }
    }, [response]);

    return (
        <div>
            <h1 className="mt-5 mb-3 text-center">Vos candidatures</h1>
            <p className="mb-4 text-center">Cliquez sur un candidature pour en voir les détails. Si un agent de la STPM ne l'a pas encore vue, vous pourrez la modifier.</p>
            {loading && <Loader />}
            {error && <Alert severity="error">Un problème est survenu. Veuillez réessayer plus tard.</Alert>}
            {!loading && !error && 
            <>
                {candidatures.length === 0 && <p className="mb-4 text-center fw-bold">Aucune candidature.</p>}
                {candidatures.length !== 0 && <>
                    <List>
                        {paginatedCandidatures.map((cand, index) => {
                            return <MyLink className="text-black" to={"/prestataire/candidature/" + cand.id} key={index}>
                                <Divider component="li" />
                                <ListItem className="d-flex align-items-center hover-white">
                                    <ListItemText
                                        primary={<>
                                            <h5>{cand.nomPrestataire}</h5>
                                            <p>Candidature pour le problème de type <b>{cand.typeTravaux}</b> - {(cand.createdAt) ? formatDate(cand.createdAt) : ""}</p>
                                            <p><b>Rues affectées</b> : {cand.ruesAffectees.join(", ")}</p>
                                        </>}
                                        secondary={<span className="ellipsis">{cand.description}</span>}
                                    />
                                </ListItem>
                            </MyLink>
                        })}
                    </List>
                    <MyPagination itemsPerPage={15} data={candidatures} setPaginatedData={setPaginatedCandidatures}  />
                </>}
            </>}
        </div>
    );
}