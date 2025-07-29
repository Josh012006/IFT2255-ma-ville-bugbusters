import axios from "axios";

export async function fetchHas(userId : string) {

    const response = await axios.get("http://localhost:7070/api/notification/hasNotifications/" + userId, {
        headers: { "Content-Type": "application/json" },
        validateStatus: status => status >= 200,
    });

    return (response.status === 200) ? response.data.result : false;
}