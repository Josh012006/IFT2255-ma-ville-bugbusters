import { useEffect, useState } from "react";
import type Problem from "../interfaces/Problem";
import type Projet from "../interfaces/Projet";
import Select, { type SelectChangeEvent } from "@mui/material/Select";
import MenuItem from "@mui/material/MenuItem";
import InputLabel from "@mui/material/InputLabel";
import { QUARTIERS } from "../types/Quartier";
import { TYPE_TRAVAUX } from "../types/TypesTravaux";



/**
 * Cette composante permet d'implémenter un filtrage des problèmes (par quartier, par type de travail et par priorité) et un filtrage
 * des projets (par quartier et par type travail). Elle affiche donc des éléments select pour que l'utilisateur puisse 
 * choisir l'option qu'il veut.
 * @param list qui représente la liste de projets ou de problèmes initiale à filtrer. Doit être un state pour permettre la réactivité.
 * @param setTab qui est le setter du state de la liste triée. Cela veux dire qu'on doit avoir un state séparé pour le tableau initial et celui filtré
 * @returns ReactNode
 */
export default function Filter({list, setFilteredTab} : {list: Projet[] | Problem[], setFilteredTab: React.Dispatch<React.SetStateAction<Projet[] | Problem[]>>}) {

    const [quartier, setQuartier] = useState("All");
    const [typeTravail, setTypeTravail] = useState("All");
    const [priorite, setPriorite] = useState("All");

    const [filtered, setFiltered] = useState(list);

    const isProblem = "priorite" in list[0];


    // Handlers
    const handleChangeQuartier = (event: SelectChangeEvent) => {
        setQuartier(event.target.value);
    };

    const handleChangeType = (event: SelectChangeEvent) => {
        setTypeTravail(event.target.value);
    };

    const handleChangePriorite = (event: SelectChangeEvent) => {
        setPriorite(event.target.value);
    };



    // Filtrage par quartier
    useEffect(() => {
        let filteredTab: Projet[] | Problem[];
        if(isProblem) {
            filteredTab = (quartier === "All") ? filtered as Problem[] : (filtered as Problem[]).filter((elem) => elem.quartier === quartier);
        } else {
            filteredTab = (quartier === "All") ? filtered as Projet[] : (filtered as Projet[]).filter((elem) => elem.quartier === quartier); 
        }
        setFiltered(filteredTab);
    }, [isProblem, filtered, quartier, setFilteredTab]);

    // Gestion du filtrage par types travaux
    useEffect(() => {
        let filteredTab: Projet[] | Problem[];
        if(isProblem) {
            filteredTab = (typeTravail === "All") ? filtered as Problem[] : (filtered as Problem[]).filter((elem) => elem.typeTravaux === typeTravail);
        } else {
            filteredTab = (typeTravail === "All") ? filtered as Projet[] : (filtered as Projet[]).filter((elem) => elem.typeTravaux === typeTravail); 
        }
        setFiltered(filteredTab);
    }, [isProblem, filtered, setFilteredTab, typeTravail]);

    // Gestion du filtrage par priorité seulement si ce sont des fiches problèmes
    useEffect(() => {
        if(isProblem) {
            const filteredTab = (priorite === "All") ? filtered as Problem[] : (filtered as Problem[]).filter((elem) => elem.priorite === priorite);
            setFiltered(filteredTab)
        }
    }, [isProblem, filtered, priorite, setFilteredTab]);


    // Update l'utilisateur
    useEffect(() => {
        setFilteredTab(filtered);
    }, [filtered, setFilteredTab]);


    return (
        <div className="d-flex flex-column flex-lg-row justify-content-around align-items-center">
            <div>
                <InputLabel>Quartier</InputLabel>
                <Select
                    value={quartier}
                    onChange={handleChangeQuartier}
                    displayEmpty
                    inputProps={{ 'aria-label': 'Without label' }}
                    >
                    <MenuItem value="All">All</MenuItem>
                    {QUARTIERS.map((quartier, index) => {
                        return <MenuItem key={index} value={quartier}>{quartier}</MenuItem>
                    })}
                </Select>
            </div>
            <div>
                <InputLabel>Type de travail</InputLabel>
                <Select
                    value={typeTravail}
                    onChange={handleChangeType}
                    displayEmpty
                    inputProps={{ 'aria-label': 'Without label' }}
                    >
                    <MenuItem value="All">All</MenuItem>
                    {TYPE_TRAVAUX.map((type, index) => {
                        return <MenuItem key={index} value={type}>{type}</MenuItem>
                    })}
                </Select>
            </div>
            {isProblem && <div><InputLabel>Priorite</InputLabel>
            <Select
                value={priorite}
                onChange={handleChangePriorite}
                displayEmpty
                inputProps={{ 'aria-label': 'Without label' }}
                >
                <MenuItem value="All">All</MenuItem>
                <MenuItem value="faible">Faible</MenuItem>
                <MenuItem value="moyenne">Moyenne</MenuItem>
                <MenuItem value="élevée">Elevée</MenuItem>
            </Select></div>}
        </div>
    );
}

