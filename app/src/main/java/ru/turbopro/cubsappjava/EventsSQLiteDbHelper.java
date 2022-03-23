package ru.turbopro.cubsappjava;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventsSQLiteDbHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "EVENTS";
    private static final int DATABASE_VERSION = 1;
    public static final String ID = "ID";
    public static final String NAME = "NAME";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String DATE = "DATE";
    public static final String IMAGE = "IMAGE";

    private SQLiteDatabase mdb;

    public EventsSQLiteDbHelper(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
        this.mdb = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME +
                "(" + ID + " TEXT, " +
                NAME + " TEXT, " + DESCRIPTION + " TEXT, " + DATE +
                " TEXT, " + IMAGE + " BLOB);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insert(String id, String name, String description, String date, byte[] image) {
        ContentValues value = new ContentValues();
        value.put(ID, id);
        value.put(NAME, name);
        value.put(DESCRIPTION, description);
        value.put(DATE, date);
        value.put(IMAGE, image);

        mdb.insert(TABLE_NAME, null, value);
    }

    public void update(String id, String name, String date, String description, byte[] image) {
        ContentValues value = new ContentValues();
        value.put(ID, id);
        value.put(NAME, name);
        value.put(DESCRIPTION, description);
        value.put(DATE, date);
        value.put(IMAGE, image);
        this.mdb.update(TABLE_NAME, value,
                null,
                null);
    }
}
