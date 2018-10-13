package com.ninima.triphelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import com.ninima.triphelper.main.MainActivity;

import java.io.ByteArrayOutputStream;

import static android.content.Context.MODE_PRIVATE;

public class Manager {

    private Manager(){}

    //데이터 가져오기
    public static String getPreferences(Context context){
        SharedPreferences pref = context.getSharedPreferences("pref", MODE_PRIVATE);
        String image= pref.getString("image", ""); //key, value(defaults)
        return image;
    }

    //데이터 저장하기
    public static void savePreferences(Context context, Uri uri){
        SharedPreferences pref = context.getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("image", uri.toString());
        editor.commit();
    }

    //데이터 삭제
    public static void removePreferences(Context context){
        SharedPreferences pref = context.getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("image");
        editor.commit();
    }

    //모든 데이터 삭제
    public static void removeAllPreferences(Context context){
        SharedPreferences pref = context.getSharedPreferences("image", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }


}
