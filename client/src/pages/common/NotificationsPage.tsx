import { useState, useEffect } from "react";
import useRequest from "../../hooks/UseRequest";
import type { Prestataire } from "../../interfaces/users/Prestataire";
import type Resident from "../../interfaces/users/Resident";
import { useAppSelector } from "../../redux/store";
import Loader from "../../components/Loader";
import { Divider, List, ListItem, ListItemText } from "@mui/material";
import type Notification from "../../interfaces/users/Notification";
import { formatDate } from "../../utils/formatDate";
import MyLink from "../../components/MyLink";


/**
 * Cette page permet d'afficher la liste des notifications de l'utilisateur.
 * Il faudra envoyer une requête pour récupérer les notifications en fonctions de l'id de l'utilisateur.
 * Pour un rédident ou un prestataire, l'id est trouvé grâce à userInfos.
 * Pour le STPM, l'id est unique et précisé ici.
 * Lorsqu'on clique sur une notification, on est redirigé vers la page de visualisation de la notification en individuel.
 * @returns ReactNode
 */
export default function NotificationsPage() {
    const userInfos : Prestataire | Resident | null = useAppSelector((state) => state.auth.infos);

    const userId = (userInfos)? userInfos.id : "507f1f77bcf86cd799439011";

    // Requête au backend
    const [notifications, setNotifications] = useState<Notification[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const response = useRequest("/notification/getAll/" + userId, "GET");

    useEffect(() => {
        if (response && response.status === 200) {
            setNotifications(response.data);
        }
        setLoading(false);
    }, [response]);

    return(
        <div>
            <h1 className="mt-5 mb-3 text-center">Vos notifications</h1>
            <p className="mb-4 text-center">Cliquez sur une notification pour la lire et voir les détails</p>
            {loading && <Loader />}
            {!loading && <List>
                {notifications.map((notif, index) => {
                    return <MyLink className="text-black" to={"/notification/" + notif.id} key={index}>
                        <Divider component="li" />
                        <ListItem className="d-flex align-items-center hover-white">
                            <ListItemText
                                primary={(notif.createdAt) ? formatDate(notif.createdAt) : ""}
                                secondary={<span className="ellipsis">{notif.message}</span>}
                            />
                            {notif.statut === "non lue" && <span className="rounded-circle bg-primary mx-3 p-1"></span>}
                        </ListItem>
                    </MyLink>
                })}
            </List>}
        </div>
    );
}