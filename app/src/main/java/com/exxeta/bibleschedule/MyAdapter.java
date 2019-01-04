package com.exxeta.bibleschedule;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.exxeta.bibleschedule.Model.Schedule;

import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Adapter for manage Item in list
 */
@RequiresApi(api = Build.VERSION_CODES.O)
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

    public void updateYearMonthValues(YearMonth actualYearMonth) {
        Date dateFrom = Date.from(actualYearMonth.atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateTo = Date.from(actualYearMonth.atEndOfMonth().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        scheduleArrayList = getModelListFromTo(dateFrom, dateTo);
        notifyDataSetChanged();
    }

    private void loadSchedule() {
        Date dateFrom = Date.from(YearMonth.now().atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateTo = Date.from(YearMonth.now().atEndOfMonth().atStartOfDay(ZoneId.systemDefault()).toInstant());
        scheduleArrayList = getModelListFromTo(dateFrom, dateTo);
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
        final Schedule currentOld = scheduleArrayList.get(position);
        final Schedule current = this.findByDate(currentOld.getDate());

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
        viewHolder.tvDate.setText(new SimpleDateFormat("dd.MMM").format(current.getDate()));
        viewHolder.tvCoordinate.setText(current.getCoordinates());
        viewHolder.checkBox.setChecked(current.getWasRead());

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date searched = current.getDate();
                Toast.makeText(context, "Checkbox " + searched + " clicked!", Toast.LENGTH_SHORT).show();
                saveCheckedByDate(searched);
            }
        });
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

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

    public List<Schedule> getModelListFromTo(Date from, Date to) {
        List<Schedule> list = new ArrayList<>();
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<Schedule> results = realm.where(Schedule.class).between("date", from, to).findAll();
            list.addAll(realm.copyFromRealm(results));
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        return list;
    }

    private Schedule findByDate(Date date) {
        Schedule schedule;
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            Schedule results = realm.where(Schedule.class).equalTo("date", date).findFirst();
            schedule = realm.copyFromRealm(results);
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        return schedule;
    }

    private void saveCheckedByDate(Date date) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            Schedule findedResult = realm.where(Schedule.class).equalTo("date", date).findFirst();
            realm.beginTransaction();
            findedResult.setWasRead(true);
            realm.insertOrUpdate(findedResult);
            realm.commitTransaction();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }


    private class ViewHolder {
        protected CheckBox checkBox;
        private TextView tvCoordinate;
        private TextView tvDate;
    }
}