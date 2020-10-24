package com.matsumoto.smartconuter.MainActivity.TimestampList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.matsumoto.smartconuter.R;

import java.util.List;

public class TimestampListAdapter extends ArrayAdapter<TimestampContent> {

    private LayoutInflater layoutinflater;

    // コンストラクタ
    public TimestampListAdapter(Context context, int textViewResourceId, List<TimestampContent> objects){
        super(context, textViewResourceId, objects);
        layoutinflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TimestampContent detail = getItem(position);

//        // レイアウトが初めて作成される場合のみ作成
        if(null == convertView){
            convertView = layoutinflater.inflate(R.layout.timestamp_content, null);
        }

        TextView text2 = (TextView)convertView.findViewById(R.id.timestamp);
        text2.setText(detail.getTime_stamp());
//
        return convertView;
    }
}
