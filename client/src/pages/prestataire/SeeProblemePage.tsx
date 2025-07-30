import React, { useEffect, useState, type JSX } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Box,
  Card,
  CardContent,
  Typography,
  Button,
  CircularProgress
} from '@mui/material';

interface Probleme {
  id: string;
  titre: string;
  quartier: string;
  typeTravail: string;
  priorite: string;
  description: string;
}

export default function SeeProblemePage(): JSX.Element {
  const { id } = useParams<{ id: string }>();
  const [probleme, setProbleme] = useState<Probleme | null>(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    if (!id) return;
    fetch(`/api/problemes/${id}`)
      .then(res => res.json())
      .then((data: Probleme) => setProbleme(data))
      .catch(err => console.error('Erreur de chargement du problème :', err))
      .finally(() => setLoading(false));
  }, [id]);

  if (loading) {
    return (
      <Box p={4} textAlign="center">
        <CircularProgress />
      </Box>
    );
  }

  if (!probleme) {
    return (
      <Box p={4}>
        <Typography>Problème introuvable.</Typography>
      </Box>
    );
  }

  return (
    <Box p={4}>
      <Card>
        <CardContent>
          <Typography variant="h4" gutterBottom>
            {probleme.titre}
          </Typography>
          <Typography paragraph>
            <strong>Quartier :</strong> {probleme.quartier}
          </Typography>
          <Typography paragraph>
            <strong>Type de travail :</strong> {probleme.typeTravail}
          </Typography>
          <Typography paragraph>
            <strong>Priorité :</strong> {probleme.priorite}
          </Typography>
          <Typography paragraph>
            <strong>Description :</strong><br/>
            {probleme.description}
          </Typography>
        </CardContent>
      </Card>

      <Box mt={3}>
        <Button
          variant="contained"
          onClick={() => navigate(`/prestataire/candidature/new/${probleme.id}`)}
        >
          Soumettre une candidature
        </Button>
      </Box>
    </Box>
  );
}
