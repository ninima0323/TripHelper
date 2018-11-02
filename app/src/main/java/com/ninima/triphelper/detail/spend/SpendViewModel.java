package com.ninima.triphelper.detail.spend;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.ninima.triphelper.detail.CategoryDao;
import com.ninima.triphelper.detail.SpendDao;
import com.ninima.triphelper.detail.spend.currency.CurrencyDao;
import com.ninima.triphelper.detail.spend.currency.CurrencyM;
import com.ninima.triphelper.global.MyDatabase;
import com.ninima.triphelper.model.CategoryM;
import com.ninima.triphelper.model.Spend;

import java.util.ArrayList;
import java.util.List;

public class SpendViewModel extends ViewModel {
    private MyDatabase database = MyDatabase.instance();
    private CurrencyDao currencyDao =  database.currencyDao();
    private SpendDao spendDao = database.spendDao();
    private CategoryDao categoryDao = database.catagoryDao();
    long tid;
    int sid;
    LiveData<List<CategoryM>> categoris;
    LiveData<List<CurrencyM>> currencies;
    LiveData<List<Spend>> spendList;
    LiveData<Spend> spend;
    LiveData<List<String>> selectedCategory;

    public SpendViewModel(long tid){
        this.tid = tid;
        spendList = spendDao.getAllSpend(tid);
        this.currencies = currencyDao.getAllCurrency(tid);
        this.categoris = categoryDao.getAllCategory(tid);
        selectedCategory = categoryDao.getSelectedCategories(tid);
    }

    public SpendViewModel(int sid, long tid){
        this.sid = sid;
        this.tid = tid;
        this.currencies = currencyDao.getAllCurrency(tid);
        this.categoris = categoryDao.getAllCategory(tid);
        this.spend = spendDao.getOneSpend(sid);
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

    void insertNewCategory(final CategoryM categoryM){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                categoryDao.insert(categoryM);
            }
        });
    }

    void updateCategory(final CategoryM categoryM){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                categoryDao.update(categoryM);
            }
        });
    }

    void getSelectedCategories(final long tid){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                selectedCategory = categoryDao.getSelectedCategories(tid);
            }
        });
    }

    void setSelectedSpends(final long tid, final List<String> slist){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                spendList = spendDao.getSelectedSpend(tid, slist);
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

    static class SpendViewModelFactory2 extends ViewModelProvider.NewInstanceFactory {
        private int sid;
        private long tid;
        public SpendViewModelFactory2(int sid, long tid){
            this.sid = sid;
            this.tid = tid;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return ((T)(new SpendViewModel(sid, tid)));
        }
    }
}
