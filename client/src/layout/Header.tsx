import { Avatar } from "@mui/material";
import { useAppSelector } from "../redux/store";
import { useEffect, useState, type Dispatch, type SetStateAction } from "react";
import useRequest from "../hooks/UseRequest";
import MyLink from "../components/MyLink";

/**
 * Le header de l'application. Il définit la mise en page (layout) de l'application.
 * @returns ReactNode
 */
export default function Header({setter}: {setter: Dispatch<SetStateAction<boolean>>}) {

    const userType : string | null = useAppSelector((state) => state.auth.userType);
    const userInfos = useAppSelector((state) => state.auth.infos)

    const [has, setHas] = useState<boolean>(false);

    const userId = (userInfos)? userInfos.id : "507f1f77bcf86cd799439011";

    const response = useRequest("/notification/hasNotifications/" + userId, "GET");

    useEffect(() => {
        if (response && response.status === 200) {
            setHas(response.data.result);
        }
    }, [response]);

    console.log(has);

    return (
        <header className="d-flex justify-content-around align-items-center sticky-top sticky-lg-off">
            <img className="pointer my-2 mx-4 d-lg-none border border-2 p-1 rounded border-danger" src="/tab.png" height="35" alt="Tabs icon" onClick={() => {setter((state) => !state)}} />
            <h1>MaVille</h1>
            <div className="d-flex justify-content-around align-items-center">
                <MyLink to="/notification/list" className="position-relative m-1 m-lg-4 px-2 pointer">
                    <Avatar src="/notif.png" className="" sx={{ width: 35, height: 35 }} />
                    <span className={`p-2 rounded-circle bg-danger ${has? "d-block" : "d-none"} position-absolute bottom-0 end-0`}></span>
                </MyLink>
                <MyLink className="m-1 m-lg-4 px-2 pointer" to="/profile">
                    <Avatar src="/profile.png" className={`${userType ? "d-block" : "d-none"}`} />
                </MyLink>
            </div>
        </header>
    );
}