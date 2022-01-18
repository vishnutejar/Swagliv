package com.app.swagliv.view.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.swagliv.R;
import com.app.swagliv.databinding.ItemTinderBinding;
import com.app.swagliv.model.login.pojo.User;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {

    // variables
    private Context mContext;
    private ArrayList<User> mPeoplesList = null;

    public CardStackAdapter(Context context, ArrayList<User> mPeoplesInfoList) {
        this.mContext = context;
        this.mPeoplesList = mPeoplesInfoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTinderBinding itemTinderBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_tinder, parent, false);
        return new ViewHolder(itemTinderBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mPeoplesList.get(position);
        holder.itemTinderBinding.userAge.setText(""+user.getUserAge());
        holder.itemTinderBinding.userName.setText(user.getName());
        //-------------
        Glide.with(mContext)
                .load(user.getPersonalImage().isEmpty() ? user.getProfileImages() : user.getPersonalImage().get(0))
                .error(R.drawable.person_img)
                .placeholder(R.drawable.person_img)
                .into(holder.itemTinderBinding.itemImage);
    }

    @Override
    public int getItemCount() {
        return mPeoplesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemTinderBinding itemTinderBinding;

        public ViewHolder(@NonNull ItemTinderBinding itemTinderBinding) {
            super(itemTinderBinding.getRoot());
            this.itemTinderBinding = itemTinderBinding;
        }
    }

    public void updateData(ArrayList<User> peoplesInfos) {
        this.mPeoplesList = peoplesInfos;
        notifyDataSetChanged();
    }
}