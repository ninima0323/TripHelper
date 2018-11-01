package com.ninima.triphelper.detail.spend.currency;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity
public class CurrencyM {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int cid;

    public String tag;

    public Float price;
    public long tid;

//    @ColumnInfo(name = "post_code")
//    public int postCode;

    public CurrencyM(){
        tag = "₩";
        price = null;
        tid = -1;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    @NonNull
    public int getCid() {
        return cid;
    }

    public void setCid(@NonNull int cid) {
        this.cid = cid;
    }
}
