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

/*Creamos la clase herendando de Adapter que admite un tipo viewHolder necesario para contener los elementos de la vista */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {
    private List<Personaje> listPersonajes;
    private View.OnLongClickListener longListener;
    private View.OnClickListener smallListener;
    private CircularProgressDrawable progressDrawable;

    /**
     * Constructor
     * @param listPersonajes lista de donde saca la información
     */
    public RecyclerAdapter(List<Personaje> listPersonajes) {
        this.listPersonajes = listPersonajes;
    }

    //Esto llena cada celda del recyclerView con nuestro diseño
    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*El LayoutInflater es responsable de tomar la vista de la celda y agregarla a la estructura
        * jerárquica del padre (también conocido como "parent"). En este caso, su función es determinar
        * en qué elemento gráfico de tipo lista se mostrará la celda. Una vez completada esta tarea, el
        * ViewHolder entra en acción y enlaza los elementos del layout con los tipos de datos correspondientes
        * de cada clase, lo que permite que sean manipulados en tiempo de ejecución.*/
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plantilla_objetos,parent, false);
        RecyclerHolder recyclerHolder = new RecyclerHolder(view);
        view.setOnLongClickListener(longListener);
        view.setOnClickListener(smallListener);
        return recyclerHolder;
    }
    /*Se crea la clase heredando de ViewHolder, que permitirá recrear los elementos de la vista del layaout de cada elemento
    * de la lista según el modelo de datos que hemos creado, mi clase aquí es la clase Personajes*/
    public class RecyclerHolder extends ViewHolder{
        private ImageView imgPersonaje;
        private TextView txtViewNombre;
        private TextView txtViewGenero;
        private TextView txtViewFrase;
        private TextView txtViewSigno;
        private TextView txtViewCumple;

        /*El constructor recibe como parámetro un tipo vista(itemView) que contiene la celda ya creada
        * a partir del layout correspondiente.*/
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

    /*Esta vista se encarga de enlazar la información con cada celda, el método se llama cuando se ha
    * creado la vista de cada celda de la lista y solo se tendría que recoger la información de cada elemendo de la lista
    * y asignarlo a cada elemento gráfico de la celda*/
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

    //Setter de los listeners
    public void setOnLongListener(View.OnLongClickListener longListener) {
        this.longListener = longListener;
    }

    public void setOnSmallListener(View.OnClickListener smallListener) {
        this.smallListener = smallListener;
    }
}