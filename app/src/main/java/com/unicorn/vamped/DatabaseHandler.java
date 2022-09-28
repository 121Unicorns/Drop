package com.unicorn.vamped;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHandler extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Vamped";
    private static final String TABLE_CYCLES = "cycles";
    private static final String KEY_ID = "eventdate";
    private static final String KEY_PERIOD = "period";
    private static final String KEY_FLOW = "flow";
    private static final String KEY_SYMPTOMS = "symptoms";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CYCLE_TABLE = "CREATE TABLE " + TABLE_CYCLES
                + "("+ KEY_ID + " TEXT PRIMARY KEY,"
                + KEY_PERIOD + " INTEGER,"
                + KEY_FLOW + " INTEGER,"
                + KEY_SYMPTOMS + " TEXT)";
        db.execSQL(CREATE_CYCLE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CYCLES);
        onCreate(db);
    }

    void insertDayCycle(String date, int period, int flow, String symptoms) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.KEY_ID, date);
        values.put(DatabaseHandler.KEY_PERIOD, period);
        values.put(DatabaseHandler.KEY_FLOW, flow);
        values.put(DatabaseHandler.KEY_SYMPTOMS, symptoms);

        //db.insert(DatabaseHandler.TABLE_CYCLES, null, values);
        db.replace(DatabaseHandler.TABLE_CYCLES, null, values);
        db.close();

    }

    public ArrayList<HashMap<String, String>> getAllDayCycles(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> cycleList = new ArrayList<>();
        String query = "SELECT * FROM "+ DatabaseHandler.TABLE_CYCLES;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> cyc = new HashMap<>();
            cyc.put("date",cursor.getString(cursor.getColumnIndex(KEY_ID)));
            cyc.put("period",cursor.getString(cursor.getColumnIndex(KEY_PERIOD)));
            cyc.put("flow",cursor.getString(cursor.getColumnIndex(KEY_FLOW)));
            cyc.put("symptoms",cursor.getString(cursor.getColumnIndex(KEY_SYMPTOMS)));
            cycleList.add(cyc);
        }
        return  cycleList;
    }

    public int updateCycle(String date, int period, int flow, String symptoms) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PERIOD, period);
        values.put(KEY_FLOW, flow);
        values.put(KEY_SYMPTOMS, symptoms);

        int count = db.update(TABLE_CYCLES, values, KEY_ID + " = ?", new String[] { String.valueOf(date) });
        return  count;
    }
}