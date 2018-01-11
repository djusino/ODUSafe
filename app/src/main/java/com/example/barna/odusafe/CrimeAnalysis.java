package com.example.barna.odusafe;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CrimeAnalysis extends AppCompatActivity {

    PieChart pieChart ;
    ArrayList<Entry> entries ;
    ArrayList<String> PieEntryLabels ;
    PieDataSet pieDataSet ;
    PieData pieData ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_analysis);


        pieChart = (PieChart) findViewById(R.id.chart1);
        pieChart.setDescription("Crime Analysis");

        entries = new ArrayList<>();

        PieEntryLabels = new ArrayList<String>();





        FirebaseDatabase database = FirebaseDatabase.getInstance();
        analysis();


    }






    public void analysis(){
        //DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("events");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("events");

        ref.addValueEventListener(new ValueEventListener() {
            int countTheft = 0;
            int countHarassment =0 ;
            int countSexualAssault =0;
            int countDisturbance =0;
            int countAccident=0;
            int countIntoxication=0;
            int countShooting=0;
            int countBurglary=0;
            int countDrug=0;
            int countOthers=0;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapShot: dataSnapshot.getChildren()){

                    Event post = snapShot.getValue(Event.class);

                    switch(post.getCrimeType()){
                   // switch(snapShot.child("crimeType").getValue(Event.class)){

                        case "Theft":
                            ++countTheft;
                            Log.i("Counting Theft: ", Integer.toString(countTheft));
                            break;

                        case "Disturbance":
                            ++countDisturbance;
                            Log.i("Counting Disturbance: ", Integer.toString(countDisturbance));
                            break;

                        case "Harassment":
                            ++countHarassment;
                            Log.i("Counting Harassment", Integer.toString(countHarassment));
                            break;

                        case "Accident":
                            ++countAccident;
                            Log.i("Counting Accident", Integer.toString(countAccident));
                            break;

                        case "Drug/Alcohol Possession":
                            ++countDrug;
                            Log.i("Counting Drug/Alcohol", Integer.toString(countDrug));
                            break;

                        case "Shooting":
                            ++countShooting;
                            Log.i("Counting Shooting", Integer.toString(countShooting));
                            break;

                        case "Burglary":
                            ++countBurglary;
                            Log.i("Counting Burglary", Integer.toString(countBurglary));
                            break;

                        case "Suspicious Activity":
                            ++countSexualAssault;
                            Log.i("Counting Suspicious", Integer.toString(countSexualAssault));
                            break;

                        case "Sexual Assault":
                            ++countHarassment;
                            Log.i("Count Sexual Assault", Integer.toString(countHarassment));
                            break;

                        case "Public Intoxication":
                            ++countIntoxication;
                            Log.i("Counting Intoxication", Integer.toString(countIntoxication));
                            break;

                        case "Other":
                            ++countOthers;
                            Log.i("Counting Others: ", Integer.toString(countOthers));
                            break;

                    }// end of the switch
                }// end of the for loop
                graph();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            public void graph(){


                entries.add(new BarEntry(countTheft, 0));
                entries.add(new BarEntry(countSexualAssault, 1));
                entries.add(new BarEntry(countDisturbance, 2));
                entries.add(new BarEntry(countIntoxication, 3));
                entries.add(new BarEntry(countShooting, 4));
                entries.add(new BarEntry(countBurglary, 5));
                entries.add(new BarEntry(countHarassment, 6));
                entries.add(new BarEntry(countAccident, 7));
                entries.add(new BarEntry(countDrug, 8));
                entries.add(new BarEntry(countOthers, 9));

                PieEntryLabels.add("Theft");
                PieEntryLabels.add("Sexual Assault");
                PieEntryLabels.add("Disturbance");
                PieEntryLabels.add("Intoxication");
                PieEntryLabels.add("Shooting");
                PieEntryLabels.add("Burglary");
                PieEntryLabels.add("Harassment");
                PieEntryLabels.add("Accident");
                PieEntryLabels.add("Drug");
                PieEntryLabels.add("Other");



                pieDataSet = new PieDataSet(entries, "");

                pieDataSet.setValueTextSize(12f);
                //PieEntryLabels.setValueTextSize(12f);

                pieData = new PieData(PieEntryLabels, pieDataSet);

                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                pieChart.setData(pieData);


                pieChart.animateY(3000);
            }






        }); // end of the ref function
    }// end of the counting function


    public void displayStats(View view){


    }

}// end of the java class
