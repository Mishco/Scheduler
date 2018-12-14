package com.exxeta.bibleschedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.exxeta.bibleschedule.Model.Schedule;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Adapter for manage Item in list
 */
public class MyAdapter extends BaseAdapter implements Filterable {
    private Context context;
    public static List<Schedule> scheduleArrayList;
    public List<Schedule> orig;
    Realm realm;


    public MyAdapter(Context context) {
        super();
        this.context = context;
        realm = Realm.getDefaultInstance();

        loadSchedule();
        sortSchedule();
    }

    private void loadSchedule() {
        scheduleArrayList = getModelList();
        //controller.getWeekRecordsFromAllCoordinates(LocalDate.parse("12/31/2018", dateFormatter)); // from db
    }

    private void sortSchedule() {
        Collections.sort(scheduleArrayList, ((Schedule o1, Schedule o2) -> o1.getDate().compareTo(o2.getDate())));
    }

    @Override
    public int getCount() {
        return scheduleArrayList.size();
    }

    @Override
    public Schedule getItem(int position) {
        return scheduleArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * SOURCE https://demonuts.com/listview-checkbox/
     *
     * @param position
     * @param convertView
     * @param container
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        final ViewHolder viewHolder;
        final Schedule current = scheduleArrayList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, container, false);

            viewHolder = new ViewHolder();
            viewHolder.tvDate = convertView.findViewById(R.id.text1);
            viewHolder.tvCoordinate = convertView.findViewById(R.id.text2);
            viewHolder.checkBox = convertView.findViewById(R.id.checkbox1);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("MMM/dd");

        viewHolder.tvDate.setText(new SimpleDateFormat("dd.MMM").format(current.getDate()));
        viewHolder.tvCoordinate.setText(current.getCoordinates());
        viewHolder.checkBox.setChecked(current.getWasRead());




//        TextView textView = (TextView) convertView.findViewById(android.R.id.text2);
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO check this textviews and checkbox after click na read the text
//                Intent intent = new Intent(context.getApplicationContext(), NextActivity.class);
//                context.getApplicationContext().startActivity(intent);
//            }
//        });

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
                final List<Schedule> results = new ArrayList<>();
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
                scheduleArrayList = (List<Schedule>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public List<Schedule> getModelList() {
        List<Schedule> list = new ArrayList<>();
        Realm realm = null;
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
        return list;
    }

    private class ViewHolder {
        protected CheckBox checkBox;
        private TextView tvCoordinate;
        private TextView tvDate;
    }


}