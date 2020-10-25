package com.matsumoto.smartconuter;

import android.content.Context;
import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import android.util.Pair;

import java.util.ArrayList;


public class ControllDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DB_NAME = "database.sqlite3";
    private String pass = "2DD29CA851E7B56E4697B0E1F08507293D761A05CE4D1B628663F411A8086D99";

    public ControllDB(final Context context_, final String pass_) {
        super(context_, DB_NAME, null, DATABASE_VERSION);
        SQLiteDatabase.loadLibs(context_);
//        this.pass = pass_;

        SQLiteDatabase db = getWritableDatabase(this.pass);

        String SQL_CREATE_ENTRY;
//       active_negativeについて
//        active状態->1
//        negative状態->0
        try{
            StringBuffer tmp_query = new StringBuffer();

            tmp_query.append("create table if not exists content_name");
            tmp_query.append("(");
            tmp_query.append("_id integer primary key autoincrement,");
            tmp_query.append("name text,");
            tmp_query.append("active_negative integer,");
            tmp_query.append("check(active_negative=0 or active_negative=1)");
            tmp_query.append(")");
            SQL_CREATE_ENTRY = tmp_query.toString();
            db.execSQL(SQL_CREATE_ENTRY);

            tmp_query.delete(0,tmp_query.length());
            tmp_query.append("create table if not exists content_type");
            tmp_query.append("(");
            tmp_query.append("_id integer,");
            tmp_query.append("count_type text,");
            tmp_query.append("count_orient text,");
            tmp_query.append("foreign key(_id) references content_name,");
            tmp_query.append("check(");
            tmp_query.append("(count_type='normal' or count_type='limited')");
            tmp_query.append(" and ");
            tmp_query.append("(count_orient='plus' or count_orient='minus')");
            tmp_query.append(")");
            tmp_query.append(");");
            SQL_CREATE_ENTRY = tmp_query.toString();
            db.execSQL(SQL_CREATE_ENTRY);

            tmp_query.delete(0,tmp_query.length());
            tmp_query.append("create table if not exists limited_status(");
            tmp_query.append("_id integer,");
            tmp_query.append("upper_limit integer,");
            tmp_query.append("lower_limit integer,");
            tmp_query.append("foreign key(_id) references content_name,");
            tmp_query.append("check(upper_limit>=0 and lower_limit>=0)");
            tmp_query.append(");");
            SQL_CREATE_ENTRY = tmp_query.toString();
            db.execSQL(SQL_CREATE_ENTRY);

            tmp_query.delete(0,tmp_query.length());
            tmp_query.append("create table if not exists record_count(");
            tmp_query.append("_id integer,");
            tmp_query.append("orientation integer,");
            tmp_query.append("is_canceled integer,");
            tmp_query.append("time TIMESTAMP,");
            tmp_query.append("foreign key(_id) references content_name,");
            tmp_query.append("check(");
            tmp_query.append("(orientation=1 or orientation=-1)");
            tmp_query.append(" and ");
            tmp_query.append("(is_canceled=1 or is_canceled=0)");
            tmp_query.append("));");
            SQL_CREATE_ENTRY = tmp_query.toString();
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
        SQLiteDatabase db = getWritableDatabase(this.pass);


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

        SQLiteDatabase db = getWritableDatabase(this.pass);


        String query = "insert into record_count values("+id_.toString()+",1,0,DATETIME('now','+9 hours'))";
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

        SQLiteDatabase db = getWritableDatabase(this.pass);


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

        SQLiteDatabase db = getWritableDatabase(this.pass);

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

        SQLiteDatabase db = getReadableDatabase(this.pass);

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
        SQLiteDatabase db = getWritableDatabase(this.pass);


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

        SQLiteDatabase db = getWritableDatabase(this.pass);
        String query = "update content_name set active_negative=0 where _id=" + Integer.toString(id_);
        try{
            db.execSQL(query);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
    }

    public ArrayList<Pair<String,String>> getAllActiveContents() {
        SQLiteDatabase db = getReadableDatabase(this.pass);

        final String query = "select name,is_canceled,orientation,time from content_name,record_count where active_negative = 1 order by (time)";
        Cursor cursor = db.rawQuery(query,null);
        ArrayList<Pair<String,String>> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow("name"));
            String time_stamp = cursor.getString(
                    cursor.getColumnIndexOrThrow("time"));
            Pair<String,String> content = new Pair<String, String>(name,time_stamp);
            result.add(content);
        }
        cursor.close();
        return result;
    }

    public ArrayList<Pair<String, String>> getAllTimestamp() {
        SQLiteDatabase db = getReadableDatabase(this.pass);

        String query = "select name, time from content_name, record_count where content_name._id=record_count._id order by (time)";

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


    public void deleteAll(){
        SQLiteDatabase db = getWritableDatabase(this.pass);

        try{
            String SQL_DELETE_ENTRIES = "drop table if exists content_type";
            db.execSQL(SQL_DELETE_ENTRIES);
            SQL_DELETE_ENTRIES = "drop table if exists limited_status";
            db.execSQL(SQL_DELETE_ENTRIES);
            SQL_DELETE_ENTRIES = "drop table if exists record_count";
            db.execSQL(SQL_DELETE_ENTRIES);
            SQL_DELETE_ENTRIES = "drop table if exists content_name";
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }catch (Exception e){
            e.printStackTrace();
        }
        db.close();
    }
}
