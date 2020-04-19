package com.diazmiranda.juanjose.mibebe.requests.Model;

import com.diazmiranda.juanjose.mibebe.requests.APIUrl;
import com.diazmiranda.juanjose.mibebe.util.Constants;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UsuarioService {

    @POST(APIUrl.LOGIN)
    Call<String> login(@Body RequestBody data);

    @POST(APIUrl.FACEBOOK_LOGIN)
    Call<String> facebookLogin(@Body RequestBody data);

    @POST(APIUrl.REGISTRO)
    Call<String> registrar(@Body RequestBody data);

    @POST(APIUrl.LOGOUT)
    @Headers(Constants.AUTH)
    Call<String> logout();

    @POST(APIUrl.CAMBIO_CONTRASEÑA)
    @Headers(Constants.AUTH)
    Call<String> actulizarContraseña(@Body RequestBody data);

    @POST(APIUrl.ACTUALIZAR_FCM_TOKEN)
    @Headers(Constants.AUTH)
    Call<String> updateFCMToken(@Body RequestBody data);

    @POST(APIUrl.REGISTRO_DEPENDIENTE)
    @Headers(Constants.AUTH)
    Call<String> registroDependiente(@Body RequestBody data);

    @GET(APIUrl.LISTA_DEPENDIENTE)
    @Headers(Constants.AUTH)
    Call<String> getListaDependiente();

    @POST(APIUrl.REGISTRO_CRECIMIENTO)
    @Headers(Constants.AUTH)
    Call<String> registroCrecimiento(@Body RequestBody data);

    @GET(APIUrl.DATOS_CRECIMIENTO)
    @Headers(Constants.AUTH)
    Call<String> getDatosCrecimiento(@Path("IdDependiente") int IdDependiente);

    @GET(APIUrl.USUARIO_HISTORIAL)
    @Headers(Constants.AUTH)
    Call<String> getHistorialConsulta();

    @GET(APIUrl.DETALLE_CONSULTA)
    @Headers(Constants.AUTH)
    Call<String> getDetalleConsulta(@Path("consultaId") int consultaId);

    @PUT(APIUrl.FOTO_VACUNA)
    @Headers(Constants.AUTH)
    @Multipart
    Call<String> addFotoVacuna(
            @Part("id") RequestBody consultaId,
            @Part MultipartBody.Part foto
    );
}
