package com.ninima.triphelper.detail.memo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.ninima.triphelper.global.MyDatabase;
import com.ninima.triphelper.model.Memo;

import java.util.List;

public class MemoViewModel extends ViewModel {
    private MyDatabase database = MyDatabase.instance();
    private MemoDao memoDao = database.memoDao();
    long tid;
    LiveData<List<Memo>> memoList;

    public MemoViewModel(long tid){
        this.tid = tid;
        memoList = memoDao.getAllMemo(tid);
    }

    void insertNewMemo(final Memo memo){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                memoDao.insert(memo);
            }
        });
    }

    void deleteMemo(final Memo memo){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                memoDao.delete(memo);
            }
        });
    }

    void updateMemo(final Memo memo){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                memoDao.update(memo);
            }
        });
    }

    static class MemoViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        private long tid;
        public MemoViewModelFactory(long tid){
            this.tid = tid;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return ((T)(new MemoViewModel(tid)));
        }
    }
}
