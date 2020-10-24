package com.matsumoto.smartconuter.MainActivity.TimestampList;

import android.content.Context;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

public class TimestampListView {
    private ListView listview;
    private Context MainActivity_context;

    public TimestampListView(Context mainActivity_context_, ListView listview_){
        this.MainActivity_context = mainActivity_context_;
        this.listview = listview_;
    }

    public ListView getListview(ArrayList<String> time_stamp_){

        List<TimestampContent> objects = new ArrayList<>();
        for(String tmp_stmp:time_stamp_){
            TimestampContent item = new TimestampContent();
            item.setTime_stamp(tmp_stmp);
            objects.add(item);
        }

        // ArrayAdapterを設定
        TimestampListAdapter timestampListAdapter = new TimestampListAdapter(MainActivity_context, 0, objects);

        this.listview.setAdapter(timestampListAdapter);

        return this.listview;

//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                // リストビューの項目を取得
//                ListView listview = (ListView) parent;
//                TimestampContent item = (TimestampContent) listview.getItemAtPosition(position);
//            }
//        });
//
//        // リストビューの項目が長押しされたときのコールバックリスナーを登録
//        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//                // リストビューの項目を取得
//                ListView listview = (ListView) parent;
//                RowDetail item = (RowDetail) listview.getItemAtPosition(position);
//                String text = "長押ししました:" + item.getTitle() + ":" + item.getDetail();
//
//                // onItemClickを実行しない
//                return true;
//            }
//        });
    }
}
