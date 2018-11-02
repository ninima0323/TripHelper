package com.ninima.triphelper.detail;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ninima.triphelper.model.CategoryM;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insert(CategoryM categoryM);

    @Delete
    void delete(CategoryM categoryM);

    @Update
    void update(CategoryM categoryM);

    @Query("SELECT * FROM CategoryM WHERE tid = :tid ORDER BY category")
    LiveData<List<CategoryM>> getAllCategory(long tid);

    @Query("SELECT * FROM CategoryM WHERE categoryId = :id")
    LiveData<CategoryM > getOneCategory(int id);

    @Query("SELECT category FROM CategoryM WHERE tid = :tid AND isSelected = 'true' ORDER BY category")
    LiveData<List<String>> getSelectedCategories(long tid);

}
