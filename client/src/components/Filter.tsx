import { useEffect, useState } from "react";
import type Problem from "../interfaces/Problem";
import type Projet from "../interfaces/Projet";
import Select, { type SelectChangeEvent } from "@mui/material/Select";
import MenuItem from "@mui/material/MenuItem";
import InputLabel from "@mui/material/InputLabel";
import { QUARTIERS } from "../types/Quartier";
import { TYPE_TRAVAUX } from "../types/TypesTravaux";


interface FilterProps {
    /**qui représente la liste de projets ou de problèmes initiale à filtrer. Doit être un state pour permettre la réactivité.*/
    tab: Projet[] | Problem[], 
    /**qui est le setter du state de la liste triée. Cela veux dire qu'on doit avoir un state séparé pour le tableau initial et celui filtré */  
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    setFilteredTab: React.Dispatch<React.SetStateAction<any[]>>
}

/**
 * Cette composante permet d'implémenter un filtrage des problèmes (par quartier, par type de travail et par priorité) et un filtrage
 * des projets (par quartier et par type travail). Elle affiche donc des éléments select pour que l'utilisateur puisse 
 * choisir l'option qu'il veut.
 * @returns ReactNode
 */
export default function Filter(props : FilterProps) {

    const { tab, setFilteredTab } = props;

    const [quartier, setQuartier] = useState("All");
    const [typeTravail, setTypeTravail] = useState("All");
    const [priorite, setPriorite] = useState("All");

    const [filtered, setFiltered] = useState(tab);


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
        let filteredTab = [...tab];

        if (quartier !== "All") {
            filteredTab = filteredTab.filter((elem) => elem.quartier === quartier);
        }

        if (typeTravail !== "All") {
            filteredTab = filteredTab.filter((elem) => elem.typeTravaux === typeTravail);
        }

        if (priorite !== "All") {
            filteredTab = filteredTab.filter((elem) => elem.priorite === priorite);
        }

        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        setFiltered(filteredTab as any[]);
    }, [tab, quartier, typeTravail, priorite, setFiltered]);



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

