package com.ninima.triphelper.detail.spend.currency;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ninima.triphelper.R;

import java.util.List;

public class CurrencyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    List<CurrencyM> currencies;
    long tid;
    ItemDeleteListener listener;
    public CurrencyAdapter(Context context, List<CurrencyM> List, long tid, com.ninima.triphelper.detail.spend.currency.ItemDeleteListener listener){
        this.context = context;
        this.currencies = List;
        this.tid = tid;
        this.listener = listener;
    }

    public class CurrencyHolder extends RecyclerView.ViewHolder {

        TextView price, tag;

        public CurrencyHolder(View v) {
            super(v);
            price = (TextView)itemView.findViewById(R.id.priceTv_currency);
            tag = (TextView)itemView.findViewById(R.id.tagTv_currency);
        }
    }

    @Override
    public CurrencyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_currency, parent, false);
        return new CurrencyHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final CurrencyM currency = currencies.get(position);
        final int pos = position;
        final int cid = currency.getCid();

        ((CurrencyHolder)holder).price.setText(currency.getPrice().toString());
        ((CurrencyHolder)holder).tag.setText(currency.getTag());

        if(!currency.getTag().equals("₩")){
            CurrencyViewModel.CurrencyViewModelFactory2 factory = new CurrencyViewModel.CurrencyViewModelFactory2(cid, true);
            final CurrencyViewModel viewModel = ViewModelProviders.of((FragmentActivity) context, factory)
                    .get(CurrencyViewModel.class);

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setMessage("동작을 선택하세요.");
                    dialog.setCancelable(true);
                    dialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            listener.onItemClick(currency);
                        }
                    });
                    AlertDialog.Builder builder = dialog.setNegativeButton("수정", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            Dialog d = new CurrencyDialog(context, tid, cid, true, viewModel);
                            d.show();
                        }
                    });
                    dialog.show();
                    return false;
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        if(currencies ==null) return 0;
        return currencies.size();
    }
}

interface ItemDeleteListener {
    void onItemClick(CurrencyM currencyM);
}
