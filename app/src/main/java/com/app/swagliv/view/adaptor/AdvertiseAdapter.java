package com.app.swagliv.view.adaptor;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import com.app.swagliv.R;
import com.app.swagliv.databinding.ItemAdvertiseBinding;
import com.app.swagliv.model.profile.pojo.Subscription;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdvertiseAdapter extends PagerAdapter {
    Context context;
    ArrayList<Subscription> subscriptionsTypes;

    public AdvertiseAdapter(Context context, ArrayList<Subscription> advertiseModelsList) {
        this.context = context;
        this.subscriptionsTypes = advertiseModelsList;
    }


    @Override
    public int getCount() {
        return subscriptionsTypes.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        ItemAdvertiseBinding itemAdvertiseBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_advertise, container, false);
        Subscription subscription = subscriptionsTypes.get(position);

        //Variable Set
        itemAdvertiseBinding.setSubscription(subscription);
        itemAdvertiseBinding.likeText.setText(Html.fromHtml(subscription.getDescription()));
        Glide.with(context)
                .load(subscription.getSubscriptionImageURL())
                .into(itemAdvertiseBinding.icon);
        container.addView(itemAdvertiseBinding.getRoot());
        return itemAdvertiseBinding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void updateData(ArrayList<Subscription> subscriptions) {
        this.subscriptionsTypes = subscriptions;
        notifyDataSetChanged();
    }
}
