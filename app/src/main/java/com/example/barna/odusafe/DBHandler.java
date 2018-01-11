package com.example.barna.odusafe;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael Blackwell on 9/20/2017.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "eventDB.sqlite";
    private static final String TABLE_EVENTS = "profile";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FULLNAME = "fullname";
    public static final String COLUMN_FIRSTNAME = "firstName";
    public static final String COLUMN_GIVENNAME = "givenName";
    public static final String COLUMN_ADDRESS = "Address";
    public static final String COLUMN_PHONENUMBER = "phoneNumber";
    public static final String COLUMN_EMAIL = "email";

    private static DBHandler sInstance;

    //TODO make a fake database using SQLite manager
    //TODO make a dashboard that displays the whole database in an edit text file

    public DBHandler(Context context) {

        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        //db = SQLiteDatabase.openDatabase("./eventDB.sqlite", null, 0);
        String CREATE_EVENTS_TABLE = "CREATE TABLE " +
                TABLE_EVENTS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_FULLNAME + " TEXT," +
                COLUMN_FIRSTNAME + " TEXT," +
                COLUMN_GIVENNAME + " TEXT," +
                COLUMN_ADDRESS + " TEXT," +
                COLUMN_PHONENUMBER + " TEXT," +
                COLUMN_EMAIL + " TEXT" + ")";

        db.execSQL(CREATE_EVENTS_TABLE);
    }

    //Added by Jennnifer - Singleton Pattern to create single instance of DB
    public static synchronized DBHandler getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DBHandler(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }

    public void addProfile (Profile profile){

        ContentValues values = new ContentValues();
        values.put(COLUMN_FULLNAME, profile.getFullName());
        values.put(COLUMN_FIRSTNAME, profile.getFirstName());
        values.put(COLUMN_GIVENNAME, profile.getLastName());
        values.put(COLUMN_ADDRESS, profile.getAddress());
        values.put(COLUMN_PHONENUMBER, profile.getPhoneNumber());
        values.put(COLUMN_EMAIL, profile.getEmail());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_EVENTS, null, values);
        db.close();
    }

    public List<Profile> getAllProfiles(){
        List<Profile> profileList = new ArrayList<Profile>();
        String query = "Select * FROM " + TABLE_EVENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Profile profile = new Profile();
                profile.setid(Integer.parseInt(cursor.getString(0)));
                profile.setFullName(cursor.getString(1));
                profile.setFirstName(cursor.getString(2));
                profile.setLastName(cursor.getString(3));
                profile.setAddress(cursor.getString(4));
                profile.setPhoneNumber(cursor.getString(5));
                profile.setEmail(cursor.getString(6));
                // Adding contact to list
                profileList.add(profile);
            } while (cursor.moveToNext());
        }

        db.close();
        return profileList;
    }

    public Profile findProfileAccount (String fullname){
        String query = "Select * FROM " + TABLE_EVENTS + " WHERE " +
                COLUMN_FULLNAME + " = \"" + fullname + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Profile profile = new Profile();

        if(cursor.moveToFirst()) {
            cursor.moveToFirst();

            profile.setid(Integer.parseInt(cursor.getString(0)));
            profile.setFullName(cursor.getString(1));
            profile.setFirstName(cursor.getString(2));
            profile.setLastName(cursor.getString(3));
            profile.setAddress(cursor.getString(4));
            profile.setPhoneNumber(cursor.getString(5));
            profile.setEmail(cursor.getString(6));

        } else {
            profile = null;
        }
        db.close();
        return profile;
    }

    public boolean deleteProfile(String fullname) {
        boolean result = false;
        String query = "Select * FROM " + TABLE_EVENTS + " WHERE " +
                COLUMN_FULLNAME + " = \"" + fullname + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Profile profile = new Profile();

        if(cursor.moveToFirst()){
            profile.setid(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_EVENTS, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(profile.getid())});
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public void removeAll()
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete( TABLE_EVENTS, null, null);
    }

    public void dropTable(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE " + name);
    }

/*    public boolean  isMasterEmpty() {

        boolean flag;
        String quString = "select exists(select 1 from " + TABLE_EVENTS  + ");";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(quString, null);
        cursor.moveToFirst();
        int count= cursor.getInt(0);
        if (count ==1) {
            flag =  false;
        } else {
            flag = true;
        }
        cursor.close();
        db.close();

        return flag;
    }*/

    public boolean checkForTables(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_EVENTS, null);

        if(cursor != null){

            cursor.moveToFirst();

            int count = cursor.getInt(0);

            if(count > 0){
                return true;
            }

            cursor.close();
        }

        db.close();
        return false;
    }

    public boolean verification(String fullName) throws SQLException {
/*        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT 1 FROM "+TABLE_EVENTS+" WHERE "+COLUMN_FULLNAME+"=?", new String[] {fullName});

        boolean exists = c.moveToFirst();
        c.close();
        return exists;*/



 /*     int count = -1;
        Cursor c = null;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String query = "SELECT COUNT(*) FROM "
                    + TABLE_EVENTS + " WHERE " + COLUMN_FULLNAME + " = ?";
            c = db.rawQuery(query, new String[] {fullName});
            if (c.moveToFirst()) {
                count = c.getInt(0);
            }
            return count > 0;
        }
        finally {
            if (c != null) {
                c.close();
            }
        }*/

        return true;
    }
}
