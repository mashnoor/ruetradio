package com.radioruet.android.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.radioruet.android.activities.SMSActivity;
import com.radioruet.android.activities.SecretMessage;

/**
 * Created by Mashnoor on 5/6/17.
 */

public class Sidebar {
    public static void showSidebar(final Activity activity)
    {
        PrimaryDrawerItem listen = new PrimaryDrawerItem().withName("Listen Live");
        PrimaryDrawerItem archive = new PrimaryDrawerItem().withName("Archive");
        PrimaryDrawerItem schedule = new PrimaryDrawerItem().withName("Schedule");
        PrimaryDrawerItem about = new PrimaryDrawerItem().withName("About Us");
        PrimaryDrawerItem exit = new PrimaryDrawerItem().withName("Exit");


        new DrawerBuilder()
                .withActivity(activity)

                .addDrawerItems(
                        listen,
                        archive,
                        about,
                        exit
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position)
                        {
                            case 2:
                                Intent smsactivity = new Intent(activity, SMSActivity.class);
                                activity.startActivity(smsactivity);
                                break;
                            case 3:
                                Intent scrtmsg = new Intent(activity, SecretMessage.class);
                                activity.startActivity(scrtmsg);

                        }

                        return true;
                    }
                })
                .build();

    }



}
