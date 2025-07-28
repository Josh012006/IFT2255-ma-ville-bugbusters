import { useParams } from "react-router-dom";
import useRequest from "../../hooks/UseRequest";
import { useEffect, useState } from "react";
import type Signalement from "../../interfaces/Signalement";
import Loader from "../../components/Loader";
import Problem from "../../interfaces/Problem";
import { isSimilar } from "../../utils/isSimilar";


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

    const [signalement, setSignalement] = useState<Signalement | null>(null);
    const [loading, setLoading] = useState<boolean>(true);
    const [loading1, setLoading1] = useState<boolean>(true);

    const [similarProblems, setSimilarProblems] = useState<Problem[]>([]);

    const response = useRequest("/signalement/" + signalementId, "GET");
    const response1 = useRequest("/probleme/getSimilar?quartier=" + signalement?.quartier.replace(" ", "+") + "&type=" + signalement?.typeProbleme.replace(" ", "+"), "GET");


    useEffect(() => {
        if(response && response.status === 200) {
            setSignalement(response.data);
        }
        setLoading(false);
    }, [response]);

    useEffect(() => {
        if(response1 && response1.status === 200) {
            if(signalement) {
                const similar : Problem[] = [];
                for(const problem of response1.data) {
                    const myProblem = problem as Problem;
                    if(isSimilar(signalement.description, myProblem.description)) {
                        similar.push(myProblem);
                    }
                }

                setSimilarProblems(similar);
            }
        }
        setLoading1(false);
    }, [response1, signalement])



    return(
        <div>
            <h1 className="mt-5 mb-3 text-center">Nouveaux signalements</h1>
            {(loading || loading1) && <Loader />}
            {!loading && !loading1 && signalement && <div>
                
            </div>}
        </div>
    );

}