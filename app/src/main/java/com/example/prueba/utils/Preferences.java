package com.example.prueba.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

import androidx.constraintlayout.widget.ConstraintLayout;

public class Preferences {
    /**
     * Método que modifica el color de fondo de la actividad actual en función de lo establecido en
     * las preferencias
     * @param context Contexto en el cual se encuentra en ese momento la aplicación
     * @param layout Layout de la actividad
     */
    public static void loadPreferences(Context context, ConstraintLayout layout){
        int numeroColor = Color.WHITE;
        // Obtenemos la instancia del gestor de las preferencias
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        // Obtenemos el valor asociado a la clave de la preferencia del color correspondiente al
        // componente del fragment de las preferencias
        String color = sharedPreferences.getString("colorPreference","Blanco");
        switch (color){
            case "Azul":
                numeroColor = Color.BLUE;
                break;
            case "Rojo":
                numeroColor = Color.RED;
                break;
            case "Verde":
                numeroColor = Color.GREEN;
                break;
        }
        layout.setBackgroundColor(numeroColor);
    }

    /**
     * Método que informa sobre la preferencia de notificaciones informativas para el usuario
     * @param context Contexto en el cual se encuentra en ese momento la aplicación
     * @return Devuelve verdadero si las notificaciones están actividas y falso si están desactivadas
     */
    public static boolean notificationPreference(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean("notificationPreference", true);
    }
}
