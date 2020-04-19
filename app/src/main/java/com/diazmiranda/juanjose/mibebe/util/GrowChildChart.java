package com.diazmiranda.juanjose.mibebe.util;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.diazmiranda.juanjose.mibebe.requests.Model.UsuarioService;
import com.diazmiranda.juanjose.mibebe.requests.RetrofitRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.diazmiranda.juanjose.mibebe.R;
import com.diazmiranda.juanjose.mibebe.roomdata.entities.BabyData;
import com.diazmiranda.juanjose.mibebe.roomdata.entities.Dependiente;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

//import androidx.annotation.IdRes;

public class GrowChildChart {
    private final static String TAG = "GrowChildChart";
    private LineChart chart;
    private Activity activity;
    private List<Entry> childData;

    private String FILE;
    TYPE type = null;

    /**
     * Tipos de datos
     */
    public enum TYPE {
        WFA_BOYS,
        WFA_GIRLS,
        LHFA_BOYS,
        LHFA_GIRLS,
        HCFA_BOYS,
        HCFA_GIRLS
    }

    public GrowChildChart(Activity activity) {
        this.activity = activity;
    }

    public void setType(TYPE type) {
        this.type = type;
        //Obtiene el nombre del archivo dependiendo el tipo de dato que se solicita
        switch (type) {
            case WFA_BOYS:
                FILE = "wfa_boys.csv";
                break;
            case WFA_GIRLS:
                FILE = "wfa_girls.csv";
                break;
            case LHFA_BOYS:
                FILE = "lhfa_boys.csv";
                break;
            case LHFA_GIRLS:
                FILE = "lhfa_girls.csv";
                break;
            case HCFA_BOYS:
                FILE = "hcfa_boys.csv";
                break;
            case HCFA_GIRLS:
                FILE = "hcfa_girls.csv";
                break;
        }
    }

   /* public void setChart(@IdRes int resId) {
        this.chart = activity.findViewById( resId );
        Log.i( "Grow", this.chart.toString() );
    }*/

    public void setChartView(LineChart chart) {
        this.chart = chart;
    }

    public void start() {
        //Obtiene los colores que se utilizarán
        int normalColor = activity.getResources().getColor(type == TYPE.WFA_BOYS || type == TYPE.LHFA_BOYS ? R.color.chart_normal_boys : R.color.chart_normal_girls);
        int alertColor = activity.getResources().getColor(R.color.chart_alert);
        int riskColor = activity.getResources().getColor(R.color.chart_risk);
        int overweightColor = activity.getResources().getColor(R.color.chart_overweight);

        //Lista de entradas para cada percentil
        List<Entry> p3 = new ArrayList<>();
        List<Entry> p15 = new ArrayList<>();
        List<Entry> p50 = new ArrayList<>();
        List<Entry> p85 = new ArrayList<>();
        List<Entry> p97 = new ArrayList<>();

        //Llena los datos de cada percentil
        List<EntryData> list = getData();
        for (EntryData data : list) {
            p3.add(new Entry(data.getEdad(), data.getP3()));
            p15.add(new Entry(data.getEdad(), data.getP15()));
            p50.add(new Entry(data.getEdad(), data.getP50()));
            p85.add(new Entry(data.getEdad(), data.getP85()));
            p97.add(new Entry(data.getEdad(), data.getP97()));
        }

        ArrayList<ILineDataSet> lines = new ArrayList<>();
        LineDataSet dsP3 = createPercentilDataSet(p3, "Alerta", alertColor);
        LineDataSet dsP15 = createPercentilDataSet(p15, "", normalColor);
        LineDataSet dsP50 = createPercentilDataSet(p50, "Normal", normalColor);
        LineDataSet dsP85 = createPercentilDataSet(p85, "", normalColor);
        LineDataSet dsP97 = createPercentilDataSet(p97, "Riesgo Sobrepeso", type == TYPE.LHFA_BOYS || type == TYPE.LHFA_GIRLS ? normalColor : riskColor);
        LineDataSet dsChildData = createChildDataSet(childData);

        lines.add(dsP97);
        lines.add(dsP85);
        lines.add(dsP50);
        lines.add(dsP15);
        lines.add(dsP3);
        lines.add(dsChildData);

        chart.setData(new LineData(lines));
        chart.setGridBackgroundColor(type == TYPE.WFA_BOYS || type == TYPE.WFA_GIRLS ? overweightColor : alertColor);
        chart.setDrawGridBackground(true);

        //Quita de la gráfica un texto que aparece por default
        Description d = new Description();
        d.setText("");
        chart.setDescription(d);

        chart.animateY(500); //Duración de la animación al dibujar la gráfica
        chart.setTouchEnabled(true);

        //Configuación del los ejes laterales
        YAxis yAxisLeft = chart.getAxisLeft();
        YAxis yAxisRight = chart.getAxisRight();

        if (type == TYPE.LHFA_BOYS || type == TYPE.LHFA_GIRLS) {
            yAxisLeft.setAxisMinimum(40f);
            yAxisLeft.setAxisMaximum(130f);
            yAxisLeft.setLabelCount(11, false);

            yAxisRight.setAxisMinimum(40f);
            yAxisRight.setAxisMaximum(130f);
            yAxisRight.setLabelCount(11, false);
        } else if (type == TYPE.HCFA_BOYS || type == TYPE.HCFA_GIRLS) {
            yAxisLeft.setAxisMinimum(30f);
            yAxisLeft.setAxisMaximum(60f);
            yAxisLeft.setLabelCount(10, false);

            yAxisRight.setAxisMinimum(30f);
            yAxisRight.setAxisMaximum(60f);
            yAxisRight.setLabelCount(10, false);
        } else {
            yAxisLeft.setLabelCount(13, false);
            yAxisRight.setLabelCount(13, false);
        }

        yAxisLeft.setDrawGridLinesBehindData(false);
        chart.getLegend().setEnabled(false); //Oculta los labels de los datos

        //Configuración del eje X
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.setXAxisRenderer(new XAxisRenderer(chart.getViewPortHandler(), xAxis, chart.getTransformer(YAxis.AxisDependency.LEFT)) {
            float aux;

            @Override
            protected void drawLabel(Canvas c, String formattedLabel, float x, float y, MPPointF anchor, float angleDegrees) {
                if (formattedLabel.contains("Año")) { //Si es año, hace mas gránde la letra y la hace negrita
                    aux = mAxisLabelPaint.getTextSize();
                    mAxisLabelPaint.setTextSize(aux * 1.2f);
                    mAxisLabelPaint.setFakeBoldText(true);
                } else { //Si no es año la reestablece al valor original
                    mAxisLabelPaint.setTextSize(aux);
                    mAxisLabelPaint.setFakeBoldText(false);
                }
                super.drawLabel(c, formattedLabel, x, y, anchor, angleDegrees);
            }
        });
        xAxis.setValueFormatter(new ValueFormatter() {
            int aux;

            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                if (value % 30.4375f != 0)
                    return ""; //Si el valor no es exactamente un mes, entonces no muestra nada
                aux = (int) (value / 30.4375f);
                if (aux % 12 == 0) { //Años
                    return aux / 12 + (aux / 12 == 1 ? " Año" : " Años");
                }
                return aux + " Meses";
                //return aux % 12 + (aux % 12 == 1 ? " Mes" : " Meses");
            }
        });

        xAxis.setDrawGridLinesBehindData(false);
        xAxis.setGranularityEnabled(true); //Permite mostrar datos en un intervalo
        xAxis.setGranularity(30.4375f); //Establece el valor del intervalo en que se mostrarán los datos, en este caso cada mes
        xAxis.setAxisMinimum(0); //Valor minimo
        //xAxis.setAxisMaximum(732f); //Valor maximo (2 años)
        xAxis.setAxisMaximum( 1856f ); //Valor maximo (5 años)
        xAxis.setGridColor(Color.BLACK); //Color de líneas de los ejes

        chart.setVisibleXRangeMaximum(121.75f); //permite que se vean 4 valores a la vez (30.4375 * 4 = 4 meses)
        chart.moveViewToX(0); //Posiciona la vista del eje x al inicio

        chart.invalidate(); //refresh
    }

    private LineDataSet createPoint(int color, int x, int y) {
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(x, y));
        LineDataSet dataset = new LineDataSet(entries, ""); // add entries to dataset
        dataset.setCircleRadius(4f);
        dataset.setCircleColor(color);
        dataset.setDrawCircleHole(false);
        dataset.setDrawCircles(true);
        dataset.setDrawValues(false);
        return dataset;

    }

    /**
     * Crea el objeto para dibujar la linea
     *
     * @param entries datos del percentil
     * @param label
     * @param color   Color para rellenar el espacio abarcado
     * @return dataset
     */
    private LineDataSet createPercentilDataSet(List<Entry> entries, String label, int color) {
        LineDataSet dataset = new LineDataSet(entries, label); // add entries to dataset
        dataset.setDrawCircles(false);
        dataset.setDrawValues(false);
        dataset.setColor(Color.argb(60, 0, 0, 0));
        dataset.setDrawFilled(true);
        dataset.setFillAlpha(255);
        dataset.setFillColor(color);
        //dataset.setAxisDependency( YAxis.AxisDependency.LEFT);
        return dataset;
    }

    private LineDataSet createChildDataSet(List<Entry> entries) {
        LineDataSet dataset = new LineDataSet(entries, ""); // add entries to dataset
        dataset.setDrawCircles(true);
        dataset.setDrawCircleHole(false);
        dataset.setCircleRadius(3.5f);
        dataset.setDrawValues(true);
        dataset.setColor(Color.rgb(0, 0, 255));
        return dataset;
    }

    /**
     * Obtiene los datos del archivo
     *
     * @return Lista de los datos ya estructurados
     */
    private List<EntryData> getData() {
        BufferedReader reader = null;
        List<EntryData> dataList = new ArrayList<>();
        try {
            reader = new BufferedReader(new InputStreamReader(activity.getAssets().open(FILE), "UTF-8"));

            String mLine;
            while ((mLine = reader.readLine()) != null) {
                EntryData data = new EntryData();
                //Separa por comas para obtener cada dato
                String[] lineData = mLine.split(",");
                data.setEdad(Integer.parseInt(lineData[0]));
                data.setP3(Float.parseFloat(lineData[1]));
                data.setP15(Float.parseFloat(lineData[2]));
                data.setP50(Float.parseFloat(lineData[3]));
                data.setP85(Float.parseFloat(lineData[4]));
                data.setP97(Float.parseFloat(lineData[5]));
                dataList.add(data);
            }
        } catch (IOException e) {
            Log.e("Crecimeitni", e.getMessage(), e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error al leer los datos del archivo", e);
                }
            }
        }
        return dataList;
    }

    public void addEntry(float peso, int edad) {
        //Lista de entradas para cada percentil
        List<Entry> p3 = new ArrayList<>();
        p3.add(new Entry(edad, peso));

        LineDataSet dataset = new LineDataSet(p3, ""); // add entries to dataset
        dataset.setDrawCircles(true);
        dataset.setDrawValues(true);
        dataset.setColor(Color.argb(60, 0, 0, 255));
        dataset.setDrawFilled(true);
        dataset.setFillAlpha(255);
        dataset.setFillColor(Color.BLUE);

        chart.getData().addDataSet(dataset);
        chart.invalidate();
    }

    public void setData(List<BabyData> babyDataList) {
        childData = new ArrayList<>();
        boolean isPeso = type == TYPE.WFA_BOYS || type == TYPE.WFA_GIRLS;
        boolean isTalla = type == TYPE.LHFA_BOYS || type == TYPE.LHFA_GIRLS;
        for (BabyData data : babyDataList) {
            childData.add(new Entry(data.getDias(), isPeso ? data.getPeso() : (isTalla ? data.getTalla() : data.getPerimetroCraneal())));
        }
    }

            private class EntryData {
                private int edad;
                private float p3;
                private float p15;
                private float p50;
                private float p85;
                private float p97;

                public int getEdad() {
                    return edad;
                }

                public void setEdad(int edad) {
                    this.edad = edad;
                }

                public float getP3() {
                    return p3;
                }

                public void setP3(float p3) {
                    this.p3 = p3;
                }

                public float getP15() {
                    return p15;
                }

                public void setP15(float p15) {
                    this.p15 = p15;
                }

                public float getP50() {
                    return p50;
                }

                public void setP50(float p50) {
                    this.p50 = p50;
                }

                public float getP85() {
                    return p85;
                }

                public void setP85(float p85) {
                    this.p85 = p85;
                }

                public float getP97() {
                    return p97;
                }

                public void setP97(float p97) {
                    this.p97 = p97;
                }
            }
        }
