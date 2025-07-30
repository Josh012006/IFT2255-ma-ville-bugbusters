import { useEffect, useState } from "react";
import useRequest from "../../hooks/UseRequest";
import type  Projet  from "../../interfaces/Projet";
import { useAppSelector } from "../../redux/store"
import { List, ListItem, ListItemText, Alert, Divider } from "@mui/material";
import Loader from "../../components/Loader";
import Filter from "../../components/Filter";
import MyLink from "../../components/MyLink";
import MyPagination from "../../components/MyPagination";
import { formatDate } from "../../utils/formatDate";



/**
 * Cette page permet de visualiser les projets du prestataire. Il faut donc les récupérer depuis le backend. Il faut inclure un filtre si nécessaire.
 * Lorsqu'on clique sur un projet de la liste, on est redirigé vers la page de visualisation individuelle de ce projet.
 * @returns ReactNode
 */
export default function PrestataireProjetsPage() {

    const prestataireId = useAppSelector((state) => state.auth.infos)?.id?? "";
    
    const [projets, setProjets] = useState<Projet[]>([]);
    const [paginatedProjets, setPaginatedProjets] = useState<Projet[]>([]); 
    const [filteredProjets, setFilteredProjets] = useState<Projet[]>([]);
    
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(false);
    

    const response = useRequest(`/projet/getByPrestataire/${prestataireId}`, "GET");

    useEffect(() => {
        if(response) {
            if (response.status === 200) {
                setProjets(response.data.reverse());
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
            <h1 className="mt-5 mb-3 text-center">Vos projets</h1>
            <p className="mb-4 text-center">Cliquez sur un projet pour en revoir les détails et éventuellement mettre à jour les informations.</p>
            {loading && <Loader />}
            {error && <Alert severity="error">Un problème est survenu. Veuillez réessayer plus tard.</Alert>}
            {!loading && !error && 
                <>
                    {projets.length === 0 && <p className="mb-4 text-center fw-bold">Aucun projet trouvé.</p>}
                    {projets.length !== 0 && 
                        <>
                            <Filter tab={projets} setFilteredTab={setFilteredProjets} />
                            {filteredProjets.length === 0 && <p className="mb-4 text-center fw-bold">Aucun projet trouvé.</p>}
                            {filteredProjets.length !== 0 && <List>
                                {paginatedProjets.map((projet, index) => {
                                    return <MyLink className="text-black" to={"/prestataire/projet/" + projet.id} key={index}>
                                        <Divider component="li" />
                                        <ListItem className="d-flex align-items-center hover-white">
                                            <ListItemText
                                                primary={<>
                                                    <h5>{projet.titreProjet}</h5>
                                                    <p><b>Type de travaux</b> : {projet.typeTravaux}</p>
                                                    <p><b>Quartier</b> : {projet.quartier}</p>
                                                    <p><b>Statut</b> : {projet.statut?? ""}</p>
                                                    <p><b>Période de réalisation prévue</b> : {formatDate(projet.dateDebut)} - {formatDate(projet.dateFin)}</p>
                                                </>}
                                                secondary={<span className="ellipsis">{projet.description}</span>}
                                            />
                                        </ListItem>
                                    </MyLink>
                                })}
                            </List>}
                            <MyPagination itemsPerPage={15} data={filteredProjets} setPaginatedData={setPaginatedProjets}  />
                        </>
                    }
                </>
            }
        </div>
    );
}