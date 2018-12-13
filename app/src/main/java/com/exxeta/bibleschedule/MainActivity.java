package com.exxeta.bibleschedule;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.exxeta.bibleschedule.Model.Schedule;
import com.exxeta.bibleschedule.Model.User;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String MAIN = "MAIN";
    private DBController controller = DBController.getInstance(this);
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);

        Realm realm = Realm.getDefaultInstance();

//        realm.beginTransaction();
//        User user = realm.createObject(User.class);
//        user.setAge(10);
//        user.setDateOfBirth(LocalDate.now().toDate());
//        user.setName("Miso");
//        realm.commitTransaction();

        RealmResults<User> result = realm.where(User.class)
                .lessThan("age", 45)//find all users with age less than 45
                .findAll();//return all result that reach criteria

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < result.size(); i++) {
            stringBuilder.append(result.get(i).getName() + "  " + result.get(i).getDateOfBirth().toString());
        }
        System.out.println(stringBuilder);


        realm.close();

        //initDb();
        //findViewsById();

        //mListView.setItemChecked(2, true);
//        List<Schedule> list = controller.getAllCoordinates();
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).getWasRead().equals(Util.WAS_READ)) {
//                mListView.setItemChecked(i, true);
//            }
//        }

    }

    private void findViewsById() {
        mListView = findViewById(R.id.MainListView);
        mListView.setTextFilterEnabled(true);
        mListView.setAdapter(new MyAdapter(MainActivity.this, controller));

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    private List<Schedule> getSelectedItems() {
        List<Schedule> result = new ArrayList<>();
        SparseBooleanArray checkedItems = mListView.getCheckedItemPositions();

        for (int i = 0; i < mListView.getCount(); i++) {
            if (checkedItems.size() > i && checkedItems.valueAt(i)) {
                result.add((Schedule) mListView.getItemAtPosition(checkedItems.keyAt(i)));
            }
        }
        return result;
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
        if (id == R.id.action_show_number_of_checked) {
            List<Schedule> selected = getSelectedItems();
            if (selected != null) {
                String logString = "Count items: " + selected.size();
                Toast.makeText(this, logString, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Initialization sqlite database
     */
    private void initDb() {
        //.controller.dropDatabase();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            int value = extras.getInt("id");
            if (value > 0) {
                // Update the db
                Log.d(MAIN, "Database was updated");
            } else {
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
        Log.i(MAIN, "save instance state");
        List<Schedule> list = getSelectedItems();
        for (int i = 0; i < list.size(); i++) {
            controller.updateScheduleOnCoordinate(list.get(i).getCoordinate());
        }
    }

    /**
     * Restore
     *
     * @param savedInstanceState
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(MAIN, "restore instance state");
    }

    // Update database information
    @Override
    public void onPause() {
        super.onPause();

        List<Schedule> list = getSelectedItems();
        for (int i = 0; i < list.size(); i++) {
            controller.updateScheduleOnCoordinate(list.get(i).getCoordinate());
        }


//        System.err.println("Size of scheduleArrayList: " + MyAdapter.scheduleArrayList.size());
//        ArrayList<Schedule> scheduleList = MyAdapter.scheduleArrayList;
//        ArrayList<Schedule> resultList = new ArrayList<>();
//        for (Schedule sc : scheduleList) {
//            if (sc.getWasRead().contains("TRUE")) {
//                resultList.add(sc);
//            }
//        }
        // System.err.println("Size of clicked item: " + resultList.size());

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