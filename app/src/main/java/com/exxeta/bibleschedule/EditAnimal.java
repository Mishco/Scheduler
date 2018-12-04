package com.exxeta.bibleschedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;

public class EditAnimal extends Activity {

    EditText scheduleDate;
    EditText scheduleCoordinates;
    EditText scheduleWasRead;


    DBController controller = new DBController(this,1);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_animal);

        scheduleDate = (EditText) findViewById(R.id.scheduleDate);
        scheduleCoordinates = (EditText) findViewById(R.id.scheduleCoordinates);
        scheduleWasRead = (EditText) findViewById(R.id.scheduleWasRead);

        Intent objIntent = getIntent();
        String scheduleId = objIntent.getStringExtra("scheduleId");
        Log.d("Reading: ", "Reading all contacts..");
        HashMap<String, String> scheduleList = controller.getCoordinatesInfo(scheduleId);
        Log.d("scheduleDate", scheduleList.get("date"));
        if (scheduleList.size() != 0) {
            scheduleDate.setText(scheduleList.get("date"));
            scheduleCoordinates.setText(scheduleList.get("coordinates"));
            scheduleWasRead.setText(scheduleList.get("wasRead"));
        }
    }

    public void editAnimal(View view) {
        HashMap<String, String> queryValues = new HashMap<String, String>();
        scheduleDate = (EditText) findViewById(R.id.scheduleDate);
        scheduleCoordinates = (EditText) findViewById(R.id.scheduleCoordinates);
        scheduleWasRead = (EditText) findViewById(R.id.scheduleWasRead);

        Intent objIntent = getIntent();
        String scheduleId = objIntent.getStringExtra("scheduleId");
        queryValues.put("scheduleId", scheduleId);
        queryValues.put("date", scheduleDate.getText().toString());
        queryValues.put("coordinates", scheduleCoordinates.getText().toString());
        queryValues.put("wasRead", scheduleWasRead.getText().toString());

        controller.updateSchedule(queryValues);
        this.callHomeActivity(view);
    }

    public void removeAnimal(View view) {
        Intent objIntent = getIntent();
        String scheduleId = objIntent.getStringExtra("scheduleId");
        controller.deleteSchedule(scheduleId);
        this.callHomeActivity(view);

    }

    public void callHomeActivity(View view) {
        Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(objIntent);
    }
}