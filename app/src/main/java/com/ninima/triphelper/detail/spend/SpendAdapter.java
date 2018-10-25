package com.ninima.triphelper.detail.spend;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ninima.triphelper.model.Spend;
import com.ninima.triphelper.databinding.ItemSpendBinding;

import java.util.List;

public class SpendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Spend> spendList;
    ItemDeleteListener listener;
    public SpendAdapter(Context context, List<Spend> spendList, ItemDeleteListener listener){
        this.context = context;
        this.spendList = spendList;
        this.listener = listener;
    }

    public class SpendHolder extends RecyclerView.ViewHolder {

        ItemSpendBinding binding;
        public SpendHolder(ItemSpendBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Spend spend) {
            binding.setSpend(spend);
        }
    }

    @Override
    public SpendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSpendBinding binding = ItemSpendBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SpendHolder(binding);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Spend spend = spendList.get(position);
        ((SpendHolder)holder).bind(spend);
        final int pos = position;

        Log.e("!!!!!!!adapter",pos+"!!!!"+spend.getTitle());

        if(spend.getRegisterDate()==null){
            ((SpendHolder) holder).binding.timeTvSpend.setText("");
        }
        if(spend.getPlace()==null){
            ((SpendHolder) holder).binding.placeTvSpend.setText("");
        }
        if(spend.getDetail()==null);


        //지출총액 확인은 어케??
        ((SpendHolder) holder).binding.timeTvSpend.setText(spend.getTitle());
        ((SpendHolder) holder).binding.priceTvSpend.setText(Float.toString(spend.getPrice()));
        ((SpendHolder) holder).binding.categoryTvSpend.setText(spend.getCategory());


//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage("여행을 삭제하시겠습니까?");
                dialog.setCancelable(false);
                dialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onItemClick(spend);
                        dialog.dismiss();
                    }
                });
                dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        if(spendList==null) return 0;
        return spendList.size();
    }
}

interface ItemDeleteListener {
    void onItemClick(Spend spend);
}
