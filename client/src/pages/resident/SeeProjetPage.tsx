import { useParams } from "react-router-dom";
import useRequest from "../../hooks/UseRequest";
import type Projet from "../../interfaces/Projet";
import { useEffect, useState } from "react";
import Loader from "../../components/Loader";
import { formatDate } from "../../utils/formatDate";


/**
 * Cette page permet d'afficher un projet spécifique en fonction de l'id. Il faut donc le récupérer de la base de données
 * en utilisant l'id précisé et afficher ses informations.
 * @returns ReactNode
 */
export default function SeeProjetPage() {
    const projetId = useParams().id;

    const [projet, setProjet] = useState<Projet | null>(null);
    const [loading, setLoading] = useState(true);

    const response = useRequest("/projet/" + projetId, "GET");

    useEffect(() => {
        if(response && response.status === 200) {
            setProjet(response.data);
            setLoading(false);
        }
    }, [response]);



    return (
        <div>
            <h1 className="mt-5 mb-3 text-center">Projet</h1>
            {loading && <Loader />}
            {!loading && projet && <div className="mt-5 mb-3 d-flex flex-column align-items-center">
                <h5 className="fw-bold my-3">Projet proposé par {projet.nomPrestataire}</h5>
                <p><b>Titre du projet</b> : {projet.titreProjet}</p>
                <p><b>Type de travaux requis</b> : {projet.typeTravaux}</p>
                <p><b>Quartier</b> : {projet.quartier}</p>
                <p><b>Période de réalisation prévue</b> : {formatDate(projet.dateDebut, true)} - {formatDate(projet.dateFin, true)}</p>
                <p><b>Rues affectées</b> : {projet.ruesAffectees.join(", ")}</p>
                <br />
                <p><b>Priorité</b> : {projet.priorite}</p>
                <p><b>Coût estimé</b> : {projet.cout} $</p>
                <p><b>Statut</b> : {projet.statut}</p>
                <br/>
                <p className="my-2 mx-4 mx-lg-5"><b>Description du projet</b> : {projet.description}</p>
                <br/>
                <br/>
                <p className="my-2 mx-5 text-center"> <b>NB</b> : 
                    Si vous souhaitez <b>recevoir des notifications</b> par rapport à ce projet, assurez-vous que le <b>quartier</b> ou l'une des <b>rues 
                    affectées</b> est marquée comme une de vos <b>préférences de notifications</b>. Vous pouvez modifier ces préférences en vous dirigeant vers votre <b>profil</b>.
                </p>
            </div>}
        </div>
    );
}