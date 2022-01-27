package com.app.swagliv.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.common.constant.AppCommonConstants;
import com.app.common.preference.AppPreferencesManager;
import com.app.common.utils.Utility;
import com.app.swagliv.R;
import com.app.swagliv.SocketChatApplication;
import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.constant.AppInstance;
import com.app.swagliv.databinding.ActivityChatBinding;
import com.app.swagliv.model.call.api.TwilioVoiceTokenService;
import com.app.swagliv.model.call.pojo.TwilioVoiceTokenResponseBaseModel;
import com.app.swagliv.model.chat.pojo.chat.Message;
import com.app.swagliv.model.login.pojo.User;
import com.app.swagliv.network.ApplicationRetrofitServices;
import com.app.swagliv.twiliovoice.VoiceActivity;
import com.app.swagliv.view.adaptor.ChatMessagesAdapter;
import com.app.swagliv.viewmodel.chats.ChatsViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";

    private ActivityChatBinding mBinding;
    private ChatsViewModel chatsViewModel;
    private Socket mSocket;
    private User mUser;

    private ChatMessagesAdapter chatMessagesAdapter;
    private ArrayList<Message> messageArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        findViewById(R.id.call_btn).setOnClickListener(view -> getTwilioToken());
        mUser = AppInstance.getAppInstance().getAppUserInstance(this);
        SocketChatApplication socketChatApplication = SocketChatApplication.getSocketChatApplication();
        mSocket = socketChatApplication.getSocket();
        mSocket.on(AppConstant.CHAT.ADD_USER, onNewMessage);
        mSocket.on(AppConstant.CHAT.USER_STATUS, onTyping);
        mSocket.connect();
        mBinding.ivSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSend();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mBinding.chatRecyclerView.setLayoutManager(linearLayoutManager);
        chatMessagesAdapter = new ChatMessagesAdapter(this, messageArrayList, chatsViewModel);
        mBinding.chatRecyclerView.setAdapter(chatMessagesAdapter);

    }

    private void attemptSend() {

        Message messageObj = new Message();
        messageObj.setMessage(mBinding.inputMessage.getText().toString());
        messageObj.setMessageId("" + System.currentTimeMillis());
        messageObj.setMessageId("" + System.currentTimeMillis());
        messageObj.setMessageType(0);
        messageObj.setMessageReceivedCode(0);

        addMessage(messageObj);

        // perform the sending message attempt.
        Emitter addUser = mSocket.emit(AppConstant.CHAT.ADD_USER, new Gson().toJson(messageObj));

    }

    private void addMessage(Message messageObj) {
        messageArrayList.add(messageObj);
        scrollToBottom();
    }


    public void getTwilioToken() {
        String userId = AppPreferencesManager.getString(AppConstant.PREFERENCE_KEYS.CURRENT_USER_ID, this);
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
                        AppPreferencesManager.putString(AppConstant.PREFERENCE_KEYS.TWILIO_ACCESS_TOKEN, tokenResponse.getAccessToken(), ChatActivity.this);
                        startActivity(new Intent(ChatActivity.this, VoiceActivity.class));
                    }
                }
            }

            @Override
            public void onFailure(Call<TwilioVoiceTokenResponseBaseModel> call, Throwable t) {
                Log.e("Twilio API error", t.getMessage());
            }
        });
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.e("test", "" + args);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("test", "" + args[0]);

                }
            });
        }
    };

    private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Message messageObj = new Message();
                    messageObj.setMessage(mBinding.inputMessage.getText().toString());
                    messageObj.setMessageId("" + System.currentTimeMillis());
                    messageObj.setTime(System.currentTimeMillis());
                    messageObj.setMessageType(1);
                    messageObj.setMessageReceivedCode(1);
                    // JsonObject data = (JsonObject) args[0];
                    // Message message = new Gson().fromJson(data, Message.class);
                    Utility.printLogs("dataa", messageObj.toString());
                    messageArrayList.add(messageObj);
                    scrollToBottom();
                }
            });
        }
    };

    private void scrollToBottom() {
        mBinding.chatRecyclerView.scrollToPosition(chatMessagesAdapter.getItemCount() - 1);
    }
}