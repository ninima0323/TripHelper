package com.ninima.triphelper.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class CategoryM {
    @PrimaryKey
    @NonNull
    private String category;

    private boolean isSelected;
    private long tid;

    public CategoryM(){
        this.category = "기타";
        this.isSelected = true;
        this.tid=-1;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }
}
