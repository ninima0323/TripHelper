package com.ninima.triphelper.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.ninima.triphelper.global.MyDatabase;
import com.ninima.triphelper.model.Trip;

import java.util.List;

//뷰모델은 무조건 public 접근제한자 필요
public class MainViewModel extends ViewModel {
    private MyDatabase database = MyDatabase.instance();
    private TripDao tripDao =  database.tripDao();
    LiveData<Trip> trip;//이렇게 해 두면 디비가 바뀔 때마다 trip 에 있는 값이 바뀜
    //LiveData 에 있는 값을 확인하려면 trip.getValue()로 가져옴
    //LiveData 는 백그라운드 작업에서 할 필요 없이 알아서 백그라운드 쓰레드로 돌려줘서 쓰레드 처리 할 필요는 없음

    LiveData<List<Trip>> tripList;

    //생성자는 ViewModelProviders.of(this).get(MainViewModel.class);에서 알아서 호출해 줌
    public MainViewModel(){
        trip = tripDao.getRecentOne();
        tripList = tripDao.getAllTrip();
    }

    void insertNewTrip(final Trip trip){
        //AsyncTask.execute() 에서 Runnable 에 DB 작업을 해주면 백그라운드로 돌아감
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                tripDao.insert(trip);
            }
        });
    }

    void deleteTrip(final Trip trip){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                tripDao.delete(trip);
            }
        });
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
}
