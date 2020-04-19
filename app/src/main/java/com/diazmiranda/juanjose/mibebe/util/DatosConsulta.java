package com.diazmiranda.juanjose.mibebe.util;

import android.content.Context;
import android.net.Uri;

import com.diazmiranda.juanjose.mibebe.roomdata.entities.Dependiente;
import com.diazmiranda.juanjose.mibebe.roomdata.entities.Doctor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DatosConsulta implements Serializable {
    private int id;
    private List<Archivo> archivos;
    private Dependiente dependiente;
    private Doctor pediatra;
    private String sintomas;
    private Float temperatura;
    private Float peso;
    private Archivo comprobantePago;
    private String fecAtendido;
    private String fecRegistro;
    private Boolean atendido;
    private Paypal paypal;

    public DatosConsulta() {
        this.archivos = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addArchivo(Uri uri, String nombre, String mediaType) {
        this.archivos.add( new Archivo(uri, nombre, mediaType) );
    }

    public Archivo getComprobantePago() {
        return comprobantePago;
    }

    public void setComprobantePago(Archivo comprobantePago) {
        this.comprobantePago = comprobantePago;
    }

    public List<Archivo> getArchivos() {
        return this.archivos;
    }

    public void setDependiente(Dependiente dependiente) {
        this.dependiente = dependiente;
    }

    public Dependiente getDependiente() {
        return this.dependiente;
    }

    public void setPediatra(Doctor pediatra) {
        this.pediatra = pediatra;
    }

    public Doctor getPediatra() {
        return this.pediatra;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public String getSintomas() {
        return sintomas;
    }

    public void setTemperatura(Float temperatura) {
        this.temperatura = temperatura;
    }

    public Float getTemperatura() {
        return temperatura;
    }

    public Float getPeso() {
        return peso;
    }

    public void setPeso(Float peso) {
        this.peso = peso;
    }

    public String getFecAtendida() {
        return fecAtendido;
    }

    public void setFecAtendida(String fechaAtendida) {
        this.fecAtendido = fechaAtendida;
    }

    public String getFecRegistro() {
        return fecRegistro;
    }

    public void setFecRegistro(String fecRegistro) {
        this.fecRegistro = fecRegistro;
    }

    public Boolean isAtendido() {
        return atendido;
    }

    public void setAtendido(Boolean atendido) {
        this.atendido = atendido;
    }

    public Paypal getPaypal() {
        return paypal;
    }

    public void setPaypal(Paypal paypal) {
        this.paypal = paypal;
    }

    void writeFile(InputStream in, File file) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if ( out != null ) {
                    out.close();
                }
                in.close();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }

    public void loadFiles(Context context) {
        try {
            InputStream inputStream;
            for(Archivo archivo : archivos) {
                inputStream = context.getContentResolver().openInputStream(archivo.getUri());
                File file = new File(context.getCacheDir().getAbsolutePath()+"/" + archivo.getNombre());
                writeFile(inputStream, file);
                archivo.setFile( file );
            }

            inputStream = context.getContentResolver().openInputStream(comprobantePago.getUri());
            File file = new File(context.getCacheDir().getAbsolutePath()+"/" + comprobantePago.getNombre());
            writeFile(inputStream, file);
            comprobantePago.setFile( file );
        } catch(Exception e) {

        }
    }

    public void limpiarCache() {
        for(Archivo archivo : archivos) {
            archivo.getFile().delete();
        }
        comprobantePago.getFile().delete();
    }

    public static class Paypal {
        private String paymentId;
        private String transactionId;
        private String createTime;
        private String amount;
        private String state;

        public String getPaymentId() {
            return paymentId;
        }

        public void setPaymentId(String paymentId) {
            this.paymentId = paymentId;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }
}
