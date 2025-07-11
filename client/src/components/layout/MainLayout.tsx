import { Outlet } from "react-router-dom";
import Footer from "./Footer";
import Header from "./Header";



/**
 * It represents the app's layout.
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