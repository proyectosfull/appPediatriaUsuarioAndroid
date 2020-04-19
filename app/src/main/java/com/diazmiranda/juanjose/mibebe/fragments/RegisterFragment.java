package com.diazmiranda.juanjose.mibebe.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.diazmiranda.juanjose.mibebe.R;
import com.diazmiranda.juanjose.mibebe.requests.Model.UsuarioService;
import com.diazmiranda.juanjose.mibebe.requests.RetrofitRequest;
import com.diazmiranda.juanjose.mibebe.util.SharedPreferencesHelper;
import com.diazmiranda.juanjose.mibebe.util.UI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    private static final String TAG = "RegisterFragment";

    private EditText edtNombre;
    private EditText edtApellidos;
    private EditText edtFechaNacimiento;
    private RadioGroup rdgRelacion;
    private RadioGroup rdgSexo;
    private EditText edtNotas;
    private Button btnRegistrar;
    private SharedPreferencesHelper sharedPreferences;

    private ProgressDialog progressDialog;
    private View root;
    private Calendar calendar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString( ARG_PARAM1, param1 );
        args.putString( ARG_PARAM2, param2 );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        if (getArguments() != null) {
            mParam1 = getArguments().getString( ARG_PARAM1 );
            mParam2 = getArguments().getString( ARG_PARAM2 );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate( R.layout.fragment_register, container, false );

        calendar = Calendar.getInstance();
        startComponents( root );
        startListeners();


        return root;
    }

    public void startComponents(View root) {
        edtNombre = root.findViewById( R.id.txt_nombre );
        edtApellidos = root.findViewById( R.id.txt_apellidos );
        edtFechaNacimiento = root.findViewById( R.id.txt_fec_nacimiento );
        rdgRelacion = root.findViewById( R.id.radio_parentesco );
        rdgSexo = root.findViewById( R.id.radio_sexo );
        edtNotas = root.findViewById( R.id.txt_notas );
        btnRegistrar = root.findViewById( R.id.btn_registrar );
    }

    public void startListeners() {
        btnRegistrar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar();
            }
        } );

        edtFechaNacimiento.setOnFocusChangeListener( new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if(!isFocused) return;

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month++;
                        calendar.set(year, month, day);
                        String text = String.format(Locale.getDefault(),"%d-%02d-%02d", year,  month, day);
                        edtFechaNacimiento.setText(text);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) ,calendar.get(Calendar.DATE));
                datePickerDialog.getDatePicker().setMaxDate( GregorianCalendar.getInstance().getTimeInMillis());

                datePickerDialog.show();
            }
        } );

        edtFechaNacimiento.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month++;
                        calendar.set(year, month, day);
                        String text = String.format(Locale.getDefault(),"%d-%02d-%02d", year,  month, day);
                        edtFechaNacimiento.setText(text);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) ,calendar.get(Calendar.DATE));
                datePickerDialog.getDatePicker().setMaxDate( GregorianCalendar.getInstance().getTimeInMillis());

                datePickerDialog.show();
            }
        } );
    }

    public void registrar() {
        RadioButton radio = root.findViewById( rdgRelacion.getCheckedRadioButtonId() );
        String relacion = radio.getText().toString();
        RadioButton rdSexo = root.findViewById( rdgSexo.getCheckedRadioButtonId() );
        String sexo = rdSexo.getText().toString();

        try{
            JSONObject datos = new JSONObject();
            JSONObject dependiente = new JSONObject();

            dependiente.put("nombre", edtNombre.getText().toString());
            dependiente.put("apellidos", edtApellidos.getText().toString());
            dependiente.put("fecha_nacimiento", edtFechaNacimiento.getText().toString());
            switch (sexo){
                case "Niño":
                    dependiente.put("sexo", 1);
                    break;
                case "Niña":
                    dependiente.put("sexo", 0);
                    break;
            }
            dependiente.put("notas", edtNotas.getText().toString());



            datos.put("dependiente", dependiente);
            switch (relacion){
                case "Padre":
                    datos.put("parentesco", 1);
                    break;
                case "Madre":
                    datos.put("parentesco", 2);
                    break;
                case "Tutor":
                    datos.put("parentesco", 3);
                    break;
            }


            enviarDatos(datos.toString());


        }catch (JSONException e){
            Log.e(TAG, e.getMessage());
        }
    }

    /*private class DependienteAsync extends AsyncTask<Dependiente, Void, Boolean> {
            @Override
            protected Boolean doInBackground(Dependiente... dependientes) {
                Dependiente dependiente = dependientes[0];
                AppDatabase.getInstance(getActivity()).dependiente().insert(dependiente);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                Toast.makeText( getActivity(), "Se registró al bebé", Toast.LENGTH_SHORT ).show();
                limpiar();
            }
    }*/

    public void limpiar() {
        edtNombre.getText().clear();
        edtApellidos.getText().clear();
        edtFechaNacimiento.getText().clear();
        edtNotas.getText().clear();
        rdgRelacion.check( R.id.radio_madre );
        rdgSexo.check( R.id.radio_masculino );
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction( uri );
        }
    }

    public void enviarDatos(String datos) {
        UsuarioService service = RetrofitRequest.create(UsuarioService.class);
        RequestBody body = RetrofitRequest.createBody(datos);
        Call<String> resp = service.registroDependiente(body);
        progressDialog = UI.showWaitDialog(getActivity());
        resp.enqueue( new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressDialog.dismiss();
                if(response.code() != 200) {
                    Toast.makeText( getActivity(), "Ocurrió un error\n" + response.message(), Toast.LENGTH_LONG ).show();
                    return;
                }

                try {
                    JSONObject jres = new JSONObject(response.body());
                    if(!jres.getBoolean( "OK" )) {
                        Toast.makeText( getActivity(), jres.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }

                    limpiar();
                } catch(JSONException e) {
                    Log.e(TAG, "Error al convertir en json los datos\n" + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_LONG ).show();
            }
        } );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach( context );
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnFragmentInteractionListener" );
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
