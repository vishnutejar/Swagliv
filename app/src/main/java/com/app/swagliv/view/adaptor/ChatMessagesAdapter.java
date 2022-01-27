package com.app.swagliv.view.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.swagliv.R;
import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.databinding.RowLeftChatBinding;
import com.app.swagliv.databinding.RowRightChatBinding;
import com.app.swagliv.model.chat.pojo.chat.Message;
import com.app.swagliv.viewmodel.chats.ChatsViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatMessagesAdapter extends RecyclerView.Adapter<ChatMessagesAdapter.ViewHolder> {

    private ChatsViewModel chatViewModel;
    Context context;
    List<Message> modelChatList;

    public ChatMessagesAdapter(Context context, List<Message> modelChatList, ChatsViewModel chatViewModel) {
        this.context = context;
        this.modelChatList = modelChatList;
        this.chatViewModel = chatViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == AppConstant.CHAT.MESSAGE_RECEIVED_CODE_LEFT) {

            RowRightChatBinding rowRightChatBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.row_right_chat,
                    parent,
                    false
            );

            ViewHolder viewHolderRight = new ViewHolder(rowRightChatBinding);
            return viewHolderRight;
        } else {
            RowLeftChatBinding rowLeftChatBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.row_left_chat,
                    parent,
                    false
            );
            ViewHolder viewHolderLeft = new ViewHolder(rowLeftChatBinding);
            return viewHolderLeft;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Message modelChat = modelChatList.get(position);
        if (holder.rowRightChatBinding != null) {
            holder.rowRightChatBinding.setPosition(position);
            holder.rowRightChatBinding.setComment(modelChat);
            holder.rowRightChatBinding.setViewModel(chatViewModel);
        } else if (holder.rowLeftChatBinding != null) {
            holder.rowLeftChatBinding.setPosition(position);
            holder.rowLeftChatBinding.setComment(modelChat);
            holder.rowLeftChatBinding.setViewModel(chatViewModel);
        }
    }

    @Override
    public int getItemCount() {
        return modelChatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RowRightChatBinding rowRightChatBinding;
        RowLeftChatBinding rowLeftChatBinding;

        public ViewHolder(@NonNull RowRightChatBinding rowRightChatBinding) {
            super(rowRightChatBinding.getRoot());
            this.rowRightChatBinding = rowRightChatBinding;
        }

        public ViewHolder(@NonNull RowLeftChatBinding rowLeftChatBinding) {
            super(rowLeftChatBinding.getRoot());
            this.rowLeftChatBinding = rowLeftChatBinding;
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = modelChatList.get(position);
        if (message.getMessageReceivedCode() == AppConstant.CHAT.MESSAGE_RECEIVED_CODE_RIGHT) {
            return AppConstant.CHAT.MESSAGE_RECEIVED_CODE_RIGHT;
        } else {
            return AppConstant.CHAT.MESSAGE_RECEIVED_CODE_LEFT;
        }
    }

    private String getTime(String timestamp) {
        Long ts = Long.valueOf(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:a");
        String time = sdf.format(new Date(ts));
        return time;
    }
}
