package com.radioruet.android.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mikepenz.materialdrawer.Drawer;
import com.radioruet.android.R;
import com.radioruet.android.utils.ConnectionChecker;
import com.radioruet.android.utils.Constants;
import com.radioruet.android.utils.Sidebar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import hybridmediaplayer.HybridMediaPlayer;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
public class MainActivity extends Activity {

    @BindView(R.id.btnListen)
    BootstrapButton listenButton;
    @BindView(R.id.txtShowName)
    AwesomeTextView txtShowName;
    private HybridMediaPlayer player;
    private FirebaseAnalytics mFirebaseAnalytics;
    AsyncHttpClient client;
    private static final String TXT_LISTEN = "Listen";
    private static final String TXT_PLAYING = "Playing";
    private static final String TXT_BUFFERING = "Buffering...";
    private static final String TXT_STOP = "Stop";
    private static final String TXT_CONNECTING = "Connecting...";
    String showname = "None";
    Drawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
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
                showname = txtShowName.getText().toString();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        retriveShowName();
                    }
                }, 120000);

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
        if (player != null) {
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
                showToast("Streaming Started!");
            }
        });
        player.setOnErrorListener(new HybridMediaPlayer.OnErrorListener() {
            @Override
            public void onError(Exception e, HybridMediaPlayer hybridMediaPlayer) {
                showToast("Couldn't reach streaming server");
                listenButton.setEnabled(true);
                listenButton.setText(TXT_LISTEN);
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
        if (player != null && player.isPlaying()) {
            listenButton.setText(TXT_STOP);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    void call() {
        Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+8801789597090"));
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
