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
import com.diazmiranda.juanjose.mibebe.adapters.VacunaAdapter;
import com.diazmiranda.juanjose.mibebe.requests.Model.VacunaService;
import com.diazmiranda.juanjose.mibebe.requests.RetrofitRequest;
import com.diazmiranda.juanjose.mibebe.roomdata.entities.Vacuna;
import com.diazmiranda.juanjose.mibebe.util.Constants;

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
public class CartillaVacunacionFragment extends Fragment {

    private static final String TAG = "PediatraFragment";
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private VacunaAdapter adapter;

    public CartillaVacunacionFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate( R.layout.fragment_cartilla_vacunacion, container, false );

        progressBar = root.findViewById(R.id.progress_bar);
        recyclerView = root.findViewById(R.id.recycler_vacunas);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);





        return root;
    }

    private List<Vacuna> getLista(JSONObject data) throws JSONException {
        JSONArray lista = data.getJSONArray("vacunas");
        List<Vacuna> vacunas = new ArrayList<>();

        JSONObject object;
        for(int i = 0, length = lista.length(); i < length; i++) {
            object = lista.getJSONObject(i);
            Vacuna vacuna = new Vacuna();

            vacuna.setId(object.getJSONObject("vacuna").getInt("id"));
            vacuna.setNombre(object.getJSONObject("vacuna").getString("nombre"));
            vacuna.setAplicacion(object.getJSONObject("aplicacion").getString("aplicacion"));
            vacuna.setFecha(object.getString("fecha"));

            vacunas.add( vacuna );
        }
        return vacunas;
    }

    private void showProgressBar(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(!show ? View.VISIBLE : View.GONE);
    }

    private void realizarPeticion() {
        VacunaService service = RetrofitRequest.create(VacunaService.class);
        Call<String> resp = service.get( Constants.currentBaby );

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
                    List<Vacuna> lista = getLista(jresponse.getJSONObject("data"));
                    adapter = new VacunaAdapter(lista);
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

    @Override
    public void onResume() {
        super.onResume();
        realizarPeticion();
    }
}
