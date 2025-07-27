
/**
 * Le footer de l'application. Il définit la mise en page (layout) de l'application.
 * @returns ReactNode
 */
export default function Footer() {
    return (
        <footer className="d-flex  flex-column justify-content-center align-items-center w-100 p-2">
            <img className="m-2" src="/montreal.png" height="30" alt="Logo de la ville de Montréal" />
            <p>&copy; 2025 BugBusters. Tous droits réservés. DM2 IFT2255 - UdeM</p>
        </footer>
    );
}