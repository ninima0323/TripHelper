package com.ninima.triphelper.detail.memo;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ninima.triphelper.R;
import com.ninima.triphelper.detail.RecyclerSectionItemDecoration;
import com.ninima.triphelper.model.Memo;

import java.text.SimpleDateFormat;
import java.util.List;

@SuppressLint("ValidFragment")
public class MemoFragment extends Fragment {
    long tid;

    @SuppressLint("ValidFragment")
    public MemoFragment(long tid){
        this.tid = tid;
    }

    MemoViewModel viewModel;

    RecyclerView rv;
    MemoAdapter mAdapter;
    List<Memo> memoList;
    RecyclerSectionItemDecoration sectionItemDecoration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memo, container, false);

        MemoViewModel.MemoViewModelFactory factory = new MemoViewModel.MemoViewModelFactory(tid);
        viewModel = ViewModelProviders.of(this, factory)
                .get(MemoViewModel.class);

        viewModel.memoList.observe(this, new Observer<List<Memo>>() {
            @Override
            public void onChanged(@Nullable List<Memo> memos) {
                memoList = memos;
                mAdapter.memoList=memos;
                mAdapter.notifyDataSetChanged();

                if(!memos.isEmpty()){
                    sectionItemDecoration =
                            new RecyclerSectionItemDecoration(getResources().getDimensionPixelSize(R.dimen.header),
                                    true,  //아이템이 헤더에 씹히는 경우가 있으면, 디멘션에서 dp늘려서 해결
                                    getSectionCallback(memos));
                    if(rv.getItemDecorationCount()>0) rv.removeItemDecorationAt(0);
                    rv.addItemDecoration(sectionItemDecoration);
                }
            }
        });

        rv = (RecyclerView)view.findViewById(R.id.memo_rv);
        mAdapter = new MemoAdapter(getContext(), memoList, new ItemDeleteListener() {
            @Override
            public void onItemClick(Memo memo) {
                viewModel.deleteMemo(memo);
                //위에서 노티파이해줬어여
            }
        });
        rv.setAdapter(mAdapter);

        return view;
    }

    private RecyclerSectionItemDecoration.SectionCallback getSectionCallback(final List<Memo> list) {
        return new RecyclerSectionItemDecoration.SectionCallback() {
            @Override
            public boolean isSection(int position) {   //디비에서 정렬해서 가져올테니 순서대로 비교

                return position == 0
                        || list.get(position).getTitleDate().compareTo(list.get(position - 1).getTitleDate())!=0;
            }

            @Override
            public String getSectionHeader(int position) {
                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
                String d=transFormat.format(list.get(position).getTitleDate());
                return d;
            }
        };
    }
}
