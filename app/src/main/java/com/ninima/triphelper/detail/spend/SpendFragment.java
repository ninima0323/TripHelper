package com.ninima.triphelper.detail.spend;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ninima.triphelper.R;
import com.ninima.triphelper.detail.RecyclerSectionItemDecoration;
import com.ninima.triphelper.model.CategoryM;
import com.ninima.triphelper.model.Spend;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    RecyclerSectionItemDecoration sectionItemDecoration;

    FloatingActionButton fab;
    RecyclerView rvFab;
    FabAdapter fabAdapter;
    boolean isFabClicked = false;
    Context context = getContext();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spend, container, false);

        SpendViewModel.SpendViewModelFactory factory = new SpendViewModel.SpendViewModelFactory(tid);
        viewModel = ViewModelProviders.of(this, factory)
                .get(SpendViewModel.class);

        viewModel.spendList.observe(this, new Observer<List<Spend>>() {
            @Override
            public void onChanged(@Nullable List<Spend> spends) {
                spendList = spends;
                mAdapter.spendList=spends;
                mAdapter.notifyDataSetChanged();

                if(!spends.isEmpty()){
                    sectionItemDecoration =
                            new RecyclerSectionItemDecoration(getResources().getDimensionPixelSize(R.dimen.header),
                                    true,  //아이템이 헤더에 씹히는 경우가 있으면, 디멘션에서 dp늘려서 해결
                                    getSectionCallback(spends));
                    if(rv.getItemDecorationCount()>0) rv.removeItemDecorationAt(0);
                    rv.addItemDecoration(sectionItemDecoration);
                }
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

        rvFab = (RecyclerView)view.findViewById(R.id.fab_rv);
        viewModel.categoris.observe(this, new Observer<List<CategoryM>>() {
            @Override
            public void onChanged(@Nullable List<CategoryM> categoryMS) {

                fabAdapter = new FabAdapter(context,categoryMS, viewModel, tid);
                rvFab.setAdapter(fabAdapter);
                List<String> clist = new ArrayList<>();
                List<Spend> slist = new ArrayList<>();
                int i;
                for(i=0; i<categoryMS.size();i++){
                    if(categoryMS.get(i).isSelected()){
                        clist.add(categoryMS.get(i).getCategory());
                    }
                }
                if(spendList!=null){
                    for(i=0; i<spendList.size(); i++){
                        if(clist.contains(spendList.get(i).getCategory())){
                            slist.add(spendList.get(i));
                        }
                    }
                    mAdapter.spendList = slist;
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        rvFab.bringToFront();
        rvFab.setVisibility(View.INVISIBLE);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFabClicked){//트루인상태면 지금 보여지고 있는 상태, 이때 누르면 폴스로 바꾸고 없앰
                    isFabClicked = false;
                    rvFab.setVisibility(View.INVISIBLE);
                }else{//폴스인상태에서 누르면 트루로 바뀌고 화면 뜸
                    isFabClicked = true;
                    rvFab.setVisibility(View.VISIBLE);
                }

            }
        });

        //viewModel.getSelectedCategories(tid);
//        viewModel.selectedCategory.observe(this, new Observer<List<String>>() {
//            @Override
//            public void onChanged(@Nullable List<String> strings) {
//                viewModel.setSelectedSpends(tid, strings);
//            }
//        });

        return view;
    }

    private RecyclerSectionItemDecoration.SectionCallback getSectionCallback(final List<Spend> list) {
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
