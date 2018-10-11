package com.ninima.triphelper.global;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.ninima.triphelper.model.Trip;
import com.ninima.triphelper.main.TripDao;

@Database(entities = {Trip.class}, version = 1, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    private static MyDatabase instance;
    private final static String DB_NAME = "Trip_Helper_DB";

    static void initDatabase(Context context){
        instance = Room.databaseBuilder(context, MyDatabase.class, DB_NAME).build();
    }
    public static MyDatabase instance(){
        return instance;
    }//DB를 가져올 땐 MyDataBase.instance()만 호출하면 됨

    public abstract TripDao tripDao();//Dao 가 필요하면 instance().~~Dao();
    //other Dao
}
