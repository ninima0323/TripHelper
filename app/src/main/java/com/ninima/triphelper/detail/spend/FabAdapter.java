package com.ninima.triphelper.detail.spend;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ninima.triphelper.R;
import com.ninima.triphelper.model.CategoryM;

import java.util.List;

public class FabAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<CategoryM> categories;
    SpendViewModel viewModel;
    long tid;

    public FabAdapter(Context context, List<CategoryM> list, SpendViewModel viewModel, long id){
        this.context = context;
        this.categories = list;
        this.viewModel = viewModel;
        this.tid = id;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        CheckBox cb;

        public ViewHolder(View itemView){
            super(itemView);
            tv = (TextView)itemView.findViewById(R.id.fab_tv);
            cb = (CheckBox)itemView.findViewById(R.id.fab_checkbox);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fab, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        final CategoryM cate = categories.get(position);

        if(cate.isSelected()){
            ((ViewHolder)viewHolder).cb.setChecked(true);
        }

        final CategoryM c = new CategoryM();
        c.setCategoryId(cate.getCategoryId());
        c.setTid(tid);
        c.setCategory(cate.getCategory());

        ((ViewHolder)viewHolder).tv.setText(cate.getCategory());
        ((ViewHolder) viewHolder).cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(cate.isSelected()!=isChecked){
                        c.setSelected(true);
                        viewModel.updateCategory(c);
//                        viewModel.getSelectedCategories(tid);
//                        viewModel.selectedCategory.observe((LifecycleOwner) context, new Observer<List<String>>() {
//                            @Override
//                            public void onChanged(@Nullable List<String> strings) {
//                                viewModel.setSelectedSpends(tid, strings);
//                            }
//                        });
                    }
                }else{
                    if(cate.isSelected()!=isChecked){
                        c.setSelected(false);
                        viewModel.updateCategory(c);
//                        viewModel.getSelectedCategories(tid);
//                        viewModel.selectedCategory.observe((LifecycleOwner) context, new Observer<List<String>>() {
//                            @Override
//                            public void onChanged(@Nullable List<String> strings) {
//                                viewModel.setSelectedSpends(tid, strings);
//                            }
//                        });
                    }
                }
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //카테고리 삭제 구현
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
