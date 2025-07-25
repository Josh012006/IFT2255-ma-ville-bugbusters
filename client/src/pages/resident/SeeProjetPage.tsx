import { useParams } from "react-router-dom";


/**
 * Cette page permet d'afficher un projet spécifique en fonction de l'id. Il faut donc le récupérer de la base de données
 * en utilisant l'id précisé et afficher ses informations.
 * @returns ReactNode
 */
export default function SeeProjetPage() {
    const projetId = useParams().id;

    return (
        <div>

        </div>
    );
}