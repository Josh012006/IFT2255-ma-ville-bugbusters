import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getProjetsByPrestataire } from "../../services/projetService";
import type { Projet } from "../../interfaces/Projet";
import { useAuth } from "../../context/AuthContext";


/**
 * Cette page permet de visualiser les projets du prestataire. Il faut donc les récupérer depuis le backend. Il faut inclure un filtre si nécessaire.
 * Lorsqu'on clique sur un projet de la liste, on est redirigé vers la page de visualisation individuelle de ce projet.
 * @returns ReactNode
 */
export default function PrestataireProjetsPage() {

    const [projets, setProjets] = useState<Projet[]>([]);
    const [filtre, setFiltre] = useState("");
    const navigate = useNavigate();
    const { user } = useAuth();

    useEffect(() => {
        if (!user || !user.est_prestataire) return;
        getProjetsByPrestataire(user._id).then(setProjets);
    }, [user]);

    const projetsFiltres = projets.filter((p) =>
        p.nom.toLowerCase().includes(filtre.toLowerCase())
    );
    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-4">Mes projets</h1>

            <input
                type="text"
                placeholder="Filtrer par nom"
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
                        <h2 className="text-lg font-semibold">{p.nom}</h2>
                        <p>{p.description}</p>
                        <p className="text-sm text-gray-600">Statut : {p.statut}</p>
                    </li>
                ))}
                {projetsFiltres.length === 0 && <p>Aucun projet trouvé.</p>}
            </ul>
        </div>
    );
}