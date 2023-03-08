package com.example.prueba.Controller;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.prueba.R;
import com.example.prueba.fragments.SettingsFragment;
import com.example.prueba.utils.Preferences;

public class SettingActivity extends AppCompatActivity {
    // Declaración de variables
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        constraintLayout = findViewById(R.id.layout_setting_preferences);

        // Cargamos las preferencias
        Preferences.loadPreferences(this, constraintLayout);
        // Introducimos el fragment creado en el contenedor de la vista padre
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_setting_preferences, new SettingsFragment())
                .commit();

        // Activamos el icono de "Volver"
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    // Asociamos un oyente al evento de pulsar el icono de volver
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
