import { useParams } from "react-router-dom";



/**
 * Cette page permet de visualiser une projet d'un prestataire. 
 * Il faut donc chercher le projet depuis le backend en utilisant l'id.
 * Le prestataire peut aussi modifier certaines informations du projets, notamment les dates 
 * de fin et de début, la description ou le statut.
 * @returns ReactNode
 */
export default function PrestataireProjetPage() {
    const projetId = useParams().id;

    return (
        <div>

        </div>
    );
}