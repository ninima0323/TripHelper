package com.ninima.triphelper.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ninima.triphelper.BR;
import com.ninima.triphelper.model.Trip;

import java.util.List;

import com.ninima.triphelper.databinding.ItemTripBinding;

public class TripAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Trip> tripList;
    ItemDeleteListener listener;
    public TripAdapter(Context context, List<Trip> tripList, ItemDeleteListener listener){
        this.context = context;
        this.tripList = tripList;
        this.listener = listener;
    }

    public class TripHolder extends RecyclerView.ViewHolder {

        ItemTripBinding binding;
        public TripHolder(ItemTripBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Trip trip) {
            binding.setVariable(BR.trip, trip);
        }
    }

    @Override
    public TripHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemTripBinding binding = ItemTripBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TripHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Trip trip = tripList.get(position);
        ((TripHolder)holder).bind(trip);
        final int pos = position;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //상세로 이동하기
//                Intent intent = new Intent(context, DetailActivity.class);
//                intent.putExtra("TripTitle", trip.getTitle());
//                 intent.putExtra("tid", trip.getTid());
//                context.startActivity(intent);
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