package com.diazmiranda.juanjose.mibebe;

import android.app.DatePickerDialog;
import android.content.pm.ActivityInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.diazmiranda.juanjose.mibebe.requests.Model.VacunaService;
import com.diazmiranda.juanjose.mibebe.requests.RetrofitRequest;
import com.diazmiranda.juanjose.mibebe.roomdata.entities.CatVacuna;
import com.diazmiranda.juanjose.mibebe.roomdata.entities.TipoAplicacion;
import com.diazmiranda.juanjose.mibebe.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VacunaActivity extends AppCompatActivity {
    private static final String TAG = "VacunaActivity";

    private Spinner spVacuna;
    private Spinner spAplicacion;
    private EditText edtFecha;
    private EditText edtVacuna;
    private Button btnAgregar;

    private Calendar _calendar;
    private SpinnerAdapter vacunasAdpater;
    private SpinnerAdapter aplicacionAdpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_vacuna );
        setTitle("Datos de Vacunación");

        getVacunas();
        getAplicacion();

        _calendar = Calendar.getInstance();
        startComponents();
        startListeners();
        /*Picasso.get()
                .load( APIUrl.DEPENDIENTE_FOTO_VACUNA + "/" + Constants.currentBaby + "/" + Constants.imagenVacuna)
                .resize( 1000, 0 )
                //.placeholder(R.drawable.user_placeholder)
                //.error(R.drawable.user_placeholder_error)
                .into(imvVacuna);
*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void startComponents() {
        spVacuna = findViewById(R.id.sp_vacuna);
        spAplicacion = findViewById(R.id.sp_aplicacion);
        edtFecha = findViewById(R.id.txt_fecha);
        edtVacuna = findViewById(R.id.txt_otro);
        btnAgregar = findViewById(R.id.btn_agregar);
    }

    public void limpiar() {

        //edtEstatura.getText().clear();
        /*edtPeso.getText().clear();
        edtPerimetroCraneal.getText().clear();*/
        edtFecha.getText().clear();
    }

    private List<CatVacuna> getListaVacunas(JSONObject data) throws JSONException {
        JSONArray lista = data.getJSONArray("vacunas");
        List<CatVacuna> vacunas = new ArrayList<>();

        JSONObject object;
        for(int i = 0, length = lista.length(); i < length; i++) {
            object = lista.getJSONObject(i);
            CatVacuna vacuna = new CatVacuna();

            vacuna.setId(object.getInt("id"));
            vacuna.setNombre(object.getString( "nombre" ));

            vacunas.add( vacuna );
        }
        return vacunas;
    }

    private List<TipoAplicacion> getListaAplicacion(JSONObject data) throws JSONException {
        JSONArray lista = data.getJSONArray("tipo_aplicacion");
        List<TipoAplicacion> aplicaciones = new ArrayList<>();

        JSONObject object;
        for(int i = 0, length = lista.length(); i < length; i++) {
            object = lista.getJSONObject(i);
            TipoAplicacion aplicacion = new TipoAplicacion();

            aplicacion.setId(object.getInt("id"));
            aplicacion.setNombre(object.getString( "aplicacion" ));

            aplicaciones.add( aplicacion );
        }
        return aplicaciones;
    }

    private void startListeners() {
        btnAgregar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtFecha.getText().toString().trim().length() == 0) {
                    Toast.makeText( VacunaActivity.this, "La fecha no puede quedar vacia", Toast.LENGTH_LONG ).show();
                    return;
                }
                try {
                    JSONObject datos = new JSONObject();
                    JSONObject vacuna = new JSONObject();
                    JSONObject aplicacion = new JSONObject();
                    JSONObject dependiente = new JSONObject();

                    int vacunaId = ((CatVacuna) spVacuna.getSelectedItem()).getId();
                    int aplicacionId = ((TipoAplicacion) spAplicacion.getSelectedItem()).getId();


                    vacuna.put("id", vacunaId);
                    datos.put("vacuna", vacuna);

                    aplicacion.put("id", aplicacionId);
                    datos.put("aplicacion", aplicacion);

                    dependiente.put("id", Constants.currentBaby);
                    datos.put("dependiente", dependiente);

                    datos.put("fecha", edtFecha.getText().toString());

                    enviarDatos(datos.toString());

                }catch (JSONException e){
                    Log.e(TAG, e.getMessage());
                }
            }
        } );

        edtFecha.setOnFocusChangeListener( new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if(!isFocused) return;
                //Calendar calendar = GregorianCalendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(VacunaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month++;
                        _calendar.set( year, month, day );
                        String text = String.format( Locale.getDefault(),"%d-%02d-%02d", year, month, day);
                        edtFecha.setText(text);
                    }
                }, _calendar.get( Calendar.YEAR), _calendar.get(Calendar.MONTH) ,_calendar.get(Calendar.DATE));
                datePickerDialog.getDatePicker().setMinDate(Constants.fechaNac.getTime());
                datePickerDialog.show();
            }
        } );

        edtFecha.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Calendar calendar = GregorianCalendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(VacunaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month++;
                        _calendar.set( year, month, day );
                        String text = String.format( Locale.getDefault(),"%d-%02d-%02d", year, month, day);
                        edtFecha.setText(text);
                    }
                }, _calendar.get( Calendar.YEAR ), _calendar.get( Calendar.MONTH ) ,_calendar.get( Calendar.DATE ));
                datePickerDialog.getDatePicker().setMinDate(Constants.fechaNac.getTime());
                datePickerDialog.show();
            }
        } );

        /*spVacuna.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                final boolean show = ((CatVacuna) adapterView.getSelectedItem()).getNombre().equalsIgnoreCase("otro");
                Log.i( TAG, "Se muestra: " + show );
                edtVacuna.setVisibility(show ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        } );*/
    }

    public void enviarDatos(String datos) {
        VacunaService service = RetrofitRequest.create(VacunaService.class);
        RequestBody body = RetrofitRequest.createBody(datos);
        Call<String> resp = service.add(body);

        resp.enqueue( new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.code() != 200) {
                    Toast.makeText( VacunaActivity.this, "Ocurrió un error\n" + response.message(), Toast.LENGTH_LONG ).show();
                    return;
                }

                try {
                    JSONObject jres = new JSONObject(response.body());
                    if(!jres.getBoolean( "OK" )) {
                        Toast.makeText( VacunaActivity.this, jres.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText( VacunaActivity.this, "Datos guardados", Toast.LENGTH_LONG).show();
                    limpiar();
                } catch(JSONException e) {
                    Log.e(TAG, "Error al convertir en json los datos\n" + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText( VacunaActivity.this, t.getMessage(), Toast.LENGTH_LONG ).show();
            }
        } );
    }

    public void getVacunas() {
        VacunaService service = RetrofitRequest.create(VacunaService.class);
        Call<String> resp = service.getVacunas();

        resp.enqueue( new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //showProgressBar(false);
                if(response.code() != 200) {
                    Toast.makeText( VacunaActivity.this, "Ocurrió un error\n" + response.code() + " " + response.message(), Toast.LENGTH_LONG ).show();
                    return;
                }
                try {
                    JSONObject jresponse = new JSONObject(response.body());
                    if(!jresponse.getBoolean( "OK" )) {
                        Toast.makeText( VacunaActivity.this, jresponse.getString("message"), Toast.LENGTH_LONG ).show();
                        return;
                    }
                    List<CatVacuna> lista = getListaVacunas(jresponse.getJSONObject("data"));
                    vacunasAdpater = new ArrayAdapter<>(VacunaActivity.this, android.R.layout.simple_spinner_dropdown_item, lista);
                    spVacuna.setAdapter(vacunasAdpater);
                } catch(JSONException e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText( VacunaActivity.this, t.getMessage(), Toast.LENGTH_LONG ).show();
            }

            @Override
            protected void finalize() throws Throwable {
                //showProgressBar(false);
            }
        } );
    }

    public void getAplicacion() {
        VacunaService service = RetrofitRequest.create(VacunaService.class);
        Call<String> resp = service.getTipoAplicacion();

        resp.enqueue( new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //showProgressBar(false);
                if(response.code() != 200) {
                    Toast.makeText( VacunaActivity.this, "Ocurrió un error\n" + response.code() + " " + response.message(), Toast.LENGTH_LONG ).show();
                    return;
                }
                try {
                    JSONObject jresponse = new JSONObject(response.body());
                    if(!jresponse.getBoolean( "OK" )) {
                        Toast.makeText( VacunaActivity.this, jresponse.getString("message"), Toast.LENGTH_LONG ).show();
                        return;
                    }
                    List<TipoAplicacion> lista = getListaAplicacion(jresponse.getJSONObject("data"));
                    aplicacionAdpater = new ArrayAdapter<>(VacunaActivity.this, android.R.layout.simple_spinner_dropdown_item, lista);
                    spAplicacion.setAdapter(aplicacionAdpater);
                } catch(JSONException e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText( VacunaActivity.this, t.getMessage(), Toast.LENGTH_LONG ).show();
            }

            @Override
            protected void finalize() throws Throwable {
                //showProgressBar(false);
            }
        } );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
