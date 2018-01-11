package com.example.barna.odusafe;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.barna.odusafe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Michael Blackwell on 9/5/2017.
 */


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    ArrayList<Event> events = new ArrayList<>();

    public EventAdapter(DatabaseReference ref) {
        ref.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                events.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Event e = postSnapshot.getValue(Event.class);
                    events.add(e);
                    Log.e("tiplist", e.getDescription());
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }

        });
    }

    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout, parent, false);
        EventViewHolder eventViewHolder = new EventViewHolder(view);
        return eventViewHolder;
    }

    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event ev = events.get(position);
        holder.event_date.setText(ev.getDate());
        holder.event_crime_type.setText(ev.getCrimeType());
    }

    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView event_date;
        TextView event_crime_type;

        public EventViewHolder(View view){
            super(view);
            event_date = (TextView)view.findViewById(R.id.event_date);
            event_crime_type = (TextView)view.findViewById(R.id.event_crime_type);

        }
    }
};
