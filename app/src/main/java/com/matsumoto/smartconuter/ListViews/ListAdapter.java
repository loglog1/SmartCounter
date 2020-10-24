package com.matsumoto.smartconuter.ListViews;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.matsumoto.smartconuter.MainActivity.GeneralRecords;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    private ArrayList<GeneralRecords> log_contents;

    public ListAdapter(Context context){
        super();
    }

    @Override
    public int getCount() {
        return log_contents.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    // getViewメソッドをOverride
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return convertView;
    }
}
