package ru.turbopro.cubsappjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class EventsActivity extends AppCompatActivity {

    private ArrayList<CardEvent> eventsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        eventsList = new ArrayList<>();
        addCard();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewEvents);
        RVAdapterForEvents recyclerViewAdapter = new RVAdapterForEvents(eventsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter.setOnItemClickListener(data -> {
            Intent intent = new Intent(EventsActivity.this, EventDetailActivity.class);
            intent.putExtra("event_image", data.getImage());
            intent.putExtra("event_name", data.getTitle());
            intent.putExtra("event_date", "February, 26");
            intent.putExtra("event_text", "Here will be a numerous text which have additional information with all details. It must have exact data.");
            startActivity(intent);
        });
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void addCard() {
        CardEvent event = new CardEvent("Construction robots", R.drawable.example_event);
        eventsList.add(event);
        event = new CardEvent("The best ideas about Mars", R.drawable.example_event2);
        eventsList.add(event);
        event = new CardEvent("Build your own lego vehicle", R.drawable.example_event3);
        eventsList.add(event);
        event = new CardEvent("Math olympiad 2022", R.drawable.example_event4);
        eventsList.add(event);
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