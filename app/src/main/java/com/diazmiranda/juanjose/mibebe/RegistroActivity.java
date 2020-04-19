package com.diazmiranda.juanjose.mibebe;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.diazmiranda.juanjose.mibebe.requests.Model.UsuarioService;
import com.diazmiranda.juanjose.mibebe.requests.RetrofitRequest;
import com.diazmiranda.juanjose.mibebe.util.UI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.GregorianCalendar;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroActivity extends AppCompatActivity {
    private static final String TAG = "RegistroActivity";
    private EditText edtNombre;
    private EditText edtApellidos;
    private EditText edtCorreo;
    private EditText edtPassword;
    private EditText edtRePassword;
    private EditText edtFechaNacimiento;
    private Button btnRegistrar;
    private CheckBox checkTerminos;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        startComponents();
        startListeners();

    }

    private void startComponents() {
        edtNombre = findViewById(R.id.txt_nombre);
        edtApellidos = findViewById(R.id.txt_apellidos);
        edtCorreo = findViewById(R.id.txt_correo);
        edtPassword = findViewById(R.id.txt_password);
        edtRePassword = findViewById(R.id.txt_repassword);
        edtFechaNacimiento = findViewById(R.id.txt_fec_nacimiento);
        checkTerminos = findViewById(R.id.check_terminos);
        btnRegistrar = findViewById(R.id.btn_registrar);
    }

    private void startListeners() {

        checkTerminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*AlertDialog alertDialog = new AlertDialog.Builder(RegistroActivity.this).create();
                String str = getString(R.string.terminos);
                final ScrollView s_view = new ScrollView(getApplicationContext());
                final TextView t_view = new TextView(getApplicationContext());
                t_view.setText(Html.fromHtml(str));
                t_view.setTextSize(14);
                s_view.addView(t_view);
                alertDialog.setTitle("Terminos");
                alertDialog.setView(s_view);

                alertDialog.show();*/

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegistroActivity.this);

                final ScrollView s_view = new ScrollView(getApplicationContext());

                final WebView w_view = new WebView(getApplicationContext());
                String texto = getString(R.string.terminos);



                w_view.loadData(texto, "text/html", "utf-8");
                WebSettings webSettings = w_view.getSettings();
                webSettings.setDefaultFontSize(12);
                w_view.setBackgroundColor(Color.TRANSPARENT);
                s_view.addView(w_view);
                s_view.setPadding(20,20,20,20);
                alertDialogBuilder.setView(s_view);
                alertDialogBuilder.setPositiveButton("Acepto",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int arg1) {
                                checkTerminos.setChecked(true);
                                w_view.destroy();
                                dialog.cancel();
                            }
                        });

                //alertDialogBuilder.setTitle("Terminos y Condiciones");

                alertDialogBuilder.setNegativeButton("No acepto",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int arg1) {
                                checkTerminos.setChecked(false);
                                Toast.makeText(RegistroActivity.this, "Debes aceptar terminos y condiciones", Toast.LENGTH_LONG).show();
                                w_view.destroy();
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        edtFechaNacimiento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if (!isFocused) return;

                DatePickerDialog datePickerDialog = new DatePickerDialog(RegistroActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month++;
                        edtFechaNacimiento.setText("" + year + "-" + (month < 10 ? "0" + month : "" + month) + "-" + (day < 10 ? "0" + day : "" + day));
                    }
                }, 2000, 1, 1);
                datePickerDialog.getDatePicker().setMaxDate(GregorianCalendar.getInstance().getTimeInMillis());

                datePickerDialog.show();
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Error", "Entro aqui");
                if (!checkTerminos.isChecked()) {
                    Toast.makeText(RegistroActivity.this, "Debes aceptar terminos y condiciones", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!datosValidos()) {
                    Toast.makeText(RegistroActivity.this, "Faltan datos", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!coincidenContrasenas()) {
                    Toast.makeText(RegistroActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    JSONObject datos = new JSONObject();
                    datos.put("nombre", edtNombre.getText().toString());
                    datos.put("apellidos", edtApellidos.getText().toString());
                    datos.put("correo", edtCorreo.getText().toString());
                    datos.put("password", edtPassword.getText().toString());
                    datos.put("fecha_nacimiento", edtFechaNacimiento.getText().toString());

                    enviarDatos(datos.toString());
                } catch (JSONException e) {
                    Log.i("Error", "Entro aqui 1.1");
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    private boolean datosValidos() {
        return !TextUtils.isEmpty(edtCorreo.getText().toString().trim()) &&
                !TextUtils.isEmpty(edtNombre.getText().toString().trim()) &&
                !TextUtils.isEmpty(edtApellidos.getText().toString().trim()) &&
                !TextUtils.isEmpty(edtPassword.getText().toString().trim()) &&
                !TextUtils.isEmpty(edtRePassword.getText().toString().trim()) &&
                !TextUtils.isEmpty(edtFechaNacimiento.getText().toString().trim());
    }

    private boolean coincidenContrasenas() {
        return TextUtils.equals(edtPassword.getText().toString().trim(), edtRePassword.getText().toString().trim());
    }

    private void goToLogin() {
        onBackPressed();
    }

    private void enviarDatos(String datos) {
        Log.i("Error", "Entro aqui 2");
        UsuarioService service = RetrofitRequest.create(UsuarioService.class);
        RequestBody body = RetrofitRequest.createBody(datos);
        Call<String> resp = service.registrar(body);
        progressDialog = UI.showWaitDialog(this);
        resp.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Error", "Entro aqui 4");
                progressDialog.dismiss();
                if (response.code() != 200) {
                    Log.i("Error", "Diferente a 200");
                    Toast.makeText(RegistroActivity.this, "Ocurrió un error\n" + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    Log.i("Error", "Entro aqui 4.1");

                    JSONObject jres = new JSONObject(response.body());
                    if (!jres.getBoolean("OK")) {
                        Log.i("Error", "Entro aqui 4.2");
                        Toast.makeText(RegistroActivity.this, jres.getString("message"), Toast.LENGTH_LONG).show();
                        Log.i("Error", jres.getString("message") );
                        return;
                    }

                    Toast.makeText(RegistroActivity.this, "Se registró correctamente", Toast.LENGTH_LONG).show();
                    goToLogin();
                } catch (JSONException e) {
                    Log.e(TAG, "Error al convertir en json los datos\n" + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("Error", "Entro aqui 3");
                Toast.makeText(RegistroActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }


}
