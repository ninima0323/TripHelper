package com.ninima.triphelper.detail.spend;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ninima.triphelper.R;
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
        ImageView img;

        public SpendHolder(ItemSpendBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            img = (ImageView)itemView.findViewById(R.id.img_spend);
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

        if(TextUtils.isEmpty(spend.getRegisterDate().toString())){
            ((SpendHolder) holder).binding.timeTvSpend.setText("");
        }
        if(TextUtils.isEmpty(spend.getPlace())){
            ((SpendHolder) holder).binding.placeTvSpend.setText("");
        }
        if(TextUtils.isEmpty(spend.getDetail())){
            ((SpendHolder) holder).binding.detailTvSpend.setVisibility(View.GONE);
        }


        String bi = spend.getPicUri();
        if(!TextUtils.isEmpty(bi)){
//            try {
                Uri photoUri = Uri.parse(bi);
                ((SpendHolder) holder).img.setImageURI(photoUri);
                ((SpendHolder) holder).img.setBackground(null);
//            }catch (Exception e){
//                e.printStackTrace();
//                //((SpendHolder) holder).img.setImageDrawable();
//                Log.e("!!!!!!!!","에러러러러ㅓ러럴");
//                Toast.makeText(context,"사진이 삭제되어 기본이미지를 셋팅합니다.", Toast.LENGTH_SHORT).show();
//            }
//            Uri photoUri = Uri.parse(bi);
//            Glide.with(context)
//                    .load(photoUri)
//                    .into(((SpendHolder) holder).img);
        }

        //지출총액 확인은 어케??
        ((SpendHolder) holder).binding.timeTvSpend.setText(spend.getTitle());
        ((SpendHolder) holder).binding.priceTvSpend.setText(Float.toString(spend.getPrice()));
        ((SpendHolder) holder).binding.currencySTvSpend.setText(spend.getCurrencyS());
        ((SpendHolder) holder).binding.categoryTvSpend.setText(spend.getCategory());


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage("동작을 선택하세요");
                dialog.setCancelable(true);
                dialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onItemClick(spend);
                        dialog.dismiss();
                    }
                });
                dialog.setNegativeButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, EditSpendActivity.class);
                        intent.putExtra("sid", spend.getSid());
                        intent.putExtra("isEdit", true);
                        intent.putExtra("tid", spend.getTripId());
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
        if(spendList==null) return 0;
        return spendList.size();
    }
}

interface ItemDeleteListener {
    void onItemClick(Spend spend);
}
