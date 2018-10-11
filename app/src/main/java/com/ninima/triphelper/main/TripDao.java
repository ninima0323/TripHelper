package com.ninima.triphelper.main;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.ninima.triphelper.model.Trip;

@Dao
public interface TripDao {
    @Insert
    void insert(Trip trip);

    @Query("SELECT * FROM Trip ORDER BY registerTime DESC LIMIT 1")
    LiveData<Trip> getOne();
}
