package com.example.prueba.Controller;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dd.CircularProgressButton;
import com.example.prueba.IO.DBAccess;
import com.example.prueba.R;

public class RegistrarUsuarioActivity extends AppCompatActivity {
    private EditText usuario;
    private EditText pass;
    private CircularProgressButton aceptar;
    private DBAccess mDB;

    /**
     * Método se llama cuando se crea una nueva instancia de la actividad y se utiliza para realizar tareas
     * @param savedInstanceState almacena y restaura el estado de la actividad en caso de que sea destruida
     *                           y recreada por el sistema operativo.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar_usuario);

        usuario = (EditText) findViewById(R.id.txtUser);
        pass = (EditText) findViewById(R.id.txtPassword);
        aceptar = (CircularProgressButton)findViewById(R.id.aceptar);

        mDB = new DBAccess(this);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = usuario.getText().toString().trim();
                String password = pass.getText().toString().trim();

                //comprueba que el campo del usuario y la contraseña no están vacios
                if (user.equals("") || password.equals("")){
                    Toast.makeText(RegistrarUsuarioActivity.this, "Los campos no pueden estar vacios", Toast.LENGTH_LONG).show();
                    aceptar.setProgress(-1);
                }else{
                    //Mira si el usuario ya ha sido creado en la base de datos, sino, se guardan el usuario y la contraseña
                    if (mDB.compararUsuario(user) == true){
                        Toast.makeText(RegistrarUsuarioActivity.this,"Este nombre de usuario ya ha sido registrado", Toast.LENGTH_LONG).show();
                        aceptar.setProgress(-1);
                    }else{
                        mDB.insert(user, password);
                        Toast.makeText(RegistrarUsuarioActivity.this,"Usuario creado correctamente", Toast.LENGTH_SHORT).show();
                        aceptar.setProgress(0);
                    }
                }
            }
        });
    }
}
