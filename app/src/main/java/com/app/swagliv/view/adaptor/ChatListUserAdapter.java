package com.app.swagliv.view.adaptor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.common.constant.AppCommonConstants;
import com.app.common.preference.AppPreferencesManager;
import com.app.swagliv.R;
import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.databinding.ItemChatBinding;
import com.app.swagliv.model.call.api.TwilioVoiceTokenService;
import com.app.swagliv.model.call.pojo.TwilioVoiceTokenResponseBaseModel;
import com.app.swagliv.model.chat.pojo.chatlist.UserChats;
import com.app.swagliv.network.ApplicationRetrofitServices;
import com.app.swagliv.twiliovoice.VoiceActivity;
import com.app.swagliv.view.activities.ChatActivity;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        Glide.with(context).load(userChats.getProfileImage()).placeholder(R.drawable.ic_blank_user_profile).into(holder.itemChatBinding.imgProfile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChatActivity.class);
                intent.putExtra("Receiver Profile Image",userChats.getProfileImage());
                intent.putExtra("Receiver Id", userChats.getId());
                intent.putExtra("Receiver Name", userChats.getUserName());
                view.getContext().startActivity(intent);
            }
        });

        holder.chat_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTwilioToken(view,userChats.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUserChats.size();
    }


    public class ChatListUsers extends RecyclerView.ViewHolder {
        ItemChatBinding itemChatBinding;
        ImageView chat_call;
        ImageView chat_video;

        public ChatListUsers(@NonNull ItemChatBinding itemView) {
            super(itemView.getRoot());
            itemChatBinding = itemView;
            chat_call = itemView.getRoot().findViewById(R.id.chat_call);
            chat_video = itemView.getRoot().findViewById(R.id.chat_video);

        }
    }

    public void updateData(ArrayList<UserChats> userChats) {
        this.mUserChats = userChats;
        notifyDataSetChanged();
    }


    public void getTwilioToken(View view, String receiverId) {
        String userId = AppPreferencesManager.getString(AppConstant.PREFERENCE_KEYS.CURRENT_USER_ID, view.getContext());
        TwilioVoiceTokenService twilioVoiceTokenService = ApplicationRetrofitServices.getInstance().getTwilioTokenService();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_id", userId);
        Call<TwilioVoiceTokenResponseBaseModel> call = twilioVoiceTokenService.getTwilioAccessToken(jsonObject);
        call.enqueue(new Callback<TwilioVoiceTokenResponseBaseModel>() {
            @Override
            public void onResponse(Call<TwilioVoiceTokenResponseBaseModel> call, Response<TwilioVoiceTokenResponseBaseModel> response) {
                TwilioVoiceTokenResponseBaseModel tokenResponse = response.body();
                if (response.isSuccessful() && tokenResponse != null) {
                    if (tokenResponse.getStatus() == AppCommonConstants.API_SUCCESS_STATUS_CODE) {
                        AppPreferencesManager.putString(AppConstant.PREFERENCE_KEYS.TWILIO_ACCESS_TOKEN, tokenResponse.getAccessToken(), view.getContext());
                        Intent intent = new Intent(view.getContext(), VoiceActivity.class);
                        intent.putExtra("Receiver Id",receiverId);
                        view.getContext().startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<TwilioVoiceTokenResponseBaseModel> call, Throwable t) {
                Log.e("Twilio API error", t.getMessage());
            }
        });
    }

}
