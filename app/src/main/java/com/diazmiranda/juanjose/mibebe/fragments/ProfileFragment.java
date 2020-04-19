package com.diazmiranda.juanjose.mibebe.fragments;


import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.diazmiranda.juanjose.mibebe.R;
import com.diazmiranda.juanjose.mibebe.requests.Model.UsuarioService;
import com.diazmiranda.juanjose.mibebe.requests.RetrofitRequest;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private EditText edtPasswordAcual;
    private EditText edtPasswordNuevo;
    private Button btnCambiarContraseña;

    private View root;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_profile, container, false);

        startComponents(root);
        startListeners();

        return root;

    }

    private void startComponents(View root) {
        edtPasswordAcual = root.findViewById(R.id.txt_pass_actual);
        edtPasswordNuevo = root.findViewById(R.id.txt_pass_nueva);
        btnCambiarContraseña = root.findViewById(R.id.btn_cambiar_pass);

    }

    private void startListeners() {
        btnCambiarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject data = new JSONObject();
                    data.put("current_password", edtPasswordAcual.getText().toString());
                    data.put("new_password", edtPasswordNuevo.getText().toString());
                    enviarDatos(data.toString());
                } catch (JSONException e) {
                    Log.e(TAG, "Error al convertir en json los datos\n" + e.getMessage());
                }

                closeKeyboard();

            }
        });
    }

    private void closeKeyboard() {
        View view = root;
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void limpiar() {
        edtPasswordNuevo.getText().clear();
        edtPasswordAcual.getText().clear();
    }

    public void enviarDatos(String datos) {
        UsuarioService service = RetrofitRequest.create(UsuarioService.class);
        RequestBody body = RetrofitRequest.createBody(datos);
        Call<String> resp = service.actulizarContraseña(body);
        resp.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() != 200) {
                    Toast.makeText(getActivity(), "Ocurrió un error\n" + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    JSONObject jres = new JSONObject(response.body());
                    if (!jres.getBoolean("OK")) {
                        Toast.makeText(getActivity(), jres.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                    limpiar();
                    Toast.makeText(getActivity(), "Se actualizo la contraseña correctamente", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    Log.e(TAG, "Error al convertir en json los datos\n" + e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}
