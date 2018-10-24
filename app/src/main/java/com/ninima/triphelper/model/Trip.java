package com.ninima.triphelper.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;
import java.util.Map;

@Entity(indices = {@Index("registerTime")})
public class Trip {
    @PrimaryKey
    private long registerTime;

    private Date startDate, endDate;

    private String title, place, comment,picUri;

    public Map<String, Boolean> categories;

//    @Ignore
//    private float totalPrice;

    public Trip(){
        registerTime = System.currentTimeMillis();
    }

    public long getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(long registerTime) {
        this.registerTime = registerTime;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPicUri() {
        return picUri;
    }

    public void setPicUri(String picUri) {
        this.picUri = picUri;
    }

    public Map<String, Boolean> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, Boolean> categories) {
        this.categories = categories;
    }
}
