package com.ninima.triphelper.detail;

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
import com.ninima.triphelper.model.Spend;

import java.util.List;

@SuppressLint("ValidFragment")
public class SpendFragment extends Fragment {
    long tid;
    @SuppressLint("ValidFragment")
    public SpendFragment(long tid){
        this.tid = tid;
    }

    SpendViewModel viewModel;

    RecyclerView rv;
    SpendAdapter mAdapter;
    List<Spend> spendList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spend, container, false);

        SpendViewModel.SpendViewModelFactory factory = new SpendViewModel.SpendViewModelFactory(tid);
        viewModel = ViewModelProviders.of(this, factory)
                .get(SpendViewModel.class);

        viewModel.spendList.observe(this, new Observer<List<Spend>>() {
            @Override
            public void onChanged(@Nullable List<Spend> spends) {
                mAdapter.spendList=spends;
                mAdapter.notifyDataSetChanged();
            }
        });

        rv = (RecyclerView)view.findViewById(R.id.spend_rv);
        mAdapter = new SpendAdapter(getContext(), spendList, new ItemDeleteListener() {
            @Override
            public void onItemClick(Spend spend) {
                viewModel.deleteSpend(spend);
                //위에서 노티파이해줬어여
            }
        });
        rv.setAdapter(mAdapter);

        return view;
    }
}
