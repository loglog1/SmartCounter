package com.matsumoto.smartconuter.MainActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.matsumoto.smartconuter.ControllDB;
import com.matsumoto.smartconuter.R;
import com.matsumoto.smartconuter.SettingModel.SettingActivity;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{
    private static Context instance = null;
    private static final int REQUEST_CODE_PICKER = 1;
    private static final int REQUEST_CODE_PERMISSION = 2;
    private MainAcitivtyViewModel viewModel;
    private ContentsSettings contentsSettings;
    public static Widget widget;

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
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
        contentsSettings = new ContentsSettings(this,button_id,widget);
        fixButtonSize();

    }

    public void onClickContents(View view){
        final Button button = view.findViewById(view.getId());
        if (isSettingMode()) {
            contentsSettings.drawSettingWindow(button);
        } else {
            viewModel.addCount(button);
        }
    }

    public void onClickSettingButton(View view){
        Switch swt = view.findViewById(R.id.setting_mode_switch);
        if(isSettingMode()){
            swt.setText("設定モード");
            this.changeButtonColor(true);
        }else{
            swt.setText("記録モード");
            this.changeButtonColor(false);
        }
    }

    public void changeButtonColor(Boolean is_setting_mode_) {
        final String RED = "ffaaaa";
        final String BLUE = "9bdeff";
        final String GREEN = "b0ffb0";

        if(is_setting_mode_) {
            widget.contents.get("upper_left").setBackgroundColor(Color.parseColor("#AA" + RED));
            widget.contents.get("upper_center").setBackgroundColor(Color.parseColor("#AA" + RED));
            widget.contents.get("upper_right").setBackgroundColor(Color.parseColor("#AA" + RED));

            widget.contents.get("center_left").setBackgroundColor(Color.parseColor("#AA" + BLUE));
            widget.contents.get("center_center").setBackgroundColor(Color.parseColor("#AA" + BLUE));
            widget.contents.get("center_right").setBackgroundColor(Color.parseColor("#AA" + BLUE));

            widget.contents.get("lower_left").setBackgroundColor(Color.parseColor("#AA" + GREEN));
            widget.contents.get("lower_center").setBackgroundColor(Color.parseColor("#AA" + GREEN));
            widget.contents.get("lower_right").setBackgroundColor(Color.parseColor("#AA" + GREEN));
        }else{
            widget.contents.get("upper_left").setBackgroundColor(Color.parseColor("#" + RED));
            widget.contents.get("upper_center").setBackgroundColor(Color.parseColor("#" + RED));
            widget.contents.get("upper_right").setBackgroundColor(Color.parseColor("#" + RED));

            widget.contents.get("center_left").setBackgroundColor(Color.parseColor("#" + BLUE));
            widget.contents.get("center_center").setBackgroundColor(Color.parseColor("#" + BLUE));
            widget.contents.get("center_right").setBackgroundColor(Color.parseColor("#" + BLUE));

            widget.contents.get("lower_left").setBackgroundColor(Color.parseColor("#" + GREEN));
            widget.contents.get("lower_center").setBackgroundColor(Color.parseColor("#" + GREEN));
            widget.contents.get("lower_right").setBackgroundColor(Color.parseColor("#" + GREEN));

        }
    }

    public void toWholeSettings(final View view){
        Intent intent = new Intent(instance,SettingActivity.class);
        instance.startActivity(intent);
    }

    public Boolean isSettingMode() {
        return widget.swt.isChecked();
    }

    private Widget setWidget(Widget widget){
        widget.swt = (Switch)findViewById(R.id.setting_mode_switch);
        widget.setting=(Button)findViewById(R.id.whole_settings);
        widget.contents = new HashMap<>();
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

    private void fixButtonSize(){

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.wrap_buttons);
        int layout_height = linearLayout.getHeight();

        int each_width = (int)(this.widget.window_width/3.);
        int each_height = (int)(layout_height/3.);

        for(int id:button_id){
            Button btn = findViewById(id);
            btn.setWidth(each_width);
            btn.setHeight(each_height);
        }
    }

    //    Preferenceを使って表示するデータのDBのIDを取得する。
    //    存在しなければDBのIDは-1
    private SparseArray<Integer> getBtnidDBidPair(){
        SparseArray<Integer> map=new SparseArray<Integer>();
        SharedPreferences pref = getSharedPreferences("idpair",MODE_PRIVATE);
        for(Integer btn_id:button_id){
            Integer db_id = pref.getInt(btn_id.toString(),-1);
            Log.d("aaa", "getBtnidDBidPair: "+btn_id.toString()+","+db_id.toString());
            map.put(btn_id,db_id);
        }
        return map;
    }

    @Override
    public void onPause(){
        SharedPreferences pref = getSharedPreferences("idpair",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        for(Integer btn_id:button_id){
            editor.putInt(btn_id.toString(),widget.id_pare.get(btn_id));
        }
        editor.apply();
        super.onPause();
    }
    @Override
    public void onStop(){
        super.onStop();
    }
}
