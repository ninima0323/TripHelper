package com.ninima.triphelper.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Trip.class, parentColumns = "tid", childColumns = "trip_id",
        onDelete = CASCADE))
public class Spend {
    @PrimaryKey(autoGenerate = true)
    private int sid;

    @ColumnInfo(name = "trip_id")
    private long tripId;

    private Date registerDate, titleDate;

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;

        Calendar c = Calendar.getInstance();
        c.setTime(registerDate);
        int year = c.get(Calendar.YEAR); // current year
        int month = c.get(Calendar.MONTH); // current month
        int day = c.get(Calendar.DAY_OF_MONTH); // current day
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, 00);
        c.set(Calendar.MINUTE, 00);
        c.set(Calendar.SECOND, 00);
        c.set(Calendar.MILLISECOND, 0);
        titleDate = c.getTime();
    }

    public int getSid() {
        return sid;
    }

    public long getTripId() {
        return tripId;
    }

    public Date getTitleDate() {
        return titleDate;
    }
}
