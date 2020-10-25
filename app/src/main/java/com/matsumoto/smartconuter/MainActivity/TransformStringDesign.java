package com.matsumoto.smartconuter.MainActivity;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class TransformStringDesign {
    public TransformStringDesign(){

    }

    public static SpannableStringBuilder transformStrings (String theme, String count){
        String alt_theme="";
        if(theme.length()>5){
            alt_theme = theme.substring(0,5)+"...";
            Log.d(TAG, "transformStrings: "+alt_theme);
        }else{
            alt_theme = theme;
        }
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(alt_theme);
        sb.setSpan(new RelativeSizeSpan(1.5f), 0, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.append("\n");
        sb.append("\n");
        int start = sb.length();
        sb.append(count + "回");
        sb.setSpan(new RelativeSizeSpan(1.0f), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}
