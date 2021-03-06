package com.ninima.triphelper.main;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ninima.triphelper.detail.spend.currency.CurrencyM;
import com.ninima.triphelper.model.Trip;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Dao
public interface TripDao {
    @Insert
    void insert(Trip trip);

    @Delete
    void delete(Trip trip);

    @Update
    void update(Trip trip);

    @Query("SELECT * FROM Trip ORDER BY registerTime DESC LIMIT 1")
    LiveData<Trip> getRecentOne();

    @Query("SELECT * FROM Trip ORDER BY registerTime DESC")
    LiveData<List<Trip>> getAllTrip();

    @Query("SELECT * FROM Trip WHERE registerTime = :tid")
    LiveData<Trip> getOneTrip(long tid);

//    @Query("SELECT picUri FROM Spend WHERE tripId = :tid AND picUri != null ORDER BY registerDate DESC LIMIT 10")
//    LiveData<List<String>> getRecentPics(long tid);
//
//    @Query("SELECT categories FROM Trip WHERE registerTime = :tid")
//    LiveData<String> getCategories(long tid);
//
//    @Query("SELECT cList FROM Trip WHERE registerTime = :tid")
//    LiveData<ArrayList<CurrencyM>> getCurrencies(long tid);



}
