package com.diazmiranda.juanjose.mibebe.roomdata.entities;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "babyData", indices = {@Index(value = "timeMillis", unique = true)})
public class BabyData implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int dependienteId;
    private float peso;
    private float talla;
    private float perimetroCraneal;
    private long timeMillis;
    private int dias;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDependienteId() {
        return dependienteId;
    }

    public void setDependienteId(int dependienteId) {
        this.dependienteId = dependienteId;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public float getTalla() {
        return talla;
    }

    public void setTalla(float talla) {
        this.talla = talla;
    }

    public float getPerimetroCraneal() {
        return perimetroCraneal;
    }

    public void setPerimetroCraneal(float perimetroCraneal) {
        this.perimetroCraneal = perimetroCraneal;
    }

    public long getTimeMillis() {
        return timeMillis;
    }

    public void setTimeMillis(long timeMillis) {
        this.timeMillis = timeMillis;
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }
}
