package com.example.prueba.Controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import android.view.ActionMode;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prueba.IO.HttpConnectAnimalCrossing;
import com.example.prueba.R;
import com.example.prueba.model.EspaciadorRecV;
import com.example.prueba.model.Personaje;
import com.example.prueba.model.RecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Rest extends AppCompatActivity {

    private ArrayList<Personaje> vecinos = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerAdapter recAdapter;
    private EspaciadorRecV espacioRv = new EspaciadorRecV(10);
    private int posicionPulsada;
    private ActionMode mActionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maestro);

        /**Se inicializan los objetos recyclerView y recyclerAdapter, pasandole a este último la lista*/
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recAdapter = new RecyclerAdapter(vecinos);

        /**La clase layaoutManager se encarga de gestionar la disposición de los elementos de la lista dentro del recyclerView.
         * Existen diferentes opciones, como gridLayoutManager que los coloca en vista de rejilla.
         * Para este caso se ha usado Linear, para una disposición básica (un elemento debajo de otro).*/
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recAdapter.setOnSmallListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posicionPulsada = recyclerView.getChildAdapterPosition(view);
                Personaje personaje = recAdapter.getPersonaje(posicionPulsada);
                Intent i = new Intent(view.getContext(), Detalle.class);
                //Añadimos información adicional al intent en forma de clave-valor, la clave la cadena y el valor el tipo de dato
                i.putExtra("imagen",personaje.getImagenURL());
                i.putExtra("nombre",personaje.getNombre());
                i.putExtra("genero",personaje.getGenero());
                i.putExtra("frase",personaje.getFrase());
                i.putExtra("signo",personaje.getSigno());
                i.putExtra("cumpleaños",personaje.getCumple());
                i.putExtra("personalidad",personaje.getPersonalidad());
                i.putExtra("especie",personaje.getEspecie());
                i.putExtra("muletilla",personaje.getMuletilla());
                i.putExtra("ropa",personaje.getRopa());

                view.getContext().startActivity(i);
            }
        });

        recAdapter.setOnLongListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                posicionPulsada = recyclerView.getChildAdapterPosition(view);
                mActionMode = startActionMode(mActionCallback);
                view.setSelected(true);
                return true;
            }
        });

        /**Por último solo debemos añadir los elementos creados anteriormente a la vista padre (RecyclerView)
         * con sus respectivos métodos.*/
        recyclerView.setAdapter(recAdapter);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(espacioRv);
        new taskConnections().execute("GET","/villagers");
    }

    private ActionMode.Callback mActionCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.action_menu, menu);
            actionMode.setTitle("WikiVecinos");
            return  true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            int itemId = menuItem.getItemId();
            if(itemId == R.id.act_bin){
                Personaje personaje = recAdapter.getPersonaje(posicionPulsada);

                AlertDialog alertDialog = createAlertDialog("Eliminar", "¿Quieres eliminar a "+ personaje.getNombre() + "?");
                alertDialog.show();

                actionMode.finish();
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            mActionMode = null;
        }
    };

    /**Al ser una tarea que implica una espera, como es la respuesta del servidor, se tiene que llevar
     * a cabo a través de un hilo secundario.*/
    private class taskConnections extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String result = null;

            switch (strings[0]){
                case "GET":
                    result = HttpConnectAnimalCrossing.getRequest(strings[1]);
                    break;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if(s != null){
                    Log.d("D","DATOS: " + s);

                    /** La respuesta que nos devuelve es un texto en formato JSON. Para ello, en este caso,
                     * haremos uso de las clases que nos proporciona Android. Antes que nada, se deberá consultar
                     * la documentación para conocer el formato de la respuesta del servidor, y así saber cómo deserializar el mensaje.*/
                    JSONArray jsonArray = new JSONArray(s);

                    for(int i = 0; i < jsonArray.length(); i++){
                        String img = jsonArray.getJSONObject(i).getString("image_url");
                        String name = jsonArray.getJSONObject(i).getString("name");
                        String genero = jsonArray.getJSONObject(i).getString("gender");
                        String frase = jsonArray.getJSONObject(i).getString("phrase");
                        String signo = jsonArray.getJSONObject(i).getString("sign");
                        String cumpleDia = jsonArray.getJSONObject(i).getString("birthday_day");
                        String cumpleMes = jsonArray.getJSONObject(i).getString("birthday_month");
                        String personalidad = jsonArray.getJSONObject(i).getString("personality");
                        String especie = jsonArray.getJSONObject(i).getString("species");
                        String muletilla = jsonArray.getJSONObject(i).getString("quote");
                        String ropa = jsonArray.getJSONObject(i).getString("clothing");

                        //String cumple = cumpleDia + " " + cumpleMes;
                        vecinos.add(new Personaje(img, name, genero, "'" + frase + "'", signo, cumpleDia + " " + cumpleMes, personalidad, especie, "'" + muletilla +"'", ropa));
                    }

                    /** Una vez tenemos los datos en nuestra colección debemos avisar al adaptador que la información ha cambiado.*/
                    recAdapter.notifyDataSetChanged();
                    Log.d("D","Array: " + recAdapter.toString());
                }else{
                    Toast.makeText(Rest.this, "Problema al cargar los datos", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public AlertDialog createAlertDialog(String titulo, String mensaje){
        //Constructor para ayudar a configurar el cuadro de dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(Rest.this);

        //Permite añadir todas las configuraciones que se necesiten antes de crear el alertdialog
        builder.setMessage(mensaje).setTitle(titulo);

        //Boton para el sí
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Personaje personaje = recAdapter.getPersonaje(posicionPulsada);
                vecinos.remove(personaje);
                recAdapter.notifyItemRemoved(posicionPulsada);

                Toast.makeText(Rest.this," Has pulsado sí", Toast.LENGTH_SHORT).show();
            }
        });

        //Botón para el no
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Rest.this,"Has pulsado no", Toast.LENGTH_SHORT).show();
            }
        });
        //Una vez añadidas las configuraciones creamos el alertDialog, y devolvemos el objeto creado
        return builder.create();
    }
}