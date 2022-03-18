package ru.turbopro.cubsappjava;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

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
    private final int GALLERY = 1;
    private static final int CAMERA = 2;
    //  private static final int RESULT_OK = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button btnSettings;
    private CardView cardEvents;
    private CardView cardQR;
    private CircleImageView image;
    private TextView tvPoints;
    private TextView tvName;
    private TextView tvStatus;
    private TextView tvLevel;
    private View view;
    Globals globals;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private UserSQLiteDbHelper dbHelper;
    private String[] projection = {
            dbHelper.ID,
            dbHelper.FULL_NAME,
            dbHelper.LOGIN,
            dbHelper.ACHIEVEMENTS,
            dbHelper.ALL_POINTS,
            dbHelper.POINTS,
            dbHelper.HOURS,
            dbHelper.IMAGE};

    private Cursor cursor;
    private SQLiteDatabase db;

    private int idColumnIndex;
    private int nameColumnIndex;
    private int loginColumnIndex;
    private int achievementsColumnIndex;
    private int allpointsColumnIndex;
    private int pointsColumnIndex;
    private int hoursColumnIndex;
    private int imageColumnIndex;
    private String currentName;
    private int currentAllpoints;
    private int currentPoints;
    private int currentHours;
    private String currentLogin;
    private String currentAchiv;
    private byte[] currentImageBlob;

    private Bitmap posterBitmap;

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
        globals = new Globals();
        view = inflater.inflate(R.layout.fragment_home, container, false);
        tvPoints = view.findViewById(R.id.tvPointsHome);
        tvName = view.findViewById(R.id.tvNameHome);
        tvStatus = view.findViewById(R.id.tvStatusHome);
        tvLevel = view.findViewById(R.id.tvLevelHome);
        cardEvents = view.findViewById(R.id.cardview_events_home);
        cardQR = view.findViewById(R.id.cardview_qr_home);
        image = view.findViewById(R.id.userImageHome);
        btnSettings = view.findViewById(R.id.btnSettingsHome);

        dbHelper = new UserSQLiteDbHelper(getActivity());
        db = dbHelper.getReadableDatabase();
        cursor = db.query(
                dbHelper.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

            MainActivity mainActivity = new MainActivity();
            DatabaseReference myRef = database.getReference("users");
            if (firebaseUser != null) {
                String userId = mainActivity.getUserId();
                myRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            User user = snapshot.getValue(User.class);
                            if (user != null) {
                                if (cursor.getCount() != 0 && cursor.moveToFirst()) {
                                    idColumnIndex = cursor.getColumnIndex(dbHelper.ID);
                                    nameColumnIndex = cursor.getColumnIndex(dbHelper.FULL_NAME);
                                    loginColumnIndex = cursor.getColumnIndex(dbHelper.LOGIN);
                                    achievementsColumnIndex = cursor.getColumnIndex(dbHelper.ACHIEVEMENTS);
                                    allpointsColumnIndex = cursor.getColumnIndex(dbHelper.ALL_POINTS);
                                    pointsColumnIndex = cursor.getColumnIndex(dbHelper.POINTS);
                                    hoursColumnIndex = cursor.getColumnIndex(dbHelper.HOURS);
                                    imageColumnIndex = cursor.getColumnIndex(dbHelper.IMAGE);

                                    //int currentID = cursor.getInt(idColumnIndex);
                                    currentName = cursor.getString(nameColumnIndex);
                                    currentAllpoints = cursor.getInt(allpointsColumnIndex);
                                    currentPoints = cursor.getInt(pointsColumnIndex);
                                    currentHours = cursor.getInt(hoursColumnIndex);
                                    currentLogin = cursor.getString(loginColumnIndex);
                                    //String currentImage = cursor.getString(imageColumnIndex);
                                    currentAchiv = cursor.getString(achievementsColumnIndex);
                                    currentImageBlob = cursor.getBlob(imageColumnIndex);
                                    /*
                                    System.out.println("\n" + currentID + " - " +
                                            currentName + " - " +
                                            currentAllpoints + " - " +
                                            currentPoints + " - " +
                                            currentHours + " - " +
                                            currentLogin + " - " +
                                            currentImage + " - " +
                                            currentAchiv);*/
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<ArrayList<String>>() {
                                    }.getType();
                                    ArrayList<String> achiv = gson.fromJson(currentAchiv, type);
                                    if (!user.getName().equals(currentName) || !user.getLogin().equals(currentLogin) || user.getPoints() != currentPoints ||
                                            user.getAll_points() != currentAllpoints || user.getHours() != currentHours ||
                                            !user.getAchiv_progress().toString().equals(achiv.toString())) {
                                        dbHelper.update(user.getName(), user.getLogin(), user.getPoints(), user.getAll_points(), user.getHours(), user.getUser_image_URL(), user.getAchiv_progress());
                                    } else System.out.println("nothing has update +++++");
                                    cursor.moveToPosition(nameColumnIndex - 1);
                                    tvName.setText(user.getNameWP());
                                    tvPoints.setText(user.getPoints() + "");
                                    Picasso.get().load(user.getUser_image_URL()).into(image);
                                    cursor.moveToNext();
                                } else {
                                    dbHelper.insert(user.getName(), user.getLogin(), user.getPoints(), user.getAll_points(), user.getHours(), getArrayOfBytesFromBitmap(getBitmapFromURL(user.getUser_image_URL())), user.getAchiv_progress());
                                    tvName.setText(user.getNameWP());
                                    tvPoints.setText(user.getPoints() + "");
                                    Picasso.get().load(user.getUser_image_URL()).into(image);
                                }
                            }
                            System.out.println("+++++++++++ name: " + user.getName() + " pass: " + user.getPassword() + " level: " + user.getLevel() + " achiv: " + user.getAchiv_progress() + " image: " + user.getUser_image_URL());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("TAG", "onCancelled: Something went wrong! Error:" + error.getMessage());
                    }
                });
            }

            if (cursor.getCount() != 0 && cursor.moveToFirst()) {
                idColumnIndex = cursor.getColumnIndex(dbHelper.ID);
                nameColumnIndex = cursor.getColumnIndex(dbHelper.FULL_NAME);
                loginColumnIndex = cursor.getColumnIndex(dbHelper.LOGIN);
                achievementsColumnIndex = cursor.getColumnIndex(dbHelper.ACHIEVEMENTS);
                allpointsColumnIndex = cursor.getColumnIndex(dbHelper.ALL_POINTS);
                pointsColumnIndex = cursor.getColumnIndex(dbHelper.POINTS);
                hoursColumnIndex = cursor.getColumnIndex(dbHelper.HOURS);
                imageColumnIndex = cursor.getColumnIndex(dbHelper.IMAGE);

                //int currentID = cursor.getInt(idColumnIndex);
                currentName = cursor.getString(nameColumnIndex);
                currentAllpoints = cursor.getInt(allpointsColumnIndex);
                currentPoints = cursor.getInt(pointsColumnIndex);
                currentHours = cursor.getInt(hoursColumnIndex);
                currentLogin = cursor.getString(loginColumnIndex);
                //String currentImage = cursor.getString(imageColumnIndex);
                currentAchiv = cursor.getString(achievementsColumnIndex);
                currentImageBlob = cursor.getBlob(imageColumnIndex);
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<String>>() {
                }.getType();
                ArrayList<String> achiv = gson.fromJson(currentAchiv, type);
                cursor.moveToPosition(nameColumnIndex - 1);
                tvName.setText(currentName);
                tvPoints.setText(currentPoints + "");

                if (currentImageBlob != null) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(currentImageBlob, 0, currentImageBlob.length);
                    image.setImageBitmap(bmp);
                }

//                Picasso.get().load(cursor.getString(Integer.parseInt(currentImage))).into(image);
                cursor.moveToNext();
            }


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
        image.setOnClickListener(view1 -> {
            AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
            //pictureDialog.setTitle("Select Action");
            String[] pictureDialogItems = {
                    "Select photo from gallery",
                    "Capture photo from camera"};
            pictureDialog.setItems(pictureDialogItems,
                    (dialog, which) -> {
                        switch (which) {
                            case 0:
                                chooseImageFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    });
            pictureDialog.show();
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        cursor.close();
        db.close();
    }

    private Bitmap getBitmapFromURL(String src) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            URL url = new URL(src);
            posterBitmap = BitmapFactory.decodeStream((InputStream) url.getContent());
        } catch (IOException e) {
            Log.e("TAG", e.getMessage());
        }

        return posterBitmap;
    }

    private byte[] getArrayOfBytesFromBitmap(Bitmap bmp) {
        if (bmp != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            bmp.recycle();
            return stream.toByteArray();
        }
        return null;
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(Intent.createChooser(intent, "Take photo"), CAMERA);
    }

    private void chooseImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY);
    }

    private void uploadImage() {
        FirebaseStorage storage;
        StorageReference storageReference;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users").child(MainActivity.getUserId());

        System.out.println("fiiiilleePath ==== "+globals.filePathUserPhoto);
        if (globals.filePathUserPhoto != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(Uri.parse(globals.filePathUserPhoto))
                    .addOnSuccessListener(taskSnapshot -> {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                        taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(
                                task -> {
                                    String fileLink = task.getResult().toString();
                                    reference.child("user_image_URL").setValue(fileLink);
                                });
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
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }

        if (requestCode == GALLERY) {
            if (data.getData() != null) {
                globals.filePathUserPhoto = data.getData().toString();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(globals.filePathUserPhoto));
                    image.setImageBitmap(bitmap);
                    uploadImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == CAMERA) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(bitmap);
            Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
            Uri tempUri = getImageUri(getActivity().getApplicationContext(), bitmap);
            //File finalFile = new File(getRealPathFromURI(tempUri));
            globals.filePathUserPhoto = tempUri.toString();
            System.out.println("uurrrrrrlllllllll ==== "+tempUri.toString());
            uploadImage();
            //saveImage(thumbnail);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000,true);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getActivity().getContentResolver() != null) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }
    /*public String saveImage(Bitmap myBitmap) {
    // it's from https://demonuts.com/pick-image-gallery-camera-android/
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }*/
}