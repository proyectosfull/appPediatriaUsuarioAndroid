package com.diazmiranda.juanjose.mibebe.requests;

public abstract class APIUrl {

    //public static final String BASE = "http://209.58.143.105/HDK/api/"; //Server
    public static final String BASE = "http://23.82.16.144:8080/HDK/api/"; //Server

    public static final String LOGIN = "auth/usuario/login";
    public static final String LOGOUT = "auth/usuario/logout";
    public static final String REGISTRO = "usuario/nuevo";
    public static final String ACTUALIZAR_FCM_TOKEN = "usuario/actualizar/token";
    public static final String FACEBOOK_LOGIN = "auth/usuario/login/fb";
    public static final String CAMBIO_CONTRASEÑA = "auth/usuario/password-restore";

    public static final String REGISTRO_DEPENDIENTE = "dependiente/nuevo";
    public static final String LISTA_DEPENDIENTE = "dependiente/consulta";
    public static final String FOTO_VACUNA = "dependiente/vacuna";

    public static final String DEPENDIENTE_FOTO_VACUNA = BASE + "/multimedia/vacuna";

    public static final String REGISTRO_CRECIMIENTO = "dependiente/crecimiento";
    public static final String DATOS_CRECIMIENTO = "dependiente/crecimiento/{IdDependiente}";
    public static final String USUARIO_HISTORIAL = "consulta/usuario/historial";
    public static final String DETALLE_CONSULTA = "consulta/{consultaId}";

    public static final String REGISTRO_VACUNAS = "vacuna";
    public static final String VACUNAS = "vacuna/dependiente/{dependienteId}";
    public static final String LISTA_VACUNAS = "vacuna";
    public static final String LISTA_APLICACION_VACUNA = "vacuna/tipoaplicacion";


    public static final String LISTA_PEDIATRA = "pediatra/lista";


    public static final String NUEVA_CONSULTA = "consulta/nuevo";
}
