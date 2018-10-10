package com.ninima.triphelper;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface TripDao {
    @Insert
    public void insert(Trip trip);

    @Query("SELECT * FROM Trip ORDER BY registerTime DESC LIMIT 1")
    public LiveData<Trip> getOne();
}
