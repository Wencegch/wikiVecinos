package com.example.prueba.model;

import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.example.prueba.R;

import java.util.List;

/** Se crea la clase herendando de Adapter que admite un tipo viewHolder necesario para contener los elementos de la vista */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {
    private List<Personaje> listPersonajes;
    private View.OnLongClickListener longListener;
    private View.OnClickListener smallListener;
    private CircularProgressDrawable progressDrawable;

    //Constructor
    public RecyclerAdapter(List<Personaje> listPersonajes) {
        this.listPersonajes = listPersonajes;
    }

    //Setter de los listeners
    public void setOnLongListener(View.OnLongClickListener longListener) {
        this.longListener = longListener;
    }

    public void setOnSmallListener(View.OnClickListener smallListener) {
        this.smallListener = smallListener;
    }

    //Esto llena cada celda del recyclerView con nuestro diseño
    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /**El layoutInflater podría verse como un elemento que se encarga de coger la vista de la celda y anidarla en
         la estructura jerárquica del padre (parent) en este caso responde a la pregunta.
         "Esta celda ¿En qué elemento gráfico de tipo lista va a mostrarse? Una vez hecho eso se pasa al viewHolder
         para que enlace los elementos del layaut con los tipos de datos de cada clase para que puedan ser manipulados en tiempo de ejecución*/
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plantilla_objetos,parent, false);
        RecyclerHolder recyclerHolder = new RecyclerHolder(view);
        view.setOnLongClickListener(longListener);
        view.setOnClickListener(smallListener);
        return recyclerHolder;
    }
    /**Primero se crea la clase que hereda de ViewHolder. Esta clase tiene como finalidad
     recrear los elementos de la vista del layout de cada elemento de la lista acorde al modelo
     de datos creado (en este caso la clase Personajes)*/
    public class RecyclerHolder extends ViewHolder{
        private ImageView imgPersonaje;
        private TextView txtViewNombre;
        private TextView txtViewGenero;
        private TextView txtViewFrase;
        private TextView txtViewSigno;
        private TextView txtViewCumple;

        /**El constructor recibe como parámetro un tipo vista(itemView) que contiene la celda ya creada
         a partir del layout correspondiente.*/
        public RecyclerHolder(@NonNull View itemView){
            super(itemView);
            imgPersonaje = (ImageView)itemView.findViewById(R.id.imagePersonaje);
            txtViewNombre = (TextView) itemView.findViewById(R.id.txtNombre);
            txtViewGenero = (TextView) itemView.findViewById(R.id.txtGeneroPersonaje);
            txtViewFrase = (TextView) itemView.findViewById(R.id.txtFrase);
            txtViewSigno = (TextView) itemView.findViewById(R.id.txtSigno);
            txtViewCumple = (TextView) itemView.findViewById(R.id.txtCumple);
        }
    }

    /**Esta vista se encarga de enlazar la información con cada celda. Este método es llamado una vez se ha
    creado la vista de cada celda de la lista. y lo único que hay que hacer es extraer la información del elemento
    en la lista y asígnarselo a cada elemento gráfico de la celda.*/
    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        Personaje personaje = listPersonajes.get(position);

        //Configuración del CircularProgressDrawable
        progressDrawable = new CircularProgressDrawable(holder.itemView.getContext());
        progressDrawable.setStrokeWidth(10f);
        progressDrawable.setStyle(CircularProgressDrawable.LARGE);
        progressDrawable.setColorFilter(holder.itemView.getResources().getColor(R.color.verde_lima), PorterDuff.Mode.ADD);
        progressDrawable.setCenterRadius(30f);
        progressDrawable.start();

        Glide.with(holder.itemView)
                .load(personaje.getImagenURL())
                .placeholder(progressDrawable)
                .error(R.drawable.imagennoencontrada)
                .into(holder.imgPersonaje);

        //Recogemos los campos del json y los metemos en las etiquetas
        holder.txtViewNombre.setText(personaje.getNombre());
        holder.txtViewGenero.setText(personaje.getGenero());
        holder.txtViewFrase.setText(personaje.getFrase());
        holder.txtViewSigno.setText(personaje.getSigno());
        holder.txtViewCumple.setText(personaje.getCumple());

    }

    @Override
    public int getItemCount() {
        if (listPersonajes == null){
            return 0;
        }
        return listPersonajes.size();
    }

    public Personaje getPersonaje(int pos){
        return this.listPersonajes.get(pos);
    }
}