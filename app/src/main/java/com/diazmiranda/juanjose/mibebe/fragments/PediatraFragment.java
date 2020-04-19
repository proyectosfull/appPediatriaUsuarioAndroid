package com.diazmiranda.juanjose.mibebe.fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.diazmiranda.juanjose.mibebe.R;
import com.diazmiranda.juanjose.mibebe.adapters.DoctorAdapter;
import com.diazmiranda.juanjose.mibebe.adapters.DoctorViewHolder;
import com.diazmiranda.juanjose.mibebe.requests.Model.PediatraService;
import com.diazmiranda.juanjose.mibebe.requests.RetrofitRequest;
import com.diazmiranda.juanjose.mibebe.roomdata.entities.Doctor;
import com.diazmiranda.juanjose.mibebe.util.DatosConsulta;
import com.diazmiranda.juanjose.mibebe.util.FragmentInteraction;
import com.diazmiranda.juanjose.mibebe.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PediatraFragment extends FragmentInteraction {

    private static final String TAG = "PediatraFragment";
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private DoctorAdapter adapter;

    public PediatraFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate( R.layout.fragment_pediatra, container, false );

        progressBar = root.findViewById(R.id.progress_bar);
        recyclerView = root.findViewById(R.id.recycler_doctors);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);



        realizarPeticion();

        return root;
    }

    private List<Doctor> getListaDoctores(JSONObject data) throws JSONException {
        JSONArray lista = data.getJSONArray("pediatras");
        List<Doctor> doctores = new ArrayList<>();

        JSONObject object;
        for(int i = 0, length = lista.length(); i < length; i++) {
            object = lista.getJSONObject(i);
            Doctor doctor = new Doctor();

            doctor.setId(object.getInt("id"));
            doctor.setNombre(object.getString( "nombre" ));
            doctor.setApellidos(object.getString( "apellidos" ));
            doctor.setTarifa(object.getString( "tarifa" ));

            object = object.getJSONObject( "lugarAtencion" );
            doctor.setLugarAtencion( object.getString( "tipo" ) );
            doctor.setDireccion( object.getString( "direccion" ) );
            doctor.setLatitud( object.getDouble( "latitud" ) );
            doctor.setLongitud( object.getDouble( "longitud" ) );

            doctores.add( doctor );
        }

        /*for(int i = 0; i < 10; i++){
            Doctor d = new Doctor();
            d.setNombre("Doctor " + i);
            d.setApellidos( "Ape"  + i );
            d.setDireccion( "Norte" );
            d.setDisponible( i % 3 == 0 );
            d.setLugarAtencion( "Casa" );
            doctores.add( d );
        }*/
        return doctores;
    }

    private void showProgressBar(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(!show ? View.VISIBLE : View.GONE);
    }

    private void realizarPeticion() {
        PediatraService service = RetrofitRequest.create(PediatraService.class);
        Call<String> resp = service.getLista();

        resp.enqueue( new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                showProgressBar(false);
                if(response.code() != 200) {
                    Toast.makeText( getActivity(), "Ocurrió un error\n" + response.code() + " " + response.message(), Toast.LENGTH_LONG ).show();
                    return;
                }
                try {
                    JSONObject jresponse = new JSONObject(response.body());
                    if(!jresponse.getBoolean( "OK" )) {
                        Toast.makeText( getActivity(), jresponse.getString("message"), Toast.LENGTH_LONG ).show();
                        return;
                    }
                    List<Doctor> lista = getListaDoctores(jresponse.getJSONObject("data"));
                    adapter = new DoctorAdapter( lista, new DoctorViewHolder.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(Doctor doctor) {

                            if(Util.DATOS == null )
                                Util.DATOS = new DatosConsulta();
                            Util.DATOS.setPediatra( doctor );

                            PediatraFragment.super.mListener.showFloatingButton(true);
                        }
                    } );
                    recyclerView.setAdapter(adapter);
                } catch(JSONException e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_LONG ).show();
            }

            @Override
            protected void finalize() throws Throwable {
                showProgressBar(false);
            }
        } );
    }

}
