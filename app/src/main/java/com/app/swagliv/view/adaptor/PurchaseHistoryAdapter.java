package com.app.swagliv.view.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.swagliv.R;
import com.app.swagliv.databinding.ItemPurchaseBinding;
import com.app.swagliv.model.profile.pojo.Subscription;

import java.util.ArrayList;

public class PurchaseHistoryAdapter extends RecyclerView.Adapter<PurchaseHistoryAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Subscription> mPurchaseHistory;

    public PurchaseHistoryAdapter(Context mContext, ArrayList<Subscription> mPurchaseHistory) {
        this.mContext = mContext;
        this.mPurchaseHistory = mPurchaseHistory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPurchaseBinding itemPurchaseBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_purchase, parent, false);
        return new ViewHolder(itemPurchaseBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subscription subscription = mPurchaseHistory.get(position);
        holder.itemPurchaseBinding.setPurchase(subscription);
      //  holder.itemPurchaseBinding.rate.setText(mContext.getString(R.string.rupees) + subscription.getTotalAmountInRupees());
    }

    @Override
    public int getItemCount() {
        return mPurchaseHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemPurchaseBinding itemPurchaseBinding;

        public ViewHolder(@NonNull ItemPurchaseBinding itemView) {
            super(itemView.getRoot());
            itemPurchaseBinding = itemView;
        }
    }

    public void updateData(ArrayList<Subscription> subscriptions) {
        this.mPurchaseHistory = subscriptions;
        notifyDataSetChanged();
    }
}
