package com.matsumoto.smartconuter.AllDataListShowAcitivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.widget.ListView;

import com.matsumoto.smartconuter.AllDataListShowAcitivity.DoubleList.DoubleListView;
import com.matsumoto.smartconuter.ControllDB;
import com.matsumoto.smartconuter.R;

import java.util.ArrayList;

public class AllDataListShowActivity extends AppCompatActivity {
    private AllDataListShowActivityViewModel allDataListShowActivityViewModel;
    private ControllDB controllDB;
    private Context instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = AllDataListShowActivity.this;
        Intent intent = getIntent();
        String db_pass = intent.getStringExtra("db_pass");

        controllDB = new ControllDB(instance,db_pass);
        setContentView(R.layout.activity_all_data_list_show);
        ListView listView = findViewById(R.id.show_all);
        allDataListShowActivityViewModel = new AllDataListShowActivityViewModel();
        allDataListShowActivityViewModel.onCreateViewModel(instance,listView);
        ArrayList<Pair<String,String>> allTimestamps=controllDB.getAllTimestamp();
        allDataListShowActivityViewModel.drawAllTimestamps(allTimestamps);
    }
}
