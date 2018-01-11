package com.example.barna.odusafe;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.barna.odusafe.R.attr.layoutManager;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient mGoogleApiClient;
    //DBHandler dbh;
    String databaseDumpString;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboardhome);


        //DBHandler dbh = new DBHandler(this);

        //FIREBASE
        //A connection is made with google firebase
        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("events");


        recyclerView = (RecyclerView) findViewById(R.id.rv_d);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new EventAdapterDashboard(myRef);
        recyclerView.setAdapter(adapter);






        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboardhome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent home = new Intent(Dashboard.this,Dashboard.class);
            startActivity(home);
        } else if (id == R.id.nav_report) {
            Intent report = new Intent(this,ReportTips.class);
            startActivity(report);
        } else if (id == R.id.nav_crimelog){
            Intent log = new Intent(this,Crimelog_Downloader.class);
            startActivity(log);
        }   else if (id == R.id.nav_faq) {
            Intent faq = new Intent(Dashboard.this,FAQ.class);
            startActivity(faq);
        }  else if (id == R.id.nav_contact) {
            Intent contact = new Intent(Dashboard.this,Contact.class);
            startActivity(contact);
        } else if (id == R.id.nav_tip_list) {
            Intent tiplist = new Intent(Dashboard.this,TipListActivity.class);
            startActivity(tiplist);
        }else if(id == R.id.nav_Crime_Analysis){
            Intent CrimeAnalysis = new Intent(Dashboard.this,CrimeAnalysis.class);
            startActivity(CrimeAnalysis);
        }
        else if (id == R.id.nav_Panic_Button) {
            Intent panicbutton = new Intent(Dashboard.this,PanicButton.class);
            startActivity(panicbutton);
        } else if (id == R.id.nav_map) {
            Intent map = new Intent(Dashboard.this,Crime_Map.class);
            startActivity(map);
        } else if (id == R.id.nav_signout) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            Intent homepage = new Intent(Dashboard.this,HomePage.class);
                            startActivity(homepage);
                        }
                    });
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        // Handled by the navigation menu bar, but required for mGoogleApiClient to work.
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Connection has failed and the Google API could not be contacted.
        Log.d("GoogleSignInActivity", "onConnectionFailed:" + connectionResult);
    }



}
