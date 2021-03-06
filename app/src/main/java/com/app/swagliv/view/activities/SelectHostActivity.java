package com.app.swagliv.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.app.common.utils.Utility;
import com.app.swagliv.R;
import com.app.swagliv.databinding.ActivitySearchGifBinding;
import com.app.swagliv.databinding.ActivitySelectHostBinding;
import com.app.swagliv.view.adaptor.SearchGIFAdapter;
import com.app.swagliv.view.adaptor.SelectHostAdapter;

import java.util.ArrayList;

public class SelectHostActivity extends AppCompatActivity {

    ActivitySelectHostBinding selectHostBinding;
    SelectHostAdapter adapter;
    ArrayList<String> hostsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        selectHostBinding = ActivitySelectHostBinding.inflate(getLayoutInflater());
        setContentView(selectHostBinding.getRoot());

        selectHostBinding.commonHeader.headerTitle.setText(R.string.select_your_host);
        selectHostBinding.commonHeader.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        selectHostBinding.btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.showToast(SelectHostActivity.this, "Multiple hosts not accepted in trial version");
            }
        });
        hostsList.add("sfsd");
        hostsList.add("sfsd");
        hostsList.add("sfsd");
        hostsList.add("sfsd");
        hostsList.add("sfsd");
        hostsList.add("sfsd");
        hostsList.add("sfsd");
        hostsList.add("sfsd");
        hostsList.add("sfsd");

        adapter = new SelectHostAdapter(hostsList);
        selectHostBinding.hostRecycler.setAdapter(adapter);

    }
}