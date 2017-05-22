package com.radioruet.android.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.radioruet.android.R;
import com.radioruet.android.activities.SMSActivity;
import com.radioruet.android.activities.Schedule;
import com.radioruet.android.activities.SecretMessage;

/**
 * Created by Mashnoor on 5/6/17.
 */

public class Sidebar {
    public static Drawer showSidebar(final Activity activity)
    {
        PrimaryDrawerItem listen = new PrimaryDrawerItem().withName("Listen Live").withIcon(ContextCompat.getDrawable(activity, R.drawable.listenlive));
        PrimaryDrawerItem archive = new PrimaryDrawerItem().withName("Archive").withIcon(ContextCompat.getDrawable(activity, R.drawable.archive));
        PrimaryDrawerItem schedule = new PrimaryDrawerItem().withName("Schedule").withIcon(ContextCompat.getDrawable(activity, R.drawable.schedule));
        PrimaryDrawerItem about = new PrimaryDrawerItem().withName("About Us").withIcon(ContextCompat.getDrawable(activity, R.drawable.about));
        return new DrawerBuilder()
                .withActivity(activity)

                .addDrawerItems(
                        listen,
                        schedule,
                        archive,
                        about

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position)
                        {
                            case 1:
                                Intent scheduleActivity = new Intent(activity, Schedule.class);
                                activity.startActivity(scheduleActivity);
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
