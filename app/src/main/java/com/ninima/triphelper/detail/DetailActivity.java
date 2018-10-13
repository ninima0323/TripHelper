package com.ninima.triphelper.detail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.ninima.triphelper.R;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView t = (TextView)findViewById(R.id.tv_detail);
        Intent intent = getIntent();
        long tid = intent.getLongExtra("tid",-1);
        String title = intent.getStringExtra("trip_title");
        t.setText(tid+"\n"+title);
    }
}
