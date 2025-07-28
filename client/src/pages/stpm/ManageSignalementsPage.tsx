import { useEffect, useState } from "react";
import type Signalement from "../../interfaces/Signalement";
import useRequest from "../../hooks/UseRequest";
import Loader from "../../components/Loader";
import MyPagination from "../../components/MyPagination";
import { Divider, List, ListItem, ListItemText } from "@mui/material";
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
    const response = useRequest("/signalement/getAll/", "GET");

    useEffect(() => {
        if (response && response.status === 200) {
            setSignalements(response.data);
        }
        setLoading(false);
    }, [response]);


    return(
        <div>
            <h1 className="mt-5 mb-3 text-center">Nouveaux signalements</h1>
            <p className="mb-4 text-center">Cliquez sur un signalement pour en voir les détails. Vous pourrez ensuite, soit le lier à une fiche problème existante ou encore en créer une nouvelle en lui affecter une priorité</p>
            {loading && <Loader />}
            {!loading && <><List>
                {paginatedSignalements.map((signal, index) => {
                    return <MyLink className="text-black" to={"/stpm/signalement/" + signal.id} key={index}>
                        <Divider component="li" />
                        <ListItem className="d-flex align-items-center hover-white">
                            <ListItemText
                                primary={<p>Problème de type <b>{signal.typeProbleme}</b> dans le quartier <b>{signal.quartier}</b> - {(signal.updatedAt) ? formatDate(signal.updatedAt) : ""}</p>}
                                secondary={<span className="ellipsis">{signal.description}</span>}
                            />
                        </ListItem>
                    </MyLink>
                })}
            </List>
            <MyPagination itemsPerPage={15} data={signalements} setPaginatedData={setPaginatedSignalements}  />
            </>}
        </div>
    );
}