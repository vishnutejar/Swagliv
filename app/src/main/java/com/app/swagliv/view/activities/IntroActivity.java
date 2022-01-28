package com.app.swagliv.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager2.widget.ViewPager2;

import com.app.swagliv.R;
import com.app.swagliv.databinding.ActivityIntroBinding;
import com.app.swagliv.model.IntroModel;
import com.app.swagliv.view.adaptor.SliderAdapter;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;

public class IntroActivity extends AppCompatActivity {

    // Widgets
    private WormDotsIndicator wormDotsIndicator;
    private ViewPager2 mViewPager2;

    // variables
    private SliderAdapter mSliderAdapter;
    private ArrayList<IntroModel> mIntroModelList = new ArrayList<>();
    private ActivityIntroBinding mBinding;
    private int mCurrentPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_intro);

        //-------------
        mViewPager2 = findViewById(R.id.view_pager2);
        mIntroModelList.add(new IntroModel(mCurrentPosition, getString(R.string.title1)));
        mIntroModelList.add(new IntroModel(mCurrentPosition, getString(R.string.title2)));
        mIntroModelList.add(new IntroModel(mCurrentPosition, getString(R.string.title3)));
        mIntroModelList.add(new IntroModel(mCurrentPosition, getString(R.string.title4)));

        mSliderAdapter = new SliderAdapter(mIntroModelList, IntroActivity.this);
        mViewPager2.setAdapter(mSliderAdapter);
        wormDotsIndicator = (WormDotsIndicator) findViewById(R.id.wormDotsIndicator);
        wormDotsIndicator.setViewPager2(mViewPager2);

        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mCurrentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        mBinding.skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IntroActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

        mBinding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPosition >= 3)
                    startActivity(new Intent(IntroActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                else
                    mViewPager2.setCurrentItem(mCurrentPosition + 1);
            }
        });

    }
}