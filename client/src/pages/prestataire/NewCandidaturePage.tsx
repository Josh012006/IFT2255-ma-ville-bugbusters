import { useParams } from "react-router-dom";


/**
 * Cette page permet au prestataire de soumettre une nouvelle candidature pour une fiche problème qu'il a repéré.
 * Il faut donc utiliser un formulaire pour récupérer les informations nécessaires, les valider et les soumettre au backend.
 * Dans la soumission, il y aura un besoin de connaitre l'id de la fiche. C'est cette information qui est contenue dans ficheProbleme
 * récupéré dans le path.
 * @returns ReactNode
 */
export default function NewCandidaturePage() {
    const ficheProbleme = useParams().problemId;

    const [prestataire, setPrestataire] = useState('');
    const [nomPrestataire, setNomPrestataire] = useState('');
    const [numeroEntreprise, setNumeroEntreprise] = useState('');
    const [titreProjet, setTitreProjet] = useState('');
    const [description, setDescription] = useState('');
    const [typeTravaux, setTypeTravaux] = useState('');
    const [coutEstime, setCoutEstime] = useState('');
    const [dateDebut, setDateDebut] = useState('');
    const [dateFin, setDateFin] = useState('');
    const [ruesAffectees, setRuesAffectees] = useState('');
    const [erreur, setErreur] = useState('');

    return (
        <div>

        </div>
    );
}

