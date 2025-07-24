import { Outlet } from "react-router-dom";
import Footer from "./Footer";
import Header from "./Header";
import SideBar from "./SideBar";



/**
 * Une classe qui représente le composant définissant la mise en page de l'application (layout).
 * Elle fait usage des composants {@link SideBar}, {@link Footer} et {@link Header}
 * @returns ReactNode
 */
export default function MainLayout() {
    const userType : string | null = useAppSelector((state) => state.auth.userType);

    return (
        <>
            <Header />
            <SideBar>
                <Outlet /> {/* Here the page corresponding to the route is displayed */}
            </SideBar>
            <Footer />
        </>
    );
}