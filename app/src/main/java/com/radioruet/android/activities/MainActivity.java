package com.radioruet.android.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.loopj.android.http.AsyncHttpClient;
import com.radioruet.android.R;
import com.radioruet.android.utils.ConnectionChecker;
import com.radioruet.android.utils.Constants;
import com.radioruet.android.utils.Sidebar;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hybridmediaplayer.HybridMediaPlayer;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;
import wseemann.media.FFmpegMediaPlayer;


@RuntimePermissions
public class MainActivity extends Activity {

    @BindView(R.id.btnListen)
    BootstrapButton listenButton;
    private HybridMediaPlayer player;
    private FirebaseAnalytics mFirebaseAnalytics;
    AsyncHttpClient client;
    private static final String TXT_LISTEN = "Listen";
    private static final String TXT_PLAYING = "Playing";
    private static final String TXT_BUFFERING = "Buffering...";
    private static final String TXT_STOP = "Stop";
    private static final String TXT_CONNECTING = "Connecting...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        ButterKnife.bind(this);
        Sidebar.showSidebar(this);
        client = new AsyncHttpClient();
        final String TAG = "--------";







    }

    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }


    @OnClick(R.id.btnListen)
    void lisentHandler() {
        //Check if the media player is playing. If playing, then release it
        if (player!=null) {
            player.release();
            player = null;


            listenButton.setText(TXT_LISTEN);

            showToast("Streaming stopped");
            return;
        }
        //First Check if connected to the internt
        if (!ConnectionChecker.haveNetworkConnection(MainActivity.this)) {
            showToast("Can't connect to the server");
            return;
        }
        //Check if the show is on air
        //To-Do

        //Play the radio
        player = HybridMediaPlayer.getInstance(MainActivity.this);
        player.setDataSource(Constants.STREAMING_SOURCE);
        player.prepare();
        listenButton.setText(TXT_CONNECTING);
        listenButton.setEnabled(false);
        player.setOnPreparedListener(new HybridMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(HybridMediaPlayer hybridMediaPlayer) {
                player.play();
                listenButton.setEnabled(true);
                listenButton.setText(TXT_STOP);
            }
        });








    }

    @Override
    protected void onResume() {
        super.onResume();
        if(player!=null && player.isPlaying())
        {
            listenButton.setText(TXT_STOP);
        }
    }

    @OnClick(R.id.btnMessage)
    void showMessageBox() {
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
    void call() {
        Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:01826636115"));
        try {
            startActivity(in);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "Could not find an activity to place the call.", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btnCall)
    void makeCall() {
        MainActivityPermissionsDispatcher.callWithCheck(this);
    }

    @OnPermissionDenied(Manifest.permission.CALL_PHONE)
    void showDeniedForCall() {
        Toast.makeText(this, "Call Permission not granted!", Toast.LENGTH_LONG).show();
    }
}
