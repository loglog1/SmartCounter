package com.matsumoto.smartconuter.SettingModel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.matsumoto.smartconuter.ControllDB;
import com.matsumoto.smartconuter.MainActivity.MainActivity;
import com.matsumoto.smartconuter.MainActivity.SingleList.SingleListView;
import com.matsumoto.smartconuter.MainActivity.TopPageWidgetManagement;
import com.matsumoto.smartconuter.R;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {
    private ListView listview;
    private ControllDB controllDB;
    private Context instance;
    private String db_pass;
    private TopPageWidgetManagement widget;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Intent intent = getIntent();
        this.widget = MainActivity.widget;
        this.db_pass = intent.getStringExtra("db_pass");

        this.listview = (ListView)findViewById(R.id.setting_menu);
        this.controllDB = new ControllDB(this,this.db_pass);
        this.instance = SettingActivity.this;

        ArrayList<String> content = new ArrayList<>();
        content.add("全データを削除する");
        content.add("現在のデータを共有する");
        content.add("現在のログを共有する");

        ListView.OnItemClickListener listener = new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
//                        全データ削除
                        delteAllData();
                        break;
//                        現在の記録の外部アプリへの共有
                    case 1:
                        shareActiveCountOtherApps();
                        break;
//                        ログの外部アプリへの共有
                    case 2:
                        shareLogOtherApps();
                        break;
                }
            }
        };
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SingleListView singleListView = new SingleListView(this.instance, this.listview);
        singleListView.setOnItemClick(listener);
        this.listview = singleListView.getListview(content);

    }

    private void delteAllData(){
        AlertDialog.Builder builder = new AlertDialog.Builder(instance);

        builder.setTitle("全データ削除")
                .setMessage("削除されたデータは復旧できません。本当によろしいですか？")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        controllDB.deleteAll();
                        Toast.makeText(instance, "データ全削除しました！",Toast.LENGTH_LONG).show();
                        widget.clearAllDBID();
                        Intent return_intent = new Intent();
                        return_intent.putExtra("is_deleteAll",true);
                        setResult(RESULT_OK,return_intent);
                        finish();
                    }
                }).setCancelable(true);
        builder.show();
    }

    public void shareActiveCountOtherApps() {
        List<Integer> btn_ids = this.widget.getButtonId();

        String record = "";
        for(Integer btn_id:btn_ids){
            Integer db_id = this.widget.getDBID(btn_id);
            if(db_id==-1) continue;
            String name =controllDB.getContentName(db_id);
            String count = controllDB.getTimStampCount(db_id);
            record+= name+","+count+"\n";
        }

        if(record.isEmpty()){
            Toast.makeText(this.instance,"データがありません",Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, "[Smart Counter]記録したログ");
        intent.putExtra(Intent.EXTRA_TEXT, record);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void shareLogOtherApps() {
        ControllDB controllDB = new ControllDB(instance,db_pass);

        ArrayList<Pair<String,String>>timestamps = controllDB.getAllActiveContents();
        if(timestamps.isEmpty()){
            Toast.makeText(instance,"データがありません",Toast.LENGTH_LONG).show();
            return;
        }
        String record = "";
        for(Pair<String,String> t:timestamps){
            record+= t.first+","+t.second+"\n";
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, "[Smart Counter]記録したログ");
        intent.putExtra(Intent.EXTRA_TEXT, record);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
