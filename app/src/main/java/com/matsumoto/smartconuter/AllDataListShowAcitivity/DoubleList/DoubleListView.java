package com.matsumoto.smartconuter.AllDataListShowAcitivity.DoubleList;

import android.content.Context;
import android.util.Pair;
import android.widget.AdapterView;
import android.widget.ListView;

import com.matsumoto.smartconuter.MainActivity.SingleList.SingleContent;
import com.matsumoto.smartconuter.MainActivity.SingleList.SingleListAdapter;

import java.util.ArrayList;
import java.util.List;

public class DoubleListView {
    private ListView listview;
    private Context context;

    public DoubleListView(Context context_, ListView listview_){
        this.context = context_;
        this.listview = listview_;
    }

    public ListView getListview(ArrayList<Pair<String,String>> contents_) {

        List<DoubleContent> objects = new ArrayList<>();
        for (Pair<String,String> content : contents_) {
            DoubleContent item = new DoubleContent();
            item.setContent(content.first, content.second);
            objects.add(item);
        }

        // ArrayAdapterを設定
        DoubleListAdapter doubleListView = new DoubleListAdapter(this.context, 0, objects);
        this.listview.setAdapter(doubleListView);

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
