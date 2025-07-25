import { Navigate, Outlet } from "react-router-dom";
import Footer from "./Footer";
import Header from "./Header";
import SideBar from "./SideBar";
import { useAppSelector } from "../redux/store";



/**
 * Une classe qui représente le composant définissant la mise en page de l'application (layout).
 * Elle fait usage des composants {@link SideBar}, {@link Footer} et {@link Header}
 * @returns ReactNode
 */
export default function MainLayout() {
    const userType : string | null = useAppSelector((state) => state.auth.userType);

    if(!userType) {
        return <Navigate to="/auth" replace />
    } else {
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
    
}