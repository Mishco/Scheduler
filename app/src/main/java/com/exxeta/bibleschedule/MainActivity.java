package com.exxeta.bibleschedule;


import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.exxeta.bibleschedule.adapters.RealmScheduleAdapter;
import com.exxeta.bibleschedule.adapters.ScheduleAdapter;
import com.exxeta.bibleschedule.model.Schedule;
import com.exxeta.bibleschedule.realm.RealmController;
import com.exxeta.bibleschedule.realm.RealmImporter;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Main Activity class
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
    private TextView actualMonthView;
    private YearMonth thisMonth = YearMonth.now();
    private Realm realm;
    private ScheduleAdapter adapter;
    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler = findViewById(R.id.recycler);

        // Get realm instance
        this.realm = RealmController.with(this).getRealm();

        setupRecycler();

        if (!Prefs.with(this).getPreLoad()) {
            setRealmData();
        }

        // refresh the realm instance
        RealmController.with(this).refresh();
        // get all persisted objects
        // create the helper adapter and notify data set changes
        // changes will be reflected automatically
        setRealmAdapter(RealmController.with(this).getScheduleFromMonth(thisMonth));

        Toast.makeText(this, "Press card item for read, long press to set was read item", Toast.LENGTH_LONG).show();

        findViewsById();
    }

    public void setRealmAdapter(RealmResults<Schedule> schedules) {
        RealmScheduleAdapter realmAdapter = new RealmScheduleAdapter(this.getApplicationContext(), schedules, true);
        // Set the data and tell the RecyclerView to draw
        adapter.setRealmAdapter(realmAdapter);
        adapter.notifyDataSetChanged();
    }

    private void setupRecycler() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recycler.setHasFixedSize(true);

        // use a linear layout manager since the cards are vertically scrollable
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);

        // create an empty adapter and add it to the recycler view
        adapter = new ScheduleAdapter(this);
        recycler.setAdapter(adapter);
    }

    private void setRealmData() {
        List<Schedule> books = new ArrayList<>();
        books.add(new Schedule(new Date(), "Gen 1,1", false));

        for (Schedule b : books) {
            // Persist your data easily
            realm.beginTransaction();
            realm.copyToRealm(b);
            realm.commitTransaction();
        }
        Prefs.with(this).setPreLoad(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void findViewsById() {
        actualMonthView = findViewById(R.id.actualMonth);
        Log.v(TAG, "This month " + thisMonth.format(monthYearFormatter));
        actualMonthView.setText(thisMonth.format(monthYearFormatter));

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    public void onNextMonthClick(View view) {
        YearMonth lastMonth = thisMonth.plusMonths(1);
        actualMonthView.setText(lastMonth.format(monthYearFormatter));
        thisMonth = lastMonth;

        setRealmAdapter(RealmController.with(this).getScheduleFromMonth(thisMonth));
    }

    public void onBeforeMonthClick(View view) {
        YearMonth lastMonth = thisMonth.minusMonths(1);
        actualMonthView.setText(lastMonth.format(monthYearFormatter));
        thisMonth = lastMonth;

        setRealmAdapter(RealmController.with(this).getScheduleFromMonth(thisMonth));
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
            int scheduleList = realm.where(Schedule.class).findAll().size();
            if (scheduleList > 0) {
                Snackbar.make(getWindow().getDecorView(),
                        "Found: " + scheduleList +
                                " records in the database", Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(getWindow().getDecorView(), "Found no records in the database!", Snackbar.LENGTH_LONG).show();
            }
            return true;
        }
        if (id == R.id.action_import_from_csv) {
            // TODO TASK do not add much than 365 records per year
            RealmImporter.importFromJson(getResources());
            setRealmAdapter(RealmController.with(this).getScheduleFromMonth(thisMonth));
            Log.v(TAG, "Data was successfully imported");
        }
        if (id == R.id.action_remove_data) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            Snackbar.make(getWindow().getDecorView(), "Data was deleted!", Snackbar.LENGTH_LONG).show();
                            realm.deleteAll();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                        default:
                            // Default action is doing nothing
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }

        return super.onOptionsItemSelected(item);
    }
}