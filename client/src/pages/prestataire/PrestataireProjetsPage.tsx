import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useRequest } from "../../hooks/UseRequest";
import type  Projet  from "../../interfaces/Projet";
import { useAppSelector } from "../../redux/store"
import {
    Container,
    Typography,
    TextField,
    List,
    ListItem,
    ListItemText,
    Paper,
    CircularProgress,
  } from "@mui/material";



/**
 * Cette page permet de visualiser les projets du prestataire. Il faut donc les récupérer depuis le backend. Il faut inclure un filtre si nécessaire.
 * Lorsqu'on clique sur un projet de la liste, on est redirigé vers la page de visualisation individuelle de ce projet.
 * @returns ReactNode
 */
export default function PrestataireProjetsPage() {
    const navigate = useNavigate();
  const userId = useAppSelector((state: any) => state.auth.userId);
  const [filtre, setFiltre] = useState("");

  const result = useRequest(`/projets/prestataire/${userId}`, "GET");
  const projets: Projet[] = result?.data || [];

  const projetsFiltres = projets.filter((p) =>
    p.titreProjet.toLowerCase().includes(filtre.toLowerCase())
  );

  if (!result) {
    return (
      <Container sx={{ mt: 4 }}>
        <CircularProgress />
      </Container>
    );
  }

  return (
    <Container sx={{ mt: 4 }}>
      <Typography variant="h4" gutterBottom>
        Mes projets
      </Typography>

      <TextField
        label="Filtrer par titre"
        variant="outlined"
        fullWidth
        value={filtre}
        onChange={(e) => setFiltre(e.target.value)}
        sx={{ mb: 3 }}
      />

      {projetsFiltres.length === 0 ? (
        <Typography>Aucun projet trouvé.</Typography>
      ) : (
        <List>
          {projetsFiltres.map((p) => (
            <Paper
              key={p.id}
              elevation={3}
              sx={{ mb: 2, cursor: "pointer" }}
              onClick={() => navigate(`/prestataire/projet/${p.id}`)}
            >
              <ListItem>
                <ListItemText
                  primary={p.titreProjet}
                  secondary={
                    <>
                      <Typography variant="body2">{p.description}</Typography>
                      <Typography variant="caption" color="text.secondary">
                        Statut : {p.statut}
                      </Typography>
                    </>
                  }
                />
              </ListItem>
            </Paper>
          ))}
        </List>
      )}
    </Container>
  );
}