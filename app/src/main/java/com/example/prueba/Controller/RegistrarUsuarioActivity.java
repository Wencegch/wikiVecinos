package com.example.prueba.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.dd.CircularProgressButton;
import com.example.prueba.IO.DBAccess;
import com.example.prueba.R;
import com.example.prueba.utils.Preferences;

public class RegistrarUsuarioActivity extends AppCompatActivity {
    private ConstraintLayout constraintLayout;

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
        setContentView(R.layout.activity_registrar_usuario);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraint_layout_registrar_usuario);

        usuario = (EditText) findViewById(R.id.txtUser);
        pass = (EditText) findViewById(R.id.txtPassword);
        aceptar = (CircularProgressButton)findViewById(R.id.aceptar);

        mDB = new DBAccess(this);

        // Cargamos la preferencias
        Preferences.loadPreferences(this, constraintLayout);
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

    @Override
    protected void onResume() {
        super.onResume();
        // Cargamos las preferencias
        Preferences.loadPreferences(this, constraintLayout);
    }

    // Sobreescribimos el metodo onCreateOptionsMenu para crearnos un menu personalizada
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Usamos un inflater para construir la vista pasandole el menu por defecto como parámetro
        // para colocarlo en la vista
        getMenuInflater().inflate(R.menu.simple_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    // Sobrescribimos el metodo onOptionsItemSelected para manejar las diferentes opciones del menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            // Si queremos modificar las preferencias
            case R.id.item_preferencias:
                Intent preferencias = new Intent(RegistrarUsuarioActivity.this, SettingActivity.class);
                startActivity(preferencias);
                break;
            // Si queremos volver a la actividad anterior
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return true;
    }
}
