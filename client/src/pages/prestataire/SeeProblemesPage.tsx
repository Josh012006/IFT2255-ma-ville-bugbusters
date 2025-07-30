import { useEffect, useState, type ReactNode } from 'react';
import {
  Alert,
  List,
  Divider,
  ListItemText,
  ListItem
} from '@mui/material';
import type Problem from '../../interfaces/Problem';
import useRequest from '../../hooks/UseRequest';
import Loader from '../../components/Loader';
import Filter from '../../components/Filter';
import MyPagination from '../../components/MyPagination';
import MyLink from '../../components/MyLink';
import { formatDate } from '../../utils/formatDate';

/**
 * Cette page permet d'afficher les fiches problèmes. Il faut donc les chercher depuis le backend.
 * Lorsqu'on clique sur une fiche problème, on a accès à la page de visualisation individuelle de la fiche problème.
 * @returns ReactNode
 */
export default function SeeProblemesPage(): ReactNode {

    const [problemes, setProblemes] = useState<Problem[]>([]);
    const [paginatedProblemes, setPaginatedProblemes] = useState<Problem[]>([]); 
    const [filteredProblemes, setFilteredProblemes] = useState<Problem[]>([]);

    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState(false);
    const response = useRequest("/probleme/getAll", "GET");

    useEffect(() => {
        if(response) {
            if (response.status === 200) {
                setProblemes(response.data.reverse());
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
        <h1 className="mt-5 mb-3 text-center">Fiches problèmes</h1>
        <p className="mb-4 text-center">Cliquez sur une fiche pour voir les détails du problème et éventuellement déposer une candidature.</p>
        {loading && <Loader />}
        {error && <Alert severity="error">Un problème est survenu. Veuillez réessayer plus tard.</Alert>}
        {/* Liste */}
        {!loading && !error &&
            <>
                {problemes.length === 0 && <p className="mb-4 text-center fw-bold">Aucune fiche problème trouvée.</p>}
                {problemes.length !== 0 && <>
                    {/* Filtre */}
                    <Filter tab={problemes} setFilteredTab={setFilteredProblemes} />
                    {filteredProblemes.length === 0 && <p className="mb-4 text-center fw-bold">Aucune fiche problème trouvée.</p>}
                    {filteredProblemes.length !== 0 && <List>
                        {paginatedProblemes.map((problem, index) => {
                            return <MyLink className="text-black" to={"/prestataire/probleme/" + problem.id} key={index}>
                                <Divider component="li" />
                                <ListItem className="d-flex align-items-center hover-white">
                                    <ListItemText
                                        primary={<>
                                            <p>Problème de type <b>{problem.typeTravaux}</b> dans le quartier <b>{problem.quartier}</b></p>
                                            <p><b>Priorité</b> : {problem.priorite}</p>
                                            <p><b>Date de création</b> : {problem.createdAt ? formatDate(problem.createdAt) : ""}</p>
                                        </>}
                                        secondary={<span className="ellipsis">{problem.description}</span>}
                                    />
                                </ListItem>
                            </MyLink>
                        })}
                    </List>}
                    <MyPagination itemsPerPage={15} data={filteredProblemes} setPaginatedData={setPaginatedProblemes}  />
                </>}
            </>
        }
    </div>
  );
}
