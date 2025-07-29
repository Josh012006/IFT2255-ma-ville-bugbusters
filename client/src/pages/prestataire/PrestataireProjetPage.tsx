import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import type  Projet  from "../../interfaces/Projet";
import axios from "axios";
import type { AxiosResponse } from "axios";




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
  .then((res: AxiosResponse<Projet>) => setProjet(res.data))
  .catch((err: unknown) => console.error("Erreur lors du chargement du projet :", err));
   }, [projetId]);
 
   if (!projet) {
     return <p className="p-6">Chargement...</p>;
   }
 
   return (
     <div className="p-6 space-y-4">
       <h1 className="text-2xl font-bold">{projet.titreProjet}</h1>
       <p className="text-gray-700">{projet.description}</p>
       <p><strong>Statut :</strong> {projet.statut}</p>
       <p><strong>Date début :</strong> {new Date(projet.dateDebut).toLocaleDateString()}</p>
       <p><strong>Date fin :</strong> {new Date(projet.dateFin).toLocaleDateString()}</p>
       <p><strong>Coût :</strong> {projet.cout}$</p>
       <p><strong>Quartier :</strong> {projet.quartier}</p>
       <p><strong>Rues affectées :</strong> {projet.ruesAffectees.join(", ")}</p>
 
       <button
         onClick={() => navigate("/prestataire/projets")}
         className="mt-4 bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
       >
         Retour à la liste
       </button>
     </div>
   );
 }