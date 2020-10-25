package com.matsumoto.smartconuter.MainActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.matsumoto.smartconuter.AllDataListShowAcitivity.AllDataListShowActivity;
import com.matsumoto.smartconuter.R;
import com.matsumoto.smartconuter.SettingModel.SettingActivity;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;


import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{
    private static Context instance = null;
    private static final int REQUEST_CODE_PICKER = 1;
    private static final int REQUEST_CODE_PERMISSION = 2;
    private static final int REQUEST_SETTINGS = 10;
    private MainAcitivtyViewModel viewModel;
    private ContentsSettings contentsSettings;
    public static TopPageWidgetManagement widget;
    private String db_pass = "";
    private View root_view;

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


        widget = new TopPageWidgetManagement();
        widget.setDBIDs( getBtnidDBidPair() );

        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        widget.setWindowSize(size.x,size.y);

        HashMap<String,String> pass = getPassword();
        db_pass = pass.get("db_pass");

        instance = MainActivity.this;
        root_view = this.findViewById(R.id.setting_mode_switch).getRootView();

        viewModel = new MainAcitivtyViewModel(instance, widget,db_pass);

        viewModel.onCreateViewModel(root_view);
        contentsSettings = new ContentsSettings(this,widget, db_pass);
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
            swt.setText("記録モード");
            this.changeButtonColor(true);
        }else{
            swt.setText("設定モード");
            this.changeButtonColor(false);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    public void changeButtonColor(Boolean is_setting_mode_) {
        final String RED = "ffaaaa";
        final String BLUE = "9bdeff";
        final String GREEN = "b0ffb0";

        if(is_setting_mode_) {
            findViewById(R.id.upper_left).setBackgroundColor(Color.parseColor("#AA" + RED));
            findViewById(R.id.upper_center).setBackgroundColor(Color.parseColor("#AA" + RED));
            findViewById(R.id.upper_right).setBackgroundColor(Color.parseColor("#AA" + RED));

            findViewById(R.id.center_left).setBackgroundColor(Color.parseColor("#AA" + BLUE));
            findViewById(R.id.center_center).setBackgroundColor(Color.parseColor("#AA" + BLUE));
            findViewById(R.id.center_right).setBackgroundColor(Color.parseColor("#AA" + BLUE));

            findViewById(R.id.lower_left).setBackgroundColor(Color.parseColor("#AA" + GREEN));
            findViewById(R.id.lower_center).setBackgroundColor(Color.parseColor("#AA" + GREEN));
            findViewById(R.id.lower_right).setBackgroundColor(Color.parseColor("#AA" + GREEN));
        }else{
            findViewById(R.id.upper_left).setBackgroundColor(Color.parseColor("#" + RED));
            findViewById(R.id.upper_center).setBackgroundColor(Color.parseColor("#" + RED));
            findViewById(R.id.upper_right).setBackgroundColor(Color.parseColor("#" + RED));

            findViewById(R.id.center_left).setBackgroundColor(Color.parseColor("#" + BLUE));
            findViewById(R.id.center_center).setBackgroundColor(Color.parseColor("#" + BLUE));
            findViewById(R.id.center_right).setBackgroundColor(Color.parseColor("#" + BLUE));

            findViewById(R.id.lower_left).setBackgroundColor(Color.parseColor("#" + GREEN));
            findViewById(R.id.lower_center).setBackgroundColor(Color.parseColor("#" + GREEN));
            findViewById(R.id.lower_right).setBackgroundColor(Color.parseColor("#" + GREEN));

        }
    }

    public void toWholeSettings(final View view){
        Intent intent = new Intent(instance,SettingActivity.class);
        intent.putExtra("db_pass",db_pass);
        startActivityForResult(intent,REQUEST_SETTINGS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SETTINGS:
                if (RESULT_OK == resultCode) {
                    Boolean deleteAll = data.getBooleanExtra("is_deleteAll",false);
                    if(deleteAll){
                        viewModel.onCreateViewModel(root_view);
                    }
                }
                break;
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    public void toAllList(View view){
        Intent intent = new Intent(instance, AllDataListShowActivity.class);
        intent.putExtra("db_pass",db_pass);
        instance.startActivity(intent);
    }

    public Boolean isSettingMode() {
        Switch swt = (Switch)findViewById(R.id.setting_mode_switch);
        return swt.isChecked();
    }

    private void fixButtonSize(){

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.wrap_buttons);
        int layout_height = linearLayout.getHeight();

        int each_width = (int)(widget.getWindowWidth());
        int each_height = (int)(widget.getWindowHeight()/3.);

        for(Integer id:widget.getButtonId()){
            Button btn = findViewById(id);
            btn.setWidth(each_width);
            btn.setHeight(each_height);
        }
    }

    //    Preferenceを使って表示するデータのDBのIDを取得する。
    //    存在しなければDBのIDは-1
    private SparseArray<Integer> getBtnidDBidPair(){
        SparseArray<Integer> map=new SparseArray<>();
        SharedPreferences pref = getSharedPreferences("idpair",MODE_PRIVATE);
        for(Integer btn_id:widget.getButtonId()){
            Integer db_id = pref.getInt(btn_id.toString(),-1);
            map.put(btn_id,db_id);
        }
        return map;
    }

    public void writeBtnidDBidPair(){
        SharedPreferences pref = getSharedPreferences("idpair",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        for(Integer btn_id:widget.getButtonId()){
            Integer db_id = widget.getDBID(btn_id);
            editor.putInt(btn_id.toString(),db_id);
        }
        editor.apply();
    }

    private HashMap<String,String> getPassword() {
        SharedPreferences pass = getSharedPreferences("pass_key",MODE_PRIVATE);
        HashMap<String,String> password = new HashMap<>();
//        パスワード生成済みの場合
        if(pass.contains("db_pass")){
            password.put("db_pass",pass.getString("db_pass","") );
            return password;
        }

        PasswordGenerator gen = new PasswordGenerator();
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(2);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);

        CharacterData specialChars = new CharacterData() {
            public String getErrorCode() {
                return "error";
            }

            public String getCharacters() {
                return "[email protected]#$%^&** ()__+";
            }
        };
        CharacterRule splCharRule = new CharacterRule(specialChars);
        splCharRule.setNumberOfCharacters(2);

        String tmp_pass = gen.generatePassword(10, splCharRule, lowerCaseRule,
                upperCaseRule, digitRule);
        password.put("db_pass",tmp_pass);
        
        SharedPreferences.Editor editor = pass.edit();
        editor.putString("db_pass",password.get("db_pass"));
        editor.apply();
        return password;
    }

    private String getEncryptedString(String value, String secretKey) throws Exception {
        if (value == null) {
            throw new Exception("値が空です。");
        }
        // 暗号化
        SharedPreferenceEncrypt cryptUtil = new SharedPreferenceEncrypt();
        String encValue = cryptUtil.encrypt(value, secretKey);
        if (encValue == null) {
            throw new Exception("暗号化に失敗しました。");
        }
        return encValue;

    }

    private String getDecyptedString(String value,String secretKey) throws Exception {
        // 値取得
        if (value == null || value.length() == 0) {
            throw new Exception("値が空です");
        }
        // 復号
        SharedPreferenceEncrypt cryptUtil = new SharedPreferenceEncrypt();
        String decValue = cryptUtil.decrypt(value, secretKey);
        if (decValue == null) {
            throw new Exception("復号に失敗しました。");
        }
        return decValue;
    }

    @Override
    public void onPause(){
        super.onPause();
    }
    @Override
    public void onStop(){
        writeBtnidDBidPair();
        super.onStop();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
