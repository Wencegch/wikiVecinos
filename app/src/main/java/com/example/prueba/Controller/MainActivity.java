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
    /*comprueba si el usuario y la contraseña introducidos existen en la base de datos y, en caso
    afirmativo, permite al usuario acceder a la siguiente actividad de la aplicación.*/
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

    //guarda la información del usuario en la memoria interna del dispositivo
    public void saveUserPreferences(){
        boolean activado;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if (saveUser.isChecked()){
            activado = true;

            sharedPreferences.edit().putString ("USUARIO",nombreUsuario.getText().toString()).commit();
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putBoolean("SAVE_USER", activado);
            editor.apply();
        } else {
            activado = false;

            sharedPreferences.edit().remove("USUARIO").commit();
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putBoolean("SAVE_USER", activado);
            editor.apply();
        }
    }

    //carga la información al iniciar la aplicación
    public void loadPreferences(){

        //recuperamos las preferencias guardadas en la memoria interna del dispositivo
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        boolean save = sharedPreferences.getBoolean("SAVE_USER", false);

        // si la opción de guardar usuario está seleccionada
        if (save) {
            String user = sharedPreferences.getString("USUARIO", "");
            nombreUsuario.setText(user);
            saveUser.setChecked(true);
        }
    }
}