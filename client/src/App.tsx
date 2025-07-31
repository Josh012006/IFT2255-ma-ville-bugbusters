import { BrowserRouter, Routes, Route } from "react-router-dom";
import HomePage from "./pages/auth/HomePage";
import MainLayout from "./layout/MainLayout";
import RequireAuth from "./pages/auth/RequireAuth";
import ChoicePage from "./pages/auth/ChoicePage";
import ProfilePage from "./pages/common/ProfilePage";
import DashboardPage from "./pages/common/DashboardPage";
import NotFoundPage from "./pages/NotFoundPage";
import NotificationsPage from "./pages/common/NotificationsPage";
import NotificationPage from "./pages/common/NotificationPage";
import ManageSignalementsPage from "./pages/stpm/ManageSignalementsPage";
import ManageSignalementPage from "./pages/stpm/ManageSignalementPage";
import ManageCandidaturesPage from "./pages/stpm/ManageCandidaturesPage";
import ManageCandidaturePage from "./pages/stpm/ManageCandidaturePage";
import SeeProjetsPage from "./pages/resident/SeeProjetsPage";
import SeeProjetPage from "./pages/resident/SeeProjetPage";
import ChoiceOptionSignalementPage from "./pages/resident/ChoiceOptionSignalementPage";
import NewSignalementPage from "./pages/resident/NewSignalementPage";
import ResidentSignalementsPage from "./pages/resident/ResidentSignalementsPage";
import ResidentSignalementPage from "./pages/resident/ResidentSignalementPage";
import PrestataireProjetsPage from "./pages/prestataire/PrestataireProjetsPage";
import PrestataireProjetPage from "./pages/prestataire/PrestataireProjetPage";
import NewCandidaturePage from "./pages/prestataire/NewCandidaturePage";
import PrestataireCandidaturesPage from "./pages/prestataire/PrestataireCandidaturesPage";
import PrestataireCandidaturePage from "./pages/prestataire/PrestataireCandidaturePage";
import SeeProblemesPage from "./pages/prestataire/SeeProblemesPage";
import SeeProblemePage from "./pages/prestataire/SeeProblemePage";


/**
 * La configuration du routage de l'application avec React-router
 */
function App() {

  return (
    <BrowserRouter>
        <Routes>
            
            {/* Les pages d'authentification */}
            <Route path="/auth">
                {/* La page d'acceuil avec le choix du type d'utilisateur */}
                <Route index element={<HomePage />} />
                {/* Route pour le résident ou le prestataire pour choisir le profil. */}
                <Route path="choix" element={<ChoicePage />} />
            </Route>

            <Route path="/" element={<MainLayout />}>

                {/* Route pour le prestataire et le résident pour voir leur profil et modfier leurs préférences de notifications */}
                <Route path="profile" element={<ProfilePage />} />
                
                {/* Route pour le dashboard des utilisateurs */}
                <Route index element={<DashboardPage />} />

                {/* Routes pour les notifications des utilisateurs */}
                <Route path="notification">
                    <Route path="list" element={<NotificationsPage />} />
                    <Route path=":id" element={<NotificationPage />} />
                </Route>


                {/* Les pages spécifiques au STPM */}
                <Route path="stpm" element={<RequireAuth role="stpm" />}>
                    {/* Routes pour la gestion des nouveaux signalements */}
                    <Route path="signalement">
                        <Route path="list" element={<ManageSignalementsPage />} />
                        <Route path=":id" element={<ManageSignalementPage />} />
                    </Route>
                    {/* Routes pour la gestion des nouvelles candidatures */}
                    <Route path="candidature">
                        <Route path="list" element={<ManageCandidaturesPage />} />
                        <Route path=":id" element={<ManageCandidaturePage />} />
                    </Route>
                </Route>

                {/* Les pages spécifiques aux résidents */}
                <Route path="resident" element={<RequireAuth role="resident" />}>
                    {/* Routes pour la visualisation des projets */}
                    <Route path="projet">
                        <Route path="list" element={<SeeProjetsPage />} />   {/* Ici, on gère à la fois les projets en cours et ceux à venir */}
                        <Route path=":id" element={<SeeProjetPage />} />
                    </Route>
                    {/* Routes pour l'envoi, la modification et la visualisation de signalements */}
                    <Route path="signalement">
                        <Route index element={<ChoiceOptionSignalementPage />} />
                        <Route path="new" element={<NewSignalementPage />} />
                        <Route path="list" element={<ResidentSignalementsPage />} />
                        <Route path=":id" element={<ResidentSignalementPage />} />   {/* On inclut aussi dans la page, la modification du signalement */}
                    </Route>
                </Route>

                {/* Les pages spécifiques aux prestataires */}
                <Route path="prestataire" element={<RequireAuth role="prestataire" />}>
                    {/* Routes pour la visualisation et la modification des projets */}
                    <Route path="projet">
                        <Route path="list" element={<PrestataireProjetsPage />} /> 
                        <Route path=":id" element={<PrestataireProjetPage />} />   {/* On inclut aussi dans la page la modification du projet */}
                    </Route>
                    {/* Routes pour la visualisation, la modification et l'envoi de candidatures  */}
                    <Route path="candidature">
                        <Route path="new/:problemId" element={<NewCandidaturePage />} />
                        <Route path="list" element={<PrestataireCandidaturesPage />} />
                        <Route path=":id" element={<PrestataireCandidaturePage />} />   {/* On inclut aussi dans la page la modification de la candidature */}
                    </Route>
                    {/* Routes pour la visualisation des problèmes */}
                    <Route path="probleme">
                        <Route path="list" element={<SeeProblemesPage />} />   {/* Ici, on n'oublie pas d'inclure le filtrage */}
                        <Route path=":id" element={<SeeProblemePage />} />
                    </Route>
                </Route>

            </Route>

            {/* L'affichage lorsqu'on accède à une route non définie */}
            <Route path="*" element={<NotFoundPage />} />

        </Routes>
    </BrowserRouter>
  );
}

export default App;
