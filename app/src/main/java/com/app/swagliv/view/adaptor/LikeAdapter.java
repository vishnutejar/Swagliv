package com.app.swagliv.view.adaptor;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.swagliv.R;
import com.app.swagliv.databinding.ItemLikePictureBinding;
import com.app.swagliv.model.login.pojo.User;
import com.app.swagliv.model.profile.pojo.Subscription;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.ViewHolder> {
    Context context;
    ArrayList<User> mUserLikes;
    Subscription mSubscription;

    public LikeAdapter(Context context, ArrayList<User> mLikePeople, Subscription subscription) {
        this.context = context;
        this.mUserLikes = mLikePeople;
        this.mSubscription = subscription;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLikePictureBinding itemLikePictureBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_like_picture, parent, false);
        return new ViewHolder(itemLikePictureBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUserLikes.get(position);
        holder.itemLikePictureBinding.getUser();


        if (mSubscription == null) {
            Glide.with(context)
                    .load(user.getPersonalImage().isEmpty() ? user.getProfileImages() : user.getPersonalImage().get(0))
                    .placeholder(R.drawable.ic_blank_user_profile)
                    .apply(bitmapTransform(new BlurTransformation(25, 3)))
                    .into(holder.itemLikePictureBinding.userLikeProfile);
        } else {
            Glide.with(context)
                    .load(user.getPersonalImage().isEmpty() ? user.getProfileImages() : user.getPersonalImage().get(0))
                    .placeholder(R.drawable.ic_blank_user_profile)
                    .into(holder.itemLikePictureBinding.userLikeProfile);
        }


    }

    @Override
    public int getItemCount() {
        return mUserLikes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemLikePictureBinding itemLikePictureBinding;

        public ViewHolder(@NonNull ItemLikePictureBinding itemLikePictureBinding) {
            super(itemLikePictureBinding.getRoot());
            this.itemLikePictureBinding = itemLikePictureBinding;
        }
    }

    public void updateData(ArrayList<User> users) {
        this.mUserLikes = users;
        notifyDataSetChanged();
    }
}
