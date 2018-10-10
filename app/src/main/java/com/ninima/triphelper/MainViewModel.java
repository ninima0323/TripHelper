package com.ninima.triphelper;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.ninima.triphelper.global.MyDatabase;

public class MainViewModel extends ViewModel {
    public MutableLiveData<Trip> trip = new MutableLiveData<>();
    private MyDatabase database = MyDatabase.instance();
    private TripDao tripDao = database.tripDao();

    public MainViewModel(){
        trip.postValue(tripDao.getOne().getValue());
    }

    public void insertNewTrip(Trip trip){
        tripDao.insert(trip);
    }

    public void createNewTrip(Trip trip){
        this.trip.postValue(trip);
    }
}
