package com.ninima.triphelper.global;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ninima.triphelper.detail.spend.currency.CurrencyM;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

//    @TypeConverter
//    public static Map<String, Float> fromCurrencyString(String value) {
//        Type listType = new TypeToken<Map<String, Float>>() {}.getType();
//        return new Gson().fromJson(value, listType);
//    }
//
//    @TypeConverter
//    public static String fromCurrencyMap(Map<String, Float> list) {
//        Gson gson = new Gson();
//        String json = gson.toJson(list);
//        return json;
//    }

    @TypeConverter
    public static ArrayList<CurrencyM> fromString(String value) {
        Type listType = new TypeToken<ArrayList<CurrencyM>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromCurrencyList(ArrayList<CurrencyM> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
