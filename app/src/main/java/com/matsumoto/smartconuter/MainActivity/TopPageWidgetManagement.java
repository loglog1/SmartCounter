package com.matsumoto.smartconuter.MainActivity;

import android.util.SparseArray;

import com.matsumoto.smartconuter.R;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;


public class TopPageWidgetManagement implements Serializable {
    private SparseArray<Integer> id_pare;
    private int window_width;
    private int window_height;

    private final List<Integer> button_id= Arrays.asList(
            R.id.upper_left,
            R.id.upper_center,
            R.id.upper_right,
            R.id.center_left,
            R.id.center_center,
            R.id.center_right,
            R.id.lower_left,
            R.id.lower_center,
            R.id.lower_right
    );


    public void setDBID(final Integer button_id_, final Integer db_id_){
        id_pare.put(button_id_,db_id_);
    }

    public void setDBIDs(final SparseArray<Integer> id_pair){
        this.id_pare = id_pair;
    }

    public Integer getDBID(final Integer button_id_){
        if(this.id_pare.indexOfKey(button_id_)<0){
            throw new ArrayIndexOutOfBoundsException("TopPageWidgetManagement#getDBID:button_id_ is out of range.");
        }
        return id_pare.get(button_id_);
    }

    public void clearAllDBID(){
        for(Integer id:this.button_id){
            id_pare.put(id,-1);
        }
    }

    public List<Integer> getButtonId(){
        return this.button_id;
    }

    public void setWindowSize(final int width_, final int height_){
        this.window_width = width_;
        this.window_height = height_;
    }

    public int getWindowWidth(){
        return this.window_width;
    }

    public int getWindowHeight(){
        return this.window_height;
    }
}
