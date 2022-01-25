package com.app.swagliv.view.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.swagliv.R;
import com.app.swagliv.databinding.ActivityWalletBinding;

public class WalletActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityWalletBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_wallet);
        mBinding.commonHeader.backBtn.setOnClickListener(this);
        mBinding.commonHeader.headerTitle.setText(R.string.title_wallet);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
        }

    }
}