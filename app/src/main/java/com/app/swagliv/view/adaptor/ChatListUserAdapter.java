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
import com.app.swagliv.databinding.ItemChatBinding;
import com.app.swagliv.model.chat.pojo.chatlist.UserChats;
import com.app.swagliv.view.activities.ChatActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ChatListUserAdapter extends RecyclerView.Adapter<ChatListUserAdapter.ChatListUsers> {
    private Context context;
    private ArrayList<UserChats> mUserChats;


    public ChatListUserAdapter(Context context, ArrayList<UserChats> userChats) {
        this.context = context;
        this.mUserChats = userChats;
    }


    @NonNull
    @Override
    public ChatListUsers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChatBinding itemChatBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat, parent, false);
        return new ChatListUsers(itemChatBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListUsers holder, int position) {
        UserChats userChats = mUserChats.get(position);
        holder.itemChatBinding.setChats(userChats);
        Glide.with(context).load("https://english.cdn.zeenews.com/sites/default/files/2021/11/15/987821-ravi-shastri.jpg").placeholder(R.drawable.ic_blank_user_profile).into(holder.itemChatBinding.imgProfile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChatActivity.class);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUserChats.size();
    }


    public class ChatListUsers extends RecyclerView.ViewHolder {
        ItemChatBinding itemChatBinding;

        public ChatListUsers(@NonNull ItemChatBinding itemView) {
            super(itemView.getRoot());
            itemChatBinding = itemView;
        }
    }

    public void updateData(ArrayList<UserChats> userChats) {
        this.mUserChats = userChats;
        notifyDataSetChanged();
    }
}
