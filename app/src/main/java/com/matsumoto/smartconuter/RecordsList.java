package com.matsumoto.smartconuter;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.matsumoto.smartconuter.MainActivity.MainActivity;

import java.util.ArrayList;

public class RecordsList extends AppCompatActivity{

    Adapter arrayAdapter;
    ArrayList<GeneralRecords> log_records;
    int contents_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_list);

        Intent intent = getIntent();
         log_records = MainActivity.log_records;

        ListView listview = (ListView)findViewById(R.id.ListView);
        arrayAdapter = new Adapter(this);
        arrayAdapter.addRecords_data(log_records);

        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String theme = log_records.get(position).getName();
                Toast.makeText(RecordsList.this, theme, Toast.LENGTH_LONG).show();

                log_records.remove(position);
                for(GeneralRecords l:log_records) Log.d("aaaa", "onItemClick: "+l.getTime());
                arrayAdapter.notifyDataSetChanged();
            }
        });
    }

}
