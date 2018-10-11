package com.ninima.triphelper.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.ninima.triphelper.global.MyDatabase;
import com.ninima.triphelper.model.Trip;

//뷰모델은 무조건 public 접근제한자 필요
public class MainViewModel extends ViewModel {
    private MyDatabase database = MyDatabase.instance();
    private TripDao tripDao =  database.tripDao();
    LiveData<Trip> trip;//이렇게 해 두면 디비가 바뀔 때마다 trip 에 있는 값이 바뀜
    //LiveData 에 있는 값을 확인하려면 trip.getValue()로 가져옴
    //LiveData 는 백그라운드 작업에서 할 필요 없이 알아서 백그라운드 쓰레드로 돌려줘서 쓰레드 처리 할 필요는 없음

    //생성자는 ViewModelProviders.of(this).get(MainViewModel.class);에서 알아서 호출해 줌
    public MainViewModel(){
        trip = tripDao.getOne();
    }

    void insertNewTrip(final Trip trip){
        //runInTransaction 에서 Runnable 에 쿼리 작업을 해주면 백그라운드로 돌아감
        database.runInTransaction(new Runnable() {
            @Override
            public void run() {
                tripDao.insert(trip);
            }
        });
    }
}
