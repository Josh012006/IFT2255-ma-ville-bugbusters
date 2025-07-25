import { useParams } from "react-router-dom";


/**
 * Cette page est al page de traitement du signalement par le STPM. Elle inclut la récupération individuelle
 * de la notification grâce à signalementId. Après récupération des informations, il faut les afficher.
 * Le STPM a deux possibilités. La première est de choisir une priorité et confirmer. Une fois cela fait, 
 * il faut envoyer une requête pour créer une fiche problème.
 * La deuxième possibilité est de reconnaitre que ce signalement est déjà en lien avec un problème créé et lui lier le signalement.
 * Pour cette deuxième option, il faudra qu'on filtre les problèmes existants et faire un filtrage par description,
 * par quartier et par ville pour trouver des similitudes. Ensuite les proposer au STPM pour qu'il associe un au signelement.
 * Une requête dera ensuite envoyée pour marquer l'opération.
 * @returns ReactNode
 */
export default function ManageSignalementPage() {

    const signalementId = useParams().id;

    return(
        <div>

        </div>
    );

}