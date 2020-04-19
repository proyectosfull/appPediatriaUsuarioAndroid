package com.diazmiranda.juanjose.mibebe.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diazmiranda.juanjose.mibebe.R;
import com.diazmiranda.juanjose.mibebe.roomdata.entities.Doctor;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorViewHolder> implements DoctorViewHolder.OnItemSelectedListener {

    private final List<Doctor> items;
    private DoctorViewHolder.OnItemSelectedListener listener;

    public DoctorAdapter(@NonNull List<Doctor> items, DoctorViewHolder.OnItemSelectedListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public DoctorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_pediatra, parent, false);
        return new DoctorViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(DoctorViewHolder holder, int position) {
        Doctor doctor = items.get(position);
        //holder.imgDoctor.setText(items.get(position).getAlert());
        holder.txvNombre.setText(doctor.getNombreCompleto());
        holder.txvConsulta.setText(doctor.getLugarAtencion());
        holder.txvDireccion.setText(doctor.getDireccion());
        holder.txvTarifa.setText("MX $" + doctor.getTarifa());
        holder.imgStatus.setImageResource( doctor.isDisponible() ? R.drawable.ic_status_online : R.drawable.ic_status_offline );
        holder.doctor = items.get(position);
        holder.setChecked(doctor.isSelected());
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public void onItemSelected(Doctor doctor) {
        for(Doctor item : items) {
            item.setSelected(false);
        }
        doctor.setSelected(true);
        notifyDataSetChanged();
        listener.onItemSelected(doctor);
    }
}
