package com.exxeta.bibleschedule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class NextActivity extends AppCompatActivity {

    private TextView textCoordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        textCoordinates = (TextView) findViewById(R.id.tv);

        for (int i = 0; i <  MyAdapter.scheduleArrayList.size(); i++){
            if(MyAdapter.scheduleArrayList.get(i).isSelected()) {
                textCoordinates.setText(textCoordinates.getText() + " " + MyAdapter.scheduleArrayList.get(i).getCoordinate());
            }
        }

    }

}
