import { useParams } from "react-router-dom";



/**
 * Cette page permet d'afficher les détails d'un signalement du résident. Il faut donc récupérer les informations depuis le backend.
 * Le résident à la possibilité de modifier ou de supprimer un signalement tantq u'il n'est pas encore marqué comme
 * statut 'vu' par le STPM.
 * @returns ReactNode
 */
export default function ResidentSignalementPage() {

    // Ne pas oublier  + "?stpm=false"
    const signalementId = useParams().id;

    return (
        <div>

        </div>
    );
}