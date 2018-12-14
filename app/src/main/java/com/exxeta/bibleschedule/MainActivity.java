package com.exxeta.bibleschedule;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import com.exxeta.bibleschedule.Model.Schedule;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String MAIN = "MAIN";
    private ListView mListView;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance();

        findViewsById();

        getSelectedItemsFromRealm();
    }

    private void getSelectedItemsFromRealm() {
        List<Schedule> list = new ArrayList<>();
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<Schedule> results = realm
                    .where(Schedule.class)
                    .findAll();
            list.addAll(realm.copyFromRealm(results));
        } finally {
            if (realm != null) {
                realm.close();
            }
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getWasRead()) {
                mListView.setItemChecked(i, true);
            }
        }
    }

    private void findViewsById() {
        mListView = findViewById(R.id.MainListView);
        mListView.setTextFilterEnabled(true);
        mListView.setAdapter(new MyAdapter(MainActivity.this));


        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    private List<Schedule> getSelectedItemsAndRefreshRealm() {
        List<Schedule> result = new ArrayList<>();
        SparseBooleanArray checkedItems = mListView.getCheckedItemPositions();

        for (int i = 0; i < mListView.getCount(); i++) {
            if (checkedItems.size() > i && checkedItems.valueAt(i)) {
                Schedule finded = (Schedule)mListView.getItemAtPosition(checkedItems.keyAt(i));
                result.add(finded);

                Schedule c = realm.where(Schedule.class).equalTo("date", finded.getDate()).findFirst();
                realm.beginTransaction();
                c.setWasRead(true); //setter method for value
                realm.insertOrUpdate(c);
                //while updating the existing entry in the database, you need not worry about the hierarchy, Realm will maintain the same hierarchy for updates
                //If you want to copy the existing entry from one object to another, you can use combination of method-1 and method-2
                realm.commitTransaction();
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
            Realm realm = Realm.getDefaultInstance();

            int scheduleList = realm.where(Schedule.class).findAll().size();
            if (scheduleList > 0) {
                Snackbar.make(getWindow().getDecorView(),
                        "Found: " + scheduleList +
                                " people in the database", Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(getWindow().getDecorView(), "Found no people in the database!", Snackbar.LENGTH_LONG).show();
            }
            return true;
        }
        if(id == R.id.action_import_from_csv) {
            // TODO do not add much than 365 records per year
            RealmImporter.importFromJson(getResources());
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    protected void onRestart() {
        getSelectedItemsFromRealm();
        super.onRestart();
    }

    @Override
    protected void onStop() {
        getSelectedItemsAndRefreshRealm();
        getSelectedItemsFromRealm();
        super.onStop();
    }

    @Override
    protected void onPause() {
        getSelectedItemsAndRefreshRealm();
        getSelectedItemsFromRealm();
        super.onPause();
    }

    @Override
    protected void onResume(){
        getSelectedItemsAndRefreshRealm();
        getSelectedItemsFromRealm();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        getSelectedItemsAndRefreshRealm();
        if (!realm.isClosed()) realm.close();
        super.onDestroy();
    }
}