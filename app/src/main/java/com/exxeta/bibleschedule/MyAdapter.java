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

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Adapter for manage Item in list
 */
public class MyAdapter extends BaseAdapter implements Filterable {
    private Context context;
    public static ArrayList<Schedule> scheduleArrayList;
    public ArrayList<Schedule> orig;

    private DBController controller;

    public MyAdapter(Context context, DBController controller) {
        super();
        this.context = context;
        this.controller = controller;

        loadSchedule();
        sortSchedule();
    }


    private void loadSchedule() {
        DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
        scheduleArrayList = controller.getAllCoordinates();
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

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("MMM/dd");

        viewHolder.tvDate.setText(dtfOut.print(current.getDate()));
        viewHolder.tvCoordinate.setText(current.getCoordinate());


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

    private class ViewHolder {
        protected CheckBox checkBox;
        private TextView tvCoordinate;
        private TextView tvDate;
    }


}