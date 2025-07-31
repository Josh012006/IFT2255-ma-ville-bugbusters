import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import useRequest from "../../hooks/UseRequest";
import type Notification from "../../interfaces/users/Notification";
import Loader from "../../components/Loader";
import MyLink from "../../components/MyLink";
import { formatDate } from "../../utils/formatDate";
import { updateHas } from "../../redux/features/authSlice";
import { useDispatch } from "react-redux";
import { useAppSelector, type AppDispatch } from "../../redux/store";
import { fetchHas } from "../../utils/fetchHas";
import { Alert } from "@mui/material";


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
    const [error, setError] = useState(false);
    const response = useRequest("/notification/" + notificationId, "GET");

    
    const dispatch = useDispatch<AppDispatch>();
    
    const userInfos = useAppSelector((state) => state.auth.infos);
    const userType = useAppSelector((state) => state.auth.userType);
    const userId = (userInfos)? userInfos.id : "507f1f77bcf86cd799439011";

    useEffect(() => {
        async function hasNotif() {
            const hasRes = await fetchHas(userId?? "");
    
            dispatch(updateHas(hasRes));
        }

        if(response) {
            if (response.status === 200) {
                setNotification(response.data);
                hasNotif();
                setLoading(false);
            } else {
                console.log(response.data);
                setLoading(false);
                setError(true);
            }
        }
        
    }, [dispatch, response, userId]);


    return (
        <div>
            <h1 className="mt-5 mb-3 text-center">Notification</h1>
            {loading && <Loader />}
            {error && <Alert severity="error">Un problème est survenu. Veuillez réessayer plus tard.</Alert>}
            {!loading && notification && (notification.user === userInfos?.id || (notification.user === "507f1f77bcf86cd799439011" && userType === "stpm")) && <div className="mt-5 d-flex flex-column align-items-center">
                <p className="text-center"><b>Date:</b> {(notification.createdAt) ? formatDate(notification.createdAt) : ""}</p>
                <h6 className="m-1 text-center">Message</h6>
                <p className="m-2 text-center"><b>Message</b> : {notification.message}</p>
                {notification.url && <MyLink to={notification.url} className="rounded-4 text-white orange p-3 m-5">En savoir plus</MyLink>}
            </div>}
        </div>
    );
}