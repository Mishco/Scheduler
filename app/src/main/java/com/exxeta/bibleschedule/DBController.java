package com.exxeta.bibleschedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.exxeta.bibleschedule.Model.Schedule;
import com.opencsv.CSVReader;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class DBController extends SQLiteOpenHelper {
    private static final String LOGCAT = null;

    public DBController(Context applicationcontext) {
        super(applicationcontext, "androidsqlite.db", null, 1);
        Log.d(LOGCAT, "Created");
    }


    public void readDataFromCsv(InputStream inputStream) {
        HashMap<String, String> query = new HashMap<>();

        CSVReader reader = null;
        String date;
        String coordinates;
        String wasRead = "false";
        try {
            reader = new CSVReader(new InputStreamReader(inputStream));

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                date = nextLine[0];
                coordinates = nextLine[1];

                query.put("date", date);
                query.put("coordinates", coordinates);
                query.put("wasRead", wasRead);
                insertCoordinates(query);
                query.clear();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initDummyCoordinates() {
        HashMap<String, String> query = new HashMap<>();
        query.put("date", "1/1/2019");
        query.put("coordinates", "Mt 1,1-17");
        query.put("wasRead", "false");

        insertCoordinates(query);
        query.clear();

        query.put("date", "1/2/2019");
        query.put("coordinates", "Mt 1,18-25");
        query.put("wasRead", "false");
        insertCoordinates(query);
    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE schedule ( scheduleId INTEGER PRIMARY KEY, date TEXT, coordinates TEXT, wasRead TEXT)";
        database.execSQL(query);
        Log.d(LOGCAT, "schedule Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS schedule";
        database.execSQL(query);
        onCreate(database);
    }

    public void insertCoordinates(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("date", queryValues.get("date"));
        values.put("coordinates", queryValues.get("coordinates"));
        values.put("wasRead", queryValues.get("wasRead"));

        database.insert("schedule", null, values);
        database.close();
    }


    public int updateSchedule(HashMap<String, String> queryValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("date", queryValues.get("date"));
        values.put("coordinates", queryValues.get("coordinates"));
        values.put("wasRead", queryValues.get("wasRead"));

        return db.update("schedule", values, "scheduleId" + " = ?", new String[]{queryValues.get("scheduleId")});
    }

    public void deleteSchedule(String id) {
        Log.d(LOGCAT, "delete");
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM schedule WHERE scheduleId='" + id + "'";
        Log.d("query", deleteQuery);
        db.execSQL(deleteQuery);
    }


    public ArrayList<Schedule> getAllCoordinates() {

        ArrayList<HashMap<String, String>> wordList = new ArrayList<>();
        ArrayList<Schedule> resultList = new ArrayList<>();

        String selectedQuery = "SELECT * FROM schedule";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectedQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                String id = cursor.getString(0);
                String date = cursor.getString(1);
                String coordinates = cursor.getString(2);
                String wasRead = cursor.getString(3);

                map.put("scheduleId", id);
                map.put("date", date);
                map.put("coordinates", coordinates);
                map.put("wasRead", wasRead);
                wordList.add(map);

                DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
                LocalDate localDate = LocalDate.now();
                try {
                    localDate = LocalDate.parse(date, dateFormatter);
                } catch (Exception ex) {
                    // FIXME ingnored
                }

                resultList.add(new Schedule(id, localDate, coordinates, wasRead));
            } while (cursor.moveToNext());
        }
        return resultList;
    }

    public HashMap<String, String> getCoordinatesInfo(String id) {
        HashMap<String, String> wordList = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM schedule WHERE scheduleId='" + id + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {

                wordList.put("scheduleId", cursor.getString(0));
                wordList.put("date", cursor.getString(1));
                wordList.put("coordinates", cursor.getString(2));
                wordList.put("wasRead", cursor.getString(3));
            } while (cursor.moveToNext());
        }
        return wordList;
    }

}
