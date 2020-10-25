package com.matsumoto.smartconuter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ControllDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DB_NAME = "content.sqlite3";

    public ControllDB(final Context context_) {
        super(context_, DB_NAME, null, DATABASE_VERSION);

        SQLiteDatabase db = getWritableDatabase();
        String SQL_CREATE_ENTRY;
//       active_negativeについて
//        active状態->1
//        negative状態->0
        try{
            SQL_CREATE_ENTRY = "create table if not exists content_name( _id integer primary key autoincrement, name text,active_negative integer,check(active_negative=0 or active_negative=1));";
            db.execSQL(SQL_CREATE_ENTRY);
            SQL_CREATE_ENTRY = "create table if not exists content_type( _id integer, display_location integer, count_type text, count_orient text, foreign key(_id) references content_name, check( display_location>=0 and ( (count_type='normal' or count_type='limited') and (count_orient='plus' or count_orient='minus') ) ));";
            db.execSQL(SQL_CREATE_ENTRY);
            SQL_CREATE_ENTRY = "create table if not exists limited_status( _id integer, upper_limit integer, lower_limit integer, foreign key(_id) references content_name,check(upper_limit>=0 and lower_limit>=0));";
            db.execSQL(SQL_CREATE_ENTRY);
            SQL_CREATE_ENTRY = "create table if not exists record_count(_id integer, time TIMESTAMP, foreign key(_id) references content_name);";
            db.execSQL(SQL_CREATE_ENTRY);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db_) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public Integer insertNewContent(final String name_){
        SQLiteDatabase db = getWritableDatabase();

        if(name_.isEmpty()) {
            throw new RuntimeException("contents name is empty.");
        }


        String query = "insert into content_name(name,active_negative) values('" + name_.toString() + "',1)";

        Integer id=0;
        try {
            db.execSQL(query);
            query = "select max(_id) from content_name";

            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                id = cursor.getInt(
                        cursor.getColumnIndexOrThrow("max(_id)"));
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return id;
    }

    public Boolean insertTimeStamp(final Integer id_){
        if(id_<0) {
//            書き込み失敗
            return false;
        }

        SQLiteDatabase db = getWritableDatabase();

        String query = "insert into record_count values("+id_.toString()+",DATETIME())";
        try{
            db.execSQL(query);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }

        return true;
    }

    public void changeTitle(Integer id, final String name_){
        if(name_.isEmpty()) {
            throw new RuntimeException("changeTitle:name is empty.");
        }

        SQLiteDatabase db = getWritableDatabase();

        String query = "update content_name set _id="+id.toString()+",name_='"+name_+"', where _id="+id.toString()+")";
        try{
            db.execSQL(query);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
    }

    public String getTimStampCount(final Integer id_){
        if(id_<0) throw new RuntimeException("id_<0");

        SQLiteDatabase db = getWritableDatabase();
        String count = "0";

        try{
            String query = "select count(time) from record_count where _id="+id_.toString();
            Cursor cursor = db.rawQuery(query,null);
            while (cursor.moveToNext()) {
                count = cursor.getString(
                        cursor.getColumnIndexOrThrow("count(time)"));

            }
            cursor.close();

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return count;
    }

    public String getContentName(final Integer id_){
        if(id_<0) throw new RuntimeException("id_<0");

        SQLiteDatabase db = getReadableDatabase();
        String name="";
        try{
            String query = "select name from content_name where _id="+id_.toString();
            Cursor cursor = db.rawQuery(query,null);

            while (cursor.moveToNext()) {
                name = cursor.getString(
                        cursor.getColumnIndexOrThrow("name"));
            }
            cursor.close();

        }catch(Exception e){
            e.printStackTrace();
        }
        return name;
    }

    public ArrayList<String> getTimeStampList(final Integer id_) {
        if(id_<0) throw new RuntimeException("id_<0");

        ArrayList<String> timestamps = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();

        try{
            String query = "select time from record_count where _id="+id_.toString()+" order by (time)";
            Cursor cursor = db.rawQuery(query,null);
            while (cursor.moveToNext()) {
                String time_stamp = cursor.getString(
                        cursor.getColumnIndexOrThrow("time"));
                timestamps.add(time_stamp);
            }
            cursor.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return timestamps;
    }

    public void changeNegativeMode(int id_) {
        if(id_<0){
            throw new RuntimeException("id_<0");
        }

        SQLiteDatabase db = getWritableDatabase();
        String query = "update content_name set active_negative=0 where _id=" + Integer.toString(id_);
        try{
            db.execSQL(query);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
    }

//    public void getAllActiveContents(SQLiteDatabase db) {
//        Cursor cursor = db.query(
//                "content_name,record_count",
//                new String[]{"name,time"},
//                "active_negative",
//                new String[]{"1"},
//                null,
//                null,
//                "time"
//        );
//
//        ArrayList<Integer> ids = new ArrayList<Integer>();
//        while (cursor.moveToNext()) {
//            Integer id = cursor.getInt(
//                    cursor.getColumnIndexOrThrow("id"));
//            ids.add(id);
//        }
//        cursor.close();
////        TODO: return オブジェクト
//    }

    public ArrayList<Pair<String, String>> getAllTimestamp() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "select name, time " +
                "from content_name, record_count " +
                "order by (time)";

        ArrayList<Pair<String,String>> result = new ArrayList<Pair<String,String>>();
        String name, time;
        try{
            Cursor cursor = db.rawQuery(query,null);
            while (cursor.moveToNext()) {
                name = cursor.getString(0);
                time = cursor.getString(1);
                result.add(new Pair<String, String>(name,time));
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return result;
    }

    public void deleteContents(final Integer id_) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "";
        query += "delete from limited_status where _id=" + id_.toString() + ";";
        query += "delete from record_count where _id=" + id_.toString() + ";";
        query += "delete from content_type where _id=" + id_.toString() + ";";
        query += "delete from content_name where _id=" + id_.toString() + ";";
        try{
            db.execSQL(query);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
    }

    public void deleteAllNegativeContents() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                "content_name",
                new String[]{"id"},
                "active_negative",
                new String[]{"0"},
                null,
                null,
                null
        );

        ArrayList<Integer> ids = new ArrayList<Integer>();
        while (cursor.moveToNext()) {
            Integer id = cursor.getInt(
                    cursor.getColumnIndexOrThrow("_id"));
            ids.add(id);
        }
        cursor.close();

        String query = "";
        for (Integer id : ids) {
            query += "delete limited_status where id=" + id.toString() + ";";
            query += "delete record_count where id=" + id.toString() + ";";
            query += "delete content_type where id=" + id.toString() + ";";
            query += "delete content_name where id=" + id.toString() + ";";
        }
        try{
            db.execSQL(query);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
    }

    public void deleteAll(){
        SQLiteDatabase db = getWritableDatabase();

        try{
            String SQL_DELETE_ENTRIES = "drop table if exists content_type;";
            db.execSQL(SQL_DELETE_ENTRIES);
            SQL_DELETE_ENTRIES += "drop table if exists limited_status;";
            db.execSQL(SQL_DELETE_ENTRIES);
            SQL_DELETE_ENTRIES += "drop table if exists record_count;";
            db.execSQL(SQL_DELETE_ENTRIES);
            SQL_DELETE_ENTRIES += "drop table if exists content_name;";
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }catch (Exception e){
            e.printStackTrace();
        }
        db.close();
    }
}
