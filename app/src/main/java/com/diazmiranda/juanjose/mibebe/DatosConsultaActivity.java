package com.diazmiranda.juanjose.mibebe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.diazmiranda.juanjose.mibebe.requests.Model.UsuarioService;
import com.diazmiranda.juanjose.mibebe.requests.RetrofitRequest;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DatosConsultaActivity extends AppCompatActivity {

    private static final String TAG = "DatosConsultaActivity";

    private TextView txvPaciente;
    private TextView txvPesoTemperatura;
    private TextView txvSintomas;
    private TextView txvFechaRegistro;
    private TextView txvPediatra;
    private TextView txvComentario;
    private TextView txvFechaAtendida;
    private Button btnReceta;
    private LinearLayout containerAtendido;


    private int id;
    private String nombre;

    private LinearLayout layoutContent;
    private ConstraintLayout layoutProgress;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Detalles de consulta");
        setContentView(R.layout.activity_datos_consulta);

        startComponents();

        int consultaId = getIntent().getIntExtra("id", -1);
        realizarPeticion(consultaId);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        startListeners();



    }

    private void showProgressDialog(boolean show) {
        layoutProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        layoutContent.setVisibility(!show ? View.VISIBLE : View.GONE);
    }

    private void startComponents() {
        txvPaciente = findViewById(R.id.txv_paciente);
        txvPesoTemperatura = findViewById(R.id.txv_temperatura_peso);
        txvSintomas = findViewById(R.id.txv_sintomas);
        txvFechaRegistro = findViewById(R.id.txv_fecha_registro);
        txvPediatra = findViewById(R.id.txv_pediatra);
        txvComentario = findViewById(R.id.txv_nota);
        txvFechaAtendida = findViewById(R.id.txv_fecha_atendido);
        btnReceta = findViewById(R.id.btn_receta);

        containerAtendido = findViewById(R.id.layout_container_atencion);

        layoutContent = findViewById(R.id.container);
        layoutProgress = findViewById(R.id.progress_bar);
    }

    private void startListeners() {
        btnReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://23.82.16.144/HDK/api/multimedia/receta/" + id + "/" + nombre));
                startActivity(i);
            }
        });
    }

    private void llenarDatos(JSONObject datos) throws JSONException {

        txvPaciente.setText(datos.getJSONObject("dependiente").getString("nombreCompleto"));
        txvPesoTemperatura.setText(datos.getDouble("peso") + " KG / " + datos.getDouble("temperatura") + "°");
        txvSintomas.setText(datos.getString("sintomas"));
        txvFechaRegistro.setText(datos.getString("fecRegistro"));

        if (!datos.getBoolean("atendido")) {
            containerAtendido.setVisibility(View.GONE);
            return;
        }

        txvPediatra.setText(getIntent().getStringExtra("pediatra"));
        txvComentario.setText(datos.has("notaPediatra") ? datos.getString("notaPediatra") : "-");
        txvFechaAtendida.setText(datos.getString("fecAtendido"));

        this.nombre = datos.getString("recetaPdf");
        this.id = datos.getJSONObject("dependiente").getInt("id");
    }

    private void realizarPeticion(int consultaId) {
        UsuarioService service = RetrofitRequest.create(UsuarioService.class);
        Call<String> resp = service.getDetalleConsulta(consultaId);

        resp.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                showProgressDialog(false);
                if (response.code() != 200) {
                    Toast.makeText(DatosConsultaActivity.this, "Ocurrió un error\n" + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    JSONObject jresponse = new JSONObject(response.body());
                    if (!jresponse.getBoolean("OK")) {
                        Toast.makeText(DatosConsultaActivity.this, jresponse.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }

                    JSONObject consulta = jresponse.getJSONObject("data").getJSONObject("consulta");
                    Log.i(TAG, consulta.toString());
                    llenarDatos(consulta);
                } catch (JSONException e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(DatosConsultaActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            protected void finalize() throws Throwable {
                showProgressDialog(false);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
