package com.ninima.triphelper.detail.memo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ninima.triphelper.R;

public class MemoFragment extends Fragment {
    public MemoFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memo, container, false);

        return view;
    }
}
