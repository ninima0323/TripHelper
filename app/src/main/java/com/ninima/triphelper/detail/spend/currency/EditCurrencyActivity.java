package com.ninima.triphelper.detail.spend.currency;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.provider.CalendarContract;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ninima.triphelper.R;

import java.util.List;

public class EditCurrencyActivity extends AppCompatActivity {

    Context context = this;
    CurrencyViewModel viewModel;
    long tid;

    RecyclerView rv;
    CurrencyAdapter mAdapter;
    List<CurrencyM> currencies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_currency);

        Intent intent = getIntent();
        tid = intent.getLongExtra("tid",-1);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.htab_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.GRAY);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("환율 설정");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        final TextView test = (TextView)findViewById(R.id.textView);

        rv = (RecyclerView)findViewById(R.id.currency_rv);
        mAdapter = new CurrencyAdapter(this, currencies, tid, new ItemDeleteListener() {
            @Override
            public void onItemClick(CurrencyM currencyM) {
                viewModel.deleteCurrency(currencyM);
            }
        });
        rv.setAdapter(mAdapter);

        CurrencyViewModel.CurrencyViewModelFactory factory = new CurrencyViewModel.CurrencyViewModelFactory(tid);
        viewModel = ViewModelProviders.of(this, factory)
                .get(CurrencyViewModel.class);

        viewModel.currencyList.observe(this, new Observer<List<CurrencyM>>() {
            @Override
            public void onChanged(@Nullable List<CurrencyM> currencyMS) {
                currencies = currencyMS;
                mAdapter.currencies = currencyMS;
                mAdapter.notifyDataSetChanged();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_add:
                Dialog dialog = new CurrencyDialog(context, tid, -1,false, viewModel);
                //Dialog dialog = new CurrencyDialog(context);
                dialog.show();
                return true ;

        }
        return super.onOptionsItemSelected(item);
    }

}
