import { useEffect, useState } from "react";
import type Signalement from "../../interfaces/Signalement";
import useRequest from "../../hooks/UseRequest";
import Loader from "../../components/Loader";
import MyPagination from "../../components/MyPagination";
import { Alert, Divider, List, ListItem, ListItemText } from "@mui/material";
import MyLink from "../../components/MyLink";
import { formatDate } from "../../utils/formatDate";


/**
 * Cette page permet d'afficher tous les signalements non traités sous forme de liste.
 * Il faut donc les récupérer du backend et les afficher.
 * Lorsqu'on clique sur un signalement en particulier, on est rediriger vers sa page individuelle de traitement.
 * @returns ReactNode
 */
export default function ManageSignalementsPage() {

    const [signalements, setSignalements] = useState<Signalement[]>([]);
    const [paginatedSignalements, setPaginatedSignalements] = useState<Signalement[]>([]);


    // Requête au backend

    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState(false);
    const response = useRequest("/signalement/getAll/", "GET");

    useEffect(() => {
        if(response) {
            if (response.status === 200) {
                setSignalements(response.data.reverse());
                setLoading(false);
            } else {
                console.log(response.data);
                setLoading(false);
                setError(true);
            }
        }
        
    }, [response]);


    return(
        <div>
            <h1 className="mt-5 mb-3 text-center">Signalements non traités</h1>
            <p className="mb-4 text-center">Cliquez sur un signalement pour en voir les détails. Vous pourrez ensuite, soit le lier à une fiche problème existante ou encore en créer une nouvelle en lui affecter une priorité</p>
            {loading && <Loader />}
            {error && <Alert severity="error">Un problème est survenu. Veuillez réessayer plus tard.</Alert>}
            {!loading && !error && <>
                {signalements.length === 0 && <p className="mb-4 text-center fw-bold">Aucun nouveau signalement.</p>}
                {signalements.length !== 0 && <><List>
                    {paginatedSignalements.map((signal, index) => {
                        return <MyLink className="text-black" to={"/stpm/signalement/" + signal.id} key={index}>
                            <Divider component="li" />
                            <ListItem className="d-flex align-items-center hover-white">
                                <ListItemText
                                    primary={<p>Problème de type <b>{signal.typeProbleme}</b> dans le quartier <b>{signal.quartier}</b> - {(signal.createdAt) ? formatDate(signal.createdAt) : ""}</p>}
                                    secondary={<span className="ellipsis">{signal.description}</span>}
                                />
                            </ListItem>
                        </MyLink>
                    })}
                </List>
                <MyPagination itemsPerPage={15} data={signalements} setPaginatedData={setPaginatedSignalements}  />
                </>}
            </>}
        </div>
    );
}