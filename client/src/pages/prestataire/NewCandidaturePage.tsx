import { useParams } from "react-router-dom";
import { useState } from "react";


/**
 * Cette page permet au prestataire de soumettre une nouvelle candidature pour une fiche problème qu'il a repéré.
 * Il faut donc utiliser un formulaire pour récupérer les informations nécessaires, les valider et les soumettre au backend.
 * Dans la soumission, il y aura un besoin de connaitre l'id de la fiche. C'est cette information qui est contenue dans ficheProbleme
 * récupéré dans le path.
 * @returns ReactNode
 */
export default function NewCandidaturePage() {
  const ficheProbleme = useParams().problemId;

  const [prestataire, setPrestataire] = useState('');
  const [nomPrestataire, setNomPrestataire] = useState('');
  const [numeroEntreprise, setNumeroEntreprise] = useState('');
  const [titreProjet, setTitreProjet] = useState('');
  const [description, setDescription] = useState('');
  const [typeTravaux, setTypeTravaux] = useState('');
  const [coutEstime, setCoutEstime] = useState('');
  const [dateDebut, setDateDebut] = useState('');
  const [dateFin, setDateFin] = useState('');
  const [ruesAffectees, setRuesAffectees] = useState('');
  const [erreur, setErreur] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();

    if (
      !prestataire || !nomPrestataire || !ficheProbleme || !numeroEntreprise ||
      !titreProjet || !description || !typeTravaux || !coutEstime ||
      !dateDebut || !dateFin || !ruesAffectees
    ) {
      setErreur("Tous les champs sont requis.");
      return;
    }

    const candidature = {
      prestataire,
      nomPrestataire,
      ficheProbleme,
      numeroEntreprise,
      titreProjet,
      description,
      typeTravaux,
      coutEstime: parseFloat(coutEstime),
      dateDebut: new Date(dateDebut).toISOString(),
      dateFin: new Date(dateFin).toISOString(),
      ruesAffectees
    };

    fetch("http://localhost:7070/candidatures", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(candidature)
    })
    .then((response) => response.json())
    .then((data) => {
      // On réinitialise les champs à vide
      setPrestataire('');
      setNomPrestataire('');
      setNumeroEntreprise('');
      setTitreProjet('');
      setDescription('');
      setTypeTravaux('');
      setCoutEstime('');
      setDateDebut('');
      setDateFin('');
      setRuesAffectees('');
      setErreur('');
      setMessage("Candidature envoyée avec succès !");
    })
    .catch(() => {
      setErreur("Une erreur est survenue.");
    });
  };


}
