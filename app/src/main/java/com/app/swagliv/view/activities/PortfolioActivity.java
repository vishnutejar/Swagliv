package com.app.swagliv.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.app.swagliv.R;
import com.app.swagliv.databinding.ActivityChangePasswordBinding;
import com.app.swagliv.databinding.ActivityPortfolioBinding;

public class PortfolioActivity extends AppCompatActivity {
    private ActivityPortfolioBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_portfolio);
        mBinding.commonHeader.headerTitle.setText(R.string.portfolio_txt);
    }
}