package com.exxeta.bibleschedule;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.exxeta.bibleschedule.Model.Schedule;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String MAIN = "MAIN";

    private final int ITEMS_COUNT = 7;
    private final int VERSION = 1;

    TextView scheduleId;
    DBController controller = new DBController(this, VERSION);

    // private SearchView mSearchView;
    private ListView mListView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // mSearchView = (SearchView) findViewById(R.id.searchView1);
        mListView = (ListView) findViewById(android.R.id.list);
        mListView.setTextFilterEnabled(true);


        initDb();
//        setupSearchView();
        mListView.setAdapter(new MyAdapter(MainActivity.this, controller));


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


//    private void setupSearchView() {
    //https://stackoverflow.com/questions/23857313/filter-text-display-in-searchview-not-removing
//        mSearchView.setIconifiedByDefault(false);
//        mSearchView.setOnQueryTextListener(this);
//        mSearchView.setSubmitButtonEnabled(true);
//        mSearchView.setQueryHint("Search Here");
//    }

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
                controller.readDataFromCsv(getResources().openRawResource(
                        getResources().getIdentifier("coordinate_2019",
                                "raw", getPackageName())));

                Log.d(MAIN, "Database was installed and data was imported");
            }
            ArrayList<Schedule> coordinatesList = controller.getAllCoordinates();

            if (coordinatesList.size() != 0) {
                Log.d(MAIN, "Database was init and number of items: " + coordinatesList.size());
            }
        } else {
            Log.e(MAIN, "Bundle extras is null");
        }
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


}