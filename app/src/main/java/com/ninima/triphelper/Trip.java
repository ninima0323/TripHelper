package com.ninima.triphelper;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Trip {
    @PrimaryKey
    private long registerTime;

    public Trip(){
        registerTime = System.currentTimeMillis();
    }

    public long getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(long registerTime) {
        this.registerTime = registerTime;
    }
}
