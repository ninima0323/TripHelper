package com.ninima.triphelper.detail.memo;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.ninima.triphelper.R;
import com.ninima.triphelper.model.Memo;

import java.util.Date;

public class EditMemoActivity extends AppCompatActivity {

    MemoViewModel viewModel;
    EditText contentEt;
    Memo memo = new Memo();
    long tid, mid;
    boolean update = false;
    Date titleD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);


        final Toolbar toolbar = (Toolbar) findViewById(R.id.htab_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.GRAY);
        toolbar.setTitleTextColor(Color.WHITE);

        contentEt = (EditText)findViewById(R.id.contentEt_editMemo);

        Intent intent = getIntent();
        tid = intent.getLongExtra("tid",-1);

        if(tid!=-1){
            MemoViewModel.MemoViewModelFactory factory = new MemoViewModel.MemoViewModelFactory(tid);
            viewModel = ViewModelProviders.of(this, factory)
                    .get(MemoViewModel.class);

            memo.setTripId(tid);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("메모 추가");
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }else{
            update = true;
            mid = intent.getLongExtra("mid", -1);

            MemoViewModel.MemoViewModelFactory2 factory = new MemoViewModel.MemoViewModelFactory2(mid);
            viewModel = ViewModelProviders.of(this, factory)
                    .get(MemoViewModel.class);
            viewModel.memo.observe(this, new Observer<Memo>() {
                @Override
                public void onChanged(@Nullable Memo memo) {
                    contentEt.setText(memo.getContent());
                    tid = memo.getTripId();
                    titleD = memo.getTitleDate();
                }
            });

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("메모 수정");
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                Toast.makeText(this, "메모가 저장되지 않습니다.", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            case R.id.action_save:
                if (!update) {
                    if(TextUtils.isEmpty(contentEt.getText())){
                        Toast.makeText(this, "입력 내용이 없어 메모가 저장되지 않습니다.", Toast.LENGTH_SHORT).show();
                    }else{
                        memo.setContent(contentEt.getText().toString());
                        viewModel.insertNewMemo(memo);
                        Toast.makeText(this, "메모가 저장되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else{
                    if(TextUtils.isEmpty(contentEt.getText())){
                        Toast.makeText(this, "입력 내용이 없어 메모가 수정되지 않습니다.", Toast.LENGTH_SHORT).show();
                    }else{
                        memo.setRegisterTime(mid);
                        memo.setTripId(tid);
                        memo.setTitleDate(titleD);
                        memo.setContent(contentEt.getText().toString());
                        viewModel.updateMemo(memo);
                        Toast.makeText(this, "메모가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "메모가 저장되지 않습니다.", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
}
