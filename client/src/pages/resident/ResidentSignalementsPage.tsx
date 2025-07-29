import { useState, useEffect } from "react";
import useRequest from "../../hooks/UseRequest";
import type Signalement from "../../interfaces/Signalement";
import { useAppSelector } from "../../redux/store";
import Loader from "../../components/Loader";
import { Alert, Divider, List, ListItem, ListItemText } from "@mui/material";
import MyLink from "../../components/MyLink";
import MyPagination from "../../components/MyPagination";
import { formatDate } from "../../utils/formatDate";


/**
 * Cette page permet d'afficher tous les signalements du résident. Il faut donc les chercher depuis le backend.
 * Lorsqu'on clique sur un signalement de la liste, on est redirigé vers la page de détail de ce signalement.
 * @returns ReactNode
 */
export default function ResidentSignalementsPage() {

    const residentId = useAppSelector((state) => state.auth.infos)?.id;

    const [signalements, setSignalements] = useState<Signalement[]>([]);
    const [paginatedSignalements, setPaginatedSignalements] = useState<Signalement[]>([]);


    // Requête au backend

    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState(false);
    const response = useRequest("/signalement/getByResident/" + (residentId?? ""), "GET");

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

    return (
        <div>
            <h1 className="mt-5 mb-3 text-center">Vos signalements</h1>
            <p className="mb-4 text-center">Cliquez sur un signalement pour en voir les détails. S'il n'a pas encore été vu par un agent de la STPM, vous pourrez alors le modifier.</p>
            {loading && <Loader />}
            {error && <Alert severity="error">Un problème est survenu. Veuillez réessayer plus tard.</Alert>}
            {!loading && <>
                {signalements.length === 0 && <p className="mb-4 text-center fw-bold">Aucun signalement.</p>}
                {signalements.length !== 0 && <><List>
                    {paginatedSignalements.map((signal, index) => {
                        return <MyLink className="text-black" to={"/resident/signalement/" + signal.id} key={index}>
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