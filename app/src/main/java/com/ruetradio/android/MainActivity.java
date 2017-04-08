package com.ruetradio.android;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.IOException;

public class MainActivity extends Activity {

    Button listenButton;
    private MediaPlayer player;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listenButton = (Button) findViewById(R.id.btnListen);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);
        listenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(listenButton.getText().toString().equals("Listen"))
                {
                    listenButton.setText("Connecting...");
                    listenButton.setEnabled(false);

                    player = new MediaPlayer();
                    try {
                        player.setDataSource("http://104.131.22.246:8000/");
                        player.prepareAsync();
                        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                listenButton.setText("Stop");
                                listenButton.setEnabled(true);
                                mp.start();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

                        public void onBufferingUpdate(MediaPlayer mp, int percent) {
                            progressBar.setSecondaryProgress(percent);
                            Log.i("Buffering", "" + percent);
                        }
                    });
                    player.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                        @Override
                        public boolean onInfo(MediaPlayer mp, int what, int extra) {
                            switch(what)
                            {
                                case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                                    listenButton.setEnabled(false);
                                    listenButton.setText("Buffering...");
                                    break;
                                case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                                    listenButton.setEnabled(true);
                                    listenButton.setText("Stop");
                                    break;

                            }

                            return false;
                        }
                    });

                }
                else
                {
                    if (player.isPlaying()) {
                        player.stop();
                        player.release();

                    }

                    listenButton.setText("Listen");

                    progressBar.setVisibility(View.INVISIBLE);

                }


            }
        });

    }
}
