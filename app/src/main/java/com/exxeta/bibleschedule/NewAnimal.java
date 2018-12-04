package com.exxeta.bibleschedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;

public class NewAnimal extends Activity {
    EditText scheduleDate;
    EditText scheduleCoordinates;
    EditText scheduleWasRead;
    DBController controller = new DBController(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_animal);
        scheduleDate = (EditText) findViewById(R.id.scheduleDate);
        scheduleCoordinates = (EditText) findViewById(R.id.scheduleCoordinates);
        scheduleWasRead = (EditText) findViewById(R.id.scheduleWasRead);
    }

    public void addNewAnimal(View view) {
        HashMap<String, String> queryValues = new HashMap<String, String>();
        queryValues.put("date", scheduleDate.getText().toString());
        queryValues.put("coordinates", scheduleCoordinates.getText().toString());
        queryValues.put("wasRead", scheduleWasRead.getText().toString());
        controller.insertCoordinates(queryValues);
        this.callHomeActivity(view);
    }

    public void callHomeActivity(View view) {
        Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(objIntent);
    }
}
