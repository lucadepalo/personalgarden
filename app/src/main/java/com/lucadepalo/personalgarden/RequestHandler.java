package com.lucadepalo.personalgarden;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Questa classe gestisce le richieste HTTP per l'applicazione.
 */
public class RequestHandler {

    /**
     * Questo metodo invia una richiesta POST all'URL specificato.
     * Nell'app si utilizza solo il metodo POST.
     * Il hashmap contiene i dati da inviare al server sotto forma di coppie chiave-valore.
     *
     * @param requestURL     L'URL a cui inviare la richiesta.
     * @param postDataParams I dati da inviare.
     * @return Una stringa contenente la risposta del server.
     */
    public String sendPostRequest(String requestURL, HashMap<String, String> postDataParams) {
        URL url;
        StringBuilder sb = new StringBuilder();
        try {
            url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                sb = new StringBuilder();
                String response;
                while ((response = br.readLine()) != null) {
                    sb.append(response);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return extractValidJson(sb.toString());
    }

    /**
     * Questo metodo converte i dati in coppie chiave-valore in una stringa di query come richiesto per l'invio al server.
     *
     * @param params I dati da convertire.
     * @return Una stringa di query.
     * @throws UnsupportedEncodingException Se l'encoding non è supportato.
     */
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) first = false;
            else result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    /**
     * Questo metodo converte la risposta JSON in un HashMap.
     *
     * @param jsonString La stringa JSON da analizzare.
     * @return Un HashMap contenente i dati estratti dalla stringa JSON.
     */
    public static HashMap<Integer, String> parseJsonToHashMap(String jsonString) {
        HashMap<Integer, String> resultMap = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            boolean error = jsonObject.getBoolean("error");
            String message = jsonObject.getString("message");

            if (!error) {
                JSONObject speciesJson = jsonObject.getJSONObject("species");
                Iterator<String> keys = speciesJson.keys();

                while (keys.hasNext()) {
                    String key = keys.next();
                    resultMap.put(Integer.parseInt(key), speciesJson.getString(key));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    /**
     * Estrae una sottostringa valida in formato JSON da una stringa di input.
     * Questo metodo è utile quando la risposta del server potrebbe contenere
     * dati extra al di fuori di una struttura JSON valida.
     *
     * @param input La stringa di input contenente potenzialmente un JSON valido.
     * @return Una sottostringa rappresentante il JSON valido o null se non viene trovato un JSON valido.
     */
    public static String extractValidJson(String input) {
        int startIndex = input.indexOf("{");
        int endIndex = input.lastIndexOf("}");

        if (startIndex == -1 || endIndex == -1) {
            return null; // Non abbiamo trovato un JSON valido nella stringa.
        }

        return input.substring(startIndex, endIndex + 1);
    }

}
