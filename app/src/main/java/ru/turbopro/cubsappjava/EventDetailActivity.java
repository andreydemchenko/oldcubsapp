package ru.turbopro.cubsappjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class EventDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView tvTitle;
    private TextView tvDate;
    private  TextView tvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        imageView = findViewById(R.id.imageViewEventDetail);
        tvTitle = findViewById(R.id.titleEventDetail);
        tvDate = findViewById(R.id.dateEventDetail);
        tvText = findViewById(R.id.mainTextEventDetail);

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            String title = arguments.getString("event_name");
            String date = arguments.getString("event_date");
            String text = arguments.getString("event_text");
            String image = arguments.getString("event_image");
            tvTitle.setText(title);
            tvDate.setText(date);
            tvText.setText(text);
            Picasso.get().load(image).into(imageView);
        }
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