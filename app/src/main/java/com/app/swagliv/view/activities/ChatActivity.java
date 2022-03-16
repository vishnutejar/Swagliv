package com.app.swagliv.view.activities;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.LayoutDirection;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.swagliv.R;
import com.app.swagliv.databinding.ActivityChatBinding;
import com.app.swagliv.databinding.RequestDialogBinding;
import com.app.swagliv.view.adaptor.ChatCommentsAdapter;

import java.util.ArrayList;


public class ChatActivity extends AppCompatActivity {
    private ChatCommentsAdapter adapter;
    private ArrayList<String> connectionsList = new ArrayList<>();
    ActivityChatBinding activityChatBinding;
    AlertDialog dialog;
    boolean isFirstGrid = false;
    int totalConnectedUser = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityChatBinding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(activityChatBinding.getRoot());

        addPersonToChat();


        activityChatBinding.commentRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        activityChatBinding.commentRecycleView.setAdapter(adapter);

        activityChatBinding.imgAddConnections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPersonToChat();
            }
        });

        activityChatBinding.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heartOnClick();
            }
        });

        activityChatBinding.coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRequestDialog();
            }
        });
        activityChatBinding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        activityChatBinding.etComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    postComment();
                    handled = true;
                }
                return handled;
            }

            private void postComment() {
            }
        });
    }

    private void addPersonToChat() {

        LinearLayout parentLayout = findViewById(R.id.streamingContainer);
        int count = parentLayout.getChildCount();


        if (totalConnectedUser < 2) {
            parentLayout.setWeightSum(++totalConnectedUser);
            activityChatBinding.streamingContainer.addView(getNewHorizontalView());
        } else if (totalConnectedUser == 2 & !isFirstGrid) {

            parentLayout.setWeightSum(2);
            LinearLayout horizontalt = new LinearLayout(ChatActivity.this);
            horizontalt.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, 0, 2);
            layoutParams.weight = 1;
            horizontalt.setLayoutParams(layoutParams);

            horizontalt.addView(getNewVerticalView());
            horizontalt.addView(getNewVerticalView());

            parentLayout.removeAllViews();
            parentLayout.addView(horizontalt);
            parentLayout.addView(getNewHorizontalView());

            isFirstGrid = true;
            totalConnectedUser++;
        } else {
            //  Even persons
            if (totalConnectedUser % 2 == 0) {
                parentLayout.setWeightSum(++count);

                LinearLayout horizontalt = new LinearLayout(ChatActivity.this);
                horizontalt.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, 0, 2);
                layoutParams.weight = 1;
                horizontalt.setLayoutParams(layoutParams);

                horizontalt.addView(getNewVerticalView());
                parentLayout.addView(horizontalt);
                totalConnectedUser++;
            }
            //odd persons
            else {
                parentLayout.removeViewAt(count - 1);
                parentLayout.setWeightSum(count);

                LinearLayout horizontalt = new LinearLayout(ChatActivity.this);
                horizontalt.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, 0, 2);
                layoutParams.weight = 1;
                horizontalt.setLayoutParams(layoutParams);

                horizontalt.addView(getNewVerticalView());
                horizontalt.addView(getNewVerticalView());

                parentLayout.addView(horizontalt);
                totalConnectedUser++;
            }
        }
    }

    View getNewHorizontalView() {
        ImageView imageView = new ImageView(ChatActivity.this);
        imageView.setImageResource(R.drawable.girl_bg_image);

        LinearLayout.LayoutParams imageLayoutParam = new LinearLayout.LayoutParams(MATCH_PARENT, 0);
        imageLayoutParam.weight = 1f;
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(imageLayoutParam);

        return imageView;
    }


    View getNewVerticalView() {
        ImageView imageView = new ImageView(ChatActivity.this);
        imageView.setImageResource(R.drawable.girl_bg_image);

        LinearLayout.LayoutParams imageLayoutParam = new LinearLayout.LayoutParams(0, MATCH_PARENT);
        imageLayoutParam.weight = 1f;
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(imageLayoutParam);

        return imageView;
    }


    public void openRequestDialog() {
        RequestDialogBinding dialogBinding = RequestDialogBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
        builder.setView(dialogBinding.getRoot());
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.TOP);
        dialog.getWindow().getAttributes().y = 90;
        dialog.show();

        dialogBinding.btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialogBinding.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    void heartOnClick() {

        // Disable clips on all parent generations
        disableAllParentsClip();

        // Create clone
        ImageView imageClone = cloneImage();

        // Animate
        animateFlying(imageClone);
        animateFading(imageClone);
    }

    private void disableAllParentsClip() {
        LinearLayout parent = activityChatBinding.cloneContainer;
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            child.setEnabled(false);
        }
    }

    private ImageView cloneImage() {
        ImageView clone = new ImageView(ChatActivity.this);
        clone.setLayoutParams(activityChatBinding.heart.getLayoutParams());
        clone.setImageDrawable(activityChatBinding.heart.getDrawable());
        activityChatBinding.cloneContainer.addView(clone);
        return clone;
    }


    private void animateFlying(ImageView image) {
        ObjectAnimator.ofFloat(image, View.TRANSLATION_Y, 800, -290f).setDuration(1800).start();
    }

    private void animateFading(ImageView image) {
        image.animate()
                .alpha(0f)
                .setDuration(2000).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                activityChatBinding.cloneContainer.removeView(image);
            }
        });
    }
}


//public class ChatActivity extends AppCompatActivity implements APIResponseHandler {
//    private static final String TAG = "ChatActivity";
//
//    private static final int TYPING_TIMER_LENGTH = 600;
//
//    private ActivityChatBinding mBinding;
//    private ChatsViewModel chatsViewModel;
//    private Socket mSocket;
//    private User mUser;
//    private Message message;
//
//    private ChatMessagesAdapter chatMessagesAdapter;
//    private ArrayList<Message> messageArrayList = new ArrayList<>();
//    private String mReceivedConversationId;
//
//    private Handler mTypingHandler = new Handler();
//    private boolean mTyping = false;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
//
//        Intent intent = getIntent();
//        String receiver_profile_img = intent.getStringExtra("Receiver Profile Image");
//        String receiver_name = intent.getStringExtra("Receiver Name");
//        String receiver_id = intent.getStringExtra("Receiver Id");
//        TextView tvUserName = findViewById(R.id.tvUserName);
//        tvUserName.setText(receiver_name);
//        ImageView imageView = findViewById(R.id.userProfileImage);
//        Glide.with(this).load(receiver_profile_img).placeholder(R.drawable.ic_blank_user_profile).into(imageView);
//
//        findViewById(R.id.call_btn).setOnClickListener(view -> getTwilioToken(receiver_id,receiver_name,receiver_profile_img));
//        mUser = AppInstance.getAppInstance().getAppUserInstance(this);
//        mSocket = SocketChatApplication.doConnect();
//
//        mSocket.on(AppConstant.CHAT.SET_UP_CONVERSATION_STATUS, onConversationStatus);
//        mSocket.on(AppConstant.CHAT.GET_MESSAGE, onMessage);
//        mSocket.on(AppConstant.CHAT.TYPING_STATUS, onTyping);
//
//
//        mBinding.ivSendMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                attemptSend();
//            }
//        });
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setStackFromEnd(true);
//        mBinding.chatRecyclerView.setLayoutManager(linearLayoutManager);
//        /*chatMessagesAdapter = new ChatMessagesAdapter(this, messageArrayList, chatsViewModel, mUser);
//        mBinding.chatRecyclerView.setAdapter(chatMessagesAdapter);*/
//
//        chatsViewModel = new ViewModelProvider(this).get(ChatsViewModel.class);
//        chatsViewModel.mutableLiveData.observe(this, new Observer<APIResponse>() {
//            @Override
//            public void onChanged(APIResponse apiResponse) {
//                onAPIResponseHandler(apiResponse);
//            }
//        });
//        //chatsViewModel.getPreviousChat("61fb782997cb15e782075e4d","61fba42e6edf1096191d0a4d",AppCommonConstants.API_REQUEST.REQUEST_ID_1001);
//
//        doConfigureSocket();
//
//        mBinding.backBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        mBinding.inputMessage.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (!mTyping) {
//                    mTyping = true;
//                    JSONObject data = new JSONObject();
//                    try {
//                        data.put("senderId", mUser.getId());
//                        data.put("receiverId", "61d9697cc828379036175d9b");
//                        mSocket.emit(AppConstant.CHAT.IS_TYPING, data);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                mTypingHandler.removeCallbacks(onTypingTimeout);
//                mTypingHandler.postDelayed(onTypingTimeout, TYPING_TIMER_LENGTH);
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//
//    }
//
//    private void doConfigureSocket() {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("senderId", mUser.getId());
//            jsonObject.put("receiverId", "61d9697cc828379036175d9b");
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        Utility.printLogs("doConfigureSocket", jsonObject.toString());
//
//        mSocket.emit(AppConstant.CHAT.SET_UP_CONVERSATION_ID, jsonObject);
//
//    }
//
//    private void attemptSend() {
//        mTyping = false;
//        String message = mBinding.inputMessage.getText().toString().trim();
//        if (message.isEmpty()) {
//            mBinding.inputMessage.requestFocus();
//            return;
//        }
//
//        mBinding.inputMessage.setText("");
//
//        Message messageObj = new Message();
//        messageObj.setConversationId(mReceivedConversationId);
//        messageObj.setMessageType(AppConstant.CHAT.MESSAGE_TYPE_TEXT);
//        messageObj.setReceiverId("61d9697cc828379036175d9b");
//        messageObj.setSenderId(mUser.getId());
//        messageObj.setMessage(message);
//        messageObj.setTime(String.valueOf(System.currentTimeMillis()));
//
//
//        JSONObject ob = new JSONObject();
//        try {
//            ob.put("conversationId", mReceivedConversationId);
//            ob.put("senderId", mUser.getId());
//            ob.put("receiverId", "61d9697cc828379036175d9b");
//            ob.put("messageType", 101);
//            ob.put("message", message);
//            mSocket.emit(AppConstant.CHAT.SEND_MESSAGE, ob);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//        addMessage(messageObj);
//
//        // perform the sending message attempt.
//        Utility.printLogs("sendMessage", new Gson().toJson(ob));
//
//    }
//
//    private void addMessage(Message messageObj) {
//        messageArrayList.add(messageObj);
//        scrollToBottom();
//    }
//
//
//    public void getTwilioToken(String receiver_id, String receiver_name, String receiver_image ) {
//        String userId = AppPreferencesManager.getString(AppConstant.PREFERENCE_KEYS.CURRENT_USER_ID, this);
//        TwilioVoiceTokenService twilioVoiceTokenService = ApplicationRetrofitServices.getInstance().getTwilioTokenService();
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("_id", userId);
//        Call<TwilioVoiceTokenResponseBaseModel> call = twilioVoiceTokenService.getTwilioAccessToken(jsonObject);
//        call.enqueue(new Callback<TwilioVoiceTokenResponseBaseModel>() {
//            @Override
//            public void onResponse(Call<TwilioVoiceTokenResponseBaseModel> call, Response<TwilioVoiceTokenResponseBaseModel> response) {
//                TwilioVoiceTokenResponseBaseModel tokenResponse = response.body();
//                if (response.isSuccessful() && tokenResponse != null) {
//                    if (tokenResponse.getStatus() == AppCommonConstants.API_SUCCESS_STATUS_CODE) {
//                        AppPreferencesManager.putString(AppConstant.PREFERENCE_KEYS.TWILIO_ACCESS_TOKEN, tokenResponse.getAccessToken(), ChatActivity.this);
//                        Intent intent = new Intent(ChatActivity.this, VoiceActivity.class);
//                        intent.putExtra("Receiver Id",receiver_id);
//                        intent.putExtra("Receiver Name",receiver_name);
//                        intent.putExtra("Receiver Image",receiver_image);
//                        startActivity(intent);
//                        //startActivity(new Intent(ChatActivity.this, VoiceActivity.class));
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<TwilioVoiceTokenResponseBaseModel> call, Throwable t) {
//                Log.e("Twilio API error", t.getMessage());
//            }
//        });
//    }
//
//
//    private Emitter.Listener onConversationStatus = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            Log.e("onConversationStatus", "" + args);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Log.e("onConversationStatus", "" + args[0]);
//                    JSONObject jsonObject = (JSONObject) args[0];
//                    try {
//                        mReceivedConversationId = (String) jsonObject.get("conversationId");
//                        mReceivedConversationId = "61fcd942a682feed228235ea";
//                        chatsViewModel.getPreviousChat("61fb782997cb15e782075e4d",mReceivedConversationId,AppCommonConstants.API_REQUEST.REQUEST_ID_1001);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }
//    };
//    private Emitter.Listener onMessage = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
////            Utility.showToast(ChatActivity.this,"onMessage :" +args[0]);
//            Log.e("onMessage", "" + args);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    JSONObject jsonObject = (JSONObject) args[0];
//                    Log.e("onMessage", "" + jsonObject);
//                    mBinding.userStatus.setText("");
//                    Message message = new Gson().fromJson(String.valueOf(jsonObject), Message.class);
//                    Log.e("message", "" + message);
//                    if (!mUser.getId().equalsIgnoreCase(message.getSenderId()))
//                        addMessage(message);
//                }
//            });
//
//        }
//    };
//
//    private Emitter.Listener onTyping = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Log.e("onTyping", "" + args[0]);
//                    JSONObject data = (JSONObject) args[0];
//
//                    mBinding.userStatus.setText("typing..");
//                }
//            });
//        }
//    };
//
//    private Runnable onTypingTimeout = new Runnable() {
//        @Override
//        public void run() {
//            if (!mTyping) return;
//
//            mTyping = false;
//            /*try {
//                JSONObject data = new JSONObject();
//                data.put("username", mUsername);
//                // perform the sending message attempt.
//                mSocket.emit("stop typing", data);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }*/
//        }
//    };
//
//
//    private void scrollToBottom() {
//        mBinding.chatRecyclerView.scrollToPosition(chatMessagesAdapter.getItemCount() - 1);
//    }
//
//    @Override
//    public void onAPIResponseHandler(APIResponse apiResponse) {
//        switch (apiResponse.status) {
//            case LOADING:
//                break;
//            case SUCCESS:
//                switch (apiResponse.requestID) {
//                    case AppCommonConstants.API_REQUEST.REQUEST_ID_1001:
//                        messageArrayList = (ArrayList<Message>) apiResponse.data;
//                        chatMessagesAdapter = new ChatMessagesAdapter(this, messageArrayList, chatsViewModel, mUser);
//                        mBinding.chatRecyclerView.setAdapter(chatMessagesAdapter);
//                        //chatMessagesAdapter.updateData(messageArrayList);
//                        //return mBinding.getRoot();
//
//                       /* userChatsArrayList = (ArrayList<UserChats>) apiResponse.data;
//                        mAdapter = new ChatListUserAdapter(getContext(), userChatsArrayList);
//                        mBinding.chatView.setAdapter(mAdapter);
//                        mAdapter.updateData(userChatsArrayList);*/
//                }
//                break;
//            case ERROR:
//                Utility.showToast(getApplicationContext(), getString(R.string.api_failure_error_msg));
//                break;
//        }
//    }
//
//}