import { useEffect, useState } from "react";
import type Candidature from "../../interfaces/Candidature";
import useRequest from "../../hooks/UseRequest";
import Loader from "../../components/Loader";
import MyLink from "../../components/MyLink";
import MyPagination from "../../components/MyPagination";
import { Alert, Divider, List, ListItem, ListItemText } from "@mui/material";
import { formatDate } from "../../utils/formatDate";



/**
 * Cette page permet d'afficher toutes les candidatures non traitées par le STPM.
 * Il faut donc les récupérer depuis le backend.
 * Lorsqu'on clique sur une candidature, on est redirigé vers la page de traitement individuel de la candidture.
 * @returns ReactNode
 */
export default function ManageCandidaturesPage() {
    const [candidatures, setCandidatures] = useState<Candidature[]>([]);
    const [paginatedCandidatures, setPaginatedCandidatures] = useState<Candidature[]>([]);


    // Requête au backend

    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState(false);
    const response = useRequest("/candidature/getAll/", "GET");

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


    return(
        <div>
            <h1 className="mt-5 mb-3 text-center">Candidatures non traitées</h1>
            <p className="mb-4 text-center">Cliquez sur un candidature pour en voir les détails. Vous pourrez ensuite l'accepter ou la refuser.</p>
            {loading && <Loader />}
            {error && <Alert severity="error">Un problème est survenu. Veuillez réessayer plus tard.</Alert>}
            {!loading && 
            <>
                {candidatures.length === 0 && <p className="mb-4 text-center fw-bold">Aucune nouvelle candidature.</p>}
                {candidatures.length !== 0 && <>
                    <List>
                        {paginatedCandidatures.map((cand, index) => {
                            return <MyLink className="text-black" to={"/stpm/candidature/" + cand.id} key={index}>
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