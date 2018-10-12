package com.ninima.triphelper.global;

import android.databinding.BindingAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BindingAdapters {
    @BindingAdapter("datetostring")
    public static void setDate(TextView tv, Date date) {
        if(date ==null) tv.setText("날짜입력");
        else{
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
            String d=transFormat.format(date);
            tv.setText(d);
        }
    }
}
