package com.ninima.triphelper.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ninima.triphelper.R;
import com.ninima.triphelper.model.Trip;

public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = findViewById(R.id.test);

        //액티비티와 생명주기를 함께하는 뷰모델 객체, 뷰모델은 액티비티가 끝나면 같이 끝나서 이미 finish 된 뷰에 ui 작업하는걸 걱정하지 않아도 됨
        viewModel = ViewModelProviders.of(this)
                .get(MainViewModel.class);

        //뷰모델에 만들어 놓은 LiveData 를 observe(구독, 관찰?)함
        //디비에 정보가 바뀔 때 마다 onChanged 호출
        viewModel.trip.observe(this, new Observer<Trip>() {
            @Override
            public void onChanged(@Nullable Trip trip) {
                //내부는 UI 쓰레드라서 ui 건드려도 상관없음
                String result = "no result";
                if(trip != null) {
                    result = trip.getRegisterTime()+"";
                }
                textView.setText(result);
            }
        });

        (findViewById(R.id.test)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //백그라운드 쓰레드 호출은 뷰모델에 맡기고 밖에서는 호출만(코드 여기저기서 쓰레드만들면 보기 더러울거같아서)
                viewModel.insertNewTrip(new Trip());
            }
        });


    }
}