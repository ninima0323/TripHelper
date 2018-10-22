package com.ninima.triphelper.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ninima.triphelper.global.MyDatabase;
import com.ninima.triphelper.main.TripDao;
import com.ninima.triphelper.model.Trip;

import java.lang.reflect.InvocationTargetException;

public class DetailViewModel extends ViewModel{
    private MyDatabase database = MyDatabase.instance();
    private TripDao tripDao =  database.tripDao();
    LiveData<Trip> trip;

    public DetailViewModel(long tid){
        trip = tripDao.getOne(tid);
        Log.e("trip", ""+(trip == null)+tid);
    }

    void setThisTrip(long tid){
        this.trip = tripDao.getOne(tid);
    }

    void updateTrip(final Trip trip){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                tripDao.update(trip);
            }
        });
    }

    void whenDoMultiTransaction(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                database.runInTransaction(new Runnable() {
                    @Override
                    public void run() {
                        //DB작업을 하나 이상 해야할 때는 이런 runInTransaction 을 이용
                        //이 블럭 안에서 여러 DB 작업 수행
                    }
                });
            }
        });
    }

    static class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        private long tid;
       public DetailViewModelFactory(long tid){
           this.tid = tid;
       }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return ((T)(new DetailViewModel(tid)));
        }
    }
}
