package com.ninima.triphelper.main;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ninima.triphelper.R;

import java.util.List;

public class TripImgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<String> imgList;
    public TripImgAdapter(Context context, List<String> List){
        this.context = context;
        this.imgList = List;
    }

    public class TripImgHolder extends RecyclerView.ViewHolder {

        ImageView img;

        public TripImgHolder(View v) {
            super(v);
            img = (ImageView)itemView.findViewById(R.id.imageView);
        }
    }

    @Override
    public TripImgHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_img, parent, false);
        return new TripImgHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final String imgUriString = imgList.get(position);
        final int pos = position;

        Uri photoUri = Uri.parse(imgUriString);
        ((TripImgHolder)holder).img.setImageURI(photoUri);
    }


    @Override
    public int getItemCount() {
        if(imgList ==null) return 0;
        return imgList.size();
    }
}
