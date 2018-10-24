package com.ninima.triphelper.global;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
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

    @TypeConverter
    public static Map<String, Boolean> fromString(String value) {
        Type listType = new TypeToken<Map<String, Boolean>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromHashMap(Map<String, Boolean> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
