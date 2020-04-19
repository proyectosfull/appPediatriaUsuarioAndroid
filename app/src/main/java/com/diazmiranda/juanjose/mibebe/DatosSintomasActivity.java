package com.diazmiranda.juanjose.mibebe;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.diazmiranda.juanjose.mibebe.util.Util;

import java.util.ArrayList;

public class DatosSintomasActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtPeso;
    private EditText edtTemperatura;
    private EditText edtSintomas;
    private ListView lvArchivos;
    private Button btnAgregarArchivos;
    private FloatingActionButton fab;
    private ArrayAdapter<String> adapter;

    public static final int CHOOSE_FILE_REQUESTCODE = 564;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_datos_sintomas );

        startComponents();

        btnAgregarArchivos.setOnClickListener(this);
        fab.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void startComponents() {
        edtPeso = findViewById( R.id.txt_peso );
        edtTemperatura = findViewById( R.id.txt_temperatura );
        edtSintomas = findViewById( R.id.txt_sintomas );
        lvArchivos = findViewById( R.id.list_archivos );
        btnAgregarArchivos = findViewById( R.id.btn_agregar_archivos );
        fab = findViewById(R.id.fab_sig);

        adapter = new ArrayAdapter<>( this, android.R.layout.simple_list_item_1 , new ArrayList<String>());
        lvArchivos.setAdapter( adapter );
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_agregar_archivos:
                //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //intent.setType("file/*");
                //startActivityForResult(intent, PICKFILE_REQUEST_CODE);
                //startActivityForResult(intent, CHOOSE_FILE_REQUESTCODE);
                //openFile("*/*");

                // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
                // browser.
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

                // Filter to only show results that can be "opened", such as a
                // file (as opposed to a list of contacts or timezones)
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                // Filter to show only images, using the image MIME data type.
                // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
                // To search for all documents available via installed storage providers,
                // it would be "*/*".
                intent.setType("*/*");

                startActivityForResult(intent, CHOOSE_FILE_REQUESTCODE);

                break;
            case R.id.fab_sig:
                if(TextUtils.isEmpty( edtSintomas.getText().toString().trim() )) {
                    Toast.makeText( this, "Debes ingresar almenos algún sintoma", Toast.LENGTH_SHORT ).show();
                    return;
                }

                Util.DATOS.setTemperatura( Float.parseFloat( edtTemperatura.getText().toString().trim() ) );
                Util.DATOS.setPeso( Float.parseFloat( edtPeso.getText().toString().trim() ) );
                Util.DATOS.setSintomas( edtSintomas.getText().toString().trim() );

                Intent i = new Intent(DatosSintomasActivity.this, PagoConsultaActivity.class);
                startActivity(i);
                break;
        }
    }

    public void openFile(String mimeType) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(mimeType);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // special intent for Samsung file manager
        Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA_MULTIPLE");
        // if you want any file type, you can skip next line
        sIntent.putExtra("CONTENT_TYPE", mimeType);
        sIntent.addCategory(Intent.CATEGORY_DEFAULT);

        Intent chooserIntent;
        if (getPackageManager().resolveActivity(sIntent, 0) != null){
            // it is device with Samsung file manager
            chooserIntent = Intent.createChooser(sIntent, "Open file");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { intent});
        } else {
            chooserIntent = Intent.createChooser(intent, "Open file");
        }

        try {
            startActivityForResult(chooserIntent, CHOOSE_FILE_REQUESTCODE);
        } catch (android.content.ActivityNotFoundException ex) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("file/*");
            //startActivityForResult(intent, PICKFILE_REQUEST_CODE);
            startActivityForResult(intent, CHOOSE_FILE_REQUESTCODE);
        }
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case CHOOSE_FILE_REQUESTCODE:
                if (data.getData() != null) {
                    try {
                        Uri uri = data.getData();
                        String[] fileInfo = getInfoName( uri );

                        adapter.add( fileInfo[1] );
                        Util.DATOS.addArchivo( uri, fileInfo[1], fileInfo[0] );
                    } catch(Exception e) {
                    }

                }
                break;
        }
        adapter.notifyDataSetChanged();

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
