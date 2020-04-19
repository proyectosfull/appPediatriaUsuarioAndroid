package com.diazmiranda.juanjose.mibebe;

import android.app.DatePickerDialog;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.diazmiranda.juanjose.mibebe.requests.Model.UsuarioService;
import com.diazmiranda.juanjose.mibebe.requests.RetrofitRequest;
import com.diazmiranda.juanjose.mibebe.roomdata.AppDatabase;
import com.diazmiranda.juanjose.mibebe.roomdata.entities.BabyData;
import com.diazmiranda.juanjose.mibebe.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DatosActivity extends AppCompatActivity {

    private EditText edtPeso;
    private EditText edtEstatura;
    private EditText edtPerimetroCraneal;
    private EditText edtFecha;
    private Button btnAgregar;

    private static final String TAG = "DatosActivity";

    private Calendar _calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setTitle("Datos de crecimiento");
        setContentView( R.layout.activity_datos );
        _calendar = Calendar.getInstance();
        startComponents();
        startListeners();
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void startComponents() {
        edtPeso = findViewById( R.id.txt_peso );
        edtEstatura = findViewById( R.id.txt_estatura );
        edtPerimetroCraneal = findViewById(R.id.txt_perimetroCraneal);
        edtFecha = findViewById( R.id.txt_fecha );
        btnAgregar = findViewById( R.id.btn_agregar );
    }

    private void startListeners() {
        btnAgregar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject datos = new JSONObject();
                    JSONObject dependiente = new JSONObject();


                    datos.put("peso", Float.parseFloat(edtPeso.getText().toString()));
                    datos.put("talla", Float.parseFloat(edtEstatura.getText().toString()));
                    datos.put("perimetroCraneal", Float.parseFloat((edtPerimetroCraneal.getText().toString())));
                    datos.put("fechaRegistro", edtFecha.getText().toString());

                    dependiente.put("id", Constants.currentBaby);
                    datos.put("dependiente", dependiente);

                    enviarDatos(datos.toString());

                }catch (JSONException e){
                    Log.e(TAG, e.getMessage());
                }
                //insertar();
            }
        } );

        edtFecha.setOnFocusChangeListener( new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if(!isFocused) return;
                //Calendar calendar = GregorianCalendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(DatosActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month++;
                        _calendar.set( year, month, day );
                        String text = String.format(Locale.getDefault(),"%d-%02d-%02d", year, month, day);
                        edtFecha.setText(text);
                    }
                }, _calendar.get(Calendar.YEAR), _calendar.get(Calendar.MONTH) ,_calendar.get(Calendar.DATE));
                datePickerDialog.getDatePicker().setMinDate(Constants.fechaNac.getTime());
                //datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

                datePickerDialog.show();
            }
        } );

        edtFecha.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Calendar calendar = GregorianCalendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(DatosActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month++;
                        _calendar.set( year, month, day );
                        String text = String.format( Locale.getDefault(),"%d-%02d-%02d", year, month, day);
                        edtFecha.setText(text);
                    }
                }, _calendar.get( Calendar.YEAR ), _calendar.get( Calendar.MONTH ) ,_calendar.get( Calendar.DATE ));
                datePickerDialog.getDatePicker().setMinDate(Constants.fechaNac.getTime());
                //datePickerDialog.getDatePicker().setMaxDate( calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        } );
    }

    public void enviarDatos(String datos) {
        UsuarioService service = RetrofitRequest.create(UsuarioService.class);
        RequestBody body = RetrofitRequest.createBody(datos);
        Call<String> resp = service.registroCrecimiento(body);

        resp.enqueue( new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.code() != 200) {
                    Toast.makeText( DatosActivity.this, "Ocurrió un error\n" + response.message(), Toast.LENGTH_LONG ).show();
                    return;
                }

                try {
                    JSONObject jres = new JSONObject(response.body());
                    if(!jres.getBoolean( "OK" )) {
                        Toast.makeText( DatosActivity.this, jres.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText( DatosActivity.this, "Datos guardados", Toast.LENGTH_LONG).show();
                    limpiar();
                } catch(JSONException e) {
                    Log.e(TAG, "Error al convertir en json los datos\n" + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText( DatosActivity.this, t.getMessage(), Toast.LENGTH_LONG ).show();
            }
        } );
    }

    public void limpiar() {
        edtEstatura.getText().clear();
        edtPeso.getText().clear();
        edtPerimetroCraneal.getText().clear();
        edtFecha.getText().clear();
    }

    private static class BabyDataAsync extends AsyncTask<BabyData, Void, BabyData> {

        private WeakReference<DatosActivity> activityReference;

        public BabyDataAsync(DatosActivity context) {
            activityReference = new WeakReference<>(context);
        }
        @Override
        protected BabyData doInBackground(BabyData... babyData) {
            BabyData data = babyData[0];
            try {
                AppDatabase.getInstance(activityReference.get()).babyData().insert(data);
            } catch(Exception e) {
                Log.i("EROOOOOOOOOOOR", e.getMessage());
                return null;
            }
            return data;
        }

        @Override
        protected void onPostExecute(BabyData data) {
            super.onPostExecute( data );
            /**
             * Peso inferior al normal	Menos de 18.5
             * Normal	18.5 – 24.9
             * Peso superior al normal	25.0 – 29.9
             * Obesidad	Más de 30.0
             */
            float talla = data.getTalla() / 100;
            float imc = data.getPeso() / (talla * talla);
            String message = "";
            if(imc < 18.5) {
                message = "Peso inferior al normal";
            } else if(imc >= 18.5 && imc <= 24.9) {
                message = "Peso normal";
            } else if(imc >= 25 && imc <= 29.9) {
                message = "Peso superior al normal";
            } else if(imc >= 30) {
                message = "Obesidad";
            }
            Toast.makeText( activityReference.get(), message, Toast.LENGTH_LONG ).show();
            activityReference.get().finish();
        }
    }
}
