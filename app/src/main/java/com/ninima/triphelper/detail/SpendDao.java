package com.ninima.triphelper.detail;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ninima.triphelper.model.Spend;

import java.util.List;

@Dao
public interface SpendDao {
    @Insert
    void insert(Spend spend);

    @Delete
    void delete(Spend spend);

    @Update
    void update(Spend spend);

    @Query("SELECT * FROM Spend WHERE tripId = :tid ORDER BY registerDate DESC")
    LiveData<List<Spend>> getAllSpend(long tid);

    @Query("SELECT * FROM Spend WHERE tripId = :tid and category in (:categoryList) ORDER BY registerDate DESC")
    LiveData<List<Spend>> getSomeSpend(long tid, List<String> categoryList);

    @Query("SELECT * FROM Spend WHERE sid = :sid")
    LiveData<Spend> getOneSpend(int sid);

    @Query("SELECT picUri FROM Spend WHERE tripId = :tid AND picUri != null ORDER BY registerDate DESC LIMIT 10")
    LiveData<List<String>> getPicList(long tid);
}
