package com.radioruet.app.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.radioruet.app.R;
import com.radioruet.app.activities.AboutActivity;
import com.radioruet.app.activities.Schedule;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Mashnoor on 5/6/17.
 */

public class Sidebar {

    public void openPlayStore(Activity activity)
    {

    }

    public static Drawer showSidebar(final Activity activity) {
        PrimaryDrawerItem listen = new PrimaryDrawerItem().withName("Listen Live").withIcon(ContextCompat.getDrawable(activity, R.drawable.listenlive));
        PrimaryDrawerItem archive = new PrimaryDrawerItem().withName("Archive").withIcon(ContextCompat.getDrawable(activity, R.drawable.archive));
        PrimaryDrawerItem schedule = new PrimaryDrawerItem().withName("Schedule").withIcon(ContextCompat.getDrawable(activity, R.drawable.schedule));
        PrimaryDrawerItem about = new PrimaryDrawerItem().withName("About Us").withIcon(ContextCompat.getDrawable(activity, R.drawable.about));
        PrimaryDrawerItem rate = new PrimaryDrawerItem().withName("Rate Us").withIcon(ContextCompat.getDrawable(activity, R.drawable.rate));

        PrimaryDrawerItem checkUpdate = new PrimaryDrawerItem().withName("Check for update").withIcon(ContextCompat.getDrawable(activity, R.drawable.update));
        return new DrawerBuilder()
                .withActivity(activity)

                .addDrawerItems(
                        listen,
                        schedule,
                        archive,
                        about,
                        rate,
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
                                final String appPackageName = activity.getPackageName();
                                try {
                                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                    break;
                                }
                                catch (android.content.ActivityNotFoundException anfe) {
                                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                    break;
                                }
                            case 5:
                                final String pname = activity.getPackageName();
                                try {
                                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pname)));
                                    break;
                                }
                                catch (android.content.ActivityNotFoundException anfe) {
                                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pname)));
                                    break;
                                }




                        }

                        return true;
                    }
                })
                .build();

    }


}
