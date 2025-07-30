import React, { useEffect, useState, type JSX } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Box,
  Card,
  CardContent,
  Typography,
  Select,
  MenuItem,
  InputLabel,
  FormControl,
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

export default function SeeProblemesPage(): JSX.Element {
  const [problemes, setProblemes] = useState<Probleme[]>([]);
  const [loading, setLoading] = useState(true);
  const [quartier, setQuartier] = useState('');
  const [typeTravail, setTypeTravail] = useState('');
  const [priorite, setPriorite] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    fetch('/api/problemes/non-traites')
      .then(res => res.json())
      .then((data: Probleme[]) => setProblemes(data))
      .catch(err => console.error('Erreur de chargement :', err))
      .finally(() => setLoading(false));
  }, []);

  const filtres = problemes.filter(p =>
    (quartier === '' || p.quartier === quartier) &&
    (typeTravail === '' || p.typeTravail === typeTravail) &&
    (priorite === '' || p.priorite === priorite)
  );

  if (loading) {
    return (
      <Box p={4} textAlign="center">
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box p={4}>
      <Typography variant="h4" gutterBottom>
        Liste des problèmes non traités
      </Typography>

      {/* Filtres */}
      <Box display="flex" flexDirection={{ xs: 'column', sm: 'row' }} gap={2} mb={4}>
        <FormControl fullWidth>
          <InputLabel id="label-quartier">Quartier</InputLabel>
          <Select
            labelId="label-quartier"
            value={quartier}
            label="Quartier"
            onChange={e => setQuartier(e.target.value)}
          >
            <MenuItem value="">Tous</MenuItem>
            <MenuItem value="Hochelaga">Hochelaga</MenuItem>
            <MenuItem value="Outremont">Outremont</MenuItem>
            <MenuItem value="Plateau">Plateau</MenuItem>
          </Select>
        </FormControl>

        <FormControl fullWidth>
          <InputLabel id="label-typeTravail">Type de travail</InputLabel>
          <Select
            labelId="label-typeTravail"
            value={typeTravail}
            label="Type de travail"
            onChange={e => setTypeTravail(e.target.value)}
          >
            <MenuItem value="">Tous</MenuItem>
            <MenuItem value="Voirie">Voirie</MenuItem>
            <MenuItem value="Éclairage">Éclairage</MenuItem>
            <MenuItem value="Égouts">Égouts</MenuItem>
          </Select>
        </FormControl>

        <FormControl fullWidth>
          <InputLabel id="label-priorite">Priorité</InputLabel>
          <Select
            labelId="label-priorite"
            value={priorite}
            label="Priorité"
            onChange={e => setPriorite(e.target.value)}
          >
            <MenuItem value="">Toutes</MenuItem>
            <MenuItem value="Faible">Faible</MenuItem>
            <MenuItem value="Moyenne">Moyenne</MenuItem>
            <MenuItem value="Haute">Haute</MenuItem>
          </Select>
        </FormControl>
      </Box>

      {/* Liste */}
      <Box display="flex" flexWrap="wrap" gap={2}>
        {filtres.length === 0 && (
          <Typography>Aucun problème trouvé.</Typography>
        )}
        {filtres.map(p => (
          <Card
            key={p.id}
            onClick={() => navigate(`/prestataire/probleme/list/${p.id}`)}
            sx={{
              cursor: 'pointer',
              flex: '1 1 calc(33% - 16px)',
              minWidth: 240
            }}
          >
            <CardContent>
              <Typography variant="h6">{p.titre}</Typography>
              <Typography variant="body2" color="text.secondary">
                Quartier : {p.quartier}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Type : {p.typeTravail}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Priorité : {p.priorite}
              </Typography>
            </CardContent>
          </Card>
        ))}
      </Box>
    </Box>
  );
}
