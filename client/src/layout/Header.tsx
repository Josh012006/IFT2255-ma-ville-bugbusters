import { Avatar } from "@mui/material";
import { useAppSelector, type AppDispatch } from "../redux/store";
import { useEffect, type Dispatch, type SetStateAction } from "react";
import MyLink from "../components/MyLink";
import { useDispatch } from "react-redux";
import { updateHas } from "../redux/features/authSlice";
import { fetchHas } from "../utils/fetchHas";
import { getRandomHexColor } from "../utils/randomColor";

/**
 * Le header de l'application. Il définit la mise en page (layout) de l'application.
 * @returns ReactNode
 */
export default function Header({setter}: {setter: Dispatch<SetStateAction<boolean>>}) {

    const userInfos = useAppSelector((state) => state.auth.infos);
    const userId = (userInfos)? userInfos.id : "507f1f77bcf86cd799439011";

    const dispatch = useDispatch<AppDispatch>();

    useEffect(() => {
        async function hasNotif() {
            const hasRes = await fetchHas(userId?? "");
    
            dispatch(updateHas(hasRes));
        }

        hasNotif();
    }, [dispatch, userId]);

    const userType : string | null = useAppSelector((state) => state.auth.userType);

    const has = useAppSelector((state) => state.auth.has);


    return (
        <header className="d-flex justify-content-around align-items-center sticky-top sticky-lg-none z-1 z-lg-0">
            <div className="row align-items-center w-100">
                <div className="col-2 d-lg-none h-100 d-flex justify-content-around align-items-center">
                    <img className="pointer m-2" src="/tab1.png" width="35" alt="sidebar icon" onClick={() => {setter((state) => !state)}} />
                </div>
                <h1 className="col-5 col-lg-7 h-100 d-flex justify-content-around align-items-center">MaVille</h1>
                <div className="col-5 d-flex justify-content-around align-items-center h-100">
                    <MyLink to="/notification/list" className="position-relative m-1 m-lg-4 px-2 pointer">
                        <img src="/notif.png" alt="cloche de notifications" width="30" height="30" />
                        <span className={`rounded-circle bg-danger ${has? "d-block" : "d-none"} position-absolute bottom-0 cloche`}></span>
                    </MyLink>
                    {(userType !== "stpm") && userInfos && <MyLink className="m-1 m-lg-4 px-2 pointer" to="/profile">
                        {userInfos.nom && <Avatar sx={{backgroundColor: getRandomHexColor()}}>{userInfos.nom.split(" ")[0]? userInfos.nom.split(" ")[0][0] : ""}{userInfos.nom.split(" ")[1]? userInfos.nom.split(" ")[1][0] : ""}</Avatar>}
                        {!userInfos.nom && <Avatar src="/profile.png" />}
                    </MyLink>}
                </div>
            </div>
        </header>
    );
}