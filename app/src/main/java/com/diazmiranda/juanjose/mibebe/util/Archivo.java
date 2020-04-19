package com.diazmiranda.juanjose.mibebe.util;

import android.net.Uri;

import java.io.File;
import java.io.Serializable;

public class Archivo implements Serializable {
    private Uri uri;
    private String nombre;
    private String mediaType;
    private File file;

    public Archivo() {

    }

    public Archivo(Uri uri, String nombre, String mediaType) {
        this.uri = uri;
        this.nombre = nombre;
        this.mediaType = mediaType;
    }

    public Uri getUri() {
        return uri;
    }

    public String getNombre() {
        return nombre;
    }

    public String getMediaType() {
        return mediaType;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }


}
