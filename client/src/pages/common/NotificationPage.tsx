import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import useRequest from "../../hooks/UseRequest";
import type Notification from "../../interfaces/users/Notification";
import Loader from "../../components/Loader";
import MyLink from "../../components/MyLink";
import { formatDate } from "../../utils/formatDate";


/**
 * Cette page permet d'afficher une notification avec toutes ses informations.
 * Elle a un paramère de path qui est l'id de la notification. A partir de cet aid, il faut alors rechercher la notification du backend
 * et afficher les informations.
 * @returns ReactNode
 */
export default function NotificationPage() {

    const notificationId = useParams().id;

    // Requête au backend
    const [notification, setNotification] = useState<Notification | null>(null);
    const [loading, setLoading] = useState<boolean>(true);
    const response = useRequest("/notification/" + notificationId, "GET");

    useEffect(() => {
        if (response && response.status === 200) {
            setNotification(response.data);
        }
        setLoading(false);
    }, [response]);

    return (
        <div>
            <h1 className="mt-5 mb-3 text-center">Notification</h1>
            {loading && <Loader />}
            {!loading && notification && <div className="mt-5 d-flex flex-column align-items-center">
                <p className="text-center"><b>Date:</b> {(notification.createdAt) ? formatDate(notification.createdAt) : ""}</p>
                <h6 className="m-1 text-center">Message</h6>
                <p className="m-2 text-center">{notification.message}</p>
                {notification.url && <MyLink to={notification.url} className="rounded-4 text-white orange p-3 m-5">En savoir plus</MyLink>}
            </div>}
        </div>
    );
}