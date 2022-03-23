package ru.turbopro.cubsappjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class EventsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private RVAdapterForEvents recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        recyclerView = findViewById(R.id.recyclerViewEvents);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        //ArrayList<CardEvent> eventsList = this.getIntent().getExtras().getParcelableArrayList("events_list");

        recyclerViewAdapter = new RVAdapterForEvents(HomeFragment.eventsList);
        layoutManager = new LinearLayoutManager(EventsActivity.this);
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}