package ru.turbopro.cubsappjava;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

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
  //  private static final int RESULT_OK = 1;
    private final int PICK_IMAGE_REQUEST = 71;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button btnSettings;
    private CardView cardEvents;
    private CardView cardQR;
    private CardView cardImage;
    private TextView tvPoints;
    private TextView tvName;
    private TextView tvStatus;
    private TextView tvLevel;
    private ImageView imageView;

    private Uri filePath;

    private View view;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
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
        cardImage = view.findViewById(R.id.cardview_image_home);
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
        cardImage.setOnClickListener(view1 -> {
            chooseImage();
            uploadImage();
        });

        MainActivity mainActivity = new MainActivity();
        DatabaseReference myRef = database.getReference("users");
        if (firebaseUser != null) {
            String userId = mainActivity.getUserId();

            System.out.println("UID ========= " + userId);
            myRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            char[] name = user.getName().toCharArray();
                            char[] name2 = new char[100];
                            int j = 0;
                            for (int i = 0; i < user.getName().length(); i++) {
                                if (name[i] == ' ') j++;
                                if (j == 2) break;
                                name2[i] = name[i];
                            }
                            tvName.setText(String.valueOf(name2));
                            tvPoints.setText("" + user.getPoints());
                            tvStatus.setText(user.getStatus());
                            tvLevel.setText("" + user.getLevel());
                            Picasso.get().load(user.getUser_image_URL()).into(imageView);
                            System.out.println("+++++++++++ name: "+user.getName()+" pass: "+user.getPassword() +" level: "+user.getLevel() +" image: "+user.getUser_image_URL());
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

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage() {
        FirebaseStorage storage;
        StorageReference storageReference;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        MainActivity mainActivity = new MainActivity();
        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users").child(mainActivity.getUserId());

        if(filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

         /*   Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + getResources().getResourcePackageName(filePath)
                    + '/' + getResources().getResourceTypeName(R.drawable.ico_face) + '/' + getResources().getResourceEntryName(R.drawable.ico_face));
*/
            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                        reference.child("userImage").setValue(filePath);
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null ) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}