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

        /*Se crean las instancias de recyclerView y recyclerAdapter, y se le pasa a este último la lista como parámetro de inicialización.*/
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recAdapter = new RecyclerAdapter(vecinos);

        /*La clase LayoutManager tiene la responsabilidad de manejar la disposición de los elementos
        * en una lista dentro de un RecyclerView. Hay varias opciones disponibles, como GridLayoutManager
        * que los coloca en una vista de rejilla. En este caso se ha utilizado el modo Linear para una
        * disposición básica, con un elemento debajo de otro.*/
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

        /*Finalmente, solo necesitamos agregar los elementos creados previamente a la vista principal (RecyclerView)
        * utilizando sus respectivos métodos.*/
        recyclerView.setAdapter(recAdapter);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(espacioRv);
        new taskConnections().execute("GET","/villagers");
    }

    private ActionMode.Callback mActionCallback = new ActionMode.Callback() {
        /* Se llama cuando se crea un nuevo modo de acción contextual. En este método, se infla el menú
        de acciones action_menu en el modo de acción contextual y se establece su título como "WikiVecinos".*/
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.action_menu, menu);
            actionMode.setTitle("WikiVecinos");
            return  true;
        }

        /*Se llama cada vez que el modo de acción contextual necesita ser actualizado, pero en este caso, simplemente se devuelve false.*/
        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        /*Se llama cuando se hace clic en un elemento del menú de acciones en el modo de acción contextual.
        * En este método, se comprueba si el elemento del menú clicado es el elemento con ID act_bin y,
        * en caso afirmativo, se crea y muestra un diálogo de alerta para confirmar la eliminación de un
        * personaje específico. Además, el modo de acción contextual se finaliza al final de este método.*/
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

        /*Se llama cuando el modo de acción contextual es destruido, y en este caso, se establece la variable mActionMode a null.*/
        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            mActionMode = null;
        }
    };

    //Dado que implica una espera por la respuesta del servidor, esta tarea debe ejecutarse en un hilo secundario.
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

                    /*En este caso, la respuesta que recibimos del servidor es un texto en formato JSON.
                    * Para manejar este tipo de formato, utilizaremos las clases proporcionadas por Android.
                    * Es importante consultar la documentación para entender el formato de la respuesta del
                    * servidor y poder deserializar el mensaje correctamente.*/
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

                        String cumple = cumpleDia + " " + cumpleMes;
                        vecinos.add(new Personaje(img, name, genero, "'" + frase + "'", signo, cumple, personalidad, especie, "'" + muletilla +"'", ropa));
                    }

                    /*Después de obtener los datos y almacenarlos en nuestra colección, es necesario
                    * notificar al adaptador que la información ha sido actualizada.*/
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