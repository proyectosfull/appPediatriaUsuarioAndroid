package com.diazmiranda.juanjose.mibebe;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.diazmiranda.juanjose.mibebe.util.DatosConsulta;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.diazmiranda.juanjose.mibebe.requests.Model.ConsultaService;
import com.diazmiranda.juanjose.mibebe.requests.RetrofitRequest;
import com.diazmiranda.juanjose.mibebe.roomdata.entities.Dependiente;
import com.diazmiranda.juanjose.mibebe.util.Archivo;
import com.diazmiranda.juanjose.mibebe.util.Constants;
import com.diazmiranda.juanjose.mibebe.util.Util;
import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagoConsultaActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "PagoConsultaActivity";
    public static final int CHOOSE_FILE_REQUESTCODE = 54;

    private Button btnComprobante;
    private Button btnPaypal;
    private TextView txvFile;
    private FloatingActionButton fab;

    private ConstraintLayout layoutContent;
    private ConstraintLayout layoutProgress;

    //Paypal
    public static final int PAYPAL_REQUEST_CODE = 7171;
    private PayPalConfiguration config;
    private String amount = "0";
    private static final String PAYMENT_STATE_APPROVED = "approved";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_pago_consulta );

        startComponents();

        fab.setOnClickListener(this);
        btnComprobante.setOnClickListener(this);
        btnPaypal.setOnClickListener(this);

        config = new PayPalConfiguration()
        .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
        .clientId(getResources().getString( R.string.client_id ));

        amount = Util.DATOS.getPediatra().getTarifa();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void startComponents() {
        fab = findViewById(R.id.fab_enviar);
        btnComprobante = findViewById( R.id.btn_comprobante_pago );
        btnPaypal = findViewById( R.id.btn_paypal_pago );
        txvFile = findViewById( R.id.txv_archivo );
        layoutContent = findViewById( R.id.constraint_content );
        layoutProgress = findViewById( R.id.progess_content );
    }

    private void showProgressDialog(boolean show) {
        layoutProgress.setVisibility( show ? View.VISIBLE : View.GONE );
        layoutContent.setVisibility( !show ? View.VISIBLE : View.GONE );
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_paypal_pago:
                PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "MXN",
                        "Pago de consulta", PayPalPayment.PAYMENT_INTENT_SALE);
                /*Intent intent = new Intent(this, PaymentActivity.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
                startActivityForResult(intent, PAYPAL_REQUEST_CODE);*/
                startActivityForResult( new Intent( PagoConsultaActivity.this, PaymentActivity.class )
                        .putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)
                        .putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment),
                        PAYPAL_REQUEST_CODE
                );
                break;
            case R.id.btn_comprobante_pago:
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, CHOOSE_FILE_REQUESTCODE);
                break;
            case R.id.fab_enviar:
                if(Util.DATOS.getComprobantePago() != null ){
                    Dependiente d = new Dependiente();
                    d.setId( Constants.currentBaby );
                    Util.DATOS.setDependiente( d );

                    String data = new Gson().toJson( Util.DATOS );
                    enviarDatos( data );
                }else{
                    Toast.makeText( PagoConsultaActivity.this, "Debes agregar un comprobante de pago", Toast.LENGTH_SHORT ).show();
                }
                break;

        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode== REQUEST_CODE){
            if(resultCode==RESULT_OK)
            {
                DropInResult result=data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce= result.getPaymentMethodNonce();
                String strNounce=nonce.getNonce();
                Log.i( TAG, strNounce );
                }
                else {
                    Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                }
            }
            else if(resultCode==RESULT_CANCELED)
            {
                Toast.makeText(this, "User canceled", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Exception error=(Exception)data.getSerializableExtra( DropInActivity.EXTRA_ERROR);
                Log.d("Err",error.toString());
            }
        }
    }*/

    public String[] getInfoName(Uri uri) {
        String[] result = new String[2];
        if (uri.getScheme().equals( ContentResolver.SCHEME_CONTENT)) {
            String[] projection = { MediaStore.MediaColumns.MIME_TYPE, MediaStore.MediaColumns.DISPLAY_NAME };
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {

                    String displayName = cursor.getString(cursor.getColumnIndex(projection[1]));
                    int indexOfDot = displayName.lastIndexOf(".");

                    result[0] = cursor.getString(cursor.getColumnIndex(projection[0]));
                    result[1] = indexOfDot != -1 ? displayName.substring(0, indexOfDot) : displayName;
                    result[1] = result[1] + "." + MimeTypeMap.getSingleton().getExtensionFromMimeType( result[0] );
                }
            } finally {
                cursor.close();
            }
        }
        return result;
    }

    public RequestBody createPartFromString(String param) {
        return RequestBody.create( MultipartBody.FORM, param );
    }

    public MultipartBody.Part prepareFilePart(String partName, Archivo archivo) {
        RequestBody requestBody = RequestBody.create(
                MediaType.parse(archivo.getMediaType()),
                archivo.getFile()
        );
        return MultipartBody.Part.createFormData( partName, archivo.getNombre(), requestBody );
    }

    private void enviarDatos(String data) {
        Util.DATOS.loadFiles( getApplicationContext() );
        List<MultipartBody.Part> parts = new ArrayList<>();
        for(int i = 0; i < Util.DATOS.getArchivos().size(); i++) {
            parts.add( prepareFilePart("files", Util.DATOS.getArchivos().get(i)));
        }

        ConsultaService service = RetrofitRequest.create(ConsultaService.class);
        Call<String> resp;
        if(Util.DATOS.getComprobantePago() != null) {
            resp = service.insert(
                    createPartFromString( data ),
                    prepareFilePart("pago", Util.DATOS.getComprobantePago()),
                    parts
            );
        } else {
            resp = service.insert(
                    createPartFromString( data ),
                    parts
            );
        }
        showProgressDialog(true);
        resp.enqueue( new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                showProgressDialog(false);
                if(response.code() != 200) {
                    Toast.makeText( PagoConsultaActivity.this, "Ocurrió un error\n" + response.code(), Toast.LENGTH_LONG ).show();
                    return;
                }
                try {
                    JSONObject jresponse = new JSONObject(response.body());
                    txvFile.setText(jresponse.toString());
                    if(!jresponse.getBoolean( "OK" )) {
                        Toast.makeText( PagoConsultaActivity.this, jresponse.getString("message"), Toast.LENGTH_LONG ).show();
                        return;
                    }

                    //Util.DATOS.limpiarCache();
                    Util.DATOS = null;

                    Toast.makeText( PagoConsultaActivity.this, "Consulta enviada", Toast.LENGTH_SHORT ).show();

                    Intent i = new Intent(PagoConsultaActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } catch(JSONException e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showProgressDialog(false);
                Toast.makeText( PagoConsultaActivity.this, t.getMessage(), Toast.LENGTH_LONG ).show();
            }

            @Override
            protected void finalize() throws Throwable {
                super.finalize();
            }
        } );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, PayPalService.class));
        //Util.DATOS.limpiarCache();
        Util.DATOS = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        switch (requestCode) {
            case CHOOSE_FILE_REQUESTCODE:
                if (resultCode != RESULT_OK)
                    return;
                if (data.getData() != null) {
                    try {
                        Uri uri = data.getData();
                        String[] fileInfo = getInfoName( uri );

                        txvFile.setText( fileInfo[1] );
                        Archivo archivo = new Archivo( uri, fileInfo[1], fileInfo[0] );
                        Util.DATOS.setComprobantePago( archivo );
                    } catch (Exception e) {
                    }

                }
                break;
            case PAYPAL_REQUEST_CODE:
                if (resultCode != RESULT_OK)
                    return;
                PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (paymentConfirmation == null)
                    return;

                if(paymentConfirmation.getProofOfPayment().getState().equalsIgnoreCase( PAYMENT_STATE_APPROVED)) {
                    Dependiente d = new Dependiente();
                    d.setId( Constants.currentBaby );
                    Util.DATOS.setDependiente( d );

                    DatosConsulta.Paypal paypal = new DatosConsulta.Paypal();
                    paypal.setPaymentId(paymentConfirmation.getProofOfPayment().getPaymentId());
                    paypal.setTransactionId(paymentConfirmation.getProofOfPayment().getTransactionId());
                    paypal.setState(paymentConfirmation.getProofOfPayment().getState());
                    paypal.setCreateTime(paymentConfirmation.getProofOfPayment().getCreateTime());
                    paypal.setAmount(amount);
                    Util.DATOS.setPaypal(paypal);

                    String jsonData = new Gson().toJson( Util.DATOS );

                    enviarDatos( jsonData );
                } else {
                    Toast.makeText( this, "Pago no aprobado: " + paymentConfirmation.getProofOfPayment().getState(), Toast.LENGTH_LONG ).show();
                }
                       /* startActivity(new Intent(this, PaymentDetails.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", amount)
                        );*/
                break;
        }
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

