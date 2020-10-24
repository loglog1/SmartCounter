package com.matsumoto.smartconuter.ListViews;

import android.widget.Button;

import java.util.ArrayList;

public class ContentsAndCount {
    public Button button;
    private String name="";
    private String over_length_name="";
    private ArrayList<String> time;

    public ContentsAndCount(){}

    public ContentsAndCount(String n, int size){
        if(n.length()>3){
            over_length_name = n;
            name = n.substring(0,2)+"...";
        }else{
            over_length_name = n;
            name = n;
        }
        time = new ArrayList<String>(size);
    }

    public long getId() {
        return -1;
    }

    public String getTime(int position){
        return time.get(position);
    }

    public String getName(){
        return name;
    }

    public void setName(String name_){
        if(name_.length()>3){
            over_length_name = name_;
            name = name_.substring(0,2)+"...";
        }else{
            over_length_name = name_;
            name = name_;
        }

    }

    public void addRecord(String time_){
        time.add(time_);
    }

    public int getNumOfRecords(){
        return time.size();
    }
    public void clearRecord() {time.clear();}

    public void eraseRecord(int position){time.remove(position);}

    public void setOver_length_name(String over_){
        over_length_name = over_;
    }

    public String getOver_length_name(){
        return over_length_name;
    }

    public ArrayList<String> getRecords(){
        return time;
    }

}
