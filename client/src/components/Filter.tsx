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
 * @param tab qui représente la liste de projets ou de problèmes initiale à filtrer. Doit être un state pour permettre la réactivité.
 * @param setTab qui est le setter du state de la liste triée. Cela veux dire qu'on doit avoir un state séparé pour le tableau initial et celui filtré
 * @returns ReactNode
 */
// eslint-disable-next-line @typescript-eslint/no-explicit-any
export default function Filter({tab, setFilteredTab} : {tab: Projet[] | Problem[], setFilteredTab: React.Dispatch<React.SetStateAction<any[]>>}) {

    const [quartier, setQuartier] = useState("All");
    const [typeTravail, setTypeTravail] = useState("All");
    const [priorite, setPriorite] = useState("All");

    const [filtered, setFiltered] = useState(tab);

    const isProblem : boolean = !("nbRapports" in tab[0]);


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
        let filteredTab: Projet[] | Problem[] = [];
        
        if(isProblem) {
            filteredTab = (quartier === "All") ? tab as Problem[] : (tab as Problem[]).filter((elem) => elem.quartier === quartier);
        } else {
            filteredTab = (quartier === "All") ? tab as Projet[] : (tab as Projet[]).filter((elem) => elem.quartier === quartier); 
        }

        if(isProblem) {
            filteredTab = (typeTravail === "All") ? filteredTab as Problem[] : (filteredTab as Problem[]).filter((elem) => elem.typeTravaux === typeTravail);
        } else {
            filteredTab = (typeTravail === "All") ? filteredTab as Projet[] : (filteredTab as Projet[]).filter((elem) => elem.typeTravaux === typeTravail); 
        }

        if(isProblem) {
            filteredTab = (priorite === "All") ? filteredTab as Problem[] : (filteredTab as Problem[]).filter((elem) => elem.priorite === priorite);
        } else {
            filteredTab = (priorite === "All") ? filteredTab as Projet[] : (filteredTab as Projet[]).filter((elem) => elem.priorite === priorite);
        }
        
        setFiltered(filteredTab);

    }, [isProblem, filtered, priorite, setFilteredTab, quartier, tab, typeTravail]);


    // Update l'utilisateur
    useEffect(() => {
        setFilteredTab(filtered);
    }, [filtered, setFilteredTab]);


    return (
        <div className="d-flex flex-column flex-lg-row justify-content-around align-items-center my-3">
            <div className="d-flex flex-column justify-content-center align-items-center">
                <InputLabel>Quartier</InputLabel>
                <Select
                    value={quartier}
                    onChange={handleChangeQuartier}
                    displayEmpty
                    inputProps={{ 'aria-label': 'Without label' }}
                    sx={{minWidth: "300px", margin: "10px"}}
                    >
                    <MenuItem value="All">All</MenuItem>
                    {QUARTIERS.map((quartier, index) => {
                        return <MenuItem key={index} value={quartier}>{quartier}</MenuItem>
                    })}
                </Select>
            </div>
            <div className="d-flex flex-column justify-content-center align-items-center">
                <InputLabel>Type de travail</InputLabel>
                <Select
                    value={typeTravail}
                    onChange={handleChangeType}
                    displayEmpty
                    inputProps={{ 'aria-label': 'Without label' }}
                    sx={{minWidth: "300px", margin: "10px"}}
                    >
                    <MenuItem value="All">All</MenuItem>
                    {TYPE_TRAVAUX.map((type, index) => {
                        return <MenuItem key={index} value={type}>{type}</MenuItem>
                    })}
                </Select>
            </div>
            <div className="d-flex flex-column justify-content-center align-items-center"><InputLabel>Priorité</InputLabel>
                <Select
                    value={priorite}
                    onChange={handleChangePriorite}
                    displayEmpty
                    inputProps={{ 'aria-label': 'Without label' }}
                    sx={{minWidth: "300px", margin: "10px"}}
                    >
                    <MenuItem value="All">All</MenuItem>
                    <MenuItem value="faible">Faible</MenuItem>
                    <MenuItem value="moyenne">Moyenne</MenuItem>
                    <MenuItem value="élevée">Elevée</MenuItem>
                </Select>
            </div>
        </div>
    );
}

