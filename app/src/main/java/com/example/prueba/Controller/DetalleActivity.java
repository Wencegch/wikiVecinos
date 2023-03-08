package com.example.prueba.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.example.prueba.R;
import com.example.prueba.utils.Preferences;

public class DetalleActivity extends AppCompatActivity {
    private ConstraintLayout constraintLayout;

    private CircularProgressDrawable progressDrawable;

    private ImageView imgDetalle;

    private TextView  nombreDetalle;
    private TextView  generoDetalle;
    private TextView  fraseDetalle;
    private TextView  signoDetalle;
    private TextView  cumpleDetalle;
    private TextView  personalidadDetalle;
    private TextView  especieDetalle;
    private TextView  muletillaDetalle;
    private TextView  ropaDetalle;

    /**
     * Método se llama cuando se crea una nueva instancia de la actividad y se utiliza para realizar tareas
     * @param savedInstanceState almacena y restaura el estado de la actividad en caso de que sea destruida
     *                           y recreada por el sistema operativo.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraint_layout_detalle);

        imgDetalle = (ImageView)findViewById(R.id.imagen_del_detalle);
        nombreDetalle = (TextView)findViewById(R.id.txtNombreDetalle);
        generoDetalle = (TextView)findViewById(R.id.txtGeneroDetalle);
        fraseDetalle = (TextView)findViewById(R.id.txtFraseDetalle);
        signoDetalle = (TextView)findViewById(R.id.txtSignoDetalle);
        cumpleDetalle = (TextView)findViewById(R.id.txtCumpleDetalle);
        personalidadDetalle = (TextView)findViewById(R.id.txtPersonalidadDetalle);
        especieDetalle = (TextView)findViewById(R.id.txtEspecieDetalle);
        muletillaDetalle = (TextView)findViewById(R.id.txtMuletillaDetalle);
        ropaDetalle = (TextView)findViewById(R.id.txtRopaDetalle);

        //Recogemos el intent que lanzamos en la clase RecyclerView con todos sus datos
        Intent i = getIntent();
        nombreDetalle.setText(i.getExtras().getString("nombre"));
        generoDetalle.setText(i.getExtras().getString("genero"));
        fraseDetalle.setText(i.getExtras().getString("frase"));
        signoDetalle.setText(i.getExtras().getString("signo"));
        cumpleDetalle.setText(i.getExtras().getString("cumpleaños"));
        personalidadDetalle.setText(i.getExtras().getString("personalidad"));
        especieDetalle.setText(i.getExtras().getString("especie"));
        muletillaDetalle.setText(i.getExtras().getString("muletilla"));
        ropaDetalle.setText(i.getExtras().getString("ropa"));

        //Configuración del CircularProgressDrawable
        progressDrawable = new CircularProgressDrawable(this);
        progressDrawable.setStrokeWidth(10f);
        progressDrawable.setStyle(CircularProgressDrawable.LARGE);
        progressDrawable.setCenterRadius(30f);
        progressDrawable.start();

        Glide.with(DetalleActivity.this)
                .load(i.getExtras().getString("imagen"))
                .placeholder(progressDrawable)
                .error(R.drawable.imagennoencontrada)
                .into(imgDetalle);

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

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_preferencias:
                Intent i = new Intent(DetalleActivity.this, SettingActivity.class);
                startActivity(i);
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
