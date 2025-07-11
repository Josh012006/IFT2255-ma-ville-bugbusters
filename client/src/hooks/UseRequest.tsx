import axios from "axios";

type requestType = "GET" | "POST" | "PATCH" | "PUT" | "DELETE";

/**
 * This is a hook that is useful to send asynchronous request to our server side.
 * It requires three main arguments to function correctly:
 * 
 * @param url it is the url of the request to our server. Don't forget the / at the start
 * @param method this indicates the HTTP method corresponding to this request
 * @param body that is optional. For request that are not of the GET type, this parameter indicates the body content
 *             to send to the server. It should be a stringified JSON object obtained by using the method
 *             JSON.stringify(myObject); where myObject is the information to send to the server.
 * @returns the result of the request. It is a JSON object that mainly contains 
 *          two interesting fields :
 *              - status: that is an integer saying what the request status is
 *              - data: that is the data returned by the server. It is already 
 *                      parsed so you can use it directly as a JSON object
 */
export async function useRequest(url: string, method: requestType, body?: string) {

    const urlHead = "http://localhost:7070/api";

    try {
        
        let result;

        if(method === "GET") {
            result = await axios.get(
                urlHead + url, 
                { 
                    headers: { 
                        'Content-Type': 'application/json' 
                    }, 
                    validateStatus: status => status >= 200 
                }
            );
        } else {
            result = await axios({
                method: method,
                url: urlHead + url,
                headers: {
                    'Content-Type': 'application/json' 
                }, 
                validateStatus: status => status >= 200,
                data: (body) ?? null,
            });
        }

        return result;

    } catch(error) {
        console.log("Une erreur dans useRequest: " + error);
    }
}

