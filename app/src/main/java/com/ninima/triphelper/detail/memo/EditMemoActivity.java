package com.ninima.triphelper.detail.memo;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.ninima.triphelper.R;
import com.ninima.triphelper.model.Memo;

public class EditMemoActivity extends AppCompatActivity {

    MemoViewModel viewModel;
    EditText contentEt;
    Memo memo = new Memo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);

        Intent intent = getIntent();
        long tid = intent.getLongExtra("tid",-1);
        memo.setTripId(tid);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.htab_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.GRAY);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("메모 추가");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MemoViewModel.MemoViewModelFactory factory = new MemoViewModel.MemoViewModelFactory(tid);
        viewModel = ViewModelProviders.of(this, factory)
                .get(MemoViewModel.class);

        contentEt = (EditText)findViewById(R.id.contentEt_editMemo);

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
                if(TextUtils.isEmpty(contentEt.getText())){
                    Toast.makeText(this, "입력 내용이 없어 메모가 저장되지 않습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    memo.setContent(contentEt.getText().toString());
                    viewModel.insertNewMemo(memo);
                    Toast.makeText(this, "메모가 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
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
