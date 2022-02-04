package com.app.swagliv.view.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.common.constant.AppCommonConstants;
import com.app.common.utils.Utility;
import com.app.swagliv.R;
import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.databinding.RowLeftChatBinding;
import com.app.swagliv.databinding.RowRightChatBinding;
import com.app.swagliv.model.chat.pojo.chat.Message;
import com.app.swagliv.model.login.pojo.User;
import com.app.swagliv.viewmodel.chats.ChatsViewModel;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMessagesAdapter extends RecyclerView.Adapter<ChatMessagesAdapter.ViewHolder> {

    private ChatsViewModel chatViewModel;
    Context context;
    List<Message> modelChatList;
    private User mUser;

    public ChatMessagesAdapter(Context context, List<Message> modelChatList, ChatsViewModel chatViewModel, User mUser) {
        this.context = context;
        this.modelChatList = modelChatList;
        this.chatViewModel = chatViewModel;
        this.mUser = mUser;
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
        Message message = modelChatList.get(position);
        final Message modelChat = modelChatList.get(position);
        if (holder.rowRightChatBinding != null) {
            holder.rowRightChatBinding.setPosition(position);
            holder.tvChatMessage.setText(message.getMessage());
            holder.tvChatTimestamp.setText(Utility.convertDate(message.getTime(), AppCommonConstants.API_DATE_FORMAT,AppCommonConstants.DATE_FORMAT_SHOW_UI));
/*            holder.rowRightChatBinding.setComment(modelChat);
            holder.rowRightChatBinding.setViewModel(chatViewModel);*/
        } else if (holder.rowLeftChatBinding != null) {
            holder.rowLeftChatBinding.setPosition(position);
            holder.tvChatMessage.setText(message.getMessage());
            //holder.tvChatTimestamp.setText(message.getTime());
            holder.tvChatTimestamp.setText(Utility.convertDate(message.getTime(), AppCommonConstants.API_DATE_FORMAT,AppCommonConstants.DATE_FORMAT_SHOW_UI));
            Glide.with(context).load("https://english.cdn.zeenews.com/sites/default/files/2021/11/15/987821-ravi-shastri.jpg").placeholder(R.drawable.ic_blank_user_profile).into(holder.ivChatImage);
            /*holder.rowLeftChatBinding.setComment(modelChat);
            holder.rowLeftChatBinding.setViewModel(chatViewModel);*/
        }
    }

    @Override
    public int getItemCount() {
        return modelChatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvChatMessage;
        TextView tvChatTimestamp;
        ImageView ivChatImage;
        RowRightChatBinding rowRightChatBinding;
        RowLeftChatBinding rowLeftChatBinding;

        public ViewHolder(@NonNull RowRightChatBinding rowRightChatBinding) {
            super(rowRightChatBinding.getRoot());
            this.rowRightChatBinding = rowRightChatBinding;
            tvChatMessage = rowRightChatBinding.getRoot().findViewById(R.id.tvChatMessage);
            tvChatTimestamp = rowRightChatBinding.getRoot().findViewById(R.id.tvChatTimestamp);
        }

        public ViewHolder(@NonNull RowLeftChatBinding rowLeftChatBinding) {
            super(rowLeftChatBinding.getRoot());
            this.rowLeftChatBinding = rowLeftChatBinding;
            tvChatMessage = rowLeftChatBinding.getRoot().findViewById(R.id.tvChatMessage);
            tvChatTimestamp = rowLeftChatBinding.getRoot().findViewById(R.id.tvChatTimestamp);
            ivChatImage = rowLeftChatBinding.getRoot().findViewById(R.id.ivChatImage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = modelChatList.get(position);
        if (!mUser.getId().equalsIgnoreCase(message.getSenderId())) {
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
