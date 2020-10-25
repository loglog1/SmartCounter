package com.matsumoto.smartconuter.AllDataListShowAcitivity;

import android.content.Context;
import android.util.Pair;
import android.widget.ListView;
import android.widget.Toast;

import com.matsumoto.smartconuter.AllDataListShowAcitivity.DoubleList.DoubleListView;

import java.util.ArrayList;

public class AllDataListShowActivityViewModel {
    private Context context;
    private ListView listView;
    private DoubleListView doubleListView;

    public void onCreateViewModel(final Context context, ListView listView){
        this.context = context;
        this.listView = listView;
        this.doubleListView = new DoubleListView(context,listView);
    }

    public void drawAllTimestamps(ArrayList<Pair<String,String>> timestamps){
        if(timestamps==null || timestamps.isEmpty()){
            Toast.makeText(context,"表示するデータがありません",Toast.LENGTH_LONG).show();
            return;
        }
        this.listView = this.doubleListView.getListview(timestamps);

    }


}
