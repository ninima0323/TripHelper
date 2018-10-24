package com.ninima.triphelper.detail;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ninima.triphelper.R;
import com.ninima.triphelper.databinding.ActivityEditSpendBinding;
import com.ninima.triphelper.global.Converters;
import com.ninima.triphelper.model.Spend;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class EditSpendActivity extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener  {

    Context context = this;

    ActivityEditSpendBinding binding;
    Spend spend = new Spend();
    SpendViewModel viewModel;
    Map<String,Boolean> categoryMap;

    Toolbar toolbar;
    View icon;

    long tripId;
    boolean istitle, isprice, iscategory;

    String title, place, detailM, category;
    Float price;
    Date registerDate;
    int nYear, nMonth, nDay, nHour, nMinute;
    int  mYear, mMonth, mDay, mHour,mMinute, mSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_edit_spend);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_spend);
        binding.setSpend(spend);

        Intent intent = getIntent();
        final long tid = intent.getLongExtra("tid",-1);
        tripId = tid;

        SpendViewModel.SpendViewModelFactory factory = new SpendViewModel.SpendViewModelFactory(tid);
        viewModel = ViewModelProviders.of(this, factory)
                .get(SpendViewModel.class);

        toolbar = (Toolbar) findViewById(R.id.htab_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.GRAY);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("지출 추가");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR); // current year
        mMonth = c.get(Calendar.MONTH); // current month
        mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        mHour = c.get(Calendar.HOUR_OF_DAY) ;//current hour
        mMinute=c.get(Calendar.MINUTE);//current minute
        mSecond = c.get(Calendar.SECOND);
        registerDate = c.getTime();

        binding.titleEtEditSpend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    istitle = false;
                } else {
                    istitle = true;
                    showSaveBtnMenu();
                }
            }
        });

        binding.categoryEditSpend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    iscategory = false;
                } else {
                    iscategory = true;
                    showSaveBtnMenu();
                }
            }
        });

        binding.priceEtEditSpend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    isprice = false;
                } else {
                    isprice = true;
                    showSaveBtnMenu();
                }
            }
        });

        binding.dateEditSpend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        EditSpendActivity.this , mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        binding.timeEditSpend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        EditSpendActivity.this ,mHour, mMinute, true ); //시, 분, 24시간형식인지
                timePickerDialog.show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        nYear = year;
        nMonth = month;
        nDay = dayOfMonth;
        registerDate = getDate(nYear,nMonth,nDay,nHour,nMinute);
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
        String to = transFormat.format(registerDate);
        binding.dateEditSpend.setText(to);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        nHour = hourOfDay;
        nMinute = minute;
        registerDate = getDate(nYear,nMonth,nDay,nHour,nMinute);
        SimpleDateFormat transFormat = new SimpleDateFormat("HH:mm");
        String to = transFormat.format(registerDate);
        binding.timeEditSpend.setText(to);
    }

    public Date getDate(int year, int month, int day, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        int sec = cal.get(Calendar.SECOND);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, sec);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public void showSaveBtnMenu(){
        if(istitle && iscategory && isprice){

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "지출 내역이 저장되지 않습니다", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            case R.id.action_save:
                Spend s = new Spend();
                title = binding.titleEtEditSpend.getText().toString();
                category = binding.categoryEditSpend.getText().toString();
                price = Float.parseFloat(binding.priceEtEditSpend.getText().toString());
                place = binding.placeEtEditSpend.getText().toString();
                detailM = binding.detailEtEditSpend.getText().toString();
                s.setTripId(tripId);
                s.setTitle(title);
                s.setPrice(price);
                s.setCategory(category);
                if(registerDate != null) s.setRegisterDate(registerDate);
                if(place != null || !place.isEmpty()) s.setPlace(place);
                if(detailM !=null || !detailM.isEmpty()) s.setDetail(detailM);
                viewModel.insertNewSpend(s);
                Toast.makeText(this, "지출 내역이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "지출 내역이 저장되지 않습니다.", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
}
