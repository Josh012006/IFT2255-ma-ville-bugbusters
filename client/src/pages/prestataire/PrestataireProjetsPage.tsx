import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useRequest } from "../../hooks/UseRequest";
import type  Projet  from "../../interfaces/Projet";
import { useAppSelector } from "../../redux/store"



/**
 * Cette page permet de visualiser les projets du prestataire. Il faut donc les récupérer depuis le backend. Il faut inclure un filtre si nécessaire.
 * Lorsqu'on clique sur un projet de la liste, on est redirigé vers la page de visualisation individuelle de ce projet.
 * @returns ReactNode
 */
export default function PrestataireProjetsPage() {
    const navigate = useNavigate();
    const userId = useAppSelector((state: any) => state.auth.userId);
    const userType = useAppSelector((state: any) => state.auth.userType);
    const [filtre, setFiltre] = useState("");
  
    const result = useRequest(`/projets/prestataire/${userId}`, "GET");
    const projets: Projet[] = result?.data || [];
  
    const projetsFiltres = projets.filter((p) =>
      p.titreProjet.toLowerCase().includes(filtre.toLowerCase())
    );
  
    return (
      <div className="p-6">
        <h1 className="text-2xl font-bold mb-4">Mes projets</h1>
  
        <input
          type="text"
          placeholder="Filtrer par titre"
          value={filtre}
          onChange={(e) => setFiltre(e.target.value)}
          className="border p-2 mb-4 w-full max-w-md"
        />
  
        <ul className="space-y-4">
          {projetsFiltres.map((p) => (
            <li
              key={p.id}
              onClick={() => navigate(`/prestataire/projet/${p.id}`)}
              className="border p-4 rounded shadow hover:bg-gray-50 cursor-pointer"
            >
              <h2 className="text-lg font-semibold">{p.titreProjet}</h2>
              <p>{p.description}</p>
              <p className="text-sm text-gray-600">Statut : {p.statut}</p>
            </li>
          ))}
          {projetsFiltres.length === 0 && <p>Aucun projet trouvé.</p>}
        </ul>
      </div>
    );
  }