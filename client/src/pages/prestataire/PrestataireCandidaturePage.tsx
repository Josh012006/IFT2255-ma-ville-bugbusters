import { useParams } from "react-router-dom";



/**
 * Cette page permet d'afficher une candidature d'un prestataire avec ses détails.
 * Il faut donc récupérer les informations depuis le backend avec l'id.
 * Le prestataire a aussi la possibilité de modifier ou de supprimer la candidature tant que son
 * statut n'est pas marquée comme 'vue' par le STPM.
 * @returns ReactNode
 */
export default function PrestataireCandidaturePage() {
    const candidatureId = useParams().id;

    return (
        <div>

        </div>
    );
}