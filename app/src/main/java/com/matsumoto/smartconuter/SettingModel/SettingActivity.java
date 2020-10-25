package com.matsumoto.smartconuter.SettingModel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.matsumoto.smartconuter.ControllDB;
import com.matsumoto.smartconuter.MainActivity.SingleList.SingleListView;
import com.matsumoto.smartconuter.R;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {
    private static final String TAG = "aaa";
    private ListView listview;
    private ControllDB controllDB;
    private Context instance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        listview = (ListView)findViewById(R.id.setting_menu);
        controllDB = new ControllDB(this);
        instance = SettingActivity.this;

        ArrayList<String> content = new ArrayList<>();
        content.add("全データを削除する");
//        content.add("データをcsvファイルに書き出す");

        ListView.OnItemClickListener listener = new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
//                        全データ削除
                        delteAllData();
                        break;
//                        CSV書き出し
//                    case 1:
//                        toCSV();
//                        break;
                }
            }
        };

        SingleListView singleListView = new SingleListView(instance, listview);
        singleListView.setOnItemClick(listener);
        listview = singleListView.getListview(content);

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
                    }
                }).setCancelable(true);
        builder.show();
    }

    private void toCSV(){

        LinearLayout linearLayout = new LinearLayout(instance);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

//        ユーザーに「.csv」を入力させない
        final EditText editText = new EditText(instance);
        editText.setWidth(400);
        TextView textView = new TextView(instance);
        textView.setText(".csv");

        linearLayout.addView(editText);
        linearLayout.addView(textView);

        AlertDialog.Builder builder = new AlertDialog.Builder(instance);
        builder.setTitle("CSV書き出し")
                .setMessage("ファイル名を指定してください（.csvは入れないでください）")
                .setView(linearLayout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String filename = editText.getText().toString();
                        Boolean is_succeed = writeCSV(filename);
                        if(is_succeed){
                            Toast.makeText(instance, filename+".csv：書き出し成功しました！",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(instance, "書き出し失敗しました！",Toast.LENGTH_LONG).show();
                        }
                    }
                }).setCancelable(true);
        builder.show();

    }

    private Boolean writeCSV(String filename) {
        if(filename.contains(".csv")){
            Toast.makeText(instance,"ファイル名に.csvを入れないでください",Toast.LENGTH_LONG).show();
            return false;
        }

        ArrayList<Pair<String, String>> list = controllDB.getAllTimestamp();
        if(list.isEmpty()){
            Toast.makeText(instance,"出力するデータがありませんでした",Toast.LENGTH_LONG).show();
            return false;
        }

        final String NEW_LINE = "\r\n";
        OutputStream outputStream ;
        FileWriter fileWriter = null;

        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/tmp/"+filename+".csv");
        if(true) {

            try (
                    FileOutputStream fileOutputStream = new FileOutputStream(file, false);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
                    BufferedWriter bw = new BufferedWriter(outputStreamWriter)
            ) {


                for (Pair<String, String> l : list) {
                    bw.write(l.first+","+l.second);
                    bw.newLine();
                }
                bw.flush();
            } catch (Exception e) {
                Log.d(TAG, "error: FileOutputStream");
                e.printStackTrace();
            }
        }

//        try {
//             outputStream = instance.openFileOutput(filename+".csv",Context.MODE_PRIVATE);
//
//
//
//            for (Pair<String, String> l : list) {
//                outputStream.write(l.first.getBytes());
//                outputStream.write(",".getBytes());
//                outputStream.write(l.second.getBytes());
//                outputStream.write(NEW_LINE.getBytes());
//            }
//            outputStream.flush();
//            outputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        Uri.fromFile(Environment.getExternalStorageDirectory()+filename+".csv"

//        composeEmail(
//                new String[]{"bassai41@gmail.com"},
//                "test",
//                getUriForFile(instance, getApplicationContext().getPackageName() + ".fileprovider", file)
//        );

        return true;
    }

    public void composeEmail(String[] addresses, String subject, Uri attachment) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_STREAM, attachment);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


}
