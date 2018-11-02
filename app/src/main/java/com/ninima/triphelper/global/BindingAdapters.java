package com.ninima.triphelper.global;

import android.databinding.BindingAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BindingAdapters {
    @BindingAdapter("datetostring")
    public static void setDate(TextView tv, Date date) {
        if(date ==null) tv.setText(" ");
        else{
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
            String d=transFormat.format(date);
            tv.setText(d);
        }
    }
    @BindingAdapter("timetostring")
    public static void setTime(TextView tv, Date date) {
        if(date ==null) tv.setText(" ");
        else{
            SimpleDateFormat transFormat = new SimpleDateFormat("HH:mm");
            String d=transFormat.format(date);
            tv.setText(d);
        }
    }
    @BindingAdapter("floattostring")
    public static void setFloat(TextView tv, Float f) {
        if(f.equals(0.0f)) tv.setHint("0.0");
        else{
            String d=Float.toString(f);
            tv.setText(d);
        }
    }
    @BindingAdapter("floattostringET")
    public static void setFloat(EditText et, Float f) {
        if(f.equals(0.0f)) et.setHint("0.0");
        else{
            String d=Float.toString(f);
            et.setText(d);
        }
    }
}
