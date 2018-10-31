package com.ninima.triphelper.detail.spend.currency;

import android.app.Dialog;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ninima.triphelper.R;

public class CurrencyDialog extends Dialog {
    EditText tag,price;
    TextView title;
    Button addbtn;
    boolean istag = false , isprice = false;
    CurrencyM c = new CurrencyM();

    public CurrencyDialog(final Context context, final long tid, final int cid,
                          final boolean edit, CurrencyViewModel viewModel) {
        super(context);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        //getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //다이얼로그의 배경을 투명으로 만듭니다.
        setContentView(R.layout.dialog_currency);     //다이얼로그에서 사용할 레이아웃입니다.
        title = (TextView)findViewById(R.id.title_dial);
        tag = (EditText) findViewById(R.id.tagEt_curDial);
        price = (EditText) findViewById(R.id.priceEt_curDial);
        price.setHint("12246.5");

        if(edit){//수정인경우
            CurrencyViewModel.CurrencyViewModelFactory2 factory = new CurrencyViewModel.CurrencyViewModelFactory2(cid, true);
            viewModel = ViewModelProviders.of((FragmentActivity) context, factory)
                    .get(CurrencyViewModel.class);
            viewModel.currency.observe((LifecycleOwner) context, new Observer<CurrencyM>() {
                @Override
                public void onChanged(@Nullable CurrencyM currencyM) {
                    c = currencyM;
                    tag.setText(currencyM.getTag());
                    price.setText(currencyM.getPrice().toString());
                    title.setText("환율 수정");
                }
            });
        }

        tag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(tag.getText())){
                    istag = false;
                }else{//제대로된 값이 입력됨
                    istag = true;
                }
            }
        });

        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(price.getText())){
                    isprice = false;
                }else{//제대로된 값이 입력됨
                    isprice = true;
                }
            }
        });

        addbtn = (Button) findViewById(R.id.addBtn_curDial);
        final CurrencyViewModel finalViewModel = viewModel;
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(istag && isprice){
                    if(edit){
                        c.setTag(tag.getText().toString());
                        c.setPrice(Float.parseFloat(price.getText().toString()));
                        finalViewModel.updateCurrency(c);
                    }else{
                        c.setTid(tid);
                        c.setTag(tag.getText().toString());
                        c.setPrice(Float.parseFloat(price.getText().toString()));
                        finalViewModel.insertNewCurrency(c);
                    }
                    dismiss();
                }else{
                    Toast.makeText(context, "모든 항목을 입력하지 않아 추가되지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
