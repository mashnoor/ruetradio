package com.radioruet.app.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.radioruet.app.R;
import com.radioruet.app.utils.ConnectionChecker;
import com.radioruet.app.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class Schedule extends Activity {

    AsyncHttpClient client;

    @BindView(R.id.txtSchedule)
    TextView txtSchedule;
    ProgressDialog dialog;

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ButterKnife.bind(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Getting data from server...");
        if (!ConnectionChecker.haveNetworkConnection(Schedule.this)) {
            showToast("Couldn't connect to the server");
            finish();
            return;
        }
        client = new AsyncHttpClient();
        client.get(Constants.GET_SCHEDULE, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                txtSchedule.setText(new String(responseBody));
                dialog.dismiss();


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(Schedule.this, "Couldn't connect to server", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                finish();
            }
        });

    }
}
