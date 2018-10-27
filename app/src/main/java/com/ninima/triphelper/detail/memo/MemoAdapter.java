package com.ninima.triphelper.detail.memo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ninima.triphelper.R;
import com.ninima.triphelper.model.Memo;
import com.ninima.triphelper.databinding.ItemMemoBinding;

import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Memo> memoList;
    ItemDeleteListener listener;
    boolean touch=false;
    public MemoAdapter(Context context, List<Memo> memoList, ItemDeleteListener listener){
        this.context = context;
        this.memoList = memoList;
        this.listener = listener;
    }

    public class MemoHolder extends RecyclerView.ViewHolder {

        ItemMemoBinding binding;
        //TextView content;

        public MemoHolder(ItemMemoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            //content = (TextView) itemView.findViewById(R.id.contentTv_memo);
        }

        void bind(Memo memo){
            binding.setMemo(memo);
        }
    }

    @Override
    public MemoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMemoBinding binding = ItemMemoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memo, parent, false);
        return new MemoHolder(binding);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Memo memo = memoList.get(position);
        ((MemoHolder)holder).bind(memo);
        final int pos = position;

        ((MemoHolder) holder).binding.contentTvMemo.setText(memo.getContent());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(touch){//열려있는 상태면
                    touch = false;
                    ((MemoHolder) holder).binding.contentTvMemo.setMaxLines(3);
                    ((MemoHolder) holder).binding.contentTvMemo.setEllipsize(TextUtils.TruncateAt.END);
                }else{//닫힌상태면
                    touch = true;
                    ((MemoHolder) holder).binding.contentTvMemo.setMaxLines(300);
                }

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage("동작을 선택하세요.");
                dialog.setCancelable(true);
                dialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onItemClick(memo);
                        dialog.dismiss();
                    }
                });
                dialog.setNegativeButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, EditMemoActivity.class);
                        intent.putExtra("mid", memo.getRegisterTime());
                        context.startActivity(intent);
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
        if(memoList==null) return 0;
        return memoList.size();
    }
}

interface ItemDeleteListener {
    void onItemClick(Memo memo);
}
