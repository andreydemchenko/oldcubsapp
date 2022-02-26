package ru.turbopro.cubsappjava;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button btnSettings;
    private CardView cardEvents;
    private CardView cardQR;
    private TextView tvPoints;
    private TextView tvName;
    private TextView tvStatus;
    private TextView tvLevel;
    private ImageView imageView;

    private View view;

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        tvPoints = view.findViewById(R.id.tvPointsHome);
        tvName = view.findViewById(R.id.tvNameHome);
        tvStatus = view.findViewById(R.id.tvStatusHome);
        tvLevel = view.findViewById(R.id.tvLevelHome);
        imageView = view.findViewById(R.id.imageAchHome);
        cardEvents = view.findViewById(R.id.cardview_events_home);
        cardQR = view.findViewById(R.id.cardview_qr_home);
        btnSettings = view.findViewById(R.id.btnSettingsHome);
        btnSettings.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        });
        cardEvents.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), EventsActivity.class);
            startActivity(intent);
        });
        cardQR.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), QRCodeScannerActivity.class);
            startActivity(intent);
        });

        MainActivity mainActivity = new MainActivity();
        DatabaseReference myRef = database.getReference("users");
        if (firebaseUser != null) {
            String userId = mainActivity.getUserId();

            System.out.println("UID ========= " + userId);
            myRef.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            tvName.setText(user.getName());
                            tvPoints.setText("" + user.getPoints());
                            tvStatus.setText(user.getStatus());
                            tvLevel.setText("" + user.getLevel());
                            Picasso.get().load(user.getImageURL()).into(imageView);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("TAG", "onCancelled: Something went wrong! Error:" + error.getMessage());
                }
            });
        }

        return view;
    }
}