package com.diazmiranda.juanjose.mibebe.adapters;

import android.graphics.Color;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.diazmiranda.juanjose.mibebe.R;
import com.diazmiranda.juanjose.mibebe.roomdata.entities.Doctor;

public class DoctorViewHolder extends RecyclerView.ViewHolder {
    protected ImageView imgDoctor;
    protected TextView txvNombre;
    protected TextView txvConsulta;
    protected TextView txvDireccion;
    protected TextView txvTarifa;
    protected CardView cardView;
    ImageView imgStatus;
    private OnItemSelectedListener onItemListener;
    Doctor doctor;

    public DoctorViewHolder(View itemView, OnItemSelectedListener listener) {
        super(itemView);
        this.onItemListener = listener;

        this.imgDoctor = itemView.findViewById( R.id.img_doctor );
        this.txvNombre = itemView.findViewById( R.id.txv_nombre );
        this.txvConsulta = itemView.findViewById( R.id.txv_lugar_consulta );
        this.txvDireccion = itemView.findViewById( R.id.txv_direccion );
        this.txvTarifa = itemView.findViewById( R.id.txv_tarifa );
        this.imgStatus = itemView.findViewById( R.id.img_status );
        this.cardView = itemView.findViewById(R.id.card_doctor);
        imgDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChecked(!doctor.isSelected());
                onItemListener.onItemSelected(doctor);
            }
        });
    }

    public void setChecked(boolean checked) {
        doctor.setSelected(checked);
        cardView.setSelected(checked);
        if(checked) {
            imgDoctor.setImageResource(R.drawable.ic_selected);
            imgDoctor.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            cardView.setBackgroundColor(Color.parseColor( "#EEEEEE" ));
        } else {
            imgDoctor.setImageResource(R.drawable.ic_pediatrician);
            imgDoctor.setScaleType(ImageView.ScaleType.CENTER_CROP);
            cardView.setBackground(null);
        }
    }

    public interface OnItemSelectedListener {
        void onItemSelected(Doctor doctor);
    }
}
