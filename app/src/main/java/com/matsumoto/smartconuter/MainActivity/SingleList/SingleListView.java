package com.matsumoto.smartconuter.MainActivity.SingleList;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SingleListView {
    private ListView listview;
    private Context MainActivity_context;

    public SingleListView(Context mainActivity_context_, ListView listview_){
        this.MainActivity_context = mainActivity_context_;
        this.listview = listview_;
    }

    public ListView getListview(ArrayList<String> contents_) {

        List<SingleContent> objects = new ArrayList<>();
        for (String content : contents_) {
            SingleContent item = new SingleContent();
            item.setContent(content);
            objects.add(item);
        }

        // ArrayAdapterを設定
        SingleListAdapter singleListAdapter = new SingleListAdapter(MainActivity_context, 0, objects);

        this.listview.setAdapter(singleListAdapter);

        return this.listview;
    }

    public void setOnItemClick(AdapterView.OnItemClickListener listener) {
        this.listview.setOnItemClickListener(listener);
    }

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
