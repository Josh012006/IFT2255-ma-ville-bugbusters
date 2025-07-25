import { useAppSelector } from "../../redux/store";


/**
 * Cette page affiche le dashboard en fonction du type d'utilisateur avec les liens et les possibilités.
 * @returns ReactNode
 */
export default function DashboardPage() {
    const userType : string | null = useAppSelector((state) => state.auth.userType);

    return (
        <div>

        </div>
    );
}