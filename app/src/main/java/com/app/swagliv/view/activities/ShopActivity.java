package com.app.swagliv.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.app.swagliv.R;
import com.app.swagliv.databinding.ActivityShopBinding;

public class ShopActivity extends AppCompatActivity {
    private ActivityShopBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_shop);
        mBinding.commonHeader.headerTitle.setText(R.string.portfolio_txt);
    }
}