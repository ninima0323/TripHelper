package com.ninima.triphelper.detail.spend.currency;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.ninima.triphelper.global.MyDatabase;
import java.util.List;

public class CurrencyViewModel extends ViewModel {
    private MyDatabase database = MyDatabase.instance();
    private CurrencyDao currencyDao =  database.currencyDao();
    long tid;
    LiveData<List<CurrencyM>> currencyList;

    public CurrencyViewModel(long tid){
        this.tid = tid;
        currencyList = currencyDao.getAllCurrency(tid);
    }

    void insertNewCurrency(final CurrencyM currencyM){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                currencyDao.insert(currencyM);
            }
        });
    }

    void deleteCurrency(final CurrencyM currencyM){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                currencyDao.delete(currencyM);
            }
        });
    }

    void updateCurrency(final CurrencyM currencyM){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                currencyDao.update(currencyM);
            }
        });
    }

//    void getCategories(final long tid){
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                categories = tripDao.getCategories(tid);
//                //옵저버에서 컨버터 사용해 맵으로 고치자
//            }
//        });
//    }


    static class CurrencyViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        private long tid;
        public CurrencyViewModelFactory(long tid){
            this.tid = tid;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return ((T)(new CurrencyViewModel(tid)));
        }
    }
}
