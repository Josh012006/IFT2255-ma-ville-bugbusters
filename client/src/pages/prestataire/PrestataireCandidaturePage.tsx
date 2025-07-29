import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {Button,Card,CardContent,Typography,Modal,TextField,Box,CardActions,} from "@mui/material";
import useRequest from "../hooks/useRequest";
import type { Candidature } from "../types/Candidature";


/**
 * Cette page permet d'afficher une candidature d'un prestataire avec ses détails.
 * Il faut donc récupérer les informations depuis le backend avec l'id.
 * Le prestataire a aussi la possibilité de modifier ou de supprimer la candidature tant que son
 * statut n'est pas marquée comme 'vue' par le STPM.
 * @returns ReactNode
 */ 

const modalStyle = {
    position: "absolute" as const,
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    width: 500,
    bgcolor: "background.paper",
    p: 4,
    borderRadius: 2,
};

export default function PrestataireCandidaturePage() {
    const candidatureId = useParams().id;
    const [candidatures, setCandidatures] = useState<Candidature[]>([]);
    const [expandedId, setExpandedId] = useState<string | null>(null);
    const [showModal, setShowModal] = useState(false);
    const [formData, setFormData] = useState({
        titreProjet: "",
        description: "",
        dateDebut: "",
        dateFin: "",
    });
    const [selectedCandidatureId, setSelectedCandidatureId] = useState<string | null>(null);  

    useEffect(() => {
    async function fetchCandidatures() {
        const res = await useRequest("/candidatures/prestataire", "GET");
        if (res.status === 200) {
            setCandidatures(res.data);
        }
    }

    fetchCandidatures();
  }, []);

    //Ouvrir ou refermer une card
    const handleExpand = (id: string) => {
        setExpandedId(expandedId === id ? null : id);
    }; 

    return (
        <div>

        </div>
    );
}