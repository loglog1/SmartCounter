package com.matsumoto.smartconuter;

import java.util.ArrayList;

public class GeneralRecords {
    private String contents = "";
    private String time = "";

    public GeneralRecords(String c, String t){
        contents = c;
        time = t;
    }

    public long getId() {
        return -1;
    }

    public String getName(){
        return contents;
    }

    public String getTime(){
        return time;
    }


}
