package com.app.swagliv.view.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.swagliv.R;
import com.app.swagliv.databinding.ChatCommentItemBinding;
import com.app.swagliv.model.livestream.pojo.AllLiveStreamCommentsResp;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ChatCommentsAdapter extends RecyclerView.Adapter<ChatCommentsAdapter.ViewHolder> {

    private ArrayList<AllLiveStreamCommentsResp.Datum> connectionsList = new ArrayList<>();

    Context context;

    public ChatCommentsAdapter(ArrayList<AllLiveStreamCommentsResp.Datum> connectionsList) {
        this.connectionsList = connectionsList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_comment_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        context = parent.getContext();
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        AllLiveStreamCommentsResp.Datum category = connectionsList.get(position);
        if (category != null) {
            holder.binding.userName.setText(category.getName());
            holder.binding.userComment.setText(category.getComment());
            Glide.with(context).load(category.getCustomerProfileImage()).
                    into(holder.binding.imgProfile)
                    .onLoadFailed(context.getDrawable(R.drawable.person_img));
        }

    }

    @Override
    public int getItemCount() {
        return connectionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ChatCommentItemBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ChatCommentItemBinding.bind(itemView);
        }
    }

}
