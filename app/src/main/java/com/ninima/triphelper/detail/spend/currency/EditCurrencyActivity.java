package com.ninima.triphelper.detail.spend.currency;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

//        viewModel.currencyList.observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
////                Map<String, Float> m = Converters.fromCurrencyString(s);
////                Set mSet = m.entrySet();
////                Iterator iterator = mSet.iterator();
////                while(iterator.hasNext()){
////                    Map.Entry entry = (Map.Entry)iterator.next();
////                    String key = (String)entry.getKey();
////                    Float value = (Float)entry.getValue();
////                    //test.setText(value+"\t"+key);
////                }
//
//                currencies = m;
//            }
//        });
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
//                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("환율 추가");
//                builder.setMessage("ex) US 1200");
//                builder.setCancelable(false);
//                @SuppressLint("ResourceType") View v = (View)findViewById(R.layout.dialog_currency);
//                builder.setView(v);
//                final EditText tag = v.findViewById(R.id.tagEt_curDial);
//                final EditText price = v.findViewById(R.id.priceEt_curDial);
//
//                price.setInputType(InputType.TYPE_CLASS_NUMBER
//                        | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
//                tag.setHint("US");
//                price.setHint("1200");
//
//                builder.setPositiveButton("추가", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        CurrencyM c = new CurrencyM();
//                        c.setTid(tid);
//                        c.setTag(tag.getText().toString());
//                        c.setPrice(Float.parseFloat(price.getText().toString()));
//                        viewModel.insertNewCurrency(c);
//                        dialog.dismiss();
//                    }
//                });
//                builder.setNegativeButton("취소",new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        dialog.dismiss();
//                    }
//                });
//                builder.show();
                Dialog dialog = new CurrencyDialog(context, tid, -1,false, viewModel);
                //Dialog dialog = new CurrencyDialog(context);
                dialog.show();
                return true ;

        }
        return super.onOptionsItemSelected(item);
    }

//    public class CurrencyDialog extends Dialog {
//        EditText tag,price;
//        Button addbtn;
//        boolean istag = false , isprice = false;
//
//        public CurrencyDialog(final Context context) {
//            super(context);
//
//            // 다이얼로그 외부 화면 흐리게 표현
////            WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
////            lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
////            lpWindow.dimAmount = 0.8f;
////            getWindow().setAttributes(lpWindow);
//
//            //getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //다이얼로그의 배경을 투명으로 만듭니다.
//            setContentView(R.layout.dialog_currency);     //다이얼로그에서 사용할 레이아웃입니다.
//            tag = (EditText) findViewById(R.id.tagEt_curDial);
//            price = (EditText) findViewById(R.id.priceEt_curDial);
//            price.setHint("12246.5");
//
//            tag.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                    if(TextUtils.isEmpty(tag.getText())){
//                        istag = false;
//                    }else{//제대로된 값이 입력됨
//                        istag = true;
//                    }
//                }
//            });
//
//            price.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                    if(TextUtils.isEmpty(price.getText())){
//                        isprice = false;
//                    }else{//제대로된 값이 입력됨
//                        isprice = true;
//                    }
//                }
//            });
//
//            addbtn = (Button) findViewById(R.id.addBtn_curDial);
//            addbtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(istag && isprice){
//                        CurrencyM c = new CurrencyM();
//                        c.setTid(tid);
//                        c.setTag(tag.getText().toString());
//                        c.setPrice(Float.parseFloat(price.getText().toString()));
//                        viewModel.insertNewCurrency(c);
//                        dismiss();
//                    }else{
//                        Toast.makeText(context, "두 항목을 입력하지 않아 추가되지 않습니다.", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//
//        }
//
//    }

}
