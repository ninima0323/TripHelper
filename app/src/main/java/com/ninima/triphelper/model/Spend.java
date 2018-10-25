package com.ninima.triphelper.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

//@Entity(foreignKeys = @ForeignKey(entity = Trip.class, parentColumns = "registerTime", childColumns = "tripId",
//        onDelete = CASCADE))
@Entity
public class Spend {
    @PrimaryKey(autoGenerate = true)
    private int sid;

    //@ColumnInfo(name = "trip_id")
    private long tripId;

    private Date registerDate = Calendar.getInstance().getTime(), titleDate;  //레지스터가 지출한 시각을 나타내서 분초 다 기록, 타이틀은 스티키헤더위한것으로 자정으로기록
    private String category, title, detail, place, picUri;
    private float price;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public void setTitleDate(Date titleDate) {
        this.titleDate = titleDate;
    }

    public String getPicUri() {
        return picUri;
    }

    public void setPicUri(String picUri) {
        this.picUri = picUri;
    }
}
