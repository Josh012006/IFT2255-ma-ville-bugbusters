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
        setMessage("Votre candidature a été envoyée avec succès !");
    })
    .catch(() => {
      setErreur("Une erreur est survenue lors de l'envoi de la candidature.");
    });
  };

  return (
    <div>
        <h2>Soumettre une candidature pour la fiche {ficheProbleme}</h2>
        <form onSubmit={handleSubmit}>

        <div>
            <label>Prestataire :</label><input value={prestataire} 
            onChange={(e) => setPrestataire(e.target.value)} />
        </div>

        <div>
            <label>Nom de l'entreprise :</label><input value={nomPrestataire} 
            onChange={(e) => setNomPrestataire(e.target.value)} />
        </div>

        <div>
            <label>Numéro d'entreprise :</label><input value={numeroEntreprise} 
            onChange={(e) => setNumeroEntreprise(e.target.value)} />
        </div>
        <div>
            <label>Titre du projet :</label><input value={titreProjet} 
            onChange={(e) => setTitreProjet(e.target.value)} />
        </div>

        <div>
            <label>Description :</label><textarea value={description} 
            onChange={(e) => setDescription(e.target.value)} />
        </div>

        <div>
            <label>Type de travaux :</label>
            <select value={typeTravaux} onChange={(e) => setTypeTravaux(e.target.value)}>
                <option value="">Choisir un type de travaux</option>
                <option value="Travaux routiers">Travaux routiers</option>
                <option value="Travaux de gaz ou électricité">Travaux de gaz ou électricité</option>
                <option value="Construction ou rénovation">Construction ou rénovation</option>
                <option value="Entretien paysager">Entretien paysager</option>
                <option value="Travaux liés aux transports en commun">Travaux liés aux transports en commun</option>
                <option value="Travaux de signalisation et éclairage">Travaux de signalisation et éclairage</option>
                <option value="Travaux souterrains">Travaux souterrains</option>
                <option value="Travaux résidentiel">Travaux résidentiel</option>
                <option value="Entretien urbain">Entretien urbain</option>
                <option value="Entretien des réseaux de télécommunication">Entretien des réseaux de télécommunication</option>
            </select>
        </div>

        <div>
            <label>Coût estimé :</label><input type="number" value={coutEstime} 
            onChange={(e) => setCoutEstime(e.target.value)} />
        </div>

        <div>
            <label>Date de début :</label><input type="date" value={dateDebut} 
            onChange={(e) => setDateDebut(e.target.value)} />
        </div>

        <div>
            <label>Date de fin :</label><input type="date" value={dateFin} 
            onChange={(e) => setDateFin(e.target.value)} />
        </div>

        <div>
            <label>Rues affectées :</label><input value={ruesAffectees} 
            onChange={(e) => setRuesAffectees(e.target.value)} />
        </div>


        // Affiche de message d'erreur ou d'envoi de candidature
        {erreur && <p>{erreur}</p>}
        {message && <p>{message}</p>}

        <button type="submit">Soumettre</button>

      </form>
    </div>
  );
}
