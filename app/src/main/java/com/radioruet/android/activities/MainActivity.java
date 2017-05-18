package com.radioruet.android.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.loopj.android.http.AsyncHttpClient;
import com.radioruet.android.utils.Sidebar;
import com.radioruet.android.R;
import java.io.IOException;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;
import wseemann.media.FFmpegMediaPlayer;


@RuntimePermissions
public class MainActivity extends Activity {

    @BindView(R.id.btnListen)
    BootstrapButton listenButton;
    private FFmpegMediaPlayer player;
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        ButterKnife.bind(this);
        Sidebar.showSidebar(this);
        AsyncHttpClient client = new AsyncHttpClient();

        listenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listenButton.getText().toString().equals("Listen"))
                {
                    listenButton.setText("Connecting...");
                    listenButton.setEnabled(false);
                    player = new FFmpegMediaPlayer();
                    try {
                        player.setDataSource("http://172.104.54.52:8000/");
                        player.prepareAsync();
                        player.setOnPreparedListener(new FFmpegMediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(FFmpegMediaPlayer mp) {
                                listenButton.setText("Stop");
                                listenButton.setEnabled(true);
                                mp.start();
                            }
                        });
                    } catch (IOException e) {
                        Toast.makeText(MainActivity.this, "Can't connect to server.", Toast.LENGTH_LONG).show();
                    }

                    player.setOnInfoListener(new FFmpegMediaPlayer.OnInfoListener() {
                        @Override
                        public boolean onInfo(FFmpegMediaPlayer mp, int what, int extra) {
                            switch(what)
                            {
                                case FFmpegMediaPlayer.MEDIA_INFO_BUFFERING_START:
                                    listenButton.setEnabled(false);
                                    listenButton.setText("Buffering...");
                                    break;
                                case FFmpegMediaPlayer.MEDIA_INFO_BUFFERING_END:
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

    @OnClick(R.id.btnMessage)
    void showMessageBox()
    {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Send Message")
                .setMessage("What type of message do you want to send?")
                .setPositiveButton("Online Message", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent smsactivity = new Intent(MainActivity.this, SMSActivity.class);
                        startActivity(smsactivity);
                    }
                })
                .setNegativeButton("Secret Message", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent scrtmsg = new Intent(MainActivity.this, SecretMessage.class);
                        startActivity(scrtmsg);
                    }
                })
                .show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    void call()
    {
        Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:01826636115"));
        try {
            startActivity(in);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "Could not find an activity to place the call.", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btnCall)
    void makeCall()
    {
        MainActivityPermissionsDispatcher.callWithCheck(this);
    }

    @OnPermissionDenied(Manifest.permission.CALL_PHONE)
    void showDeniedForCall() {
        Toast.makeText(this, "Call Permission not granted!", Toast.LENGTH_LONG).show();
    }
}
