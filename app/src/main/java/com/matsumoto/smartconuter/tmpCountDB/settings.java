package com.matsumoto.smartconuter.tmpCountDB;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

//import com.matsumoto.smartconuter.ListViews.Adapter;
import com.matsumoto.smartconuter.R;

public class settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ListView setting_list = (ListView)findViewById(R.id.set_list);
//        Adapter arrayAdapter=new Adapter(this);
//        arrayAdapter.addRecords_data(new GeneralRecords("内容","時間"));
//        arrayAdapter.addRecords_data();
    }

    /*public void clearRecords(final View view){
        Button button = findViewById(R.id.clear);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("警告")
                        .setIcon(R.drawable.ic_warning_black_24dp)
                        .setMessage("全ての記録・設定が消去されます。消去しますか？")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                log_records.clear();
                                contents.clear();
                                File file = new File(Environment.getExternalStorageDirectory(),"contents.txt");
                                file.delete();
                                file = new File(Environment.getExternalStorageDirectory(),"records.txt");
                                file.delete();
                                setElements();
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();
            }
        });
    }*/


}
