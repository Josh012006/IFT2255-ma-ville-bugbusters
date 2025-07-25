import { useParams } from "react-router-dom";


/**
 * Cette page permet d'afficher une notification avec toutes ses informations.
 * Elle a un paramère de path qui est l'id de la notification. A partir de cet aid, il faut alors rechercher la notification du backend
 * et afficher les informations.
 * @returns ReactNode
 */
export default function NotificationPage() {

    const notificationId = useParams().id;

    return (
        <div>

        </div>
    );
}