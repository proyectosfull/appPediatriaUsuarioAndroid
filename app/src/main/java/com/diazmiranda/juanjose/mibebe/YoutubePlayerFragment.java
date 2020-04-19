package com.diazmiranda.juanjose.mibebe;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;


public class YoutubePlayerFragment extends Fragment {
    final private String API_KEY = "AIzaSyAz8V-KdP_fAQSN3QrhNVyJ5poOaxAAMBc";
    private YouTubePlayer youTubePlayer;

    public YoutubePlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        youTubePlayer.release();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate( R.layout.fragment_youtube_player, container, false );

        YouTubePlayerSupportFragment playerFragment = YouTubePlayerSupportFragment.newInstance();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_layout, playerFragment).commit();

        playerFragment.initialize( API_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
                if (!wasRestored) {
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    youTubePlayer.loadVideo( "XfP31eWXli4" );
                    YoutubePlayerFragment.this.youTubePlayer = youTubePlayer;
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                youTubeInitializationResult.getErrorDialog( getActivity(), 0 );
                Toast.makeText( getActivity(), youTubeInitializationResult.toString(), Toast.LENGTH_SHORT ).show();
            }
        } );
        return root;
    }


}
