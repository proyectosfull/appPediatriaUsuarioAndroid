package com.diazmiranda.juanjose.mibebe.requests.Model;

import com.diazmiranda.juanjose.mibebe.requests.APIUrl;
import com.diazmiranda.juanjose.mibebe.util.Constants;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.Headers;


import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface ConsultaService {

    @PUT(APIUrl.NUEVA_CONSULTA)
    @Headers(Constants.AUTH)
    Call<String> insertar(@Body RequestBody data);

    @PUT(APIUrl.NUEVA_CONSULTA + "/m")
    @Headers(Constants.AUTH)
    @Multipart
    Call<String> insert(
            @Part("data") RequestBody data,
            @Part MultipartBody.Part pago,
            @Part List<MultipartBody.Part> files
    );

    @PUT(APIUrl.NUEVA_CONSULTA + "/m")
    @Headers(Constants.AUTH)
    @Multipart
    Call<String> insert(
            @Part("data") RequestBody data,
            @Part List<MultipartBody.Part> files
    );
}
