package com.app.swagliv.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.swagliv.R;
import com.app.swagliv.databinding.ActivityChatBinding;
import com.app.swagliv.twiliovoice.VoiceActivity;
import com.app.swagliv.viewmodel.chats.ChatsViewModel;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding mBinding;
    private ChatsViewModel chatsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        findViewById(R.id.call_btn).setOnClickListener(view -> startActivity(new Intent(ChatActivity.this, VoiceActivity.class)));
    }
}