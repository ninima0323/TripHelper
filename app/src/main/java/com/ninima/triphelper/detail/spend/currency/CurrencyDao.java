package com.ninima.triphelper.detail.spend.currency;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CurrencyDao {
    @Insert
    void insert(CurrencyM currencyM);

    @Delete
    void delete(CurrencyM currencyM);

    @Update
    void update(CurrencyM currencyM);

    @Query("SELECT * FROM CurrencyM WHERE tid = :tid ORDER BY cid")
    LiveData<List<CurrencyM>> getAllCurrency(long tid);

    @Query("SELECT * FROM CURRENCYM WHERE cid = :cid")
    LiveData<CurrencyM> getOneCurrency(int cid);

}
