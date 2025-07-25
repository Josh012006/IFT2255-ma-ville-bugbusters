import { useParams } from "react-router-dom";


/**
 * Cette pas est la page de traitement d'une candidature. Il faut récupérer les informations de la candidature 
 * à partir de son id et les afficher. Ensuite le STPM a le droit de l'accepter ou de la refuser. S'il la refuse, 
 * il faut lui demander une raison.
 * Une fois une décision prise, il faut faire un appel au backend pour marquer la décision.
 * @returns ReactNode
 */
export default function ManageCandidaturePage() {

    const candidatureId = useParams().id;

    return(
        <div>

        </div>
    );
}