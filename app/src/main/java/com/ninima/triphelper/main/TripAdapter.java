package com.ninima.triphelper.main;

import android.app.AlertDialog;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ninima.triphelper.BR;
import com.ninima.triphelper.R;
import com.ninima.triphelper.detail.DetailActivity;
import com.ninima.triphelper.detail.SpendDao;
import com.ninima.triphelper.global.MyDatabase;
import com.ninima.triphelper.model.Trip;

import java.util.List;

import com.ninima.triphelper.databinding.ItemTripBinding;

public class TripAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Trip> tripList;
    ItemDeleteListener listener;

    MyDatabase database = MyDatabase.instance();
    //TripDao tripDao =  database.tripDao();
    SpendDao spendDao = database.spendDao();
    public TripAdapter(Context context, ItemDeleteListener listener){
        this.context = context;
        this.listener = listener;
    }

    public class TripHolder extends RecyclerView.ViewHolder {
//
//        RecyclerView imgRv;

        ItemTripBinding binding;
        public TripHolder(ItemTripBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
//
//            imgRv = (RecyclerView)itemView.findViewById(R.id.img_rv);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
//            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//            imgRv.setLayoutManager(layoutManager);
        }

        void bind(Trip trip) {
            binding.setTrip(trip);
        }
    }

    @Override
    public TripHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemTripBinding binding = ItemTripBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TripHolder(binding);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Trip trip = tripList.get(position);
        ((TripHolder)holder).bind(trip);
        final int pos = position;
     //   Log.e("date", ""+trip.getStartDate().toString());
//        spendDao.getPicList(trip.getRegisterTime()).observe((LifecycleOwner) context, new Observer<List<String>>() {
//            @Override
//            public void onChanged(@Nullable List<String> strings) {
//                List<String> imgList = strings;
//                TripImgAdapter mAdapter = new TripImgAdapter(context, imgList);
//                ((TripHolder)holder).imgRv.setAdapter(mAdapter);
//            }
//        });


        if(trip.getStartDate()==null){
            ((TripHolder) holder).binding.tv1Trip.setText("여행 날짜를 입력해주세요.");
            ((TripHolder) holder).binding.tv2Trip.setText(" ");
        }else{

            ((TripHolder) holder).binding.tv1Trip.setText("여행 기간 : ");
            ((TripHolder) holder).binding.tv2Trip.setText(" ~ ");
        }

        //지출총액 확인은 어케??
        ((TripHolder) holder).binding.tv3Trip.setText("지출 항목을 추가하면 지출 총액이 계산됩니다.");
        ((TripHolder) holder).binding.totalTrip.setText(" ");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //상세로 이동하기
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("trip_title", trip.getTitle());
                intent.putExtra("tid", trip.getRegisterTime());
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage("여행을 삭제하시겠습니까?");
                dialog.setCancelable(false);
                dialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onItemClick(trip);
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
        if(tripList==null) return 0;
        return tripList.size();
    }
}

interface ItemDeleteListener {
         void onItemClick(Trip trip);
}