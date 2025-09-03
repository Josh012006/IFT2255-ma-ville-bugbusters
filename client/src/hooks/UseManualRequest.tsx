import { useState } from "react";
import axios from "axios";
import type { requestType, RequestResult } from "./UseRequest";

export default function useManualRequest() {
  const [result, setResult] = useState<RequestResult | null>(null);

  const send = async (url: string, method: requestType = "GET", body?: string) => {
    const urlHead = import.meta.env.VITE_API_URL ?? "http://localhost:7070/api";

    try {
      const response = method === "GET"
        ? await axios.get(urlHead + url, {
            headers: { "Content-Type": "application/json" },
            validateStatus: status => status >= 200,
          })
        : await axios({
            method,
            url: urlHead + url,
            headers: { "Content-Type": "application/json" },
            validateStatus: status => status >= 200,
            data: body ?? null,
          });

      setResult({ status: response.status, data: response.data });
    } catch (error) {
      console.error("Erreur dans useManualRequest :", error);
      setResult({ status: 500, data: { error: "Erreur interne" } });
    }
  };

  return { send, result };
}
