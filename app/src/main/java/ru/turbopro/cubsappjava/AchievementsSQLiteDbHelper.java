package ru.turbopro.cubsappjava;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AchievementsSQLiteDbHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "ACHIEVEMENTS";
    private static final int DATABASE_VERSION = 1;
    public static final String ID = "ID";
    public static final String NAME = "NAME";
    public static final String POINTS = "POINTS";
    public static final String NEED_POINTS = "NEED_POINTS";
    public static final String IMAGE = "IMAGE";
    public static final String TYPE = "TYPE";

    private SQLiteDatabase mdb;

    public AchievementsSQLiteDbHelper(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
        this.mdb = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME +
                "(" + ID + " TEXT, " +
                NAME + " TEXT, " + POINTS + " TEXT, " + NEED_POINTS +
                " TEXT, " + TYPE + " TEXT, " + IMAGE + " BLOB);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insert(int id, String name, int points, int needPoints, byte[] image, String type) {
        ContentValues value = new ContentValues();
        value.put(ID, id);
        value.put(NAME, name);
        value.put(POINTS, points);
        value.put(NEED_POINTS, needPoints);
        value.put(TYPE, type);
        value.put(IMAGE, image);

        mdb.insert(TABLE_NAME, null, value);
    }

    public void update(int id, String name, int points, int needPoints, byte[] image, String type) {
        ContentValues value = new ContentValues();
        value.put(ID, id);
        value.put(NAME, name);
        value.put(POINTS, points);
        value.put(NEED_POINTS, needPoints);
        value.put(TYPE, type);
        value.put(IMAGE, image);
        this.mdb.update(TABLE_NAME, value,
                null,
                null);
    }
}
