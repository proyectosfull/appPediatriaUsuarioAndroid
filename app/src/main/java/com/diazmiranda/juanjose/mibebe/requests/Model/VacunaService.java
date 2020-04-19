package com.diazmiranda.juanjose.mibebe.requests.Model;

import com.diazmiranda.juanjose.mibebe.requests.APIUrl;
import com.diazmiranda.juanjose.mibebe.util.Constants;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface VacunaService {

    @Headers(Constants.AUTH)
    @GET(APIUrl.VACUNAS)
    Call<String> get(@Path("dependienteId") int id);

    @Headers(Constants.AUTH)
    @PUT(APIUrl.REGISTRO_VACUNAS)
    Call<String> add(@Body RequestBody data);

    @Headers(Constants.AUTH)
    @GET(APIUrl.LISTA_VACUNAS)
    Call<String> getVacunas();

    @Headers(Constants.AUTH)
    @GET(APIUrl.LISTA_APLICACION_VACUNA)
    Call<String> getTipoAplicacion();


}
