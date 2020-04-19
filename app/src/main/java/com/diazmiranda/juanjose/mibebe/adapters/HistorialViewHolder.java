package com.diazmiranda.juanjose.mibebe.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.diazmiranda.juanjose.mibebe.R;
import com.diazmiranda.juanjose.mibebe.util.DatosConsulta;

public class HistorialViewHolder extends RecyclerView.ViewHolder {
    protected TextView txvNombre;
    protected TextView txvFecha;
    protected ImageView imvAtendida;
    private OnItemClickListener listener;
    DatosConsulta datosConsulta;

    public HistorialViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super( itemView );
        txvNombre = itemView.findViewById( R.id.txv_nombre);
        txvFecha = itemView.findViewById(R.id.txv_fecha_consulta);
        imvAtendida = itemView.findViewById(R.id.img_status);
        this.listener = listener;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistorialViewHolder.this.listener.onItemClick(datosConsulta);
            }
        });
    }

    public interface OnItemClickListener {
        void onItemClick(DatosConsulta datosConsulta);
    }

}