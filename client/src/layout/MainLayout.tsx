import { Navigate, Outlet } from "react-router-dom";
import Footer from "./Footer";
import Header from "./Header";
import SideBar from "./SideBar";
import { useAppSelector } from "../redux/store";
import { useState } from "react";



/**
 * Une classe qui représente le composant définissant la mise en page de l'application (layout).
 * Elle fait usage des composants {@link SideBar}, {@link Footer} et {@link Header}
 * @returns ReactNode
 */
export default function MainLayout() {
    const userType : string | null = useAppSelector((state) => state.auth.userType);
    const [showSide, setShowSide] = useState<boolean>(false);

    if(!userType) {
        return <Navigate to="/auth" replace />
    } else {
        return (
            <div className="p-0 h-fix overflow-y-auto mainlayout">
                <div>
                    <SideBar show={showSide} setShow={setShowSide} />
                </div>
                <div className="layout p-0 z-0 m-0 overflow-y-auto">
                    <Header setter={setShowSide} />
                    <main className="h-100 z-0">
                        <Outlet /> {/* Here the page corresponding to the route is displayed */}
                    </main>
                    <Footer />
                </div>
            </div>
        );
    }
    
}