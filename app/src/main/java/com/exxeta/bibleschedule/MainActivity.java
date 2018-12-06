package com.exxeta.bibleschedule;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import com.exxeta.bibleschedule.Model.Schedule;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String MAIN = "MAIN";
    private final int VERSION = 1; // default value

    DBController controller = DBController.getInstance(this);
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Recovering the instance state
        Intent intent = getIntent();
        long projectId = intent.getLongExtra("project_id", VERSION);


        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        mListView = (ListView) findViewById(android.R.id.list);
        mListView.setTextFilterEnabled(true);

        initDb();

        mListView.setAdapter(new MyAdapter(MainActivity.this, controller));

        if (savedInstanceState == null) {
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            SlidingTabsBasicFragment fragment = new SlidingTabsBasicFragment();
//            transaction.replace(R.id.sample_content_fragment, fragment);
//            transaction.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Initialization sqlite database
     */
    private void initDb() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            int value = extras.getInt("id");
            if (value > 0) {
                // Update the db
                Log.d(MAIN, "Database was updated");
            } else {
                // insert
                // FIXME ONLY AFTER INSTALLATION if change version
//                controller.readDataFromCsv(getResources().openRawResource(
//                        getResources().getIdentifier("coordinate_2019",
//                                "raw", getPackageName())));

                Log.d(MAIN, "Database was installed and data was imported");
            }
            ArrayList<Schedule> coordinatesList = controller.getAllCoordinates();

            if (coordinatesList.size() != 0) {
                Log.d(MAIN, "Database was init and number of items: " + coordinatesList.size());
            }
        } else {
            Log.e(MAIN, "Bundle extras is null");
        }
        // TODO !!!
//        controller.readDataFromCsv(getResources().openRawResource(
//                getResources().getIdentifier("coordinate_2019",
//                        "raw", getPackageName())));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (TextUtils.isEmpty(query)) {
            mListView.clearTextFilter();

        } else {
            mListView.setFilterText(query);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    /**
     * Save
     *
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

    }

    /**
     * Restore
     *
     * @param savedInstanceState
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    // Update database information
    @Override
    public void onPause() {
        super.onPause();

        System.err.println("Size of scheduleArrayList: " + MyAdapter.scheduleArrayList.size());
        ArrayList<Schedule> scheduleList = MyAdapter.scheduleArrayList;
        ArrayList<Schedule> resultList = new ArrayList<>();
        for (Schedule sc : scheduleList) {
            if (sc.getWasRead().contains("TRUE")) {
                resultList.add(sc);
            }
        }
        System.err.println("Size of clicked item: " + resultList.size());

        // TODO update database information
        //DBController database = new DBController(this);

        // Update the value of all the counters of the project in the database since the activity is
        // destroyed or send to the background
//        for (RowCounter rowCounter : mRowCounters) {
//            database.updateRowCounterCurrentAmount(rowCounter);
//        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

}