package com.ninima.triphelper.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.ninima.triphelper.global.MyDatabase;
import com.ninima.triphelper.main.TripDao;
import com.ninima.triphelper.model.Spend;
import com.ninima.triphelper.model.Trip;

import java.util.List;
import java.util.Map;

public class SpendViewModel extends ViewModel {
    private MyDatabase database = MyDatabase.instance();
    private TripDao tripDao =  database.tripDao();
    private SpendDao spendDao = database.spendDao();
    long tid;
    LiveData<Map<String, Boolean>> categories;
    LiveData<List<Spend>> spendList;
    LiveData<Spend> spend;

    public SpendViewModel(long tid){
        this.tid = tid;
        spendList = spendDao.getAllSpend(tid);
    }

    void insertNewSpend(final Spend spend){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                spendDao.insert(spend);
            }
        });
    }

    void deleteSpend(final Spend spend){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                spendDao.delete(spend);
            }
        });
    }

    void updateSpend(final Spend spend){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                spendDao.update(spend);
            }
        });
    }

    void getCategories(final long tid){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                categories = tripDao.getCategories(tid);
            }
        });
    }

    void filterSpendList(final List<String> categoryList){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                spendList = spendDao.getSomeSpend(tid, categoryList);
            }
        });
    }

    static class SpendViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        private long tid;
        public SpendViewModelFactory(long tid){
            this.tid = tid;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return ((T)(new SpendViewModel(tid)));
        }
    }
}
