package com.ninima.triphelper.global;

import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initRoomDatabase();
    }

    private void initRoomDatabase(){
        MyDatabase.initDatabase(getApplicationContext());
    }
}
