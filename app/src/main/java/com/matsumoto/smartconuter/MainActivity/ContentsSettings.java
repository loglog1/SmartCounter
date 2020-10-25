package com.matsumoto.smartconuter.MainActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.matsumoto.smartconuter.ControllDB;
import com.matsumoto.smartconuter.MainActivity.SingleList.SingleListView;
import com.matsumoto.smartconuter.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ContentsSettings {
    private Context MainActivity_Context;
    private int[] buttons_id;
    private MainActivity.Widget widget;
    private ControllDB control_db;

    public ContentsSettings(final Context MainActivity_Context_, int[] buttons_id_,final MainActivity.Widget wid_){
        MainActivity_Context = MainActivity_Context_;
        buttons_id = buttons_id_;
        widget = wid_;
        control_db = new ControllDB(MainActivity_Context);
    }

    public void drawSettingWindow(final Button selected_btn) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_Context);

        LinearLayout selection = new LinearLayout(MainActivity_Context);
        selection.setOrientation(LinearLayout.VERTICAL);

        Button log_list = new Button(MainActivity_Context);
        log_list.setText("過去の記録の表示");
        log_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecordedList(selected_btn);
            }
        });

        Button title_change = new Button(MainActivity_Context);
        title_change.setText("タイトルの変更");
        title_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeContentTitle(v, selected_btn);
            }
        });


        final Button negative_change = new Button(MainActivity_Context);
        negative_change.setText("この記録をネガティブにする");
        negative_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNegativeMode(v, selected_btn);
            }
        });

        Button record_delete = new Button(MainActivity_Context);
        record_delete.setText("この記録を完全抹消する");
        record_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearRecords(v, selected_btn);
            }
        });

        selection.addView(log_list);
        selection.addView(title_change);
        selection.addView(negative_change);
        selection.addView(record_delete);


        builder.setTitle("設定")
                .setView(selection)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        builder.show();
    }

    //タイトルの変更,新規追加（「未設定」からの変更）
    private void changeContentTitle(final View view, final Button selected_btn) {
        final LinearLayout edit_form = new LinearLayout(MainActivity_Context);
        edit_form.setOrientation(LinearLayout.VERTICAL);

        final EditText title = new EditText(MainActivity_Context);
        edit_form.addView(title);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_Context);

        builder.setTitle("記録タイトルの変更")
                .setMessage("新しい記録内容を入力してください")
                .setView(edit_form)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String new_name = title.getText().toString();
                        Integer db_id = widget.id_pare.get(selected_btn.getId());

                        if(db_id==-1){
                            //未設定からの変更
                            SpannableStringBuilder sb = TransformStringDesign.transformStrings(new_name,"0");
                            selected_btn.setText(sb);
                            db_id = control_db.insertNewContent(new_name);
                            widget.id_pare.put(selected_btn.getId(),db_id);
                        }else{
                            String count = control_db.getTimStampCount(db_id);
                            SpannableStringBuilder sb = TransformStringDesign.transformStrings(new_name,count);
                            selected_btn.setText(sb);
                            control_db.changeTitle(db_id, new_name);
                        }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // クリックしたときの処理
                    }
                });

        builder.show();
    }

    //データのネガティブモードへの変更
    private void changeNegativeMode(View view, final Button selected_btn_) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_Context);
        builder.setTitle("警告")
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setMessage("この記録内容をネガティブにするともうカウントできなくなります。本当にしますか？")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int db_id = widget.id_pare.get(selected_btn_.getId());
                        control_db.changeNegativeMode(db_id);
                        SpannableStringBuilder btn_text = TransformStringDesign.transformStrings(
                                MainActivity_Context.getString(R.string.no_data),
                                "0"
                        );
                        selected_btn_.setText(btn_text);
//                    ボタンのdb_idも未設定状態にする
                        widget.id_pare.put(selected_btn_.getId(),-1);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // クリックしたときの処理
                    }
                });
        builder.show();
    }

    //各コンテンツのタイムスタンプ一覧表示モード
    private void showRecordedList(Button btn_) {
        Integer btn_id = btn_.getId();
        Integer db_id = widget.id_pare.get(btn_id);
//        初期設定の場合
        if(db_id==-1){
            Toast.makeText(MainActivity_Context,"表示するデータがありません",Toast.LENGTH_LONG).show();
            return;
        }


        String name = control_db.getContentName(db_id);
        ArrayList<String> record_list = control_db.getTimeStampList(db_id);

//        データが存在しない場合
        if(record_list.isEmpty()){
            Toast.makeText(MainActivity_Context,"表示するデータがありません",Toast.LENGTH_LONG).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_Context);

        ListView listview = new ListView(MainActivity_Context);
        SingleListView singleListView = new SingleListView(MainActivity_Context, listview);
        listview = singleListView.getListview(record_list);

        builder.setMessage( name + "の記録一覧")
                .setView(listview)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setCancelable(true);
        builder.show();
    }

    //全コンテンツ削除
    private void clearRecords ( final View view, final  Button selected_btn_){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_Context);
        builder.setTitle("警告")
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setMessage("全ての記録・設定が消去されます。消去しますか？")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    int db_id = widget.id_pare.get(selected_btn_.getId());
                    control_db.deleteContents(db_id);
                    SpannableStringBuilder sb = TransformStringDesign.transformStrings(
                                MainActivity_Context.getString(R.string.no_data),"0");
                    selected_btn_.setText(sb);
//                    ボタンのdb_idも未設定状態にする
                    widget.id_pare.put(selected_btn_.getId(),-1);
                    }
                })
                .setCancelable(true);

        builder.show();
    }

}
