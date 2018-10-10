package com.ninima.triphelper.global;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.ninima.triphelper.Trip;
import com.ninima.triphelper.TripDao;

@Database(entities = {Trip.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {
    private static MyDatabase instance;
    private final static String DB_NAME = "Trip_Helper_DB";

    static void initDatabase(Context context){
        instance = Room.databaseBuilder(context, MyDatabase.class, DB_NAME).build();
    }

    public static MyDatabase instance(){
        return instance;
    }

    public abstract TripDao tripDao();

}
