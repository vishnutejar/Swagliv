package com.app.swagliv.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.swagliv.R;
import com.app.swagliv.databinding.ActivityHelpAndFaqBinding;

public class HelpAndFaqActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityHelpAndFaqBinding viewDataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_help_and_faq);

        viewDataBinding.feedBack.setOnClickListener(this);
        viewDataBinding.complaint.setOnClickListener(this);
        viewDataBinding.header.backBtn.setOnClickListener(this);
        viewDataBinding.header.headerTitle.setText(R.string.title_help);
    }

    @Override
    public void onClick(View view) {
        Intent i = null;
        switch (view.getId()) {
            case R.id.feedBack:
                i = new Intent(this, FeedbackActivity.class);
                startActivity(i);
                break;
            case R.id.complaint:
                i = new Intent(this, ComplaintActivity.class);
                startActivity(i);
                break;
            case R.id.back_btn:
                finish();
                break;
        }
    }
}