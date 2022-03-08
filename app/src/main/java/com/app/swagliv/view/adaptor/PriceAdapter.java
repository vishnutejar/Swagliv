package com.app.swagliv.view.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.swagliv.R;
import com.app.swagliv.databinding.ItemPriceBinding;
import com.app.swagliv.model.profile.pojo.Subscription;

import java.util.ArrayList;

public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.Viewholder> {
    private Context context;
    private ArrayList<Subscription> priceList;
    private PriceAdapter.onItemSelected mOnItemClickListener;

    public PriceAdapter(Context context, ArrayList<Subscription> priceList, PriceAdapter.onItemSelected onItemSelected) {
        this.context = context;
        this.priceList = priceList;
        this.mOnItemClickListener = onItemSelected;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPriceBinding itemPriceBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_price, parent, false);
        Viewholder viewholder = new Viewholder(itemPriceBinding);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        // current view data
        Subscription subscription = priceList.get(position);

        if (subscription != null) {
            // initialize xml price object
            holder.itemPriceBinding.setPrice(subscription);
            if (subscription.getDurationInMonths() != null) {
                holder.itemPriceBinding.monthNo.setText(subscription.getDurationInMonths().toString());
            }
            if (subscription.getPricePerMonth() != null)
                holder.itemPriceBinding.price.setText(context.getString(R.string.rupees) + subscription.getPricePerMonth().toString() + context.getString(R.string.per_month));

            holder.itemPriceBinding.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.OnItemSelected(subscription);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return priceList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ItemPriceBinding itemPriceBinding;

        public Viewholder(@NonNull ItemPriceBinding itemView) {
            super(itemView.getRoot());
            itemPriceBinding = itemView;
        }
    }

    public void updateList(ArrayList<Subscription> priceList) {
        this.priceList = priceList;
        notifyDataSetChanged();
    }

    public interface onItemSelected {
        void OnItemSelected(Subscription price_model);
    }
}
