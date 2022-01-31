package com.app.swagliv.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

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

    private static final int TYPING_TIMER_LENGTH = 600;

    private ActivityChatBinding mBinding;
    private ChatsViewModel chatsViewModel;
    private Socket mSocket;
    private User mUser;
    private Message message;

    private ChatMessagesAdapter chatMessagesAdapter;
    private ArrayList<Message> messageArrayList = new ArrayList<>();
    private String mReceivedConversationId;

    private Handler mTypingHandler = new Handler();
    private boolean mTyping = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat);

        findViewById(R.id.call_btn).setOnClickListener(view -> getTwilioToken());
        mUser = AppInstance.getAppInstance().getAppUserInstance(this);
        mSocket = SocketChatApplication.doConnect();

        mSocket.on(AppConstant.CHAT.SET_UP_CONVERSATION_STATUS, onConversationStatus);
        mSocket.on(AppConstant.CHAT.GET_MESSAGE, onMessage);
        mSocket.on(AppConstant.CHAT.TYPING_STATUS, onTyping);


        mBinding.ivSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSend();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mBinding.chatRecyclerView.setLayoutManager(linearLayoutManager);
        chatMessagesAdapter = new ChatMessagesAdapter(this, messageArrayList, chatsViewModel, mUser);
        mBinding.chatRecyclerView.setAdapter(chatMessagesAdapter);

        doConfigureSocket();

        mBinding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBinding.inputMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mTyping) {
                    mTyping = true;
                    JSONObject data = new JSONObject();
                    try {
                        data.put("senderId", mUser.getId());
                        data.put("receiverId", "61d9697cc828379036175d9b");
                        mSocket.emit(AppConstant.CHAT.IS_TYPING, data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mTypingHandler.removeCallbacks(onTypingTimeout);
                mTypingHandler.postDelayed(onTypingTimeout, TYPING_TIMER_LENGTH);

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void doConfigureSocket() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("senderId", mUser.getId());
            jsonObject.put("receiverId", "61d9697cc828379036175d9b");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Utility.printLogs("doConfigureSocket", jsonObject.toString());

        mSocket.emit(AppConstant.CHAT.SET_UP_CONVERSATION_ID, jsonObject);

    }

    private void attemptSend() {
        mTyping = false;
        String message = mBinding.inputMessage.getText().toString().trim();
        if (message.isEmpty()) {
            mBinding.inputMessage.requestFocus();
            return;
        }

        mBinding.inputMessage.setText("");

        Message messageObj = new Message();
        messageObj.setConversationId(mReceivedConversationId);
        messageObj.setMessageType(AppConstant.CHAT.MESSAGE_TYPE_TEXT);
        messageObj.setReceiverId("61d9697cc828379036175d9b");
        messageObj.setSenderId(mUser.getId());
        messageObj.setMessage(message);
        messageObj.setTime(System.currentTimeMillis());


        JSONObject ob = new JSONObject();
        try {
            ob.put("conversationId", mReceivedConversationId);
            ob.put("senderId", mUser.getId());
            ob.put("receiverId", "61d9697cc828379036175d9b");
            ob.put("messageType", 101);
            ob.put("message", message);
            mSocket.emit(AppConstant.CHAT.SEND_MESSAGE, ob);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        addMessage(messageObj);

        // perform the sending message attempt.
        Utility.printLogs("sendMessage", new Gson().toJson(ob));

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


    private Emitter.Listener onConversationStatus = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.e("onConversationStatus", "" + args);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("onConversationStatus", "" + args[0]);
                    JSONObject jsonObject = (JSONObject) args[0];
                    try {
                        mReceivedConversationId = (String) jsonObject.get("conversationId");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    private Emitter.Listener onMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
//            Utility.showToast(ChatActivity.this,"onMessage :" +args[0]);
            Log.e("onMessage", "" + args);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    Log.e("onMessage", "" + jsonObject);
                    Message message = new Gson().fromJson(String.valueOf(jsonObject), Message.class);
                    Log.e("message", "" + message);
                    if (!mUser.getId().equalsIgnoreCase(message.getSenderId()))
                        addMessage(message);
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
                    Log.e("onTyping", "" + args[0]);
                    JSONObject data = (JSONObject) args[0];

                }
            });
        }
    };

    private Runnable onTypingTimeout = new Runnable() {
        @Override
        public void run() {
            if (!mTyping) return;

            mTyping = false;
            /*try {
                JSONObject data = new JSONObject();
                data.put("username", mUsername);
                // perform the sending message attempt.
                mSocket.emit("stop typing", data);
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        }
    };


    private void scrollToBottom() {
        mBinding.chatRecyclerView.scrollToPosition(chatMessagesAdapter.getItemCount() - 1);
    }
}