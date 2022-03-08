package com.app.swagliv.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.swagliv.R;
import com.app.swagliv.databinding.ActivityChangePasswordBinding;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityChangePasswordBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        mBinding.submitBtn.setOnClickListener(this);
        mBinding.commonHeader.headerTitle.setText(R.string.change_password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_btn:
        }
    }
}