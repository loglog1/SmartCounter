package com.matsumoto.smartconuter.MainActivity;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.matsumoto.smartconuter.ControllDB;
import com.matsumoto.smartconuter.R;

import java.util.List;


public class MainAcitivtyViewModel {
    private Context MainActivity_Context = null;
    private TopPageWidgetManagement widget;
    private List<Integer> button_id;
    private ControllDB control_db;

    public MainAcitivtyViewModel(final Context MainActivity_Context_, final TopPageWidgetManagement wid_, final String db_pass) {
        MainActivity_Context = MainActivity_Context_;
        widget = wid_;
        button_id = widget.getButtonId();
        control_db = new ControllDB(MainActivity_Context,db_pass);
    }

//  画面を開いた時の初期設定
    public void onCreateViewModel(final View view) {

        //各ボタンの初期コンテンツ
        for (int id : button_id) {
            Button btn = view.findViewById(id);
            SpannableStringBuilder btn_text;
            int db_id = widget.getDBID(id);

            if(db_id==-1) {
                btn_text = TransformStringDesign.transformStrings(
                        MainActivity_Context.getString(R.string.no_data),
                        "0"
                );
            }else {
                String content_name = control_db.getContentName(db_id);
                String count = control_db.getTimStampCount(db_id);
                btn_text = TransformStringDesign.transformStrings(
                        content_name,
                        count
                );
            }
            btn.setText(btn_text);
        }
    }

    //通常記録モード・カウントを記録する
    public void addCount(final Button btn) {
        Integer btn_id = btn.getId();
        Boolean success = control_db.insertTimeStamp(widget.getDBID(btn_id));
        if(!success){
            Toast.makeText(MainActivity_Context,"カウント名を入力してください",Toast.LENGTH_LONG).show();
            return;
        }
        String content_name = control_db.getContentName(widget.getDBID(btn_id));
        String count = control_db.getTimStampCount(widget.getDBID(btn_id));
        SpannableStringBuilder btn_text = TransformStringDesign.transformStrings(content_name,count);
        btn.setText(btn_text);
        Toast.makeText(MainActivity_Context, "記録しました!!", Toast.LENGTH_LONG).show();
    }
}