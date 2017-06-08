package com.radioruet.app.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.devbrackets.android.exomedia.AudioPlayer;
import com.devbrackets.android.exomedia.listener.OnErrorListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mikepenz.materialdrawer.Drawer;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.radioruet.app.R;
import com.radioruet.app.utils.ConnectionChecker;
import com.radioruet.app.utils.Constants;
import com.radioruet.app.utils.PermissionCheckers;
import com.radioruet.app.utils.Sidebar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;


public class MainActivity extends Activity {

    @BindView(R.id.btnListen)
    BootstrapButton listenButton;
    @BindView(R.id.txtShowName)
    AwesomeTextView txtShowName;

    private FirebaseAnalytics mFirebaseAnalytics;
    AsyncHttpClient client;
    private static final String TXT_LISTEN = "Listen";
    private static final String TXT_PLAYING = "Playing";
    private static final String TXT_BUFFERING = "Buffering...";
    private static final String TXT_STOP = "Stop";
    private static final String TXT_CONNECTING = "Connecting...";

    Drawer drawer;
    public static AudioPlayer player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Logger.addLogAdapter(new AndroidLogAdapter());

        PermissionCheckers.checkProcessOutgoingPhonePermission(this);
        PermissionCheckers.checkReadPhoneStatePermission(this);
        player = new AudioPlayer(this);
        registerAudioPlayerListeners();


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        ButterKnife.bind(this);
        drawer = Sidebar.showSidebar(this);
        client = new AsyncHttpClient();
        retriveShowName();


    }


    private void retriveShowName() {

        client.get(Constants.GET_SHOW_NAME, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                txtShowName.setText(new String(responseBody));

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                showToast("Couldn't connect to server");

            }
        });
    }

    @OnClick(R.id.btnFb)
    public void openfb() {
        startActivity(getOpenFacebookIntent(MainActivity.this));
    }

    public static Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/948695318567007"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/radioruet"));
        }
    }


    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }


    @OnClick(R.id.btnListen)
    void lisentHandler() {
        //Check if the media player is playing. If playing, then release it
        if (player.isPlaying()) {

            player.stopPlayback();


            listenButton.setText(TXT_LISTEN);

            showToast("Radio Stopped");
            return;
        }
        //First Check if connected to the internt
        if (!ConnectionChecker.haveNetworkConnection(MainActivity.this)) {
            showToast("Couldn't connect to the server");
            return;
        }


        //Play the radio


        listenButton.setText(TXT_CONNECTING);
        listenButton.setEnabled(false);
        player.setDataSource(Uri.parse(Constants.STREAMING_SOURCE));
        player.prepareAsync();


    }

    private void registerAudioPlayerListeners() {
        player.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                showToast("Radio Started");
                player.start();
                listenButton.setText(TXT_STOP);
                listenButton.setEnabled(true);
            }
        });


        player.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(Exception e) {
                showToast("Couldn't reach streaming server");
                listenButton.setEnabled(true);
                listenButton.setText(TXT_LISTEN);
                return false;
            }
        });
    }

    @OnClick(R.id.imgMenu)
    void showMenu() {
        drawer.openDrawer();

    }

    @Override
    protected void onResume() {
        super.onResume();
        retriveShowName();
        if (player != null) {
            if (player.isPlaying()) {
                listenButton.setText(TXT_STOP);
            } else {
                listenButton.setText(TXT_LISTEN);
            }
        } else {
            listenButton.setText(TXT_LISTEN);
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

    @OnClick(R.id.btnCall)
    void call() {
        Dexter.withActivity(this).withPermission(Manifest.permission.CALL_PHONE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+8801789597090"));
                try {
                    startActivity(in);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this, "Could not find an activity to place the call.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                showToast("Permission not granted!");
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        }).check();


    }


}
