import axios from "axios";
import { useEffect, useState } from "react";

type requestType = "GET" | "POST" | "PATCH" | "PUT" | "DELETE";

export interface RequestResult {
  status: number;
  data: any;
}

export default function useRequest(url: string, method: requestType, body?: string, triggerKey?: number) {
  const urlHead = "http://localhost:7070/api";
  const [result, setResult] = useState<RequestResult | null>(null);


  useEffect(() => {
    async function fetchData() {
      try {
        const response = method === "GET"
          ? await axios.get(urlHead + url, {
              headers: { "Content-Type": "application/json", 
                "Access-Control-Allow-Origin" : "http://localhost:5173"
               },
              validateStatus: status => status >= 200,
            })
          : await axios({
              method: method,
              url: urlHead + url,
              headers: { "Content-Type": "application/json",
                "Access-Control-Allow-Origin" : "http://localhost:5173"
               },
              validateStatus: status => status >= 200,
              data: body ?? null,
            });

        setResult({ status: response.status, data: response.data });

        console.log(response);

      } catch (error) {
        console.error("Une erreur dans useRequest: ", error);
        setResult({ status: 500, data: { error: "Erreur interne" } });
      }
    }

    fetchData();
  }, [body, method, url, triggerKey]);

  return result;
}
