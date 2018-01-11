package com.example.barna.odusafe;

import android.content.Intent;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.google.android.gms.identity.intents.Address;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//Displays the confirmation that the panic button was activated
public class DisplayPanicButtonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_panic_button);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        Bundle extras = intent.getExtras(); //extract user information
        String strpbfullName = extras.getString("EXTRA_FULLNAME");
        String strpbaddress = extras.getString("EXTRA_ADDRESS");
        String strpbcontact = extras.getString("EXTRA_CONTACT");
        String lat = extras.getString("EXTRA_LAT");
        String lon = extras.getString("EXTRA_LON");

        double dlat = Double.parseDouble(lat);
        double dlon = Double.parseDouble(lon);

        Geocoder geocoder;
        List<android.location.Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(dlat, dlon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();



        //confirmation message
        String tmessage= "<h2>Success! Now stay calm!</h2>" +
                "<br>Your information has been sent to the local authorities. " +
                "<br>" +
                "<br><b>Full Name: </b>"+ strpbfullName +
                "<br><b>Address: </b>" + strpbaddress +
                "<br><b>Phone Number: </b>" + strpbcontact +
                "<br><b>GPS Coordinates: </b>(" + lat +","+lon+ ")"+
                "<br><b>Location: </b>" + address + " , " + city + " , "+ state ;



        String message = "Panic Button Activated!" +
                "\n Your information has been sent to the local authorities and emergency contacts. " +
                "\nThis includes your:" +
                "\nFull Name: "+ strpbfullName +
                "\nAddress: " + strpbaddress +
                "\nContact: " + strpbcontact ;

        // Capture the layout's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.pbConfirm);
        textView.setText(Html.fromHtml(tmessage));
    }
}
