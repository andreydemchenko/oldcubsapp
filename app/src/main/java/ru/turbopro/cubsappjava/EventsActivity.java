package ru.turbopro.cubsappjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class EventsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewEvents);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        addCards();
    }

    private void addCards() {
        ArrayList<CardEvent> eventsList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("events");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot reference : snapshot.getChildren()) {
                        CardEvent event = new CardEvent(
                                reference.getKey(),
                                reference.child("name").getValue().toString(),
                                reference.child("date").getValue().toString(),
                                reference.child("description").getValue().toString(),
                                reference.child("image").getValue().toString());
                        eventsList.add(event);
                    }
                }
                RVAdapterForEvents recyclerViewAdapter = new RVAdapterForEvents(eventsList);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(EventsActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerViewAdapter.setOnItemClickListener(data -> {
                    Intent intent = new Intent(EventsActivity.this, EventDetailActivity.class);
                    intent.putExtra("event_image", data.getImage());
                    intent.putExtra("event_name", data.getTitle());
                    intent.putExtra("event_date", data.getDate());
                    intent.putExtra("event_text", data.getDescription());
                    startActivity(intent);
                });
                recyclerView.setAdapter(recyclerViewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "onCancelled: Something went wrong! Error:" + error.getMessage());
            }
        });
        /*try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}