package com.ninima.triphelper.detail.memo;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ninima.triphelper.model.Memo;

import java.util.List;

@Dao
public interface MemoDao {
    @Insert
    void insert(Memo memo);

    @Delete
    void delete(Memo memo);

    @Update
    void update(Memo memo);

    @Query("SELECT * FROM Memo WHERE tripId = :tid ORDER BY registerTime DESC")
    LiveData<List<Memo>> getAllMemo(long tid);

    @Query("SELECT * FROM Memo WHERE registerTime = :mid")
    LiveData<Memo> getOneMemo(long mid);
}
