package com.example.prueba.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.example.prueba.IO.DBAccess;
import com.example.prueba.R;

public class MainActivity extends AppCompatActivity {
    private EditText nombreUsuario;
    private EditText password;
    private DBAccess mDB;
    private CheckBox saveUser;
    private CircularProgressButton circularIniciarSesion;
    private CircularProgressButton circularRegistrarse;

    /**
     * Método se llama cuando se crea una nueva instancia de la actividad y se utiliza para realizar tareas
     * @param savedInstanceState almacena y restaura el estado de la actividad en caso de que sea destruida
     *                           y recreada por el sistema operativo.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombreUsuario = (EditText) findViewById(R.id.txtUserInicioSesion);
        password = (EditText)findViewById(R.id.txtPasswordInicioSesion);

        circularIniciarSesion = (CircularProgressButton)findViewById(R.id.iniciarSesion);
        circularRegistrarse = (CircularProgressButton)findViewById(R.id.registrate);

        mDB = new DBAccess(this);

        saveUser = (CheckBox) findViewById(R.id.checkBoxGuardarUsuario);

        loadPreferences();

        circularIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
                saveUserPreferences();
            }
        });

        circularRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RegistrarUsuario.class);
                startActivity(i);
            }
        });

    }

    /**
     * Comprueba si el usuario y la contraseña introducidos existen en la base de datos y, en caso
     * afirmativo, permite al usuario acceder a la siguiente actividad de la aplicación.
     */
    public void login(){
        String user = nombreUsuario.getText().toString().trim();
        String pass = password.getText().toString().trim();

        //Mira si el campo del usuario está vacio
        if (user.equals("")){
            Toast.makeText(MainActivity.this,"Debes introducir un usuario", Toast.LENGTH_LONG).show();
            circularIniciarSesion.setProgress(-1);
        }else {
            //Mira si el usuario introducido existe
            if (mDB.compararUsuario(user) == false){
                Toast.makeText(MainActivity.this,"Debes introducir un usuario registrado", Toast.LENGTH_SHORT).show();
                circularIniciarSesion.setProgress(-1);
            }else{
                //Si el USUARIO y la CONTRASEÑA existen, pasa al siguiente activity
                if (mDB.inicioSesion(user, pass) == true){
                    Toast.makeText(MainActivity.this,"Accediendo a los amiibos..", Toast.LENGTH_SHORT).show();
                    circularIniciarSesion.setProgress(0);
                    Intent i = new Intent(MainActivity.this, Rest.class);
                    startActivity(i);
                }else {
                    Toast.makeText(MainActivity.this,"Contraseña incorrecta", Toast.LENGTH_LONG).show();
                    circularIniciarSesion.setProgress(-1);
                }
            }

        }
    }

    /**
     * Guarda la información del usuario en la memoria interna del dispositivo
     */
    public void saveUserPreferences(){
        boolean activado;
        //se crea un objeto SharedPreferences que se utiliza para almacenar las preferencias
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        if (saveUser.isChecked()){
            activado = true;
            //se almacena el nombre de usuario (obtenido del objeto nombreUsuario)
            sharedPreferences.edit().putString ("USUARIO",nombreUsuario.getText().toString()).commit();
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putBoolean("SAVE_USER", activado);
            editor.apply();
        /*Si activado es false, se elimina la clave "USUARIO" y se guarda el valor booleano false con la clave "SAVE_USER"
        * y se llama al método apply() del objeto SharedPreferences.Editor para guardar los cambios.*/
        } else {
            activado = false;

            sharedPreferences.edit().remove("USUARIO").commit();
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putBoolean("SAVE_USER", activado);
            editor.apply();
        }
    }

    /**
     * Carga las preferencias de usuario.
     */
    public void loadPreferences(){
        //se obtiene el objeto SharedPreferences que se utilizó para almacenar las preferencias en el método saveUserPreferences()
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        //se obtiene el valor booleano save asociado con la clave "SAVE_USER" utilizando el método getBoolean()
        boolean save = sharedPreferences.getBoolean("SAVE_USER", false);

        /*Si save es true, se obtiene el nombre de usuario asociado con la clave "USUARIO" utilizando
        * el método getString() y se establece el texto del objeto nombreUsuario con este valor. Además,
        * el botón saveUser se establece en true para indicar que se ha guardado el nombre de usuario.*/
        if (save) {
            String user = sharedPreferences.getString("USUARIO", "");
            nombreUsuario.setText(user);
            saveUser.setChecked(true);
        }
    }
}