package com.diazmiranda.juanjose.mibebe.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diazmiranda.juanjose.mibebe.R;
import com.diazmiranda.juanjose.mibebe.roomdata.entities.Vacuna;

import java.util.List;

public class VacunaAdapter  extends RecyclerView.Adapter<VacunaAdapter.VacunaViewHolder> {

    private final List<Vacuna> items;

    public VacunaAdapter(@NonNull List<Vacuna> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public VacunaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.card_vacunas, parent, false);
        return new VacunaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VacunaViewHolder holder, int position) {
        Vacuna vacuna = items.get(position);
        holder.txvVacuna.setText(vacuna.getNombre());
        holder.txvAplicacion.setText(vacuna.getAplicacion());
        holder.txvFecha.setText(vacuna.getFecha());
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public List<Vacuna> getItems() {
        return this.items;
    }

    class VacunaViewHolder extends RecyclerView.ViewHolder {

        TextView txvVacuna;
        TextView txvAplicacion;
        TextView txvFecha;

        public VacunaViewHolder(@NonNull View view) {
            super( view );

            txvVacuna = view.findViewById( R.id.txv_valor_vacuna );
            txvAplicacion = view.findViewById( R.id.txv_valor_aplicacion );
            txvFecha = view.findViewById( R.id.txv_valor_fecha );
        }
    }
}
