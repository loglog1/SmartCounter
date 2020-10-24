package com.matsumoto.smartconuter.MainActivity;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;


import com.matsumoto.smartconuter.ControllDB;
import com.matsumoto.smartconuter.R;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class MainAcitivtyViewModel {
    private Context MainActivity_Context = null;
    private ContentsSettings contents_settings;
    private MainActivity.Widget widget;
    private int[] button_id;
    private ControllDB control_db;

    public MainAcitivtyViewModel(final Context MainActivity_Context_, int[] button_id_, final MainActivity.Widget wid_) {
        MainActivity_Context = MainActivity_Context_;
        button_id = button_id_;
        widget = wid_;
        contents_settings = new ContentsSettings(MainActivity_Context, button_id, widget);
        control_db = new ControllDB(MainActivity_Context);
    }

//  画面を開いた時の初期設定
    public void onCreateViewModel(final View view) {

        //各ボタンの初期コンテンツ
        for (int id : button_id) {
            Button btn = view.findViewById(id);
            SpannableStringBuilder btn_text;
            int db_id = widget.id_pare.get(id);

            if(db_id==-1) {
                btn_text = transformStrings(
                        MainActivity_Context.getString(R.string.no_data),
                        "0"
                );
            }else {
                String content_name = control_db.getContentName(db_id);
                Log.d(TAG, "onCreateViewModel: "+content_name);
                String count = control_db.getTimStampCount(db_id);
                btn_text = transformStrings(
                        content_name,
                        count
                );
            }
            btn.setText(btn_text);
        }
    }

    //通常記録モードとセッティングモードの切り替え
    public void onClickContents(View view) {
        final Button button = view.findViewById(view.getId());
        if (isSettingMode()) {
            contents_settings.drawSettingWindow(button);
        } else {
            this.addCount(button);
        }
    }

    //通常記録モード・カウントを記録する
    public void addCount(final Button btn) {
        Integer btn_id = btn.getId();
        try{
            Boolean success = control_db.insertTimeStamp(widget.id_pare.get(btn_id));
            if(!success){
                Toast.makeText(MainActivity_Context,"カウント名を入力してください",Toast.LENGTH_LONG).show();
                return;
            }
//        TODO: DBへタイムスタンプ書き込み
//            TODO: Contextの指定->ALREADY
        }catch(Exception e){
            e.printStackTrace();
        }
        String content_name = control_db.getContentName(widget.id_pare.get(btn_id));
        String count = control_db.getTimStampCount(widget.id_pare.get(btn_id));
        Log.d(TAG, "addCount: "+btn.getText());
        SpannableStringBuilder btn_text = transformStrings(content_name,count);
        btn.setText(btn_text);
        Toast.makeText(MainActivity_Context, "記録しました!!", Toast.LENGTH_LONG).show();
    }

    private Boolean isSettingMode() {
        return widget.swt.isChecked();
    }

//    セッティングモードへ移行した直後の処理
    public void onClickSettingButton(View view) {
        Switch swt = view.findViewById(R.id.setting_mode_switch);
        if(isSettingMode()){
            swt.setText("設定モード");
            changeButtonColor(true);
        }else{
            swt.setText("記録モード");
            changeButtonColor(false);
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

    public void onStop () {
            Log.d("destroy", "onDestroy: call");
//            TODO: DBを閉じる
    }

    private SpannableStringBuilder transformStrings (String theme, String count){
        String alt_theme="";
        if(theme.length()>5){
            alt_theme = theme.substring(0,5)+"...";
            Log.d(TAG, "transformStrings: "+alt_theme);
        }else{
            alt_theme = theme;
        }
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(alt_theme);
        sb.setSpan(new RelativeSizeSpan(2.5f), 0, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.append("\n");
        sb.append("\n");
        int start = sb.length();
        sb.append(count + "回");
        sb.setSpan(new RelativeSizeSpan(2.0f), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

}