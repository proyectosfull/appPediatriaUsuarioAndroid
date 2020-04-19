package com.diazmiranda.juanjose.mibebe.roomdata.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "dependiente")
public class Dependiente implements Serializable {
    @PrimaryKey
    private int id;
    private String nombre;
    private String apellidos;
    private String urlFoto;
    private String relacion;
    private boolean sexo;
    private String notas;
    private String fecNacimiento;
    @Ignore
    private String imagenVacuna;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getRelacion() {
        return relacion;
    }

    public void setRelacion(String relacion) {
        this.relacion = relacion;
    }

    public boolean isSexo() {
        return sexo;
    }

    public void setSexo(boolean sexo) {
        this.sexo = sexo;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getFecNacimiento() {
        return fecNacimiento;
    }

    public void setFecNacimiento(String fecNacimiento) {
        this.fecNacimiento = fecNacimiento;
    }

    public String getImagenVacuna() {
        return imagenVacuna;
    }

    public void setImagenVacuna(String imagenVacuna) {
        this.imagenVacuna = imagenVacuna;
    }

    public String getNombreCompleto() {
        return this.nombre + " " + this.apellidos;
    }

    public void getEdadInMillis() {
        Date date = new Date( this.fecNacimiento );
        Calendar c = Calendar.getInstance();

    }
}
