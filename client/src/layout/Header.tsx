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
        <header className="d-flex justify-content-around align-items-center sticky-top">
            <div className="row align-items-center w-100">
                <div className="col-2 d-lg-none h-100 d-flex justify-content-around align-items-center">
                    <img className="pointer border border-2 p-1 rounded border-danger" src="/tab.png" width="35" height="35" alt="Tabs icon" onClick={() => {setter((state) => !state)}} />
                </div>
                <h1 className="col-5 col-lg-7 h-100 d-flex justify-content-around align-items-center">MaVille</h1>
                <div className="col-5 d-flex justify-content-around align-items-center h-100">
                    <MyLink to="/notification/list" className="position-relative m-1 m-lg-4 px-2 pointer">
                        <img src="/notif.png" alt="cloche de notifications" width="30" height="30" />
                        <span className={`rounded-circle bg-danger ${has? "d-block" : "d-none"} position-absolute bottom-0 cloche`}></span>
                    </MyLink>
                    <MyLink className="m-1 m-lg-4 px-2 pointer" to="/profile">
                        <Avatar src="/profile.png" className={`${userType ? "d-block" : "d-none"}`} />
                    </MyLink>
                </div>
            </div>
        </header>
    );
}