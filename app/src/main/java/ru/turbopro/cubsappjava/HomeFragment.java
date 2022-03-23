package ru.turbopro.cubsappjava;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.FrameLayout;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shinelw.library.ColorArcProgressBar;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
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
    private TextView tvName;
    private TextView tvStatus;
    private TextView tvLevel;
    private View view;
    Globals globals;
    private User _user;
    private int k;
    private boolean fAch = false;
    private FrameLayout frameAch;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private UserSQLiteDbHelper dbHelperUser;
    private final String[] projectionUser = {
            UserSQLiteDbHelper.ID,
            UserSQLiteDbHelper.NAME,
            UserSQLiteDbHelper.LOGIN,
            UserSQLiteDbHelper.ACHIEVEMENTS,
            UserSQLiteDbHelper.ALL_POINTS,
            UserSQLiteDbHelper.POINTS,
            UserSQLiteDbHelper.HOURS,
            UserSQLiteDbHelper.IMAGE};

    private int idColumnIndex;
    private int nameColumnIndex;
    private int loginColumnIndex;
    private int achievementsColumnIndex;
    private int allPointsColumnIndex;
    private int pointsColumnIndex;
    private int hoursColumnIndex;
    private int imageColumnIndex;
    private String currentName;
    private int currentAllPoints;
    private int currentPoints;
    private int currentHours;
    private String currentLogin;
    private String currentAch;
    private byte[] currentImageBlob;

    private ColorArcProgressBar seekBar;

    private Bitmap posterBitmap;

    private EventsSQLiteDbHelper dbEventHelper;
    private final String[] projectionEvent = {
            EventsSQLiteDbHelper.ID,
            EventsSQLiteDbHelper.NAME,
            EventsSQLiteDbHelper.DESCRIPTION,
            EventsSQLiteDbHelper.DATE,
            EventsSQLiteDbHelper.IMAGE};

    public static ArrayList<CardEvent> eventsList = new ArrayList<>();

    private int idColumnIndexEvent;
    private int nameColumnIndexEvent;
    private int descriptionColumnIndexEvent;
    private int dateColumnIndexEvent;
    private int imageColumnIndexEvent;
    private String currentIdEvent;
    private String currentNameEvent;
    private String currentDescriptionEvent;
    private String currentDateEvent;
    private byte[] currentImageBlobEvent;

    private AchievementsSQLiteDbHelper dbHelperAch;
    private final String[] projectionAch = {
            AchievementsSQLiteDbHelper.ID,
            AchievementsSQLiteDbHelper.NAME,
            AchievementsSQLiteDbHelper.POINTS,
            AchievementsSQLiteDbHelper.NEED_POINTS,
            AchievementsSQLiteDbHelper.IMAGE,
            AchievementsSQLiteDbHelper.TYPE};

    private int idColumnIndexAch;
    private int nameColumnIndexAch;
    private int pointsColumnIndexAch;;
    private int needPointsColumnIndexAch;
    private int typeColumnIndexAch;
    private int imageColumnIndexAch;
    private int currentIdAch;
    private String currentNameAch;
    private int currentNeedPointsAch;
    private int currentPointsAch;
    private String currentTypeAch;
    private byte[] currentImageBlobAch;

    private ImageView imageAch;

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
        _user = new User();
        view = inflater.inflate(R.layout.fragment_home, container, false);
        tvName = view.findViewById(R.id.tvNameHome);
        tvStatus = view.findViewById(R.id.tvStatusHome);
        //tvLevel = view.findViewById(R.id.tvLevelHome);
        cardEvents = view.findViewById(R.id.cardview_events_home);
        cardQR = view.findViewById(R.id.cardview_qr_home);
        image = view.findViewById(R.id.userImageHome);
        btnSettings = view.findViewById(R.id.btnSettingsHome);
        seekBar = view.findViewById(R.id.seekbarPoints);
        imageAch = view.findViewById(R.id.imageAchiv);
        frameAch = view.findViewById(R.id.frameAchievHome);
        seekBar.setMaxValues(20);
        seekBar.setScaleX(0.7f);
        seekBar.setScaleY(0.7f);
       /* ProgressBar progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 100);

        progressBar.setLayoutParams(params);
        LinearLayout test = new LinearLayout(getActivity().getApplicationContext());
        test.addView(progressBar);
        getActivity().setContentView(test);*/

        dbHelperUser = new UserSQLiteDbHelper(getActivity());
        MainActivity.dbUser = dbHelperUser.getReadableDatabase();
        MainActivity.cursorUser = MainActivity.dbUser.query(
                UserSQLiteDbHelper.TABLE_NAME,
                projectionUser,
                null,
                null,
                null,
                null,
                null);

        dbEventHelper = new EventsSQLiteDbHelper(getActivity());
        MainActivity.dbEvent = dbEventHelper.getReadableDatabase();
        MainActivity.cursorEvent = MainActivity.dbEvent.query(
                EventsSQLiteDbHelper.TABLE_NAME,
                projectionEvent,
                null,
                null,
                null,
                null,
                null);

        dbHelperAch = new AchievementsSQLiteDbHelper(getActivity());
        MainActivity.dbAch = dbHelperAch.getReadableDatabase();
        MainActivity.cursorAch = MainActivity.dbAch.query(
                AchievementsSQLiteDbHelper.TABLE_NAME,
                projectionAch,
                null,
                null,
                null,
                null,
                null);

        DatabaseReference myRef = database.getReference("users");
        if (firebaseUser != null) {
            String userId = MainActivity.getUserId();
            myRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            addAchievements(user.getAchiv_progress());
                            if (MainActivity.cursorUser.getCount() != 0 && MainActivity.cursorUser.moveToFirst()) {
                                idColumnIndex = MainActivity.cursorUser.getColumnIndex(UserSQLiteDbHelper.ID);
                                nameColumnIndex = MainActivity.cursorUser.getColumnIndex(UserSQLiteDbHelper.NAME);
                                loginColumnIndex = MainActivity.cursorUser.getColumnIndex(UserSQLiteDbHelper.LOGIN);
                                achievementsColumnIndex = MainActivity.cursorUser.getColumnIndex(UserSQLiteDbHelper.ACHIEVEMENTS);
                                allPointsColumnIndex = MainActivity.cursorUser.getColumnIndex(UserSQLiteDbHelper.ALL_POINTS);
                                pointsColumnIndex = MainActivity.cursorUser.getColumnIndex(UserSQLiteDbHelper.POINTS);
                                hoursColumnIndex = MainActivity.cursorUser.getColumnIndex(UserSQLiteDbHelper.HOURS);
                                imageColumnIndex = MainActivity.cursorUser.getColumnIndex(UserSQLiteDbHelper.IMAGE);

                                //int currentID = cursor.getInt(idColumnIndex);
                                currentName = MainActivity.cursorUser.getString(nameColumnIndex);
                                currentAllPoints = MainActivity.cursorUser.getInt(allPointsColumnIndex);
                                currentPoints = MainActivity.cursorUser.getInt(pointsColumnIndex);
                                currentHours = MainActivity.cursorUser.getInt(hoursColumnIndex);
                                currentLogin = MainActivity.cursorUser.getString(loginColumnIndex);
                                //String currentImage = cursor.getString(imageColumnIndex);
                                currentAch = MainActivity.cursorUser.getString(achievementsColumnIndex);
                                currentImageBlob = MainActivity.cursorUser.getBlob(imageColumnIndex);
                                    /*
                                    System.out.println("\n" + currentID + " - " +
                                            currentName + " - " +
                                            currentAllpoints + " - " +
                                            currentPoints + " - " +
                                            currentHours + " - " +
                                            currentLogin + " - " +
                                            currentImage + " - " +
                                            currentAchiv);*/
                                boolean f = false;
                                Gson gson = new Gson();
                                Type type = new TypeToken<ArrayList<String>>() {
                                }.getType();
                                ArrayList<String> achiv = gson.fromJson(currentAch, type);
                                if (!user.getName().equals(currentName) || !user.getLogin().equals(currentLogin) || user.getPoints() != currentPoints ||
                                        user.getAll_points() != currentAllPoints || user.getHours() != currentHours ||
                                        !user.getAchiv_progress().toString().equals(achiv.toString())) {
                                    f = true;
                                    dbHelperUser.update(user.getName(), user.getLogin(), user.getPoints(), user.getAll_points(), user.getHours(), user.getUser_image_URL(), user.getAchiv_progress());
                                } else System.out.println("nothing has update +++++");
                                MainActivity.cursorUser.moveToPosition(nameColumnIndex - 1);
                                seekBar.setCurrentValues(user.getPoints());
                                seekBar.setUnit("cubes");
                                if (f) {
                                    tvName.setText(user.getNameWP(currentName));
                                    Picasso.get().load(user.getUser_image_URL()).into(image);
                                }
                                MainActivity.cursorUser.moveToNext();
                            } else {
                                dbHelperUser.insert(user.getName(), user.getLogin(), user.getPoints(), user.getAll_points(), user.getHours(), getArrayOfBytesFromBitmap(getBitmapFromURL(user.getUser_image_URL())), user.getAchiv_progress());
                                tvName.setText(user.getNameWP(currentName));
                                seekBar.setCurrentValues(user.getPoints());
                                seekBar.setUnit("cubes");
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

        if (MainActivity.cursorUser.getCount() != 0 && MainActivity.cursorUser.moveToFirst()) {
            idColumnIndex = MainActivity.cursorUser.getColumnIndex(UserSQLiteDbHelper.ID);
            nameColumnIndex = MainActivity.cursorUser.getColumnIndex(UserSQLiteDbHelper.NAME);
            loginColumnIndex = MainActivity.cursorUser.getColumnIndex(UserSQLiteDbHelper.LOGIN);
            achievementsColumnIndex = MainActivity.cursorUser.getColumnIndex(UserSQLiteDbHelper.ACHIEVEMENTS);
            allPointsColumnIndex = MainActivity.cursorUser.getColumnIndex(UserSQLiteDbHelper.ALL_POINTS);
            pointsColumnIndex = MainActivity.cursorUser.getColumnIndex(UserSQLiteDbHelper.POINTS);
            hoursColumnIndex = MainActivity.cursorUser.getColumnIndex(UserSQLiteDbHelper.HOURS);
            imageColumnIndex = MainActivity.cursorUser.getColumnIndex(UserSQLiteDbHelper.IMAGE);

            //int currentID = cursor.getInt(idColumnIndex);
            currentName = MainActivity.cursorUser.getString(nameColumnIndex);
            currentAllPoints = MainActivity.cursorUser.getInt(allPointsColumnIndex);
            currentPoints = MainActivity.cursorUser.getInt(pointsColumnIndex);
            currentHours = MainActivity.cursorUser.getInt(hoursColumnIndex);
            currentLogin = MainActivity.cursorUser.getString(loginColumnIndex);
            currentAch = MainActivity.cursorUser.getString(achievementsColumnIndex);
            currentImageBlob = MainActivity.cursorUser.getBlob(imageColumnIndex);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            ArrayList<String> achiv = gson.fromJson(currentAch, type);
            MainActivity.cursorUser.moveToPosition(nameColumnIndex - 1);
            tvName.setText(_user.getNameWP(currentName));
            seekBar.setCurrentValues(currentPoints);
            seekBar.setUnit("cubes");

            if (currentImageBlob != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(currentImageBlob, 0, currentImageBlob.length);
                image.setImageBitmap(bmp);
            }
            MainActivity.cursorUser.moveToNext();
        }
        if (MainActivity.cursorEvent.getCount() != 0) {
            MainActivity.cursorEvent.moveToFirst();
            while (MainActivity.cursorEvent.moveToNext()) {
                currentIdEvent = MainActivity.cursorEvent.getString(idColumnIndexEvent);
                currentNameEvent = MainActivity.cursorEvent.getString(nameColumnIndexEvent);
                currentDescriptionEvent = MainActivity.cursorEvent.getString(descriptionColumnIndexEvent);
                currentDateEvent = MainActivity.cursorEvent.getString(dateColumnIndexEvent);
                currentImageBlobEvent = MainActivity.cursorEvent.getBlob(imageColumnIndexEvent);

                CardEvent event = new CardEvent(currentIdEvent, currentNameEvent, currentDescriptionEvent, currentDateEvent, currentImageBlobEvent);
                eventsList.add(event);
            }
            MainActivity.cursorEvent.moveToFirst();
        }

        if (MainActivity.cursorAch.getCount() != 0 && MainActivity.cursorAch.moveToLast()) {
            idColumnIndexAch = MainActivity.cursorAch.getColumnIndex(AchievementsSQLiteDbHelper.ID);
            nameColumnIndexAch = MainActivity.cursorAch.getColumnIndex(AchievementsSQLiteDbHelper.NAME);
            pointsColumnIndexAch = MainActivity.cursorAch.getColumnIndex(AchievementsSQLiteDbHelper.POINTS);
            needPointsColumnIndexAch = MainActivity.cursorAch.getColumnIndex(AchievementsSQLiteDbHelper.NEED_POINTS);
            typeColumnIndexAch = MainActivity.cursorAch.getColumnIndex(AchievementsSQLiteDbHelper.TYPE);
            imageColumnIndexAch = MainActivity.cursorAch.getColumnIndex(AchievementsSQLiteDbHelper.IMAGE);

            currentIdAch = MainActivity.cursorAch.getInt(idColumnIndexAch);
            currentNameAch = MainActivity.cursorAch.getString(nameColumnIndexAch);
            currentPointsAch = MainActivity.cursorAch.getInt(pointsColumnIndex);
            currentNeedPointsAch = MainActivity.cursorAch.getInt(needPointsColumnIndexAch);
            currentTypeAch = MainActivity.cursorAch.getString(typeColumnIndexAch);
            currentImageBlobAch = MainActivity.cursorAch.getBlob(imageColumnIndexAch);

            System.out.println("Achievements !!!!!!!!!!!!! \n" + currentIdAch + " - " +
                    currentNameAch + " - " +
                    currentNeedPointsAch + " - " +
                    currentPoints + " - " +
                    currentTypeAch);

            if (currentImageBlobAch != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(currentImageBlobAch, 0, currentImageBlobAch.length);
                imageAch.setImageBitmap(bmp);
            }
            MainActivity.cursorUser.moveToNext();
        }

        //startLoadEvents();

        btnSettings.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        });
        cardEvents.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), EventsActivity.class);
           /* Bundle args = new Bundle();
            args.putParcelableArrayList("events_list", eventsList);
            intent.putExtras(args);*/
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
        frameAch.setOnClickListener(view -> {
            /*AestheticDialog.Builder dialog = new AestheticDialog.Builder(getActivity(), DialogStyle.FLASH, DialogType.SUCCESS);
            dialog.setTitle("successTitle");
            dialog.setMessage("successMessage");
            dialog.setAnimation(DialogAnimation.CARD);
            dialog.setOnClickListener(view2 -> dialog.dismiss());
            dialog.show();*/
        });

        return view;
    }

    private void addAchievements(ArrayList<String> achiv_progress) {
        fAch = false;
        k = 0;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("achivments");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot reference : snapshot.getChildren()) {
                        int id = Integer.parseInt(reference.getKey());
                        String name = reference.child("name").getValue().toString();
                        int points = Integer.parseInt(reference.child("point").getValue().toString());
                        int points_need = Integer.parseInt(reference.child("points_need").getValue().toString());
                        String type = reference.child("type").getValue().toString();
                        String ach_url = reference.child("achiv_image_URL").getValue().toString();
                        Achievement achievement = new Achievement(id, getArrayOfBytesFromBitmap(getBitmapFromURL(ach_url)), name, points, points_need, type);
                        if (MainActivity.cursorAch.getCount() != 0 && MainActivity.cursorAch.moveToFirst()) {
                            idColumnIndexAch = MainActivity.cursorAch.getColumnIndex(AchievementsSQLiteDbHelper.ID);
                            nameColumnIndexAch = MainActivity.cursorAch.getColumnIndex(AchievementsSQLiteDbHelper.NAME);
                            pointsColumnIndexAch = MainActivity.cursorAch.getColumnIndex(AchievementsSQLiteDbHelper.POINTS);
                            needPointsColumnIndexAch = MainActivity.cursorAch.getColumnIndex(AchievementsSQLiteDbHelper.NEED_POINTS);
                            typeColumnIndexAch = MainActivity.cursorAch.getColumnIndex(AchievementsSQLiteDbHelper.TYPE);
                            imageColumnIndexAch = MainActivity.cursorAch.getColumnIndex(AchievementsSQLiteDbHelper.IMAGE);

                            currentIdAch = MainActivity.cursorAch.getInt(idColumnIndexAch);
                            currentNameAch = MainActivity.cursorAch.getString(nameColumnIndexAch);
                            currentPointsAch = MainActivity.cursorAch.getInt(pointsColumnIndex);
                            currentNeedPointsAch = MainActivity.cursorAch.getInt(needPointsColumnIndexAch);
                            currentTypeAch = MainActivity.cursorAch.getString(typeColumnIndexAch);
                            currentImageBlobAch = MainActivity.cursorAch.getBlob(imageColumnIndexAch);

                            boolean f = false;
                            if (achievement.getId() == currentIdAch) {
                                if (!achievement.getName().equals(currentNameAch) || achievement.getPoints() != currentPointsAch ||
                                        achievement.getPoints_need() != currentNeedPointsAch || achievement.getType() != currentTypeAch) {
                                    f = true;
                                    dbHelperAch.update(achievement.getId(), achievement.getName(), achievement.getPoints(), achievement.getPoints_need(), achievement.getAch_image(), achievement.getType());
                                } else System.out.println("ach - nothing has update +++++");
                                if (f) {
                                    Picasso.get().load(ach_url).into(imageAch);
                                }
                            }
                            MainActivity.cursorAch.moveToNext();
                        } else {
                            dbHelperAch.insert(achievement.getId(), achievement.getName(), achievement.getPoints(), achievement.getPoints_need(), achievement.getAch_image(), achievement.getType());
                            Picasso.get().load(ach_url).into(imageAch);
                        }

                        if (k == achiv_progress.size()) break;
                        if (k + 1 == achiv_progress.size()) fAch = true;

                        System.out.println(achievement.getId()+"  "+achievement.getPoints() + " "+achievement.getPoints_need());
                        k++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void startLoadEvents() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("events");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                   /* if (cursor.getCount() != 0 && cursor.moveToFirst()) {
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
                        boolean f = false;
                        if (!user.getName().equals(currentName) || !user.getLogin().equals(currentLogin) || user.getPoints() != currentPoints ||
                                user.getAll_points() != currentAllpoints || user.getHours() != currentHours ||
                                !user.getAchiv_progress().toString().equals(achiv.toString())) {
                            f = true;
                            dbHelper.update(user.getName(), user.getLogin(), user.getPoints(), user.getAll_points(), user.getHours(), user.getUser_image_URL(), user.getAchiv_progress());
                        } else System.out.println("nothing has update +++++");
                        cursor.moveToPosition(nameColumnIndex - 1);
                        if (f) {
                            tvName.setText(user.getNameWP());
                            Picasso.get().load(user.getUser_image_URL()).into(image);
                        }
                        cursor.moveToNext();
                    } else {
                        dbHelper.insert(user.getName(), user.getLogin(), user.getPoints(), user.getAll_points(), user.getHours(), getArrayOfBytesFromBitmap(getBitmapFromURL(user.getUser_image_URL())), user.getAchiv_progress());
                        tvName.setText(user.getNameWP());
                        seekBar.setCurrentValues(user.getPoints());
                        seekBar.setUnit("cubes");
                        Picasso.get().load(user.getUser_image_URL()).into(image);
                    }*/
                    Runnable runnable = () -> {
                        for (DataSnapshot reference : snapshot.getChildren()) {
                            String id = reference.getKey();
                            String name = reference.child("name").getValue().toString();
                            String date = reference.child("date").getValue().toString();
                            String description = reference.child("description").getValue().toString();
                            byte[] image = imageResize(getArrayOfBytesFromBitmap(getBitmapFromURL(reference.child("image").getValue().toString())));

                            int k = 0;
                            boolean f1 = false;
                            CardEvent event = new CardEvent(id, name, description, date, image);
                            if (MainActivity.cursorEvent.getCount() == 0) {
                                System.out.println("cursorEvent.getCount() ================= 0)");
                                dbEventHelper.insert(id, name, description, date, image);
                                eventsList.add(event);
                            } else {
                                MainActivity.cursorEvent.moveToFirst();
                                while (MainActivity.cursorEvent.moveToNext()) {
                                    currentIdEvent = MainActivity.cursorEvent.getString(idColumnIndex);
                                    if (currentIdEvent.equals(event.getId())) {
                                        System.out.println("currentIdEvent ================= event.getId()");
                                        f1 = true;
                                        break;
                                    }
                                    else k++;
                                    currentNameEvent = MainActivity.cursorEvent.getString(nameColumnIndexEvent);
                                    currentDescriptionEvent = MainActivity.cursorEvent.getString(descriptionColumnIndexEvent);
                                    currentDateEvent = MainActivity.cursorEvent.getString(dateColumnIndexEvent);
                                    currentImageBlobEvent = MainActivity.cursorEvent.getBlob(imageColumnIndexEvent);
                                    //CardEvent cursorEvent = new CardEvent(currentId, currentName, currentDescription, currentDate, currentImageBlob);

                                    System.out.println("CursorEvent      Event\n" + currentIdEvent + " - " + event.getId() + "\n" +
                                            currentNameEvent + " - " + event.getTitle() + "\n" +
                                            currentDescriptionEvent + " - " + event.getDescription() + "\n" +
                                            currentDateEvent + " - " + event.getDate() + "\n" +
                                            currentImageBlobEvent + " - " + event.getImage());
                                }
                                if (!f1 && k == MainActivity.cursorUser.getCount()) {
                                    System.out.println("insert item !!!!!!!!!!!!!!");
                                    dbEventHelper.insert(id, name, description, date, image);
                                    eventsList.add(event);
                                }
                            }
                            MainActivity.cursorUser.moveToFirst();
                        }
                    };
                    Thread thread = new Thread(runnable);
                    thread.start();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "onCancelled: Something went wrong! Error:" + error.getMessage());
            }
        });
    }

    private byte[] imageResize(byte[] image){
        while (image.length > 500000){
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth() * 0.8), (int)(bitmap.getHeight() * 0.8), true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.PNG, 100, stream);
            image = stream.toByteArray();
        }
        return image;
    }

    public Bitmap getBitmapFromURL(String src) {
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

    public byte[] getArrayOfBytesFromBitmap(Bitmap bmp) {
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