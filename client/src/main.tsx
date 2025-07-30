import React from 'react';
import { createRoot } from 'react-dom/client';
import { MemoryRouter, Routes, Route } from 'react-router-dom';
import SeeProblemesPage from './pages/prestataire/SeeProblemesPage';
import SeeProblemePage  from './pages/prestataire/SeeProblemePage';

const container = document.getElementById('root');
if (container) {
  const root = createRoot(container);

  root.render(
    <MemoryRouter initialEntries={[
      '/prestataire/probleme/list',        // pour la liste
      // '/prestataire/probleme/list/123'  // pour le détail (décommente pour tester le détail)
    ]}>
      <Routes>
        <Route
          path="/prestataire/probleme/list"
          element={<SeeProblemesPage />}
        />
        <Route
          path="/prestataire/probleme/list/:id"
          element={<SeeProblemePage />}
        />
      </Routes>
    </MemoryRouter>
  );
}
