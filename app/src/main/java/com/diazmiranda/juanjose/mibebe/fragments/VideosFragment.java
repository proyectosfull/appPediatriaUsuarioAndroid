package com.diazmiranda.juanjose.mibebe.fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.diazmiranda.juanjose.mibebe.R;
import com.diazmiranda.juanjose.mibebe.adapters.IconTreeItemHolder;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideosFragment extends Fragment {
    private final static String API_KEY = "AIzaSyAz8V-KdP_fAQSN3QrhNVyJ5poOaxAAMBc";
    private YouTubePlayer youTubePlayer;

    public VideosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate( R.layout.fragment_videos, container, false );

        TreeNode root = TreeNode.root();

        TreeNode collection = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Contenido"));
        List<TreeNode> videos = new ArrayList<>();
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "DERECHOS DEL BEBE RECIEN NACIDO", "n_5lb4tjCDs")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "PREPARACION PARA RECIBIR AL BEBE", "EdddS4go9CA")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "PARTO NATURAL", "TdIqzDfXOMs")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "NACIMIENTO POR CESAREA", "eMHbTFt03O4")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "ESTANCIA EN CUNERO PRIMERAS 4 HS DE VIDA", "m9zqgIvTRLM")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "PRIMERA EVACUACION \"MECONIO\"", "VOwHvr2OsSQ")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "CAMBIO DE PAÑAL", "vlozfQ3cVlo")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "CUIDADOS  DEL RECIEN NACIDO  LLEGADA A CASA", "uYPwT4eD5-M")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "CUIDADOS DEL RECIEN NACIDO LLEGADA A CASA", "LXT_Ehq4N_8")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "ALIMENTACION DEL RECIEN NACIDO  I", "pXkEeKjfKo4")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "ALIMENTACION DEL RECIEN NACIDO II", "eoCImUrs4hQ")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "ALIMENTACION AL SENO MATERNO", "LXT_Ehq4N_8")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "COLICOS POR EMPLEO DE BIBERONES", "zp3kyo2OReg")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "ALIMENTACION CON BIBERON", "CJcuHbNHgNk")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "ALIMENTACION CON BIBERON POSICION IDEAL", "Xf0kj8O0n3c")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "ALIMENTACION CON BIBERON TECNICA ADECUADA", "Rszx3BPQ5v4")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "BIBERONES TRADICIONAES  Vs \"MODERNOS\"", "uYPwT4eD5-M")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "PREPARACION DE BIBERONES", "wh33f7_GH9g")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "VALORACION DE DIFERENTES TIPOS DE BIBERONES", "bDYn5MqwzgI")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "\"AEROFAGIA\" Y REFLUJO POR MALA ELECCION DEL BIBERON", "v2SdehnCNWI")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "REFLUJO POR MALA TECNICA  DE ALIMENTACION", "Td9uLiVy-FY")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "REFLUJO GASTROESOFAGICO (\"ERGE\")", "jkAx7IwLG0w")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "ESTUDIOS RADIOLOGOCOS PARA VALORAR  EL REFLUJO", "HEi0TORf0es")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "COMO HACER \"ERUCTAR \" AL BEBE", "9bCrRxXWUeg")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "FUERZA DE SUCCION EN EL BEBE Y BIBERON", "81NwXjcwDAg")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "ALOJAMIENTO CONJUNTO  \"APEGO MATERNO Y PATERNO\"", "FdwdHLuN6B4")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "PREVENCION DE \"MUERTE DE CUNA\"", "ZWKU5wHVxx0")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "ICTERICIA NEONATAL", "C8esAh0kS7w")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "BAÑO  DEL BEBE", "lWPgWcNdJ3A")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "BAÑO DEL BEBE RECIEN NACIDO EN CASA", "7c-XnNOXKgQ")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "IMPLEMENTOS ESENCIAL ES PARA EL RECIEN NACIDO", "xbAYCgOki_w")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "PRIMERA CONSULTA AL PEDIATRA CON EL BEBE RECIEN NACIDO", "d84gOh8DCgg")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "PRIMERA CONSULTA AL PEDIATRA CON EL BEBE RECIEN NACIDO", "uVQpP2lVX40")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "PRIMERA CONSULTA AL PEDIARA CON EL BEBE RECIEN NACIDO", "HAm7UV9jtIk")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "PRIMERA CONSULTA AL PEDIATRA CON EL BEBE RECIEN NACIDO", "TrxYGilfMvM")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "\"HIPER ACIDEZ\" EN EL RECIEN NACIDO POR LECHE MATERNA", "Agixb7IsoLc")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "\"ESTREÑIMIENTO\" EN EL RECIEN NACIDO", "SqrgHTg8iBk")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "\"ESTREÑIMIENTO\" EN EL RECIEN NACIDO", "TrxYGilfMvM")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "SOSTENIMIOENTO DE LA CABEZA EN EL RN", "tM4qZyRABSg")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "FIMOSIS/CIRCUNCICION", "_lxcdtulSrA")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "REFLEJOS DE SUPERVIVENCIA", "uVQpP2lVX40")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "REFLEJOS DE SUPERVIVENCIA", "d84gOh8DCgg")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "PRIMER MES DE VIDA DEL BEBE", "CGSNwX5ze_s")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "SEGUNDO  MES", "J3OqrKf0nPg")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "TERCER MES", "a7w4Q1vTvMk")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "CUARTO MES", "49lkCF3cEm4")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "QUINTO MES", "PSg1ocU_dmA")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "SEXTO MES", "zC_40-DU1Rw")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "SEXTO MES \"ESTIMULACION TEMPRANA EN EL HOGAR\"", "QFbTgitR02g")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "SEPTIMO MES", "5V4-QmvQuug")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "SEPTIMO MES (II)", "DMHBMzA1L8Q")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "SEPTIMO MES (III)", "NHuJJlKOWMQ")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "OCTAVO MES", "sNUzrmRNWq8")) );
        videos.add( new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "NOVENO MES", "VWgS0YKCFrM")) );
        collection.addChildren(videos);
        root.addChildren(collection);
/*
        //Periodo de Gestación
        TreeNode I = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Periodo de Gestación"));
        TreeNode I_1 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "Desarrollo Embrionario", "81NwXjcwDAg"));
        TreeNode I_2 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "Dietas y Ejercicio para mamá gestante", "CJcuHbNHgNk"));
        TreeNode I_3 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "Preparación para el parto (Psicoprofilaxis Obstétrica)", "MuGbdy_ABI8"));
        I.addChildren(I_1, I_2, I_3);


        //Parto y/o Cesárea
        TreeNode II = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Parto y/o cesárea"));
        TreeNode II_1 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "Parto Natural domicilio/hospital", ""));
        TreeNode II_2 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "Parto Humanizado", ""));
        TreeNode II_3 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "Cesarea", ""));
        TreeNode II_4 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "Los Derechos del Recien nacido", ""));
        II.addChildren(II_1, II_2, II_3, II_4);

        //El bebé recien nacido
        TreeNode III = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "El bebé recien nacido"));

        TreeNode III_1 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "El primer minuto"));
        TreeNode III_1_1 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "Primera respración", ""));
        TreeNode III_1_2 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "Apego materno y paterno", ""));
        TreeNode III_1_3 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Primer Alimento"));
        TreeNode III_1_3_1 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Seno Materno"));
        TreeNode III_1_3_1_1 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "Importancia de Calostro", ""));
        TreeNode III_1_3_1_2 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "Evaluación práctica de la calidad de la leche materna", ""));
        TreeNode III_1_3_1_3 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "Leche materna extracción, almacenamiento y administración", ""));
        TreeNode III_1_3_2 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "Biberones", ""));
        TreeNode III_1_3_3 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "Fórmulas lácteas / Fórmulas especiales", ""));

        III_1_3_1.addChildren(III_1_3_1_1, III_1_3_1_2, III_1_3_1_3);
        III_1_3.addChildren(III_1_3_1, III_1_3_2, III_1_3_3);
        III_1.addChildren(III_1_1, III_1_2, III_1_3);

        TreeNode III_2 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Alojamiento Conjunto"));
        TreeNode III_2_1 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "Integración Familiar", ""));
        III_2.addChild(III_2_1);

        TreeNode III_3 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Rutina de Cunero"));
        TreeNode III_3_1 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "Baño", ""));
        TreeNode III_3_2 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "Control Térmico", ""));
        TreeNode III_3_3 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "Antropometría (Peso y Talla)", ""));
        TreeNode III_3_4 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "Reflejos Fisiológicos", ""));
        TreeNode III_3_5 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "lctericia neuronal", ""));
        TreeNode III_3_6 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Cólicos"));
        TreeNode III_3_6_1 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "Hiper Acidez", ""));
        TreeNode III_3_6_2 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "Reflujo Gastro Esofágico", ""));
        TreeNode III_3_7 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "Cuidado del Ombligo", ""));
        TreeNode III_3_8 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "Circunsición", ""));
        TreeNode III_3_9 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "Mitos y verdades del recién nacido", ""));
        TreeNode III_3_10 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Bancos de células madres"));
        TreeNode III_3_10_1 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_video_youtube, "Bancos de células madres", ""));

        III_3_6.addChildren(III_3_6_1, III_3_6_2);
        III_3_10.addChild(III_3_10_1);
        III_3.addChildren(III_3_1, III_3_2, III_3_3, III_3_4, III_3_5, III_3_6, III_3_7, III_3_8, III_3_9, III_3_10);

        III.addChildren(III_1, III_2, III_3);


        //computerRoot.addChildren(title1, title2, title3);
        root.addChildren(I, II, III);*/


        LinearLayout containerView = rootView.findViewById(R.id.layout_container);
        AndroidTreeView tView = new AndroidTreeView(getActivity(), root);

        tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
        tView.setDefaultViewHolder(IconTreeItemHolder.class);
        tView.setDefaultNodeClickListener(nodeClickListener);
        //tView.setDefaultNodeLongClickListener(nodeLongClickListener);

        containerView.addView(tView.getView());




        return rootView;
    }


    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(TreeNode node, Object value) {
            IconTreeItemHolder.IconTreeItem item = (IconTreeItemHolder.IconTreeItem) value;
            if(!item.isVideo())
                return;
            final String videoId = item.getVideoId();
            //Toast.makeText(getActivity(), "Last clicked: " + item.getVideoId(), Toast.LENGTH_SHORT).show();
            YouTubePlayerSupportFragment playerFragment = YouTubePlayerSupportFragment.newInstance();

            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.youtube_layout, playerFragment).commit();

            playerFragment.initialize( API_KEY, new YouTubePlayer.OnInitializedListener() {

                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
                    if (!wasRestored) {
                        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                        youTubePlayer.loadVideo(videoId);
                        youTubePlayer.setFullscreen(false);
                        VideosFragment.this.youTubePlayer = youTubePlayer;
                        //youTubePlayer.play();
                    }
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                    youTubeInitializationResult.getErrorDialog( getActivity(), 0 );
                    Toast.makeText( getActivity(), youTubeInitializationResult.toString(), Toast.LENGTH_SHORT ).show();
                }
            } );
        }
    };

    /*private TreeNode.TreeNodeLongClickListener nodeLongClickListener = new TreeNode.TreeNodeLongClickListener() {
        @Override
        public boolean onLongClick(TreeNode node, Object value) {
            IconTreeItemHolder.IconTreeItem item = (IconTreeItemHolder.IconTreeItem) value;
            Toast.makeText(getActivity(), "Long click: " + item.text, Toast.LENGTH_SHORT).show();
            return true;
        }
    };*/

}
