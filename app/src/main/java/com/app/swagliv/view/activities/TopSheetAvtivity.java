package com.app.swagliv.view.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.swagliv.R;
import com.app.swagliv.databinding.ActivityTopSheetAvtivityBinding;

public class TopSheetAvtivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityTopSheetAvtivityBinding mTopsheetBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopsheetBinding = DataBindingUtil.setContentView(this, R.layout.activity_top_sheet_avtivity);
        mTopsheetBinding.maleUnselected.setOnClickListener(this);
        mTopsheetBinding.femaleUnselectedText.setOnClickListener(this);
        mTopsheetBinding.shemaleUnselectedText.setOnClickListener(this);
        mTopsheetBinding.filterBackBtn.setOnClickListener(this);
        mTopsheetBinding.filterCheckBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.male_unselected:
                mTopsheetBinding.maleUnselected.setBackground(getDrawable(R.drawable.selected));
                break;
            case R.id.female_unselected_text:
                mTopsheetBinding.femaleUnselectedText.setBackground(getDrawable(R.drawable.selected));
                break;
            case R.id.shemale_unselected_text:
                mTopsheetBinding.shemaleUnselectedText.setBackground(getDrawable(R.drawable.selected));
                break;
            case R.id.filter_back_btn:
                finish();
                break;
            case R.id.filter_check_btn:
                finish();
                break;
            default:
                break;
        }
    }
}