package com.radioruet.android.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.radioruet.android.activities.SMSActivity;

/**
 * Created by Mashnoor on 5/6/17.
 */

public class Sidebar {
    public static void showSidebar(final Activity activity)
    {
        PrimaryDrawerItem listen = new PrimaryDrawerItem().withName("Listen Live");
        PrimaryDrawerItem archive = new PrimaryDrawerItem().withName("Archive");
        PrimaryDrawerItem sms = new PrimaryDrawerItem().withName("SMS and Call");
        PrimaryDrawerItem schedules = new PrimaryDrawerItem().withName("Schedule");
        PrimaryDrawerItem call = new PrimaryDrawerItem().withName("Call US!");
        PrimaryDrawerItem like = new PrimaryDrawerItem().withName("Like on FB");
        PrimaryDrawerItem about = new PrimaryDrawerItem().withName("About Us");
        PrimaryDrawerItem exit = new PrimaryDrawerItem().withName("Exit");


        new DrawerBuilder()
                .withActivity(activity)

                .addDrawerItems(
                        listen,
                        archive,
                        sms,
                        schedules,
                        call,
                        like,
                        about,
                        exit
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position)
                        {
                            case 2:
                                Intent i = new Intent(activity, SMSActivity.class);
                                activity.startActivity(i);

                        }

                        return true;
                    }
                })
                .build();

    }



}
