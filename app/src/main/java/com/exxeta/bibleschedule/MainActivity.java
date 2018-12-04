package com.exxeta.bibleschedule;

import android.app.ListActivity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.exxeta.bibleschedule.Model.Schedule;

import java.util.ArrayList;

public class MainActivity extends ListActivity implements SearchView.OnQueryTextListener {

    private final int ITEMS_COUNT = 7;

    TextView scheduleId;
    DBController controller = new DBController(this);

    private SearchView mSearchView;
    private ListView mListView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSearchView = (SearchView) findViewById(R.id.searchView1);
        mListView = (ListView) findViewById(android.R.id.list);
        mListView.setTextFilterEnabled(true);

        initDb();
        setupSearchView();
        setListAdapter(new MyAdapter(MainActivity.this));
    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");
    }

    /**
     * Initialization sqlite database
     */
    private void initDb() {

        // FIXME ONLY AFTER INSTALLATION
//        controller.readDataFromCsv(getResources().openRawResource(
//                getResources().getIdentifier("coordinate_2019",
//                        "raw", getPackageName())));


        ArrayList<Schedule> coordinatesList = controller.getAllCoordinates();

        if (coordinatesList.size() != 0) {
            Log.d("MAIN", "Database was init");
        }
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
     * Adapter for manage Item in list
     */
    private class MyAdapter extends BaseAdapter implements Filterable {
        public Context context;
        public ArrayList<Schedule> scheduleArrayList;
        public ArrayList<Schedule> orig;

        public MyAdapter(Context context) {
            super();
            this.context = context;
            this.scheduleArrayList = controller.getAllCoordinates(); // from db
        }


        @Override
        public int getCount() {
            return scheduleArrayList.size();
            //return controller.getAllCoordinates().size();
            //return Cheeses.CHEESES.length;
        }

        @Override
        public Schedule getItem(int position) {
            return scheduleArrayList.get(position);

            //return controller.getAllCoordinates().get(position);

            //return Cheeses.CHEESES[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
            //return controller.getAllCoordinates().get(position).hashCode();

            //return Cheeses.CHEESES[position].hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item, container, false);
            }

            ((TextView) convertView.findViewById(android.R.id.text1))
                    .setText(getItem(position).getDate().toString());
            ((TextView) convertView.findViewById(android.R.id.text2))
                    .setText(getItem(position).getCoordinate());
            return convertView;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                /**
                 * Filter due to date
                 * @param constraint
                 * @return
                 */
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<Schedule> results = new ArrayList<>();
                    if (orig == null)
                        orig = scheduleArrayList;
                    if (constraint != null) {
                        if (orig != null && orig.size() > 0) {
                            for (final Schedule g : orig) {
                                if (g.getDate().toString().toLowerCase()
                                        .contains(constraint.toString()))
                                    results.add(g);
                            }
                        }
                        oReturn.values = results;
                    }
                    return oReturn;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    scheduleArrayList = (ArrayList<Schedule>) results.values;
                    notifyDataSetChanged();
                }
            };
        }

    }


}