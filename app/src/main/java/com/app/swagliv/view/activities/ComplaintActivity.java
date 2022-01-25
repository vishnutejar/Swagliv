package com.app.swagliv.view.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.swagliv.R;
import com.app.swagliv.databinding.AppComplaintBinding;

public class ComplaintActivity extends AppCompatActivity implements View.OnClickListener {

    private AppComplaintBinding viewDataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.app_complaint);
        viewDataBinding.backHedder.backBtn.setOnClickListener(this);
        viewDataBinding.backHedder.headerTitle.setText(R.string.complaint);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
        }

    }
}