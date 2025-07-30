import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";

import type  Projet  from "../../interfaces/Projet";
import axios  from "axios";


import {
    Typography,
    List,
    ListItem,
    ListItemText,
    Button,
    Container,
    CircularProgress,
  } from "@mui/material";




/**
 * Cette page permet de visualiser un projet d'un prestataire. 
 * Il faut donc chercher le projet depuis le backend en utilisant l'id.
 * Le prestataire peut aussi modifier certaines informations du projets, notamment les dates 
 * de fin et de début, la description ou le statut.
 * @returns ReactNode
 */
export default function PrestataireProjetPage() {
   //const projetId = useParams().id;
   const { id: projetId } = useParams();
  const [projet, setProjet] = useState<Projet | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    if (!projetId) return;

    axios
      .get<Projet>(`http://localhost:7070/api/projets/${projetId}`)
      .then((res) => setProjet(res.data))
      .catch((err) =>
        console.error("Erreur lors du chargement du projet :", err)
      );
  }, [projetId]);


  if (!projet) {
    return (
      <Container sx={{ mt: 4 }}>
        <CircularProgress />
      </Container>
    );
  }

  return (
    <Container sx={{ mt: 4 }}>
      <Typography variant="h4" gutterBottom>
        {projet.titreProjet}
      </Typography>

      <Typography variant="body1" gutterBottom>
        {projet.description}
      </Typography>

      <List>
        <ListItem>
          <ListItemText primary="Statut" secondary={projet.statut} />
        </ListItem>
        <ListItem>
          <ListItemText
            primary="Date début"
            secondary={new Date(projet.dateDebut).toLocaleDateString()}
          />
        </ListItem>
        <ListItem>
          <ListItemText
            primary="Date fin"
            secondary={new Date(projet.dateFin).toLocaleDateString()}
          />
        </ListItem>
        <ListItem>
          <ListItemText primary="Coût" secondary={`${projet.cout} $`} />
        </ListItem>
        <ListItem>
          <ListItemText primary="Quartier" secondary={projet.quartier} />
        </ListItem>
        <ListItem>
          <ListItemText
            primary="Rues affectées"
            secondary={projet.ruesAffectees.join(", ")}
          />
        </ListItem>
      </List>

      <Button
        variant="contained"
        color="primary"
        sx={{ mt: 2 }}
        onClick={() => navigate("/prestataire/projets")}
      >
        Retour à la liste
      </Button>
    </Container>
  );
}