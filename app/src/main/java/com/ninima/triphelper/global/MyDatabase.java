package com.ninima.triphelper.global;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.ninima.triphelper.detail.CategoryDao;
import com.ninima.triphelper.detail.SpendDao;
import com.ninima.triphelper.detail.memo.MemoDao;
import com.ninima.triphelper.detail.spend.currency.CurrencyDao;
import com.ninima.triphelper.detail.spend.currency.CurrencyM;
import com.ninima.triphelper.model.CategoryM;
import com.ninima.triphelper.model.Memo;
import com.ninima.triphelper.model.Spend;
import com.ninima.triphelper.model.Trip;
import com.ninima.triphelper.main.TripDao;

@Database(entities = {Trip.class, Spend.class, Memo.class, CurrencyM.class, CategoryM.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
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
    public abstract SpendDao spendDao();
    public abstract MemoDao memoDao();
    public abstract CurrencyDao currencyDao();
    public abstract CategoryDao catagoryDao();
}
