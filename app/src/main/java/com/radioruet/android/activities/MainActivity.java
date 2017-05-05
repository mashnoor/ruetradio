package com.radioruet.android.activities;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.radioruet.android.utils.Sidebar;
import com.ruetradio.android.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends Activity {

    @BindView(R.id.btnListen)
    BootstrapButton listenButton;
    private MediaPlayer player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Sidebar.showSidebar(this);


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



                }


            }
        });

    }
}
