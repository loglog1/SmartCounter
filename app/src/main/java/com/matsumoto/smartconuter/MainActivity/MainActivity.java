package com.matsumoto.smartconuter.MainActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Switch;

import com.matsumoto.smartconuter.ControllDB;
import com.matsumoto.smartconuter.R;

import java.util.HashMap;
import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{
    private static Context instance = null;
    private static final int REQUEST_CODE_PICKER = 1;
    private static final int REQUEST_CODE_PERMISSION = 2;
    private MainAcitivtyViewModel viewModel;
    private Widget widget;

    public static int[] button_id={
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

    public class Widget{
        public Switch swt;
        public Button setting;
        public HashMap<String,Button> contents;
        public SparseArray<Integer> id_pare;
        public int window_width;
        public int window_height;
//        public HashMap<Integer,Integer> id_pare=new HashMap<Integer, Integer>();
    }
    
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

        instance = MainActivity.this;
         widget = new Widget();
        widget = setWidget(widget);

        viewModel = new MainAcitivtyViewModel(instance, button_id, widget);
        viewModel.onCreateViewModel(this.findViewById(R.id.setting_mode_switch).getRootView());
        fixedButtonWidth();
        ControllDB cdb = new ControllDB(MainActivity.this);
        SQLiteDatabase db = cdb.getWritableDatabase();
        try{
            cdb.onCreate(db);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void onClickContents(View view){
        viewModel.onClickContents(view);
    }

    public void onClickSettingButton(View view){
        viewModel.onClickSettingButton(view);
    }

    public void toWholeSettings(final View view){
//        Intent intent = new Intent(this, RecordsList.class);
//        startActivities(intent);
    }

    private Widget setWidget(Widget widget){
        widget.swt = (Switch)findViewById(R.id.setting_mode_switch);
        widget.setting=(Button)findViewById(R.id.whole_settings);
        widget.contents = new HashMap<String,Button>();
        widget.contents.put( "upper_left",(Button)findViewById(R.id.upper_left) );
        widget.contents.put( "upper_center",(Button)findViewById(R.id.upper_center) );
        widget.contents.put( "upper_right",(Button)findViewById(R.id.upper_right) );
        widget.contents.put( "center_left",(Button)findViewById(R.id.center_left) );
        widget.contents.put( "center_center",(Button)findViewById(R.id.center_center) );
        widget.contents.put( "center_right",(Button)findViewById(R.id.center_right) );
        widget.contents.put( "lower_left",(Button)findViewById(R.id.lower_left) );
        widget.contents.put( "lower_center",(Button)findViewById(R.id.lower_center) );
        widget.contents.put( "lower_right",(Button)findViewById(R.id.lower_right) );
        widget.id_pare = getBtnidDBidPair();

        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        this.widget.window_width = size.x;
        this.widget.window_height = size.y;
        return widget;
    }

    private void fixedButtonWidth(){
        int each_width = (int)(this.widget.window_width/3.);

        for(int id:button_id){
            Button btn = findViewById(id);
            btn.setWidth(each_width);
        }
    }

    //    Preferenceを使って表示するデータのDBのIDを取得する。
    //    存在しなければDBのIDは-1
    private SparseArray<Integer> getBtnidDBidPair(){
//    private HashMap<Integer,Integer> getBtnidDBidPair(){
//        HashMap<Integer,Integer> map=new HashMap<Integer, Integer>();
        SparseArray<Integer> map=new SparseArray<Integer>();
        SharedPreferences pref = getSharedPreferences("idpair.txt",MODE_PRIVATE);
        for(Integer btn_id:button_id){
            Integer db_id = pref.getInt(btn_id.toString(),-1);
            Log.d("aaa", "getBtnidDBidPair: "+btn_id.toString()+","+db_id.toString());
            map.put(btn_id,db_id);
        }
        return map;
    }

    @Override
    public void onStop(){
        SharedPreferences pref = getSharedPreferences("idpair.txt",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        for(Integer btn_id:button_id){
            editor.putInt(btn_id.toString(),widget.id_pare.get(btn_id));
        }
        editor.apply();
        super.onStop();
    }
}
