import axios from "axios";

type requestType = "GET" | "POST" | "PATCH" | "PUT" | "DELETE";

/**
 * Ceci est un hook utile pour envoyer des requêtes asynchrones vers notre serveur.
 * Il nécessite trois arguments principaux pour fonctionner correctement :
 * 
 * @param url c'est l'URL de la requête vers notre serveur. N'oubliez pas le / au début.
 * @param method indique la méthode HTTP correspondant à cette requête.
 * @param body (optionnel). Pour les requêtes qui ne sont pas de type GET, ce paramètre correspond au contenu
 *             du corps de la requête à envoyer au serveur. Il doit s'agir d'un objet JSON transformé en chaîne
 *             à l'aide de la méthode JSON.stringify(monObjet); où monObjet est l'information à transmettre au serveur.
 * @returns le résultat de la requête. C'est un objet JSON qui contient principalement
 *          deux champs intéressants :
 *              - status : un entier indiquant le statut de la requête
 *              - data : les données retournées par le serveur. Elles sont déjà
 *                       analysées (parsées), vous pouvez donc les utiliser directement comme un objet JSON.
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
