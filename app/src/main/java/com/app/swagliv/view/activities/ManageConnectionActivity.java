package com.app.swagliv.view.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.swagliv.R;
import com.app.swagliv.databinding.ActivityManageConnectionBinding;
import com.app.swagliv.view.adaptor.ManageConnectionAdapter;

import java.util.ArrayList;

public class ManageConnectionActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityManageConnectionBinding mBinding;
    private ManageConnectionAdapter adapter;
    private ArrayList<String> connectionsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_manage_connection);
        mBinding.commonHeader.backBtn.setOnClickListener(this);
        mBinding.commonHeader.headerTitle.setText(R.string.manage_connection);

        //Fill Data
        connectionsList.add("Test User");
        connectionsList.add("Test User With Long name");
        connectionsList.add("Test User with extra Long Name for texting length");
        connectionsList.add("Test User");
        connectionsList.add("Test User");
        connectionsList.add("Test User");
        connectionsList.add("Test User");
        connectionsList.add("Test User");
        connectionsList.add("Test User");
        connectionsList.add("Test User");
        connectionsList.add("Test User");
        connectionsList.add("Test User");
        connectionsList.add("Test User");
        connectionsList.add("Test User");

        mBinding.connectionRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new ManageConnectionAdapter(connectionsList);
        mBinding.connectionRecycleView.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
        }
    }
}