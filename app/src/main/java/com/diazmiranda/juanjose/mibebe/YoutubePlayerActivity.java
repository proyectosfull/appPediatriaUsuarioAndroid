package com.diazmiranda.juanjose.mibebe;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

public class YoutubePlayerActivity extends YouTubeBaseActivity {
    final private String API_KEY = "AIzaSyAz8V-KdP_fAQSN3QrhNVyJ5poOaxAAMBc";
    private YouTubePlayerView player;
    private YouTubeThumbnailView playerThumb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_youtube_player );

        player = findViewById( R.id.player );
        playerThumb = findViewById( R.id.player_thumb );

        playerThumb.initialize( API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo( "6Ejga4kJUts" );
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            }
        } );

        player.initialize( API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if(!b) {
                    youTubePlayer.setPlayerStyle( YouTubePlayer.PlayerStyle.DEFAULT );
                    youTubePlayer.loadVideo( "XfP31eWXli4" );

                    //youTubePlayer.setFullscreen( true );
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        } );
    }
}
