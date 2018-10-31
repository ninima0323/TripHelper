package com.ninima.triphelper.detail.spend.currency;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class CurrencyM {
    @PrimaryKey(autoGenerate = true)
    public int cid;

    public String tag;
    public Float price;
    public long tid;

//    @ColumnInfo(name = "post_code")
//    public int postCode;

    public CurrencyM(){
        tag = null;
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

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }
}
