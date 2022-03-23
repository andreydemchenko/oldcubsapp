package ru.turbopro.cubsappjava;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class UserSQLiteDbHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "USERS";
    private static final int DATABASE_VERSION = 1;
    public static final String ID = "ID";
    public static final String NAME = "NAME";
    public static final String LOGIN = "LOGIN";
    public static final String IMAGE = "IMAGE";
    public static final String ALL_POINTS = "ALL_POINTS";
    public static final String POINTS = "POINTS";
    public static final String HOURS = "HOURS";
    public static final String ACHIEVEMENTS = "ACHIEVEMENTS";

    private SQLiteDatabase mdb;

    public UserSQLiteDbHelper(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
        this.mdb = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME +
                        "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT, " + LOGIN + " TEXT, " + ACHIEVEMENTS +
                " TEXT, "+ ALL_POINTS + " INTEGER, " + POINTS + " INTEGER, " +
                HOURS + " INTEGER, " + IMAGE + " BLOB);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                onCreate(sqLiteDatabase);
    }

    public String getAchievementsInJson(ArrayList<String> achievements){
        Gson gson = new Gson();
        return gson.toJson(achievements);
    }

    public ArrayList<String> getAchievementsInArrayList(String achievements) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(achievements, type);
    }

    public Cursor readAll() {
        System.out.println("--------readAll------");

        Cursor cursor = null;
        try {
            cursor = mdb.query(TABLE_NAME, new String[] {ID, NAME, LOGIN, ACHIEVEMENTS, ALL_POINTS, POINTS, HOURS, IMAGE},
                    null, //нет столбцов для where
                    null, // нет значений для условия where
                    null, //нет групп строк
                    null, //не фильтровать с помощью групп строк
                    null // не сортировать
            );
            while (cursor.moveToNext()) {
                int id = (int) cursor.getInt(0);
                String name = cursor.getString(1);
                String login =  cursor.getString(2);
                String achievements = cursor.getString(3);
                int all_points = (int) cursor.getInt(4);
                int points = (int) cursor.getInt(5);
                int hours = (int) cursor.getInt(6);
                String image_url = cursor.getString(7);
                Log.d("mLog", "Запись(id): " + id + "    name: " + name + " " +
                        "   login: " + login+"   achievements: " + achievements + "   all_points: " + all_points+"  points: "+points + "   hours: " +hours+"  image: " + image_url);
            }
        } catch (SQLiteException e) { }

        System.out.println("--------readAll------");
        return cursor;
    }

    public User select(long NID) {
        System.out.println("----------select------------");

        Cursor cursor = this.mdb.query(TABLE_NAME,
                null,
                this.ID + "=" + NID,
                null, null, null, null);

        cursor.moveToFirst();
        int id = (int) cursor.getInt(0);
        String name = cursor.getString(1);
        String login =  cursor.getString(2);
        String achievements = cursor.getString(3);
        int all_points = (int) cursor.getInt(4);
        int points = (int) cursor.getInt(5);
        int hours = (int) cursor.getInt(6);
        String image_url = cursor.getString(7);
        Log.d("mLog", "Select: Запись(id): " + id + "    name: " + name + " " +
                "   login: " + login+"   achievements: " + achievements + "   all_points: " + all_points+"  points: "+points + "   hours: " +hours+"  image: " + image_url);
        System.out.println("----------select------------");

        return new User(name, login, points, all_points, hours, image_url, getAchievementsInArrayList(achievements));
    }

    public void insert(String name, String login, int points, int all_points, int hours, byte[] image, ArrayList<String> achiv_progress) {
        ContentValues value = new ContentValues();
      /*  ByteArrayOutputStream out = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, out);*/
        value.put(NAME, name);
        value.put(LOGIN, login);
        value.put(POINTS, points);
        value.put(ALL_POINTS, all_points);
        value.put(HOURS, hours);
        value.put(IMAGE, image);
        value.put(ACHIEVEMENTS, getAchievementsInJson(achiv_progress));

        mdb.insert(TABLE_NAME, null, value);
        Log.d("mLog", "Insert: name: " + name + " " +
                "   login: " + login+"   achievements: " + achiv_progress + "   all_points: " + all_points+"  points: "+points + "   hours: " +hours);
    }

    public void update(String name, String login, int points, int all_points, int hours, String user_image_URL, ArrayList<String> achiv_progress) {
        System.out.println("----------updateDbHelper------------");
        ContentValues value = new ContentValues();
        value.put(NAME, name);
        value.put(LOGIN, login);
        value.put(POINTS, points);
        value.put(ALL_POINTS, all_points);
        value.put(HOURS, hours);
        value.put(IMAGE, user_image_URL);
        value.put(ACHIEVEMENTS, getAchievementsInJson(achiv_progress));
        this.mdb.update(TABLE_NAME, value,
                null,
                null);
        Log.d("mLog", "Update: name: " + name + " " +
                "   login: " + login+"   achievements: " + achiv_progress + "   all_points: " + all_points+"  points: "+points + "   hours: " +hours+"  image_url: " + user_image_URL);
        System.out.println("----------updateDbHelper------------");
    }
}
