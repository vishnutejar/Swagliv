package com.app.swagliv.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.app.swagliv.R;
import com.app.swagliv.databinding.ActivitySearchGifBinding;
import com.app.swagliv.view.adaptor.SearchGIFAdapter;

import java.util.ArrayList;

public class SearchGIFActivity extends AppCompatActivity {

    ActivitySearchGifBinding activitySearchGifBinding;
    SearchGIFAdapter adapter;
    ArrayList<Integer> gifList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySearchGifBinding = ActivitySearchGifBinding.inflate(getLayoutInflater());
        setContentView(activitySearchGifBinding.getRoot());

        //  activitySearchGifBinding.commonHeader.headerTitle.setText(R.string.Search_GIF);

        gifList.add(R.drawable.banana);
        gifList.add(R.drawable.click);
        gifList.add(R.drawable.hand_shake);
        gifList.add(R.drawable.clap);
        gifList.add(R.drawable.calm);
        gifList.add(R.drawable.banana);
        gifList.add(R.drawable.click);
        gifList.add(R.drawable.hand_shake);
        gifList.add(R.drawable.clap);
        gifList.add(R.drawable.calm);
        gifList.add(R.drawable.banana);
        gifList.add(R.drawable.click);
        gifList.add(R.drawable.hand_shake);
        gifList.add(R.drawable.clap);
        gifList.add(R.drawable.calm);

        gifList.add(R.drawable.banana);
        gifList.add(R.drawable.click);
        gifList.add(R.drawable.hand_shake);
        gifList.add(R.drawable.clap);
        gifList.add(R.drawable.calm);
        gifList.add(R.drawable.banana);
        gifList.add(R.drawable.click);
        gifList.add(R.drawable.hand_shake);
        gifList.add(R.drawable.clap);
        gifList.add(R.drawable.calm);
        gifList.add(R.drawable.banana);
        gifList.add(R.drawable.click);
        gifList.add(R.drawable.hand_shake);
        gifList.add(R.drawable.clap);
        gifList.add(R.drawable.calm);

        activitySearchGifBinding.searchGifRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new SearchGIFAdapter(gifList);
        activitySearchGifBinding.searchGifRecycler.setAdapter(adapter);

        activitySearchGifBinding.commonHeader.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}