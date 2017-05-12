package com.radioruet.android.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.radioruet.android.utils.Constants;
import com.ruetradio.android.R;

import butterknife.BindBitmap;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class SMSActivity extends Activity {

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
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending Online Message...");
    }
    @OnClick(R.id.btnSend)
    void sendDataToServer()
    {
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