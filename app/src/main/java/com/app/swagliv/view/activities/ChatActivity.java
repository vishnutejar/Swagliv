package com.app.swagliv.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.common.constant.AppCommonConstants;
import com.app.common.preference.AppPreferencesManager;
import com.app.swagliv.R;
import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.databinding.ActivityChatBinding;
import com.app.swagliv.model.call.api.TwilioTokenService;
import com.app.swagliv.model.call.pojo.TokenResponseBaseModel;
import com.app.swagliv.network.ApplicationRetrofitServices;
import com.app.swagliv.twiliovoice.VoiceActivity;
import com.app.swagliv.viewmodel.chats.ChatsViewModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding mBinding;
    private ChatsViewModel chatsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        findViewById(R.id.call_btn).setOnClickListener(view -> getTwilioToken());
    }

    public void getTwilioToken() {
        String userId = AppPreferencesManager.getString(AppConstant.PREFERENCE_KEYS.CURRENT_USER_ID, this);
        TwilioTokenService twilioTokenService = ApplicationRetrofitServices.getInstance().getTwilioTokenService();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_id", userId);
        Call<TokenResponseBaseModel> call = twilioTokenService.getTwilioAccessToken(jsonObject);
        call.enqueue(new Callback<TokenResponseBaseModel>() {
            @Override
            public void onResponse(Call<TokenResponseBaseModel> call, Response<TokenResponseBaseModel> response) {
                TokenResponseBaseModel tokenResponse = response.body();
                if (response.isSuccessful() && tokenResponse != null) {
                    if (tokenResponse.getStatus() == AppCommonConstants.API_SUCCESS_STATUS_CODE) {
                        AppPreferencesManager.putString(AppConstant.PREFERENCE_KEYS.TWILIO_ACCESS_TOKEN, tokenResponse.getAccessToken(), ChatActivity.this);
                        startActivity(new Intent(ChatActivity.this, VoiceActivity.class));
                    }
                }
            }

            @Override
            public void onFailure(Call<TokenResponseBaseModel> call, Throwable t) {
                Log.e("Twilio API error", t.getMessage());
            }
        });
    }
}