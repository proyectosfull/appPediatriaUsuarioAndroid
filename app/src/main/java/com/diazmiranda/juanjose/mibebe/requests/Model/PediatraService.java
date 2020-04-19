package com.diazmiranda.juanjose.mibebe.requests.Model;

import com.diazmiranda.juanjose.mibebe.requests.APIUrl;
import com.diazmiranda.juanjose.mibebe.util.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface PediatraService {

    @GET(APIUrl.LISTA_PEDIATRA)
    @Headers(Constants.AUTH)
    Call<String> getLista();
}
