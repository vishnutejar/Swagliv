package com.app.swagliv.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.swagliv.R;
import com.app.swagliv.constant.AppInstance;
import com.app.swagliv.databinding.ActivitySideBarScreenBinding;
import com.app.swagliv.model.login.pojo.User;
import com.bumptech.glide.Glide;

public class SideBarActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivitySideBarScreenBinding mBinding;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_side_bar_screen);

        mBinding.purchaseHistory.setOnClickListener(this);
        mBinding.help.setOnClickListener(this);
        mBinding.search.setOnClickListener(this);
        mBinding.goPremium.setOnClickListener(this);
        mBinding.editProfile.setOnClickListener(this);

        mUser = AppInstance.getAppInstance().getAppUserInstance(this);
        mBinding.nameText.setText(mUser.getName());
        Glide.with(this).load(mUser.getProfileImages()).into(mBinding.imageIcon);
    }

    @Override
    public void onClick(View view) {
        Intent i = null;
        switch (view.getId()) {
            case R.id.purchaseHistory:
                i = new Intent(this, PurchaseHistoryActivity.class);
                startActivity(i);
                break;
            case R.id.help:
                i = new Intent(this, HelpAndFaqActivity.class);
                startActivity(i);
                break;
            case R.id.search:
                i = new Intent(this, SearchCrushActivity.class);
                startActivity(i);
                break;
            case R.id.goPremium:
                i = new Intent(this, SubscriptionActivity.class);
                startActivity(i);
                break;
            case R.id.editProfile:
                break;
        }
    }
}