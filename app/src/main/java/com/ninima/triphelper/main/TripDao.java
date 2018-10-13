package com.ninima.triphelper.main;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ninima.triphelper.model.Trip;

import java.util.List;

@Dao
public interface TripDao {
    @Insert
    void insert(Trip trip);

    @Delete
    void delete(Trip trip);

    @Update
    void update(Trip trip);

    @Query("SELECT * FROM Trip ORDER BY registerTime DESC LIMIT 1")
    LiveData<Trip> getOne();

    @Query("SELECT * FROM Trip ORDER BY registerTime DESC")
    LiveData<List<Trip>> getAll();


}
