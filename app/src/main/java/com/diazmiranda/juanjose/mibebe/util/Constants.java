package com.diazmiranda.juanjose.mibebe.util;

import java.util.Date;

public class Constants {
    public static Integer currentBaby = null;
    public static boolean sexo;
    public static Date fechaNac;
    public static String imagenVacuna;

    public static String FACEBOOK_URL = "pediatria.innovadora.5";
    public static String TWITTER_URL = "PediatraInnova1";
    public static String YOUTUBE_URL = "https://www.youtube.com/user/doctordiazmiranda";

    public static final String NO_AUTH = "@: NO_AUTH";

    /**
     * Auxiliares para determinar que una petición necesita enviar el token
     */
    public static final String AUTH_KEY = "@AUTH";
    public static final String AUTH = AUTH_KEY + ": YES";
}
