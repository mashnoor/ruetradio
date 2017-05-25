package com.radioruet.android.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.radioruet.android.R;
import com.radioruet.android.activities.AboutActivity;
import com.radioruet.android.activities.SMSActivity;
import com.radioruet.android.activities.Schedule;
import com.radioruet.android.activities.SecretMessage;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Mashnoor on 5/6/17.
 */

public class Sidebar {
    public static Drawer showSidebar(final Activity activity) {
        PrimaryDrawerItem listen = new PrimaryDrawerItem().withName("Listen Live").withIcon(ContextCompat.getDrawable(activity, R.drawable.listenlive));
        PrimaryDrawerItem archive = new PrimaryDrawerItem().withName("Archive").withIcon(ContextCompat.getDrawable(activity, R.drawable.archive));
        PrimaryDrawerItem schedule = new PrimaryDrawerItem().withName("Schedule").withIcon(ContextCompat.getDrawable(activity, R.drawable.schedule));
        PrimaryDrawerItem about = new PrimaryDrawerItem().withName("About Us").withIcon(ContextCompat.getDrawable(activity, R.drawable.about));

        PrimaryDrawerItem checkUpdate = new PrimaryDrawerItem().withName("Check for update").withIcon(ContextCompat.getDrawable(activity, R.drawable.update));
        return new DrawerBuilder()
                .withActivity(activity)

                .addDrawerItems(
                        listen,
                        schedule,
                        archive,
                        about,
                        checkUpdate

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position) {
                            case 2:
                                Toast.makeText(activity, "Archive not available yet. Wait for update", Toast.LENGTH_LONG).show();
                                break;

                            case 1:
                                Intent scheduleActivity = new Intent(activity, Schedule.class);
                                activity.startActivity(scheduleActivity);

                                break;
                            case 3:
                                Intent aboutActivity = new Intent(activity, AboutActivity.class);
                                activity.startActivity(aboutActivity);
                                break;
                            case 4:
                                final ProgressDialog dialog = new ProgressDialog(activity);
                                dialog.setMessage("Checking for update");
                                if (!ConnectionChecker.haveNetworkConnection(activity)) {
                                    Toast.makeText(activity, "Couldn't connect to server", Toast.LENGTH_LONG).show();
                                } else {

                                    AsyncHttpClient client = new AsyncHttpClient();
                                    client.get(Constants.GET_LATEST_VERSION, new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onStart() {
                                            dialog.show();
                                        }

                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                            String version = new String(responseBody);
                                            if (version.equals(Constants.CURRENT_VERSION)) {
                                                Toast.makeText(activity, "You are using the latest version", Toast.LENGTH_LONG).show();

                                                dialog.dismiss();
                                            } else {
                                                new AlertDialog.Builder(activity).
                                                        setTitle("Update Available!").
                                                        setMessage("There is an update for Radio RUET App.\nDownload from www.radioruet.com").
                                                        create().
                                                        show();
                                                dialog.dismiss();


                                            }
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                            Toast.makeText(activity, "Couldn't connect to server", Toast.LENGTH_LONG).show();
                                            dialog.dismiss();
                                        }
                                    });
                                }

                        }

                        return true;
                    }
                })
                .build();

    }


}
