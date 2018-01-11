package com.example.barna.odusafe;


import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crime_Map extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    final ArrayList<WeightedLatLng> list = new ArrayList<WeightedLatLng>();

    private void addHeatMap(GoogleMap map) {
        HeatmapTileProvider mProvider;
        TileOverlay mOverlay;

        // Create the gradient.
        int[] colors = {
                Color.rgb(0, 102, 0), // green
                Color.rgb(102, 0, 0)    // red
        };

        float[] startPoints = {
                0.2f, 1f
        };

        Gradient gradient = new Gradient(colors, startPoints);

        //Check if the list is empty.
        Log.d("CRIMEMAP", String.valueOf(list.isEmpty()));
        if(list.isEmpty()){
            //Do Nothing
        } else {
            // Create a heat map tile provider, passing it the latlngs of the incidents.
            mProvider = new HeatmapTileProvider.Builder()
                    .weightedData(list)
                    .radius(50)
                    .opacity(0.75)
                    //.gradient(gradient)
                    .build();
            // Add a tile overlay to the map, using the heat map tile provider.
            mOverlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Get the data: latitude/longitude positions of incidents and figure out their severity level
        // Initialize connection to Firebase
        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("events");

        myRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //iterate through a snapshot of the database and save them as an event list
                List<Event> events = new ArrayList<Event>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Event e = noteDataSnapshot.getValue(Event.class);
                    events.add(e);
                }

                for(Event e: events){
                    //Store any necessary values
                    String location = e.getLocation();
                    String severity = e.getSeverity();

                    //Ensure that the location is a GPS location, if not, do nothing with this event.
                    if(location.matches("-{0,1}[0-9]+.[0-9]+,-{0,1}[0-9]+.[0-9]+")){
                        String[] parts = location.split(" *, *");
                        double lat = Double.parseDouble(parts[0]), lon = Double.parseDouble(parts[1]);
                        double weight = 0;

                        if(severity.matches("High")){
                            weight = 2.0;
                        } else if(severity.matches("Medium")){
                            weight = 1.0;
                        } else if(severity.matches("Low")){
                            weight = 0.5;
                        }

                        Log.d("CRIMEMAP", parts[0] + ", " + parts[1] + ":" + severity + ", " + weight);
                        list.add(new WeightedLatLng(new LatLng(lat,lon), weight));
                    } else {
                        //Do Nothing, this value can't be used.
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Do Nothing
            }
        });


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime__map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        addHeatMap(googleMap);
        // Add move the camera to the User's current location or ODU's location
        LatLng oduLocation = new LatLng(36.886559, -76.304936);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(oduLocation, 16));
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
}
