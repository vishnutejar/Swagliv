package com.app.swagliv.view.adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.swagliv.R;
import com.app.swagliv.databinding.ItemSearchBinding;
import com.app.swagliv.model.login.pojo.User;
import com.app.swagliv.view.activities.UserProfileActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class SearchCrushAdapter extends RecyclerView.Adapter<SearchCrushAdapter.ViewHolder> {
    Context context;
    ArrayList<User> mCrushList;

    public SearchCrushAdapter(Context context, ArrayList<User> mCrushList) {
        this.context = context;
        this.mCrushList = mCrushList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSearchBinding itemSearchBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_search, parent, false);
        return new ViewHolder(itemSearchBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User crushList = mCrushList.get(position);
//        holder.itemSearchBinding.setCrushInfo(crushList);
//        Glide.with(context)
//                .load(crushList.getProfileImages())
//                .placeholder(R.drawable.ic_blank_user_profile)
//                .into(holder.itemSearchBinding.pictures);
//        holder.itemSearchBinding.pictures.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(context, UserProfileActivity.class);
//                i.putExtra("crushList", crushList);
//                context.startActivity(i);
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return mCrushList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemSearchBinding itemSearchBinding;

        public ViewHolder(@NonNull ItemSearchBinding itemView) {
            super(itemView.getRoot());
            itemSearchBinding = itemView;
        }
    }

    public void updateData(ArrayList<User> users) {
        this.mCrushList = users;
        notifyDataSetChanged();
    }

}
