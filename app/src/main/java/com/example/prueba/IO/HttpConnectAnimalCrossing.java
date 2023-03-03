package com.example.prueba.IO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*Esta clase es responsable de gestionar la conexión con una base de datos externa a través de una
* URL base. Para recibir la información que necesitamos, concatenamos los "endpoint" o la parte final
* de la URL. Además, si deseamos enviar parámetros, se forman en la URL utilizando el carácter '?'.*/
public class HttpConnectAnimalCrossing {

    //Declaramos la url base, que no cambia.
    private static final String URL_BASE = "https://api.nookipedia.com";

    /**
     * Este método que hemos definido se utiliza para realizar peticiones GET y consultar información
     * de la base de datos externa
     * @param endpoint parte final de la URL
     * @return devuelve la url completa de la página
     */
    public static String getRequest(String endpoint) {
        HttpURLConnection http = null;
        String content = null;
        try {
            /*Para enviar la petición, formamos la URL concatenando el endpoint correspondiente.
            Además, se establece una cabecera que permitirá especificar la codificación de los datos
            que se están transmitiendo.*/
            URL url = new URL( URL_BASE + endpoint );
            http = (HttpURLConnection)url.openConnection();
            http.setRequestProperty("Content-Type", "application/json");
            http.setRequestProperty("Accept", "application/json");
            http.setRequestProperty("X-API-KEY", "316e153d-8651-47c7-9795-eb3f20d830ee");

            /*Si el servidor devuelve un código 200 (HTTP_OK == 200), significa que ha respondido
            * correctamente a la solicitud y ha proporcionado la información solicitada.*/
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