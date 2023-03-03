package com.example.prueba.model;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
/*La clase "EspaciadorRecV" es una subclase de RecyclerView.ItemDecoration, que se utiliza para agregar
* decoraciones visuales a los elementos en un RecyclerView, como espaciado, bordes o divisores.*/
public class EspaciadorRecV extends RecyclerView.ItemDecoration {

    private int espaciado;

    /**
     * Constructor que recibe un parámetro de "espaciado" que es la cantidad de espacio(en dp) que se desea agregar
     * entre los elementos. La variable "espaciado" se inicializa con este parámetro
     * @param espaciado cantidad de espacio que se agregará al elemento inferior
     */
    public EspaciadorRecV(int espaciado){
            this.espaciado = espaciado;
    }

    /**
     * El método "getItemOffsets" que se encarga de establecer el espaciado entre los elementos en el
     * RecyclerView. En este caso, se establece la distancia del borde inferior del elemento a la cantidad
     * de espacio especificada en la variable "espaciado".
     * @param outRect representa los márgenes que se agregarán a cada elemento del RecyclerView
     * @param view representa la vista del elemento actual que se está procesando.
     * @param parent representa el RecyclerView que contiene los elementos
     * @param state  representa el estado actual del RecyclerView
     */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.bottom = espaciado;
    }
}
