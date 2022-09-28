package com.unicorn.vamped;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*======================================================EVENT CODES======================================================
    _________________________________________________________________________________________________________________________
    ****EVENTS
    _________________________________________________________________________________________________________________________
        101 - PERIOD
        -----INTENSITY---
             1 - SPOTTING
             2 - LIGHT
             3 - MEDIUM
             4 - HARD
        102 - INTERCOURSE
        103 - CONTRACEPTIVE PILL
        104 - CONTRACEPTIVE INJECTION
        105 - NEW PATCH
        106 - NEW IUD
        107 - NEW RING
    _________________________________________________________________________________________________________________________
    ****SYMPTOMS
    _________________________________________________________________________________________________________________________
        108 - CRAMPS
        109 - HEADACHE/MIGRAINE
        110 - BACKACHE
        111 - NAUSEA
        112 - DIARRHEA
        113 - THRUSH/CANDIDIASIS
        114 - TENDER BREAST
        115 - DISCHARGE
        116 - BLOATING
        117 - CRAVINGS
        118 - PIMPLES
    _________________________________________________________________________________________________________________________
    ****MOODS
    _________________________________________________________________________________________________________________________
        119 - TIRED
        120 - ENERGETIC
        121 - SAD
        122 - HAPPY
        123 - ANGRY
        124 - EDGY/IRRITABLE
        125 - FRISKY
    _________________________________________________________________________________________________________________________
     */

public class DbHandler extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "vampedb";
    private static final String TABLE_Data = "data";
    private static final String TABLE_Symptoms = "symptoms";
    private static final String KEY_eventdate = "eventdate";
    private static final String KEY_eventtype = "eventtype";
    private static final String KEY_symptom = "symptom";
    private static final String KEY_intensity = "intensity";

    public DbHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table data (" +
                "eventtype integer(3), " +
                "eventdate varchar(8), " +
                "intensity integer(3)" +
                ");");
        db.execSQL("create table symptoms (" +
                "eventdate varchar(8), " +
                "symptom integer(3)" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_Data);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_Symptoms);
            // Create tables again
            onCreate(db);
    }

    void trackPeriod(int eventdate, int eventtype, int intensity){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues events = new ContentValues();

        events.put(DbHandler.KEY_eventdate, eventdate);
        events.put(DbHandler.KEY_eventtype, eventtype);
        events.put(DbHandler.KEY_intensity, intensity);


        db.replace(DbHandler.TABLE_Data, null, events);
        db.close();
    }

    void trackSymptoms(int eventdate, int symptom){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues symptoms = new ContentValues();

        symptoms.put(DbHandler.KEY_eventdate, eventdate);
        symptoms.put(DbHandler.KEY_symptom, symptom);

        db.replace(DbHandler.TABLE_Symptoms,null, symptoms);
        db.close();
    }

    public ArrayList<HashMap<String, String>> getCycleDetails(int eventdate){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> cycleList = new ArrayList<>();
        String dataquery = String.format("SELECT * FROM " + DbHandler.TABLE_Data + " where eventdate = " + eventdate);
        Cursor cursor = db.rawQuery(dataquery,null);
        while (cursor.moveToNext()){
            HashMap<String,String> cyc = new HashMap<>();
            cyc.put("eventtype",cursor.getString(cursor.getColumnIndex(KEY_eventtype)));
            cyc.put("intensity",cursor.getString(cursor.getColumnIndex(KEY_intensity)));
            cycleList.add(cyc);
        }
        cursor.close();
        return  cycleList;
    }

    public ArrayList<HashMap<String, String>> getSymptomDetails(int eventdate){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> cycleList = new ArrayList<>();
        String symptomquery = String.format("SELECT * FROM %s where eventdate = %d", DbHandler.TABLE_Symptoms, eventdate);
        Cursor cursor = db.rawQuery(symptomquery,null);
        while (cursor.moveToNext()){
            HashMap<String,String> cyc = new HashMap<>();
            cyc.put("symptom",cursor.getString(cursor.getColumnIndex(KEY_symptom)));
            cycleList.add(cyc);
        }
        cursor.close();
        return  cycleList;

    }

}
