package com.diazmiranda.juanjose.mibebe.fragments;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.diazmiranda.juanjose.mibebe.requests.Model.UsuarioService;
import com.diazmiranda.juanjose.mibebe.requests.RetrofitRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.diazmiranda.juanjose.mibebe.R;
import com.diazmiranda.juanjose.mibebe.roomdata.entities.BabyData;
import com.diazmiranda.juanjose.mibebe.util.Constants;
import com.diazmiranda.juanjose.mibebe.util.GrowChildChart;
import com.diazmiranda.juanjose.mibebe.util.MyViewPager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class CrecimientoFragment extends Fragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private MyViewPager mViewPager;
    private final static String TAG = "CrecimientoFragment";

    public CrecimientoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_crecimiento, container, false);

        cargarDatos(root);

        return root;
    }
    public void cargarDatos(final View root) {

        UsuarioService service = RetrofitRequest.create(UsuarioService.class);
        Call<String> resp = service.getDatosCrecimiento(Constants.currentBaby);

        resp.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.code() != 200) {
                    Toast.makeText(getApplicationContext(), "Ocurrió un error\n" + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    JSONObject jresponse = new JSONObject(response.body());
                    if (!jresponse.getBoolean("OK")) {
                        Toast.makeText(getApplicationContext(), jresponse.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }

                    JSONArray crecimiento = jresponse.getJSONObject("data").getJSONArray("crecimiento");

                    ArrayList<BabyData> datosCrecimiento = new Gson().fromJson( crecimiento.toString(), new TypeToken<List<BabyData>>(){}.getType());

                    mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager(), datosCrecimiento);

                    // Set up the ViewPager with the sections adapter.
                    mViewPager = root.findViewById(R.id.container);
                    mViewPager.setAdapter(mSectionsPagerAdapter);

                    TabLayout tabLayout = root.findViewById(R.id.tabs);

                    mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                    tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
                    mViewPager.setOffscreenPageLimit(3);

                } catch (JSONException e) {
                    Log.i(TAG, e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext() , t.getMessage(), Toast.LENGTH_LONG ).show();
            }

        } );
    }

    public static class CrecimientoPesoFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.fragment_crecimiento_peso, container, false);

            final LineChart chart = root.findViewById(R.id.chart);
            boolean isBoy = Constants.sexo;
            //TableLayout tblLabels = root.findViewById(R.id.label_data);
            //tblLabels.setVisibility(View.GONE);

            ImageView labelItem = root.findViewById(R.id.label_item_normal);
            labelItem.setBackgroundColor(getActivity().getResources().getColor(isBoy ? R.color.chart_normal_boys : R.color.chart_normal_girls));

            GrowChildChart lineChart = new GrowChildChart(getActivity());
            lineChart.setChartView(chart);
            lineChart.setType(isBoy ? GrowChildChart.TYPE.WFA_BOYS : GrowChildChart.TYPE.WFA_GIRLS);

            ArrayList<BabyData> datosCrecimiento = (ArrayList<BabyData>) getArguments().getSerializable("datos");
            lineChart.setData( datosCrecimiento );
            lineChart.start();
            return root;
        }
    }

    public static class CrecimientoTallaFragment extends Fragment {

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.fragment_crecimiento_talla, container, false);
            final LineChart chart = root.findViewById(R.id.chart);
            boolean isBoy = Constants.sexo;

            GrowChildChart lineChart = new GrowChildChart(getActivity());
            lineChart.setChartView(chart);
            lineChart.setType(isBoy ? GrowChildChart.TYPE.LHFA_BOYS : GrowChildChart.TYPE.LHFA_GIRLS);

            ArrayList<BabyData> datosCrecimiento = (ArrayList<BabyData>) getArguments().getSerializable("datos");
            lineChart.setData( datosCrecimiento );
            lineChart.start();
            return root;
        }
    }

    public static class CrecimientoPerimetroCranealFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.fragment_crecimiento_perimetro_craneal, container, false);

            final LineChart chart = root.findViewById(R.id.chart);
            boolean isBoy = Constants.sexo;
            //TableLayout tblLabels = root.findViewById(R.id.label_data);
            //tblLabels.setVisibility(View.GONE);

            ImageView labelItem = root.findViewById(R.id.label_item_normal);
            labelItem.setBackgroundColor(getActivity().getResources().getColor(isBoy ? R.color.chart_normal_boys : R.color.chart_normal_girls));

            GrowChildChart lineChart = new GrowChildChart(getActivity());
            lineChart.setChartView(chart);
            lineChart.setType(isBoy ? GrowChildChart.TYPE.HCFA_BOYS : GrowChildChart.TYPE.HCFA_GIRLS);

            ArrayList<BabyData> datosCrecimiento = (ArrayList<BabyData>) getArguments().getSerializable("datos");
            lineChart.setData( datosCrecimiento );
            lineChart.start();
            return root;
        }
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private ArrayList<BabyData> datosCrecimiento;

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<BabyData> datosCrecimiento) {
            super(fm);
            this.datosCrecimiento = datosCrecimiento;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a Custom Fragment (it depends position belong).
            Bundle bundle = new Bundle();
            bundle.putSerializable("datos", this.datosCrecimiento);
            Fragment fragment = null;

            switch (position) {
                case 0:
                    fragment = new CrecimientoPesoFragment();
                    break;
                case 1:
                    fragment = new CrecimientoTallaFragment();
                    break;
                case 2:
                    fragment = new CrecimientoPerimetroCranealFragment();
                    break;
            }
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 3;
        }
    }


}
