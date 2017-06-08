package com.radioruet.app.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.radioruet.app.utils.ConnectionChecker;
import com.radioruet.app.utils.Constants;
import com.radioruet.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class SecretMessage extends Activity {

    private FirebaseAnalytics mFirebaseAnalytics;
    @BindView(R.id.txtMessage)
    EditText txtMessage;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret_message);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending Secret Message...");
    }

    @OnClick(R.id.btnSend)
    void sendDataToServer() {
        if(!ConnectionChecker.haveNetworkConnection(SecretMessage.this))
        {
            showToast("Couldn't connect to the server");
            return;
        }
        final AsyncHttpClient client = new AsyncHttpClient();
        RequestParams datas = new RequestParams();
        datas.put("message", txtMessage.getText().toString());
        client.post(Constants.SET_SECRET_MSG, datas, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                progressDialog.dismiss();
                showToast("Successfully sent secret message!");
                finish();

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                progressDialog.dismiss();
                showToast("Something went wrong! Please try again later");

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void showToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
}
