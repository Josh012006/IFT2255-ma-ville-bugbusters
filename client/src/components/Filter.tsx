import type Problem from "../interfaces/Problem";
import type Projet from "../interfaces/Projet";



/**
 * Cette composante permet d'impélmenter un filtrage des problèmes (par quartier, par type de travail et par priorité) et un filtrage
 * des projets (par quartier et par type travail). Elle affiche donc des éléments select pourq ue l'utilisateur puisse 
 * choisir l'option qu'il veut.
 * @param list qui représente la liste de projets ou de problèmes à filtrer. Doit être un state pour permettre la réactivité
 * @param setTab qui est le setter du state de la liste passée
 * @returns ReactNode
 */
export default function Filter({list, setTab} : {list: Projet[] | Problem[], setTab: React.Dispatch<React.SetStateAction<Projet[] | Problem[]>>}) {
    return (
        <div>

        </div>
    );
}

