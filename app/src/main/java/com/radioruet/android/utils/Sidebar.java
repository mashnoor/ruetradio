package com.radioruet.android.utils;

import android.app.Activity;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * Created by Mashnoor on 5/6/17.
 */

public class Sidebar {
    public static void showSidebar(Activity activity)
    {
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("Listen Live");

        new DrawerBuilder()
                .withActivity(activity)

                .addDrawerItems(
                        item1
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        return true;
                    }
                })
                .build();

    }

}
