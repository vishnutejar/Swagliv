package com.app.swagliv.view.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.app.swagliv.R;
import com.app.swagliv.constant.AppInstance;
import com.app.swagliv.databinding.ActivityMatchBinding;
import com.app.swagliv.model.login.pojo.User;
import com.bumptech.glide.Glide;

public class MatchActivity {
    private ActivityMatchBinding matchBinding;
    private User user;
    private Context mContext;
    private User mMatchUser;
    private AlertDialog alertDialog;
    private TextView sendMsgBtn, keepSwipingBtn;
    private OnMatchButtonClickListener mOnMatchButtonClickListener;

    public static MatchActivity newInstance(Context mContext, User matchedUser, OnMatchButtonClickListener onMatchButtonClickListener) {
        MatchActivity dialog = new MatchActivity();
        dialog.mContext = mContext;
        dialog.mMatchUser = matchedUser;
        dialog.mOnMatchButtonClickListener = onMatchButtonClickListener;
        return dialog;
    }

    public void show() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
        mBuilder.setCancelable(false);
        matchBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.activity_match, null, false);

        mBuilder.setView(matchBinding.getRoot());

        sendMsgBtn = matchBinding.sendMsgBtn;
        keepSwipingBtn = matchBinding.keepSwipingBtn;

        user = AppInstance.getAppInstance().getAppUserInstance(mContext);
        Glide.with(mContext).load(user.getProfileImages()).into(matchBinding.profile1);
        Glide.with(mContext).load(mMatchUser.getProfileImages()).into(matchBinding.profile2);
        matchBinding.nameTxt.setText(mMatchUser.getName() + " " + mContext.getString(R.string.like_you_too));

        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        alertDialog = mBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        alertDialog.getWindow().setLayout((6 * width) / 7, WindowManager.LayoutParams.WRAP_CONTENT);
        alertDialog.show();

        setClickListener();
    }

    public void setClickListener() {
        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                mContext.startActivity(new Intent(mContext, ChatActivity.class));

            }
        });
        keepSwipingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                mOnMatchButtonClickListener.onKeepSwiping();

            }
        });
    }

    public interface OnMatchButtonClickListener {
        public void onKeepSwiping();
    }

}