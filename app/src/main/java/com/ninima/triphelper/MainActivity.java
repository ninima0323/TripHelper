package com.ninima.triphelper;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ninima.triphelper.global.MyDatabase;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = findViewById(R.id.test);

        viewModel = ViewModelProviders.of(this)
                .get(MainViewModel.class);

        viewModel.trip.observe(this, new Observer<Trip>() {
            @Override
            public void onChanged(@Nullable Trip trip) {
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
                viewModel.createNewTrip(new Trip());
            }
        });


    }
}