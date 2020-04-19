package com.diazmiranda.juanjose.mibebe.util;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

public class SharedPreferencesHelper {

    private static final String KEY = "_MiBebe_";
    private SharedPreferences sharedPreferences;

    private SharedPreferencesHelper(){}

    public SharedPreferencesHelper(@NonNull Context context) {
        sharedPreferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
    }

    public void putFCMToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FCMToken",token);
        editor.apply();
    }

    public String getFCMToken() {
        return sharedPreferences.getString("FCMToken", null);
    }

    public void putToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Token",token);
        editor.apply();
    }

    public void putIsTokenUpdated(Boolean updated) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isTokenUpdated", updated);
        editor.apply();
    }

    public Boolean isTokenUpdated() {
        return sharedPreferences.getBoolean("isTokenUpdated", false);
    }

    public void removeToken() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("Token");
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString("Token", null);
    }

    public void clearKeys() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
