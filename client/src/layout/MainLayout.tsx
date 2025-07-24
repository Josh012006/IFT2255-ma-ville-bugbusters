import { Outlet } from "react-router-dom";
import Footer from "./Footer";
import Header from "./Header";



/**
 * Une classe qui représnete le composant définissant la mise en page de l'application (layout).
 * Elle fait usage des composants {@link Footer} et {@link Header}
 * @returns HTMLNode
 */
export default function MainLayout() {
    return (
        <>
            <Header />
            <main>
                <Outlet /> {/* Here the page corresponding to the route is displayed */}
            </main>
            <Footer />
        </>
    );
}