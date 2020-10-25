package com.matsumoto.smartconuter.AllDataListShowAcitivity.DoubleList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.matsumoto.smartconuter.MainActivity.SingleList.SingleContent;
import com.matsumoto.smartconuter.R;

import java.util.List;

public class DoubleListAdapter extends ArrayAdapter<DoubleContent> {

    private LayoutInflater layoutinflater;

    // コンストラクタ
    public DoubleListAdapter(Context context, int textViewResourceId, List<DoubleContent> objects){
        super(context, textViewResourceId, objects);
        layoutinflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DoubleContent detail = getItem(position);

//        // レイアウトが初めて作成される場合のみ作成
        if(null == convertView){
            convertView = layoutinflater.inflate(R.layout.double_content, null);
        }

        TextView text1 = (TextView)convertView.findViewById(R.id.content1);
        text1.setText(detail.getContent1());

        TextView text2 = (TextView)convertView.findViewById(R.id.content2);
        text2.setText(detail.getContent2());
//
        return convertView;
    }
}
