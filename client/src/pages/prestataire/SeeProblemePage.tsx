import { useParams } from "react-router-dom";


/**
 * Cette page est la page d'affichage individuel des fiche problème. Il faut donc rechercher la fiche par son id
 * depuis le backend. Le prestataire à la possibilité de cliquer sur un bouton soumettre une candidature pour soumettre une candidature
 * par rapport au problème. Cela le reidrige vers la page de création de la candidature.
 * @returns ReactNode
 */
export default function SeeProblemesPage() {
    const problemeId = useParams().id;

    return (
        <div>

        </div>
    );
}