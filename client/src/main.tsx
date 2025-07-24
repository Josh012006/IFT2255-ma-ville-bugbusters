import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import ReduxProvider from './redux/Provider.tsx'
import 'bootstrap/dist/css/bootstrap.min.css';

createRoot(document.getElementById('root')!).render(
    <StrictMode>
        <ReduxProvider>
            <App />
        </ReduxProvider>
    </StrictMode>,
)
