package com.matsumoto.smartconuter.MainActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.matsumoto.smartconuter.ContentsAdapter;
import com.matsumoto.smartconuter.ContentsAndCount;
import com.matsumoto.smartconuter.GeneralRecords;
import com.matsumoto.smartconuter.R;
import com.matsumoto.smartconuter.RecordsList;
import com.matsumoto.smartconuter.CountDB.settings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.text.TextUtils.split;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback,View.OnLongClickListener {
    private Context this_context = null;
    private static final int REQUEST_CODE_PICKER = 1;
    private static final int REQUEST_CODE_PERMISSION = 2;
    private String TODAY="";


    public static ArrayList<GeneralRecords> log_records = new ArrayList<GeneralRecords>();
    private HashMap<Integer, ContentsAndCount> contents = new HashMap(16);

    private int[] buttons_id = {
            R.id.upper_left,
            R.id.upper_center,
            R.id.upper_right,
            R.id.center_left,
            R.id.center_center,
            R.id.center_right,
            R.id.lower_left,
            R.id.lower_center,
            R.id.lower_right
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        if(!ActivityCompat.shouldShowRequestPermissionRationale(
                this,  Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ||!ActivityCompat.shouldShowRequestPermissionRationale(
                this,  Manifest.permission.READ_EXTERNAL_STORAGE)
        ){
            final int REQUEST_CODE_PERMISSION = 2;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION);
        }

        this_context = getApplicationContext();
        TODAY = getNowDate().replace("/","");

        setElements();
    }

    @Override
    public boolean onLongClick(final View view){
        Button btn = findViewById(view.getId());
        CharSequence tag = contents.get(view.getId()).getOver_length_name();
        final EditText log_contents = new EditText(this);
        log_contents.setHint(tag);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("記録内容の変更")
                .setMessage("新しい記録内容を入力してください")
                .setView(log_contents)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Button btn = findViewById(view.getId());
                        String change=log_contents.getText().toString();
                        if(change.length()>3){
                            String over_length = change;
                            contents.get(view.getId()).setOver_length_name(over_length);
                            change = change.substring(0,3)+"...";
                            contents.get(view.getId()).setName(change);
                        }else{
                            contents.get(view.getId()).setOver_length_name(change);
                            contents.get(view.getId()).setName(change);
                        }

                        contents.get(view.getId()).clearRecord();
                        /*文字列の大きさ制御*/
                        SpannableStringBuilder sb = transformStrings(log_contents.getText().toString()," 0");
                        btn.setText(sb);
                    }

                })
                .setNegativeButton("CANCEL", new  DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // クリックしたときの処理
                    }
                });

        builder.show();
        return true;
    }

    private void setElements(){
        String[] data = new String[9];
        File file = new File(Environment.getExternalStorageDirectory(),"contents.txt");

        if(file.exists()&&file.length()!=0) {

            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
                BufferedReader buf = new BufferedReader(inputStreamReader);
                String in_data;

                in_data = buf.readLine();
                String[] splited_data;
                splited_data = split(in_data, ",");
                contents.put(R.id.upper_left, new ContentsAndCount(splited_data[0], Integer.valueOf(splited_data[1])));
                SpannableStringBuilder sb = transformStrings(splited_data[0], splited_data[1]);
                ((Button)findViewById(R.id.upper_left)).setText(sb);

                in_data = buf.readLine();
                splited_data = split(in_data, ",");
                contents.put(R.id.upper_center, new ContentsAndCount(splited_data[0], Integer.valueOf(splited_data[1])));
                sb = transformStrings(splited_data[0], splited_data[1]);
                ((Button)findViewById(R.id.upper_center)).setText(sb);

                in_data = buf.readLine();
                splited_data = split(in_data, ",");
                contents.put(R.id.upper_right, new ContentsAndCount(splited_data[0], Integer.valueOf(splited_data[1])));
                sb = transformStrings(splited_data[0], splited_data[1]);
                ((Button)findViewById(R.id.upper_right)).setText(sb);

                in_data = buf.readLine();
                splited_data = split(in_data, ",");
                contents.put(R.id.center_left, new ContentsAndCount(splited_data[0], Integer.valueOf(splited_data[1])));
                sb = transformStrings(splited_data[0], splited_data[1]);
                ((Button)findViewById(R.id.center_left)).setText(sb);

                in_data = buf.readLine();
                splited_data = split(in_data, ",");
                contents.put(R.id.center_center, new ContentsAndCount(splited_data[0], Integer.valueOf(splited_data[1])));
                sb = transformStrings(splited_data[0], splited_data[1]);
                ((Button)findViewById(R.id.center_center)).setText(sb);

                in_data = buf.readLine();
                splited_data = split(in_data, ",");
                contents.put(R.id.center_right, new ContentsAndCount(splited_data[0], Integer.valueOf(splited_data[1])));
                sb = transformStrings(splited_data[0], splited_data[1]);
                ((Button)findViewById(R.id.center_right)).setText(sb);

                in_data = buf.readLine();
                splited_data = split(in_data, ",");
                contents.put(R.id.lower_left, new ContentsAndCount(splited_data[0], Integer.valueOf(splited_data[1])));
                sb = transformStrings(splited_data[0], splited_data[1]);
                ((Button)findViewById(R.id.lower_left)).setText(sb);

                in_data = buf.readLine();
                splited_data = split(in_data, ",");
                sb = transformStrings(splited_data[0], splited_data[1]);
                contents.put(R.id.lower_center, new ContentsAndCount(splited_data[0], Integer.valueOf(splited_data[1])));
                ((Button)findViewById(R.id.lower_center)).setText(sb);

                in_data = buf.readLine();
                splited_data = split(in_data, ",");
                contents.put(R.id.lower_right, new ContentsAndCount(splited_data[0], Integer.valueOf(splited_data[1])));
                sb = transformStrings(splited_data[0], splited_data[1]);
                ((Button)findViewById(R.id.lower_right)).setText(sb);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.d("element", "setElements: pass");
            SpannableStringBuilder sb;
            contents.put(R.id.upper_left, new ContentsAndCount("未設定1", 1));
            sb = transformStrings("未設定1"," 0");
            ((Button)findViewById(R.id.upper_left)).setText(sb);

            contents.put(R.id.upper_center, new ContentsAndCount("未設定2", 1));
            sb = transformStrings("未設定2"," 0");
            ((Button)findViewById(R.id.upper_center)).setText(sb);

            contents.put(R.id.upper_right, new ContentsAndCount("未設定3", 1));
            sb = transformStrings("未設定3"," 0");
            ((Button)findViewById(R.id.upper_right)).setText(sb);

            contents.put(R.id.center_left, new ContentsAndCount("未設定4", 1));
            sb = transformStrings("未設定4"," 0");
            ((Button)findViewById(R.id.center_left)).setText(sb);

            contents.put(R.id.center_center, new ContentsAndCount("未設定5", 1));
            sb = transformStrings("未設定5"," 0");
            ((Button)findViewById(R.id.center_center)).setText(sb);

            contents.put(R.id.center_right, new ContentsAndCount("未設定6", 1));
            sb = transformStrings("未設定6"," 0");
            ((Button)findViewById(R.id.center_right)).setText(sb);

            contents.put(R.id.lower_left, new ContentsAndCount("未設定7", 1));
            sb = transformStrings("未設定7"," 0");
            ((Button)findViewById(R.id.lower_left)).setText(sb);

            contents.put(R.id.lower_center, new ContentsAndCount("未設定8", 1));
            sb = transformStrings("未設定8"," 0");
            ((Button)findViewById(R.id.lower_center)).setText(sb);

            contents.put(R.id.lower_right, new ContentsAndCount("未設定9", 1));
            sb = transformStrings("未設定9"," 0");
            ((Button)findViewById(R.id.lower_right)).setText(sb);
        }

        file = new File(Environment.getExternalStorageDirectory(),"records.txt");
        if(file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
                BufferedReader buf = new BufferedReader(inputStreamReader);
                String in_data;
                String[] splited_data;
                while ((in_data = buf.readLine()) != null) {
                    splited_data = split(in_data, ",");
                    log_records.add(new GeneralRecords(splited_data[0], splited_data[1]));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getNowDate(){
        final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    public void clickContents(final View view){
        final Button button = (Button)findViewById(view.getId());
        final Boolean list_mode = ((Switch)findViewById(R.id.list)).isChecked();
        final Boolean change_contents_mode = ((Switch)findViewById(R.id.change_contens)).isChecked();



        //通常記録モード
        if(!change_contents_mode && !list_mode){
            String now = getNowDate();
            log_records.add(new GeneralRecords(contents.get(view.getId()).getOver_length_name(),now));
            contents.get(view.getId()).addRecord(now);

            Button btn = findViewById(view.getId());
            btn.setText(btn.getText());

            String theme = contents.get(view.getId()).getOver_length_name();
            String str_count = String.valueOf(contents.get(view.getId()).getNumOfRecords());

            /*文字列の大きさ制御*/
            SpannableStringBuilder sb = transformStrings(theme, str_count);
            btn.setText(sb);
            Toast.makeText(this_context,"記録しました!!",Toast.LENGTH_LONG).show();

//            final CharSequence tag = button.getText();
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//            builder.setTitle("カウントの確認")
//                    .setMessage(contents.get(view.getId()).getOver_length_name()+"をカウントしますか？")
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//                        public void onClick(DialogInterface dialog, int id) {
//                            String now = getNowDate();
//                            contents.get(view.getId()).addRecord(now);
//                            log_records.add(new GeneralRecords(contents.get(view.getId()).getOver_length_name(),now));
//
//                            Button btn = findViewById(view.getId());
//                            btn.setText(btn.getText());
//
//                            String theme = contents.get(view.getId()).getOver_length_name();
//                            String str_count = String.valueOf(contents.get(view.getId()).getNumOfRecords());
//
//                            /*文字列の大きさ制御*/
//                            SpannableStringBuilder sb = transformStrings(theme, str_count);
//                            btn.setText(sb);
//
//                            Toast.makeText(this_context,"記録しました!!",Toast.LENGTH_LONG).show();
//
//                        }
//
//                    })
//                    .setNegativeButton("CANCEL", new  DialogInterface.OnClickListener() {
//
//                        public void onClick(DialogInterface dialog, int which) {
//                            // クリックしたときの処理
//                        }
//
//                    });
//
//            builder.show();
        }

        //内容変更モード
        if(change_contents_mode && !list_mode) {
            CharSequence tag = contents.get(view.getId()).getOver_length_name();
            final EditText log_contents = new EditText(this);
            log_contents.setHint(tag);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("新しい記録内容を入力してください")
                    .setView(log_contents)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Button btn = findViewById(view.getId());
                            String change=log_contents.getText().toString();
                            if(change.length()>3){
                                String over_length = change;
                                contents.get(view.getId()).setOver_length_name(over_length);
                                change = change.substring(0,3)+"...";
                                contents.get(view.getId()).setName(change);
                            }else{
                                contents.get(view.getId()).setOver_length_name(change);
                                contents.get(view.getId()).setName(change);
                            }
                            
                            contents.get(view.getId()).clearRecord();
                            /*文字列の大きさ制御*/
                            SpannableStringBuilder sb = transformStrings(log_contents.getText().toString()," 0");
                            btn.setText(sb);

                            ((Switch)findViewById(R.id.change_contens)).setChecked(false);
                        }

                    })
                    .setNegativeButton("CANCEL", new  DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // クリックしたときの処理
                        }
                    });

            builder.show();
        }

        //一覧表示モード
        if(!change_contents_mode && list_mode){
            final CharSequence tag = button.getText();

            ListView listview = new ListView(this);
            ContentsAdapter arrayAdapter=new ContentsAdapter(this);

            arrayAdapter.setRecords_data(contents.get(view.getId()));

            listview.setAdapter(arrayAdapter);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(tag.toString()+"の記録一覧")
                    .setView(listview)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ((Switch)findViewById(R.id.list)).setChecked(false);
                        }

                    })
                    .setNegativeButton("データを一つ消す", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            int index=0;
                            for(int itr=log_records.size()-1;itr>=0;itr--){
                                if(log_records.get(itr).getName() == tag.toString() ) {
                                    index = itr;
                                }
                            }
                            log_records.remove(index);
                            contents.get(view.getId()).eraseRecord(contents.get(view.getId()).getNumOfRecords()-1);
                            ((Switch)findViewById(R.id.list)).setChecked(false);
                            changeMode();

                            Button btn = findViewById(view.getId());
                            btn.setText(btn.getText());

                            String theme = contents.get(view.getId()).getOver_length_name();
                            String str_count = String.valueOf(contents.get(view.getId()).getNumOfRecords());

                            /*文字列の大きさ制御*/
                            SpannableStringBuilder sb = transformStrings(theme, str_count);
                            btn.setText(sb);

                            final String RED = "ffaaaa";
                            final String BLUE = "9bdeff";
                            final String GREEN = "b0ffb0";
                            for(int btn_id:buttons_id) {
                                btn = findViewById(btn_id);
                                if (btn_id == R.id.upper_left || btn_id == R.id.upper_center || btn_id == R.id.upper_right) {
                                    btn.setBackgroundColor(Color.parseColor("#ff"+RED));
                                    continue;
                                }
                                if (btn_id == R.id.center_left || btn_id == R.id.center_center || btn_id == R.id.center_right) {
                                    btn.setBackgroundColor(Color.parseColor("#ff"+BLUE));
                                    continue;
                                }
                                if (btn_id == R.id.lower_left || btn_id == R.id.lower_center || btn_id == R.id.lower_right) {
                                    btn.setBackgroundColor(Color.parseColor("#ff"+GREEN));
                                }
                            }
                        }

                    })
            .setCancelable(true);

            builder.show();

        }

    }

    private SpannableStringBuilder transformStrings(String theme, String count){
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(theme);
        sb.setSpan(new RelativeSizeSpan(2.0f), 0, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        int start = sb.length();
        sb.append(" "+count+"回");
        sb.setSpan(new RelativeSizeSpan(1.5f), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    public void clickData(final View view){
        Intent intent = new Intent(this, RecordsList.class);
        startActivity(intent);

    }

    public void toSettings(final View view){
        Intent intent = new Intent(this, settings.class);
        startActivity(intent);
    }

     public void clearRecords(final View view){
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
    }

    public void changeMode(View view){
//        final Button button = (Button)findViewById(view.getId());
        final Boolean list_mode = ((Switch)findViewById(R.id.list)).isChecked();
        final Boolean change_contents_mode = ((Switch)findViewById(R.id.change_contens)).isChecked();
        final String RED = "ffaaaa";
        final String BLUE = "9bdeff";
        final String GREEN = "b0ffb0";

        Resources res = getResources();

        if(change_contents_mode) {

            for (int btn_id : buttons_id) {
                Button btn = findViewById(btn_id);
                if (btn_id == R.id.upper_left || btn_id == R.id.upper_center || btn_id == R.id.upper_right) {
                    btn.setBackgroundColor(Color.parseColor("#AA"+RED));
                    continue;
                }
                if (btn_id == R.id.center_left || btn_id == R.id.center_center || btn_id == R.id.center_right) {
                    btn.setBackgroundColor(Color.parseColor("#AA"+BLUE));
                    continue;
                }
                if (btn_id == R.id.lower_left || btn_id == R.id.lower_center || btn_id == R.id.lower_right) {
                    btn.setBackgroundColor(Color.parseColor("#AA"+GREEN));
                }
            }
            return;
        }

        if(list_mode) {

            for (int btn_id : buttons_id) {
                Button btn = findViewById(btn_id);
                if (btn_id == R.id.upper_left || btn_id == R.id.upper_center || btn_id == R.id.upper_right) {
                    btn.setBackgroundColor(Color.parseColor("#55"+RED));
                    continue;
                }
                if (btn_id == R.id.center_left || btn_id == R.id.center_center || btn_id == R.id.center_right) {
                    btn.setBackgroundColor(Color.parseColor("#55"+BLUE));
                    continue;
                }
                if (btn_id == R.id.lower_left || btn_id == R.id.lower_center || btn_id == R.id.lower_right) {
                    btn.setBackgroundColor(Color.parseColor("#55"+GREEN));
                }
            }
            return;
        }

        for(int btn_id:buttons_id) {
            Button btn = findViewById(btn_id);
            if (btn_id == R.id.upper_left || btn_id == R.id.upper_center || btn_id == R.id.upper_right) {
                btn.setBackgroundColor(Color.parseColor("#ff"+RED));
                continue;
            }
            if (btn_id == R.id.center_left || btn_id == R.id.center_center || btn_id == R.id.center_right) {
                btn.setBackgroundColor(Color.parseColor("#ff"+BLUE));
                continue;
            }
            if (btn_id == R.id.lower_left || btn_id == R.id.lower_center || btn_id == R.id.lower_right) {
                btn.setBackgroundColor(Color.parseColor("#ff"+GREEN));
            }
        }

    }

    public void changeMode(){
//        final Button button = (Button)findViewById(view.getId());
        final Boolean list_mode = ((Switch)findViewById(R.id.list)).isChecked();
        final Boolean change_contents_mode = ((Switch)findViewById(R.id.change_contens)).isChecked();
        final String RED = "ffaaaa";
        final String BLUE = "9bdeff";
        final String GREEN = "b0ffb0";

        Resources res = getResources();

        if(change_contents_mode) {

            for (int btn_id : buttons_id) {
                Button btn = findViewById(btn_id);
                if (btn_id == R.id.upper_left || btn_id == R.id.upper_center || btn_id == R.id.upper_right) {
                    btn.setBackgroundColor(Color.parseColor("#AA"+RED));
                    continue;
                }
                if (btn_id == R.id.center_left || btn_id == R.id.center_center || btn_id == R.id.center_right) {
                    btn.setBackgroundColor(Color.parseColor("#AA"+BLUE));
                    continue;
                }
                if (btn_id == R.id.lower_left || btn_id == R.id.lower_center || btn_id == R.id.lower_right) {
                    btn.setBackgroundColor(Color.parseColor("#AA"+GREEN));
                }
            }
            return;
        }

        if(list_mode) {

            for (int btn_id : buttons_id) {
                Button btn = findViewById(btn_id);
                if (btn_id == R.id.upper_left || btn_id == R.id.upper_center || btn_id == R.id.upper_right) {
                    btn.setBackgroundColor(Color.parseColor("#55"+RED));
                    continue;
                }
                if (btn_id == R.id.center_left || btn_id == R.id.center_center || btn_id == R.id.center_right) {
                    btn.setBackgroundColor(Color.parseColor("#55"+BLUE));
                    continue;
                }
                if (btn_id == R.id.lower_left || btn_id == R.id.lower_center || btn_id == R.id.lower_right) {
                    btn.setBackgroundColor(Color.parseColor("#55"+GREEN));
                }
            }
            return;
        }

        for(int btn_id:buttons_id) {
            Button btn = findViewById(btn_id);
            if (btn_id == R.id.upper_left || btn_id == R.id.upper_center || btn_id == R.id.upper_right) {
                btn.setBackgroundColor(Color.parseColor("#ff"+RED));
                continue;
            }
            if (btn_id == R.id.center_left || btn_id == R.id.center_center || btn_id == R.id.center_right) {
                btn.setBackgroundColor(Color.parseColor("#ff"+BLUE));
                continue;
            }
            if (btn_id == R.id.lower_left || btn_id == R.id.lower_center || btn_id == R.id.lower_right) {
                btn.setBackgroundColor(Color.parseColor("#ff"+GREEN));
            }
        }

    }

    @Override
    public void onStop(){
        Log.d("destroy", "onDestroy: call");
        File file = new File(Environment.getExternalStorageDirectory(),"contents.txt");

        Log.d("destroy", "onStop: "+contents.get(R.id.upper_left).getNumOfRecords());

        try{
            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
            BufferedWriter bw = new BufferedWriter(outputStreamWriter);

            bw.write(contents.get(R.id.upper_left).getOver_length_name()+","+contents.get(R.id.upper_left).getNumOfRecords());
            bw.newLine();
            bw.write(contents.get(R.id.upper_center).getOver_length_name()+","+contents.get(R.id.upper_center).getNumOfRecords());
            bw.newLine();
            bw.write(contents.get(R.id.upper_right).getOver_length_name()+","+contents.get(R.id.upper_right).getNumOfRecords());
            bw.newLine();

            bw.write(contents.get(R.id.center_left).getOver_length_name()+","+contents.get(R.id.center_left).getNumOfRecords());
            bw.newLine();
            bw.write(contents.get(R.id.center_center).getOver_length_name()+","+contents.get(R.id.center_center).getNumOfRecords());
            bw.newLine();
            bw.write(contents.get(R.id.center_right).getOver_length_name()+","+contents.get(R.id.center_right).getNumOfRecords());
            bw.newLine();

            bw.write(contents.get(R.id.lower_left).getOver_length_name()+","+contents.get(R.id.lower_right).getNumOfRecords());
            bw.newLine();
            bw.write(contents.get(R.id.lower_center).getOver_length_name()+","+contents.get(R.id.lower_center).getNumOfRecords());
            bw.newLine();
            bw.write(contents.get(R.id.lower_right).getOver_length_name()+","+contents.get(R.id.lower_right).getNumOfRecords());
            bw.newLine();
            bw.flush();
            bw.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        file = new File(Environment.getExternalStorageDirectory(),"records.txt");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
            BufferedWriter bw = new BufferedWriter(outputStreamWriter);
            for(GeneralRecords g:log_records){
                bw.write(g.getName()+","+g.getTime());
                bw.newLine();
            }
            bw.flush();
            bw.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        super.onStop();
    }


}
