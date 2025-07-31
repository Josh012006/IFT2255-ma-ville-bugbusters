import { useEffect, useState } from "react";
import type Projet from "../../interfaces/Projet";
import useRequest from "../../hooks/UseRequest";
import Loader from "../../components/Loader";
import { Alert, Divider, List, ListItem, ListItemText } from "@mui/material";
import MyLink from "../../components/MyLink";
import MyPagination from "../../components/MyPagination";
import { formatDate } from "../../utils/formatDate";
import Filter from "../../components/Filter";


/**
 * Cette page permet au résident de visualiser tous les projets de la base de données. Il a la possibilité de choisir soit entre
 * voir ceux à venir (dans les 3 prochains mois) ou ceux en cours.
 * Donc il est nécessaire de récupérer les projets depuis le backend et d'afficher ceux qu'il demande.
 * Il ne faut pas oublier d'inclure un filtre.
 * Lorsqu'on clique sur un projet, on a accès à la page individuelle de visualisationd u projet.
 * @returns ReactNode
 */
export default function SeeProjetsPage() {
    
    const [fetchedProjets, setFetchedProjets] = useState<Projet[]>([]);
    const [projets, setProjets] = useState<Projet[]>([]);
    const [paginatedProjets, setPaginatedProjets] = useState<Projet[]>([]); 
    const [filteredProjets, setFilteredProjets] = useState<Projet[]>([]);

    const [toShow, setToShow] = useState<"present" | "coming">("present");

    
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(false);

    const response = useRequest("/projet/getAll/", "GET");

    useEffect(() => {
        if(response) {
            if (response.status === 200) {
                setFetchedProjets(response.data.sort((a: Projet, b: Projet) => {
                    const bTime = b.updatedAt ? new Date(b.updatedAt).getTime() : new Date().getTime();
                    const aTime = a.updatedAt ? new Date(a.updatedAt).getTime() : new Date().getTime();
                    return bTime - aTime;
                }));
                setLoading(false);
            } else {
                console.log(response.data);
                setLoading(false);
                setError(true);
            }
        }
    }, [response]);

    useEffect(() => {
        if(fetchedProjets) {
            let projetsToShow : Projet[] = [];


            // Date actuelle
            const maintenant = new Date();
            // Date il y a 2 mois
            const deuxMoisAvant = new Date();
            deuxMoisAvant.setMonth(deuxMoisAvant.getMonth() - 2);
            // Date dans 4 mois
            const quatreMoisApres = new Date();
            quatreMoisApres.setMonth(quatreMoisApres.getMonth() + 4);


            if(toShow === "present") {
                projetsToShow = fetchedProjets.filter((projet) => new Date(projet.dateFin) > deuxMoisAvant && new Date(projet.dateDebut) <= maintenant);
            } else {
                projetsToShow = fetchedProjets.filter((projet) => new Date(projet.dateDebut) > maintenant && new Date(projet.dateDebut) < quatreMoisApres);
            }

            setProjets(projetsToShow);
        }
    }, [fetchedProjets, toShow]);


    return (
        <div>
            <div className="d-flex justify-content-center align-items-center w-100 my-3">
                <span onClick={() => {setToShow("present")}} className={`${(toShow === "present")? "bg-secondary text-white" : "bg-white text-black"} p-2 pointer rounded-start-2 border-end`}>En cours</span>
                <span onClick={() => {setToShow("coming")}} className={`${(toShow === "coming")? "bg-secondary text-white" : "bg-white text-black"} p-2 pointer rounded-end-2`}>A venir</span>
            </div>
            <h1 className="mt-5 mb-3 text-center">{(toShow === "coming"? "Projets à venir" : "Projets en cours")}</h1>
            <p className="mb-4 text-center">Cliquez sur un projet pour en voir les rues affectées, la priorité et autres détails.</p>
            {loading && <Loader />}
            {error && <Alert severity="error">Un problème est survenu. Veuillez réessayer plus tard.</Alert>}
            {!loading && !error && <>
                {projets.length === 0 && <p className="mb-4 text-center fw-bold">Aucun projet trouvé.</p>}
                {projets.length !== 0 && <>
                    <Filter tab={projets} setFilteredTab={setFilteredProjets} />
                    {filteredProjets.length === 0 && <p className="mb-4 text-center fw-bold">Aucun projet trouvé.</p>}
                    {filteredProjets.length !== 0 && <List>
                        {paginatedProjets.map((projet, index) => {
                            return <MyLink className="text-black" to={"/resident/projet/" + projet.id} key={index}>
                                <Divider component="li" />
                                <ListItem className="d-flex align-items-center hover-white">
                                    <ListItemText
                                        primary={<>
                                        <h5>{projet.titreProjet}</h5>
                                        <p>Organisé par <b>{projet.nomPrestataire}</b></p>
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
                </>}
            </>}
        </div>
    );
}