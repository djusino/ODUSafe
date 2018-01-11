package com.example.barna.odusafe;



import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import java.util.logging.Logger;

import static android.content.ContentValues.TAG;

/**
 * Created by Claus on 11/11/2017.
 */

///Declares a broadcast receiver. This receiver listens for volume changes. The broadcaster is only listening while
    ///the service is running.
public class PanicButtonService extends Service {
    PanicButtonReceiver mReceiver; ///receiver


    @Override
    public void onCreate() {

        //Log.i("Tag","so far so good"); //debugging

        mReceiver = new PanicButtonReceiver(); ///declare receiver
        IntentFilter filter = new IntentFilter(); //Filter which defines what triggers the receiver
        filter.addAction("android.media.VOLUME_CHANGED_ACTION"); //triggers when volume changes
        registerReceiver(mReceiver, filter); //register receiver (not using manifest to register)
    }

    public void onDestroy(){

        unregisterReceiver(mReceiver); //REQUIRED. //destroys receiver when service ends
        //cancel notifications when app closes/service ends
        //doesnt work...
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();

    }
    public IBinder onBind (Intent intent){ //lol idk what this is
        return null;
    }





}