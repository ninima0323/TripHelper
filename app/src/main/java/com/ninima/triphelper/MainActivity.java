package com.ninima.triphelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ninima.triphelper.global.MyDatabase;

public class MainActivity extends AppCompatActivity {
    private MyDatabase db = MyDatabase.instance();
    private TripDao tripDao = db.tripDao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Trip trip = tripDao.getOne();

        ((TextView)findViewById(R.id.test)).setText((trip == null)? "null" : tripDao.getOne().getRegisterTime()+"");
        (findViewById(R.id.test)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripDao.insert(new Trip());
                ((TextView)findViewById(R.id.test)).setText((trip == null)? "null" : tripDao.getOne().getRegisterTime()+"");
            }
        });
    }
}
