package com.app.swagliv.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.app.swagliv.databinding.ActivityUserRequestsBinding;
import com.app.swagliv.view.adaptor.UserRequestsAdapter;

import java.util.ArrayList;

public class UserRequestsActivity extends AppCompatActivity {

    private UserRequestsAdapter adapter;
    private ArrayList<String> connectionsList = new ArrayList<>();
    ActivityUserRequestsBinding activityUserRequestsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityUserRequestsBinding = ActivityUserRequestsBinding.inflate(getLayoutInflater());
        setContentView(activityUserRequestsBinding.getRoot());

        //Fill Data
        connectionsList.add("Test User");
        connectionsList.add("Test User With Long name");
        connectionsList.add("Test User with extra Long Name for testing length");
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

        activityUserRequestsBinding.userRequestRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new UserRequestsAdapter(connectionsList);
        activityUserRequestsBinding.userRequestRecyclerView.setAdapter(adapter);
    }
}