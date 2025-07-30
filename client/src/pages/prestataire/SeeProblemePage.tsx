import { useEffect, useState, type ReactNode } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Alert } from '@mui/material';
import Loader from '../../components/Loader';
import useRequest from '../../hooks/UseRequest';
import type Problem from '../../interfaces/Problem';
import { formatDate } from '../../utils/formatDate';


/**
 * Cette page est la page d'affichage individuel des fiche problème. Il faut donc rechercher la fiche par son id
 * depuis le backend. Le prestataire à la possibilité de cliquer sur un bouton soumettre une candidature pour soumettre une candidature
 * par rapport au problème. Cela le redirige vers la page de création de la candidature.
 * @returns ReactNode
 */
export default function SeeProblemePage(): ReactNode {
    const { id: problemId } = useParams<{ id: string }>();

    const [probleme, setProbleme] = useState<Problem | null>(null);
    const [error, setError] = useState(false);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    const response = useRequest("/probleme/" + problemId, "GET");
    
    useEffect(() => {
        if(response) {
            if(response.status === 200) {
                setProbleme(response.data);
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
            <h1 className="mt-5 mb-3 text-center">Fiche problème</h1>
            {loading && <Loader />}
            {error && <Alert severity="error">Un problème est survenu. Veuillez réessayer plus tard.</Alert>}
            {!loading && probleme && 
                <>
                    <div className="mt-5 mb-3 d-flex flex-column align-items-center">
                        <p>Problème de type <b>{probleme.typeTravaux}</b> dans le quartier <b>{probleme.quartier}</b></p>
                        <p><b>Priorité</b> : {probleme.priorite}</p>
                        <p><b>Nombre de signalements</b> : {probleme.signalements.length}</p>
                        <p><b>Date de création</b> : {probleme.createdAt ? formatDate(probleme.createdAt) : ""}</p>
                        <br />
                        <p className="my-2 mx-4 mx-lg-5"><b>Description du problème</b> : {probleme.description}</p>
                    </div>
                    <div className='d-flex justify-content-center align-items-center m-2'>
                        <button
                            type="button"
                            className="rounded-4 border-0 text-white p-3 my-4 orange"
                            onClick={() => navigate(`/prestataire/candidature/new/${probleme.id}`)}
                        >Soumettre une candidature</button>
                    </div>
                </>
            }
        </div>
    );
}
