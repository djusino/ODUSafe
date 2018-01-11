package com.example.barna.odusafe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Claus on 11/11/2017.
 */

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Broadcast Receiver. Listens for volume changes.
//If volume is 0, send broadcast to receiver in PanicButton.java where it activates the Panic Button
public class PanicButtonReceiver extends BroadcastReceiver {

    //LOTS OF SCRAP
/*
    boolean broadcastTriggered = false;
    List<Integer> myList = new ArrayList();
    List<Integer> correctList = new ArrayList();
    int i = 0;
    int changed = 5;
*/

    @Override
    public void onReceive(Context context, Intent intent) {

        //correctList.addAll(Arrays.asList(1,0,1,0,1,0));


        int volume = (Integer)intent.getExtras().get("android.media.EXTRA_VOLUME_STREAM_VALUE"); //get new volume
        /*int oldVolume = intent.getIntExtra("android.media.EXTRA_PREV_VOLUME_STREAM_VALUE", 0);

        if(oldVolume - volume == 0){ //decreased
            changed = 0;
        }
        else{
            changed = 1;
        }


        if(myList.size() >9) {
            myList.remove(myList.size() - 1);
        }

        //Log.i("Tag", "Action : "+ intent.getAction() + " / volume : "+volume);
        if(i % 5 == 0) {
            myList.add(0, volume);
            //Log.i("Tag", "Action : "+ intent.getAction() + " / volume : "+volume);
           // Log.i("Tag", "Action : "+ intent.getAction() + " / volume : "+changed);
            Log.i("Tag", "Action : "+ intent.getAction() + " / volume : "+volume);
            //Log.i("Tag", "Action : "+ intent.getAction() + " / volume : "+oldVolume);
        }
        i++;
*/
//correctList.equals(myList)
        if (volume==0) { //if volume is 0
            Log.i("Tag", "Volume is 0");

            //cant directly transfer data from broadcast to activity, so making a new broadcast.

            Bundle extras = intent.getExtras(); //start new intent
            Intent i = new Intent("broadCastName");
            // Data you need to pass to activity
            i.putExtra("message", extras.getString(Integer.toString(volume)));

            context.sendBroadcast(i); //sends volume, useless though
        }

    }

}
