package com.diazmiranda.juanjose.mibebe.util;

import java.util.ArrayList;
import java.util.List;

public class YoutubeVideo {
    public String id;
    public String title;

    public YoutubeVideo(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static List<YoutubeVideo> getPeriodoGestacionList() {
        List<YoutubeVideo> list = new ArrayList<>();
        list.add(new YoutubeVideo("zp3kyo2OReg", "Desarrollo Embrionario"));
        list.add(new YoutubeVideo("eoCImUrs4hQ", "Dietas y Ejercicios para mamás gestantes"));
        list.add(new YoutubeVideo("TdIqzDfXOMs", "Preparación para el parto"));
        return list;
    }

    public static List<YoutubeVideo> getPartoCesariaList() {
        List<YoutubeVideo> list = new ArrayList<>();
        list.add(new YoutubeVideo("zp3kyo2OReg", "Parto Natural Domicilio / Hospital"));
        list.add(new YoutubeVideo("eoCImUrs4hQ", "Parto Humanizado"));
        list.add(new YoutubeVideo("TdIqzDfXOMs", "Cesária"));
        list.add(new YoutubeVideo("TdIqzDfXOMs", "Los Derechos del Recién nacido"));
        return list;
    }
}
