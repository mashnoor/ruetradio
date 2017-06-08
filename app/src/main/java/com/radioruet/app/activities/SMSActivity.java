package com.radioruet.app.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapEditText;
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

public class SMSActivity extends Activity {
    private FirebaseAnalytics mFirebaseAnalytics;

    @BindView(R.id.txtName)
    BootstrapEditText txtname;
    @BindView(R.id.txtDepartment)
    BootstrapEditText txtDepartment;
    @BindView(R.id.txtMessage)
    EditText txtMessage;
    @BindView(R.id.txtSeries)
    BootstrapEditText txtSeries;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending Online Message");


    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @OnClick(R.id.btnSend)
    void sendDataToServer()
    {
        if(!ConnectionChecker.haveNetworkConnection(SMSActivity.this))
        {
            showToast("Couldn't connect to the server");
            return;
        }
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams datas = new RequestParams();
        datas.put("name", txtname.getText().toString());
        datas.put("department", txtDepartment.getText().toString());
        datas.put("series", txtSeries.getText().toString());
        datas.put("message", txtMessage.getText().toString());
        client.post(Constants.SET_ONLINE_MSG, datas, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("--------", new String(responseBody));
                progressDialog.dismiss();
                showToast("Successfully sent message!");
                finish();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("--------", new String(responseBody));
                progressDialog.dismiss();
                showToast("Something went wrong! Please try again later");

            }
        });
    }
    private void showToast(String s)
    {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
}
