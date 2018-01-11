package com.example.barna.odusafe;

import android.*;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.EditText;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Claus on 10/9/2017.
 */

public class PanicButton extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    String lat, lon;
    private static final int MY_PERMISSIONS_LOCATION = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) { //runs when new activity is called
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panicbutton); //loads lauout

        /////////////
        final String panicmessage = "<h2>What is the ODUSafe Panic Button?</h2><br><p>\n\n The Panic Button is a feature that allows you to send your information to local authorities in times of distress." +"\n" +
                "\n\n After activating, you will have 10 seconds to cancel the Panic Button.</p>";

        final String backgroundmessage = "<h2>Run in the background?</h2><br><p>" +
                "\n" +
                " This switch enables the Panic Button to run in the background."+
                "\n\n When this is switched on. ODUSafe detects when your volume is set to 0. When its set to 0, ODUSafe sends you a notification to activate the Panic Button</p>";
        /////////////


        panicDisplayInfo(panicmessage); //Displays Alert/Message info of the Panic Button
        GoogleApiClient(); //calls location

        //The question mark button which calls the pop-up function
        final ImageButton infobutton = (ImageButton) findViewById(R.id.pbinfobutton); //Button listener
        infobutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                panicDisplayInfo(panicmessage); //Displays Alert/Message info of the Panic Button

            }
        });

        //The exclamation mark button which calls the pop-up function
        final ImageButton infobutton2 = (ImageButton) findViewById(R.id.pbinfobutton2); //Button listener
        infobutton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Log.i("Tag", "activated");
                panicDisplayInfo(backgroundmessage); //Displays Alert/Message info of the Panic Button

            }
        });



        final Switch pswitch = (Switch) findViewById(R.id.pbswitch); //switch listener
        pswitch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                if( checkInformation() == false){ //calls "checkInformation()" function. This function checks to see if the fields have data
                    pswitch.setChecked(false); //set switch to off
                    return;
                }else if(checkInformation() == true && pswitch.isChecked() == true){
                    int volume = 0;registerReceiver(broadcastReceiver, new IntentFilter("broadCastName")); ///register receiver that listens for other receiver
                   startService(new Intent(getBaseContext(), PanicButtonService.class)); //when switch activated, starts listener

                    //where the background listener will be

                    //Intent i = new Intent(getApplicationContext(), Login.class);
                    //startActivity(i);
                }else if(pswitch.isChecked() == false){
                    stopService(new Intent(getBaseContext(), PanicButtonService.class)); //stops listener when switch is turned off
                }
            }
        });
    }


    //Broadcast Receiver for Panic Button.java.
    //It listens for broadcasts from PanicButtonReceiver
    //calls another function to start PanicButtonCancel.
    BroadcastReceiver broadcastReceiver =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        altactivate();


/*            Bundle b = intent.getExtras();

            String message = b.getString("message");

            //Log.e("newmesage", "" + message);*/
        }
    };

    //Activated by receiver
    //Creates a notification about the Panic Button.
    //When clicks, brings to Panic Button cancellation screen
    //
    public void altactivate(){
        //notification code : https://developer.android.com/training/notify-user/build-notification.html
        unregisterReceiver(broadcastReceiver);//unregister panic button receiver when is leaves activity
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Open to activate Panic Button!")
                .setContentText("You've set your volume to 0, open this notification to activate the Panic Button!");

        Intent resultIntent = new Intent(this, PanicButtonCancel.class);


        EditText pbfullName = (EditText) findViewById(R.id.pbfullname);
        EditText pbaddress = (EditText) findViewById(R.id.pbaddress);
        EditText pbcontact = (EditText) findViewById(R.id.pbcontact);
        //converting EditText to String
        String strpbfullName = pbfullName.getText().toString();
        String strpbaddress= pbaddress.getText().toString();
        String strpbcontact = pbcontact.getText().toString();

        if(mLastLocation != null || (lat != null && lon != null)){

        } else {
            lat = "0";
            lon="0";
        }

        //putting Strings into bundle
        Bundle extras = new Bundle(); //put field information into bundles
        extras.putString("EXTRA_FULLNAME",strpbfullName);
        extras.putString("EXTRA_ADDRESS",strpbaddress);
        extras.putString("EXTRA_CONTACT",strpbcontact);
        extras.putString("EXTRA_LAT",lat);
        extras.putString("EXTRA_LON",lon);
        resultIntent.putExtras(extras); //send bundle with intent





        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId = 001;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());


    }

   /* public void activate(){
        // Do something in response to button
        Intent intent = new Intent(this, PanicButtonCancel.class);
        EditText pbfullName = (EditText) findViewById(R.id.pbfullname);
        EditText pbaddress = (EditText) findViewById(R.id.pbaddress);
        EditText pbcontact = (EditText) findViewById(R.id.pbcontact);
        //converting EditText to String
        String strpbfullName = pbfullName.getText().toString();
        String strpbaddress= pbaddress.getText().toString();
        String strpbcontact = pbcontact.getText().toString();

        if(mLastLocation != null || (lat != null && lon != null)){

        } else {
            lat = "0";
            lon="0";
        }

        //putting Strings into bundle
        Bundle extras = new Bundle(); //put field information into bundles
        extras.putString("EXTRA_FULLNAME",strpbfullName);
        extras.putString("EXTRA_ADDRESS",strpbaddress);
        extras.putString("EXTRA_CONTACT",strpbcontact);
        extras.putString("EXTRA_LAT",lat);
        extras.putString("EXTRA_LON",lon);
        intent.putExtras(extras); //send bundle with intent
        startActivity(intent);
    }*/



    ///GPS coordinates - KAT's CODE
    //GPS Overrides
    @Override
    public void onConnected(Bundle bundle) {


        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10); // Update location every second

        // Request Permissions and start updating
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions now
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_LOCATION);
        } else {
            // Permissions have been granted, continue as usual
            // Request location updates
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            // Get the current location
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        }

        //Send to latitude and longitude strings
        if (mLastLocation != null) {
            lat = String.valueOf(mLastLocation.getLatitude());
            lon = String.valueOf(mLastLocation.getLongitude());
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lat = String.valueOf(location.getLatitude());
        lon = String.valueOf(location.getLongitude());

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        GoogleApiClient();
    }

    synchronized void GoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    //Override for Requesting Permissions
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(PanicButton.this,
                            "Your GPS position is helpful for submissions!",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
    //end of gps


    //Displays a pop-up message giving information on the Panic Button
    public void panicDisplayInfo(String message){
        //Alert popup for when the Panic Button Starts
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PanicButton.this); //alert builder
        LayoutInflater factory = LayoutInflater.from(PanicButton.this); //essentially pulls up a separate layout file
        final View view = factory.inflate(R.layout.panicalert, null); //pulling a separate xml file as a view
        alertDialogBuilder .setView(view);

        TextView panicButtonInfo = (TextView) view.findViewById(R.id.panicalert); //editing the textview in the separate xml/layout file
        panicButtonInfo.setText(Html.fromHtml(message));
        //"<h2>What is the ODUSafe Panic Button?</h2><br><p>\n\n The Panic Button is a feature that allows you to send your information to local authorities in times of distress." +
        //"\n\n After activating, you will have 10 seconds to cancel the Panic Button.</p>"

        alertDialogBuilder.setNeutralButton("", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {

            }
        });

        alertDialogBuilder.show();
    }

    public void switchDisplayInfo(){
        //Alert popup for when the Panic Button Starts
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PanicButton.this); //alert builder
        LayoutInflater factory = LayoutInflater.from(PanicButton.this); //essentially pulls up a separate layout file
        final View view = factory.inflate(R.layout.panicalert, null); //pulling a separate xml file as a view
        alertDialogBuilder .setView(view);

        TextView panicButtonInfo = (TextView) view.findViewById(R.id.panicalert); //editing the textview in the separate xml/layout file
        panicButtonInfo.setText(Html.fromHtml("<h2>Run in the background?</h2><br><p>\n\n This switch enables the Panic Button to run in the background." +
                "\n\n When this is switched on. ODUSafe detects when your volume is set to 0. When its set to 0, ODUSafe sends you a notification to activate the Panic Button</p>"));

        alertDialogBuilder.setNeutralButton("", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {

            }
        });

        alertDialogBuilder.show();
    }




    /** Called when the user taps the panic button */
    //When the user hits the panic button, itll bring them to cancellation activity page where they have 10 seconds to cancel.
    //If they don't cancel, it takes them to DisplayPanicButtonActivity.java, where it displays a confirmation page
    //If they do cancel, it brings them back to PanicButton.java
    public void manualPanicButton(View view) {

        if (checkInformation() == false){ //if information fields are not filled out, leave the function and return appropriate error messages
            return;
        }


        // Do something in response to button
        Intent intent = new Intent(this, PanicButtonCancel.class);
        EditText pbfullName = (EditText) findViewById(R.id.pbfullname);
        EditText pbaddress = (EditText) findViewById(R.id.pbaddress);
        EditText pbcontact = (EditText) findViewById(R.id.pbcontact);
        //converting EditText to String
        String strpbfullName = pbfullName.getText().toString();
        String strpbaddress= pbaddress.getText().toString();
        String strpbcontact = pbcontact.getText().toString();

        if(mLastLocation != null || (lat != null && lon != null)){

        } else {
            lat = "0";
            lon="0";
        }

        //putting Strings into bundle
        Bundle extras = new Bundle(); //put field information into bundles
        extras.putString("EXTRA_FULLNAME",strpbfullName);
        extras.putString("EXTRA_ADDRESS",strpbaddress);
        extras.putString("EXTRA_CONTACT",strpbcontact);
        extras.putString("EXTRA_LAT",lat);
        extras.putString("EXTRA_LON",lon);
        intent.putExtras(extras); //send bundle with intent
        startActivity(intent);
    }

    /*Checks if the fields are filled out, returns false if not.*/
    public boolean checkInformation(){
        EditText pbfullName = (EditText) findViewById(R.id.pbfullname);
        EditText pbaddress = (EditText) findViewById(R.id.pbaddress);
        EditText pbcontact = (EditText) findViewById(R.id.pbcontact);

        if( pbfullName.getText().toString().trim().equals("")){

            pbfullName.setError( "First name is required!" );
            return false;

        }else if( pbaddress.getText().toString().trim().equals("")){
            pbaddress.setError( "Address is required!" );
            return false;

        }else if( pbcontact.getText().toString().trim().equals("")){
            pbcontact.setError( "Emergency Contact is required!" );
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.general_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.goHome) {
            Intent home = new Intent(this,Dashboard.class);
            startActivity(home);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //public void

}
