import { useState, useEffect } from "react";
import useRequest from "./UseRequest";
import { type RequestResult } from "./UseRequest";

type Method = "GET" | "POST" | "PUT" | "PATCH" | "DELETE";

export default function useManualRequest() {
  const [config, setConfig] = useState<{
    url: string;
    method: Method;
    body?: string;
    triggerKey: number; // clé de déclenchement
  }>({
    url: "", method: "GET", body: undefined, triggerKey: 0
  });

  const [result, setResult] = useState<RequestResult | null>(null);

  const response = useRequest(config.url, config.method, config.body, config.triggerKey);
  useEffect(() => {
    console.log("response" + response);
    setResult(response);
  }, [response]);

  const send = (url: string, method: Method = "GET", body?: string) => {
    setConfig(prev => ({
      url,
      method,
      body,
      triggerKey: prev.triggerKey + 1, // changement de clé
    }));
  };

  return { send, result };
}
