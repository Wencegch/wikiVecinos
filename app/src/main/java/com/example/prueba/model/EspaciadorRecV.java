package com.example.prueba.model;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EspaciadorRecV extends RecyclerView.ItemDecoration {

    private int espaciado;

    public EspaciadorRecV(int espaciado){
            this.espaciado = espaciado;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.bottom = espaciado;
    }
}
