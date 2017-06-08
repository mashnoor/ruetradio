package com.radioruet.app.utils;

import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.radioruet.app.activities.MainActivity;

import java.util.Date;

/**
 * Created by Mashnoor on 5/28/17.
 */

public class CallReceiver extends PhonecallReceiver {

    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start)
    {
        Logger.addLogAdapter(new AndroidLogAdapter());
        Logger.d("Calling");
        if(MainActivity.player.isPlaying())
        {
            MainActivity.player.stopPlayback();
        }

    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start)
    {
        //
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end)
    {
        //
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start)
    {
        if(MainActivity.player.isPlaying())
        {
            MainActivity.player.stopPlayback();
        }
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end)
    {
        //
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start)
    {
        //
    }

}
