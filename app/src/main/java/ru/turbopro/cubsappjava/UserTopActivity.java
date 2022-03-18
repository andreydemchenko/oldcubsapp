package ru.turbopro.cubsappjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import ru.turbopro.cubsappjava.databinding.ActivityUserTopBinding;

public class UserTopActivity extends AppCompatActivity {

    ActivityUserTopBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_top);

        binding = ActivityUserTopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();

        if (intent != null){
            String name = intent.getStringExtra("name");
            String status = intent.getStringExtra("status");
            String points = intent.getStringExtra("points");
            ArrayList<String> achivs = intent.getStringArrayListExtra("achievs");
            int imageid = intent.getIntExtra("imageid",R.drawable.a);

            binding.nameProfile.setText(name);
            binding.phoneProfile.setText(status);
            binding.countryProfile.setText(points);
            binding.profileImage.setImageResource(imageid);

            System.out.println(achivs);
        }
    }
}