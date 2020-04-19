package com.diazmiranda.juanjose.mibebe.fragments;


import android.content.Intent;
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

import com.diazmiranda.juanjose.mibebe.DatosConsultaActivity;
import com.diazmiranda.juanjose.mibebe.R;
import com.diazmiranda.juanjose.mibebe.adapters.HistorialAdapter;
import com.diazmiranda.juanjose.mibebe.adapters.HistorialViewHolder;
import com.diazmiranda.juanjose.mibebe.requests.Model.UsuarioService;
import com.diazmiranda.juanjose.mibebe.requests.RetrofitRequest;
import com.diazmiranda.juanjose.mibebe.util.DatosConsulta;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private static final String TAG = "PediatraFragment";
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private HistorialAdapter adapter;

    public HistoryFragment() {
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

    private void showProgressBar(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(!show ? View.VISIBLE : View.GONE);
    }

    private void realizarPeticion() {
        UsuarioService service = RetrofitRequest.create(UsuarioService.class);
        Call<String> resp = service.getHistorialConsulta();

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
                    String datos = jresponse.getJSONObject("data").getJSONArray("consultas").toString();
                    List<DatosConsulta> listaDatos = new Gson().fromJson(datos, new TypeToken<List<DatosConsulta>>(){}.getType());
                    adapter = new HistorialAdapter( listaDatos, new HistorialViewHolder.OnItemClickListener() {
                        @Override
                        public void onItemClick(DatosConsulta datosConsulta) {
                            Intent intent = new Intent(getActivity(), DatosConsultaActivity.class);
                            intent.putExtra("id", datosConsulta.getId());
                            intent.putExtra("pediatra", datosConsulta.getPediatra().getNombreCompleto());
                            startActivity(intent);
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
