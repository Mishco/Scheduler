package com.exxeta.bibleschedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    private String[] data;
    private LayoutInflater inflater;

    public CustomAdapter(Context context, String[] array) {
        data = array;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final String current = data[position];

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, parent, false);

            viewHolder = new ViewHolder();
            //viewHolder.name = (TextView) convertView.findViewById(R.id.item_name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(current);

        return convertView;
    }


    private class ViewHolder {
        TextView name;
    }


}
