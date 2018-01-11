package com.example.barna.odusafe;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.location.Location;
import android.Manifest;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Katrine Gausin on 9/17/2017.
 */


public class ReportTips extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {// implements AdapterView.OnItemSelectedListener

    private Button btnSend;
    private EditText inputDescription, inputLocation;
    private CheckBox responseCheckbox;
    private Spinner CrimeType, severityLevel;
    // private DBHandler dbh; //Define a DBHandler object

    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    String lat, lon;
    private static final int MY_PERMISSIONS_LOCATION = 1;

    // Just variables for the commented out setters and getters
    // private String description1, crimeType1, location1, levelType1, dateofincident1;
    // private String reportername1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_tips);

        addItemstoCrimeSpinner();
        addItemstoSeverity();
        addListenerOnButton();
        GoogleApiClient();
    }

    /*
        Add items to the CrimeType Spinner dynamically
     */
    public void addItemstoCrimeSpinner() {
        CrimeType = (Spinner) findViewById(R.id.CrimeType);

        List<String> list = new ArrayList<String>();
        list.add("Theft");
        list.add("Burglary");
        list.add("Accident");
        list.add("Drug/Alcohol Possession");
        list.add("Shooting");
        list.add("Suspicious Activity");
        list.add("Harassment");
        list.add("Sexual Assault");
        list.add("Public Intoxication");
        list.add("Disturbance");
        list.add("Other");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        CrimeType.setAdapter(adapter);
    }

    /*
       Add items to the Severity Spinner dynamically
    */
    public void addItemstoSeverity() {
        severityLevel = (Spinner) findViewById(R.id.severityLevel);

        List<String> list2 = new ArrayList<String>();
        list2.add("Low");
        list2.add("Medium");
        list2.add("High");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list2);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        severityLevel.setAdapter(adapter2);
    }

    /*
         Retrieve all the fields submitted through the form
         and update the database
     */
    public void addListenerOnButton() {

        CrimeType = (Spinner) findViewById(R.id.CrimeType);
        severityLevel = (Spinner) findViewById(R.id.severityLevel);
        btnSend = (Button) findViewById(R.id.Submit_Tip);

        //dbh = DBHandler.getInstance(this);

        //FIREBASE
        //A connection is made with google firebase
        FirebaseApp.initializeApp(this);
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("events");


        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Get the text and CheckBox Field
                inputDescription = (EditText) findViewById(R.id.inputCrimeDescription);
                String description = inputDescription.getText().toString();

                inputLocation = (EditText) findViewById(R.id.inpuLoc);
                String location = "";
                // Temporary - We need two different location entries.
                if(mLastLocation != null || (lat != null && lon != null)){
                    location = lat + "," + lon;
                } else {
                    location = inputLocation.getText().toString();
                }

                String date = DateFormat.getDateTimeInstance().format(new Date());

                responseCheckbox = (CheckBox) findViewById(R.id.CheckBoxResponse);
                boolean bRequiresResponse = responseCheckbox.isChecked();
                String reporter = "Anonymous";
                if (!bRequiresResponse) {
                    reporter = "Get Name: Google Authentication";
                }

                // Get the spinner values selected
                String severity = String.valueOf(severityLevel.getSelectedItem());
                String crimeType = String.valueOf(CrimeType.getSelectedItem());


                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("events");
                Event event = new Event(description, crimeType, reporter, location, severity, date);
                DatabaseReference newRef = myRef.push();
                newRef.setValue(event); //adds a new event to the firebase with a random id value

                // Shortly display all the selected fields to check their values
                Toast.makeText(ReportTips.this,
                        "You have successfully submitted a tip : " +
                                "\nCrime Type : " + crimeType +
                                "\nSeverity : " + severity +
                                "\nDescription : " + description +
                                "\nLocation : " + location +
                                "\nDate Submitted : " + date +
                                "\nReporter: " + reporter,
                        Toast.LENGTH_LONG).show();

                //dbh.addEvent(new Event(description, crimeType, reporter, location, severity, date));


                // Switch to the Dashboard with the new updated event
                Intent intent = new Intent(ReportTips.this, Dashboard.class);
                startActivity(intent);

            }

        });
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
            Intent home = new Intent(this, Dashboard.class);
            startActivity(home);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_LOCATION);
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
                    Toast.makeText(ReportTips.this,
                            "Your GPS position is helpful for submissions!",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
     /*


    public String getDescription1() {
        return description1;
    }

    public String getLocation1() {
        return location1;
    }

    public String getDateofincident1() {
        return dateofincident1;
    }

    public String getCrimeType1() {
        return crimeType1;
    }

    public String getLevelType1() {
        return levelType1;
    }

    public String getReportername1() {
        return reportername1;
    }

    public String getSeverity1() {
        return levelType1;
    }

    public void setCrimeType1(String s) {
        this.crimeType1 = s;
    }

    public void setSeverityLevel1(String s) {
        this.levelType1 = s;
    }

    public void setInputDescription1(String s) {
        //public void setInputDescription1(String Description1){
        this.description1 = s;
    }

    public void setLocation1(String s) {
        this.location1 = s;
    }

    public void setDateofincident1(String s) {
        this.dateofincident1 = s;
    }

    public void setReportername1(String s) {
        this.reportername1 = s;
    }


    //insert on submit
    public void submitClick(View view) {
        insert();
    }
    */


}