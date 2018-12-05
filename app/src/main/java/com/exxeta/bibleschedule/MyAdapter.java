package com.exxeta.bibleschedule;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.exxeta.bibleschedule.Model.Schedule;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Adapter for manage Item in list
 */
public class MyAdapter extends BaseAdapter implements Filterable {
    public Context context;
    public static ArrayList<Schedule> scheduleArrayList;
    public ArrayList<Schedule> orig;

    public MyAdapter(Context context, DBController controller) {
        super();
        this.context = context;
        this.scheduleArrayList = controller.getAllCoordinates(); // from db

        sortSchedule();
    }

    private void sortSchedule() {
        Collections.sort(scheduleArrayList, ((Schedule o1, Schedule o2) -> o1.getDate().compareTo(o2.getDate())));
    }


    @Override
    public int getCount() {
        return scheduleArrayList.size();
        //return controller.getAllCoordinates().size();
    }

    @Override
    public Schedule getItem(int position) {
        return scheduleArrayList.get(position);

        //return controller.getAllCoordinates().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
        //return controller.getAllCoordinates().get(position).hashCode();
    }

    /**
     * SOURCE https://demonuts.com/listview-checkbox/
     * @param position
     * @param convertView
     * @param container
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.list_item, container, false);
        }

        ((TextView) convertView.findViewById(android.R.id.text1))
                .setText(getItem(position).getDate().toString());
        ((TextView) convertView.findViewById(android.R.id.text2))
                .setText(getItem(position).getCoordinate());

        TextView textView = (TextView) convertView.findViewById(android.R.id.text2);
        final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO check this textviews and checkbox after click na read the text
                // LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                // inflater.inflate(R.layout.list_item, container, false);
                Intent intent = new Intent(context.getApplicationContext(),NextActivity.class);
                context.getApplicationContext().startActivity(intent);
            }
        });

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