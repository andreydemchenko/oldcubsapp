package ru.turbopro.cubsappjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private BottomNavigationView bottomNavigationView;

    private HomeFragment homeFragment;
    private ShopFragment shopFragment;
    private TopFragment topFragment;
    
    private FirebaseUser currentUser;
    private static String userId;
    private SharedPreferences sharedPreferences;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            userId = arguments.getString("userId");
            editor.putString("MainActivityUserId", userId);
            editor.apply();
        }

        userId = sharedPreferences.getString("MainActivityUserId", "");
/*
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User userhere = dataSnapshot.getValue(User.class);

                    Log.e("MainActivity", "User data is changed!" + userhere.getName() + ", " + userhere.getLogin());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("MainActivity", "Failed to read user", error.toException());
            }
        });*/

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.homeItem);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.homeItem:
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();
                    return true;

                case R.id.shopItem:
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, shopFragment).commit();
                    return true;

                case R.id.topItem:
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, topFragment).commit();
                    return true;
            }
            return false;
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, new HomeFragment()).commit();
        /*mBtn.setText("Sign out");
        mBtn.setOnClickListener(view -> {
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        });*/
    }

    public String getUserId(){
        return userId;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        }
        homeFragment = new HomeFragment();
        shopFragment = new ShopFragment();
        topFragment = new TopFragment();
    }
}