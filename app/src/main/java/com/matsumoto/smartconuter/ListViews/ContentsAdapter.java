package com.matsumoto.smartconuter.ListViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.matsumoto.smartconuter.R;

public class ContentsAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater = null;
    ContentsAndCount records_data = new ContentsAndCount();

    public ContentsAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setRecords_data(ContentsAndCount records_data) {
        this.records_data = records_data;
    }

    @Override
    public int getCount() {
        return records_data.getNumOfRecords();
    }

    @Override
    public Object getItem(int position) {
        return records_data.getTime(position);
    }

    @Override
    public long getItemId(int position) {
        return records_data.getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.timestamp_content,parent,false);

        ((TextView)convertView.findViewById(R.id.price)).setText(String.valueOf(records_data.getTime(position)));

        return convertView;
    }

}
