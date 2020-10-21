package com.matsumoto.smartconuter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Adapter extends BaseAdapter{
    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<GeneralRecords> records_data = new ArrayList<GeneralRecords>();

    public Adapter(Context context) {
        super();
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setRecords_data(ArrayList<GeneralRecords> records_data) {
        this.records_data = records_data;
    }

    public void addRecords_data(GeneralRecords data){
        this.records_data.add(data);
    }

    public void addRecords_data(ArrayList<GeneralRecords> data){
        this.records_data.addAll(data);
    }


    @Override
    public int getCount() {
        return records_data.size();
    }

    @Override
    public GeneralRecords getItem(int position) {
        return records_data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return records_data.get(position).getId();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.record_contents,parent,false);

        ((TextView)convertView.findViewById(R.id.name)).setText(records_data.get(position).getName());
        ((TextView)convertView.findViewById(R.id.price)).setText(String.valueOf(records_data.get(position).getTime()));

        return convertView;
    }

}
