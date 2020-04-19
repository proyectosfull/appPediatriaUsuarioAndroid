package com.diazmiranda.juanjose.mibebe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.diazmiranda.juanjose.mibebe.requests.Model.UsuarioService;
import com.diazmiranda.juanjose.mibebe.requests.RetrofitRequest;
import com.diazmiranda.juanjose.mibebe.util.SharedPreferencesHelper;
import com.diazmiranda.juanjose.mibebe.util.UI;
import com.diazmiranda.juanjose.mibebe.util.Util;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private EditText edtCorreo;
    private EditText edtPassword;
    private TextView txvRegistrar;
    private Button btnLogin;
    private CallbackManager callbackManager;
    private SharedPreferencesHelper sharedPreferences;

    private LoginButton loginButton;

    private ProgressDialog progressDialog;

    private boolean isTokenExpired(String token) {
        try {
            token = token.split(".")[1];
            byte[] btext = Base64.decode(token, Base64.DEFAULT);
            String text = new String(btext, "UTF-8");

            JSONObject data = new JSONObject(text);
            return data.getLong("exp") < System.currentTimeMillis() + 60 * 60;
        } catch(Exception e) {
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = new SharedPreferencesHelper(this);
        Util.TOKEN = sharedPreferences.getToken();
        if((Util.TOKEN != null || AccessToken.getCurrentAccessToken() != null) && !isTokenExpired(Util.TOKEN)) {
            goToMain();
        }
        setTheme( R.style.AppTheme );
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        startComponents();

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String deviceToken = instanceIdResult.getToken();
                sharedPreferences.putFCMToken(deviceToken);
            }
        });


        txvRegistrar.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        loginButton.setOnClickListener(this);

    }

    private void startComponents() {
        edtCorreo = findViewById( R.id.txt_correo );
        edtPassword = findViewById( R.id.txt_password );
        txvRegistrar = findViewById( R.id.txv_registrar );
        btnLogin = findViewById( R.id.btn_login );
        loginButton = findViewById(R.id.loginButton);
    }

    private void goToMain() {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }


    @Override
    public void onClick(View view) {

        switch(view.getId()) {
            case R.id.txv_registrar:
                Intent intent = new Intent( LoginActivity.this,  RegistroActivity.class);
                startActivity( intent );
            break;
            case R.id.btn_login:
                if(!datosValidos()) {
                    Toast.makeText( this, "Faltan datos", Toast.LENGTH_LONG ).show();
                    return;
                }

                try {
                    JSONObject datos = new JSONObject();
                    datos.put("correo", edtCorreo.getText().toString());
                    datos.put("password", edtPassword.getText().toString());
                    datos.put("fcm_token", sharedPreferences.getFCMToken());
                    enviarDatos(datos.toString());
                } catch(JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            break;
            case R.id.loginButton:
                callbackManager = CallbackManager.Factory.create();
                loginButton.setReadPermissions(Arrays.asList("email"));
                loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        if (AccessToken.getCurrentAccessToken() != null) {
                            requestDatos(AccessToken.getCurrentAccessToken());
                        } else {
                            Toast.makeText(getApplicationContext(), "Token vacio", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getApplicationContext(), "Operacion Cancelada", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(getApplicationContext(), "Ocurrio un error conecxion a facebook", Toast.LENGTH_LONG).show();
                        Log.d("Error de facebook", error.toString());
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }



    private boolean datosValidos() {
        return !TextUtils.isEmpty(edtCorreo.getText().toString().trim()) &&
                !TextUtils.isEmpty(edtPassword.getText().toString().trim());
    }

    private void requestDatos(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (response.getError() != null) {
                    Toast.makeText(getApplicationContext(), "Ocurrio un error de datos: "+ response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    JSONObject datos = new JSONObject();
                    datos.put("correo",object.getString("email") );
                    datos.put("nombre", object.getString("first_name"));
                    datos.put("apellidos", object.getString("last_name"));
                    datos.put("fcm_token", sharedPreferences.getFCMToken());

                    Log.d("Datos de Facebook: ", datos.toString());
                    registroFacebook(datos.toString());

                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void registroFacebook(String datos) {
        UsuarioService service = RetrofitRequest.create(UsuarioService.class);
        RequestBody body = RetrofitRequest.createBody(datos);
        Call<String> resp = service.facebookLogin(body);
        progressDialog = UI.showWaitDialog(this);

        resp.enqueue( new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressDialog.dismiss();
                if(response.code() != 200) {
                    Toast.makeText( LoginActivity.this, "Ocurrió un error de envio \n" + response.message(), Toast.LENGTH_LONG ).show();
                    return;
                }

                try {
                    JSONObject jres = new JSONObject(response.body());
                    if(!jres.getBoolean( "OK" )) {
                        Toast.makeText( LoginActivity.this, jres.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }

                    sharedPreferences.putToken(jres.getJSONObject("data").getString("token"));
                    Util.TOKEN = jres.getJSONObject("data").getString("token");
                    Log.i( "TOKEN", Util.TOKEN );
                    goToMain();
                } catch(JSONException e) {
                    Log.e(TAG, "Error al convertir en json los datos\n" + e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText( LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG ).show();
            }
        } );
    }



    private void enviarDatos(String datos) {
            UsuarioService service = RetrofitRequest.create(UsuarioService.class);
            RequestBody body = RetrofitRequest.createBody(datos);
            Call<String> resp = service.login(body);
            progressDialog = UI.showWaitDialog(this);
            resp.enqueue( new Callback<String>() {

                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    progressDialog.dismiss();

                    Log.d("COdigo de respuesta", String.valueOf(response.code()));
                    if(response.code() != 200) {
                        Toast.makeText( LoginActivity.this, "Ocurrió un error\n" + response.message(), Toast.LENGTH_LONG ).show();
                        return;
                    }

                    try {
                        JSONObject jres = new JSONObject(response.body());
                        if(!jres.getBoolean( "OK" )) {
                            Toast.makeText( LoginActivity.this, jres.getString("message"), Toast.LENGTH_LONG).show();
                            Log.i("Error", jres.getString("message"));
                            return;
                        }

                        sharedPreferences.putToken(jres.getJSONObject("data").getString("token"));
                        Util.TOKEN = jres.getJSONObject("data").getString("token");

                        goToMain();
                    } catch(JSONException e) {
                        Log.e(TAG, "Error al convertir en json los datos\n" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText( LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG ).show();
                }
            } );
    }
}
