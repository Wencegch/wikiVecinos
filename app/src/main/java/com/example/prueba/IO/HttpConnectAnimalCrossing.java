package com.example.prueba.IO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**Clase que gestiona la conexión con la base de datos externa, a través de una url base a la que debemos
 *  concatenarle los "endpoint" o final de url para recibir la información que necesitemos. Así mismo los parametros
 *  que se quieran enviar se forman en la URL a través del caracter '?'*/
public class HttpConnectAnimalCrossing {

    //Declaramos la url base, que no cambia.
    private static final String URL_BASE = "https://api.nookipedia.com";

    /**
     * Definimos el método para peticiones GET el cual se usará para la consulta de información
     */
    public static String getRequest(String endpoint) {
        HttpURLConnection http = null;
        String content = null;
        try {
            /**Se forma la url más el endpoint. Así como la cabecera, que permitirá decidir la codificación
             * de los datos que se están trasmitiendo.*/
            URL url = new URL( URL_BASE + endpoint );
            http = (HttpURLConnection)url.openConnection();
            http.setRequestProperty("Content-Type", "application/json");
            http.setRequestProperty("Accept", "application/json");
            http.setRequestProperty("X-API-KEY", "316e153d-8651-47c7-9795-eb3f20d830ee");

            /** Si el servidor devuelve un codigo 200 (HTTP_OK == 200) quiere decir que ha devuelto
             * correctamente la información solicitada.*/
            if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //Se codifica el texto de la respuesta como String.
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                content = sb.toString();
                reader.close();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            //Se desconecta la conexión.
            if(http != null) http.disconnect();
        }
        return content;
    }
}