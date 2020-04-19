package com.diazmiranda.juanjose.mibebe.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diazmiranda.juanjose.mibebe.R;
import com.diazmiranda.juanjose.mibebe.util.DatosConsulta;

import java.util.List;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialViewHolder> implements HistorialViewHolder.OnItemClickListener {

    private List<DatosConsulta> items;
    private HistorialViewHolder.OnItemClickListener listener;

    public HistorialAdapter(@NonNull List<DatosConsulta> items, HistorialViewHolder.OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HistorialViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.card_historial, viewGroup, false);
        return new HistorialViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull HistorialViewHolder holder, int position) {
        DatosConsulta datos = items.get(position);
        holder.txvNombre.setText(datos.getDependiente().getNombreCompleto());
        holder.txvFecha.setText(datos.getFecRegistro());
        holder.imvAtendida.setImageResource( datos.isAtendido() ? R.drawable.ic_status_online : R.drawable.ic_status_offline );
    }

    @Override
    public void onBindViewHolder(@NonNull HistorialViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder( holder, position, payloads );
        holder.datosConsulta = items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public void onItemClick(DatosConsulta datosConsulta) {
        listener.onItemClick(datosConsulta);
    }
}
