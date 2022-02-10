package com.app.swagliv.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.app.common.constant.AppCommonConstants;
import com.app.common.interfaces.APIResponseHandler;
import com.app.common.utils.Utility;
import com.app.common.utils.api_response_handler.APIResponse;
import com.app.swagliv.R;
import com.app.swagliv.constant.AppInstance;
import com.app.swagliv.databinding.ActivitySearchCrushBinding;
import com.app.swagliv.model.login.pojo.User;
import com.app.swagliv.view.adaptor.SearchCrushAdapter;
import com.app.swagliv.viewmodel.searchCrush.repository.SearchCrushViewModel;

import java.util.ArrayList;

public class SearchCrushActivity extends AppCompatActivity implements APIResponseHandler, View.OnClickListener {
    private ActivitySearchCrushBinding mBinding;
    private SearchCrushAdapter mAdapter;
    private ArrayList<User> crushList;
    private SearchCrushViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_crush);
        mBinding.commonHeader.backBtn.setOnClickListener(this);
        mBinding.commonHeader.headerTitle.setText(R.string.txt_search_crush);
        mViewModel = new ViewModelProvider(this).get(SearchCrushViewModel.class);
        mViewModel.mutableLiveData.observe(this, new Observer<APIResponse>() {
            @Override
            public void onChanged(APIResponse apiResponse) {
                onAPIResponseHandler(apiResponse);
            }
        });
        mViewModel.getCrushList(this, "searchCrushList.json", AppCommonConstants.API_REQUEST.REQUEST_ID_1001);
        mBinding.searchCrushView.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new SearchCrushAdapter(this, crushList);
        mBinding.searchCrushView.setAdapter(mAdapter);

    }

    @Override
    public void onAPIResponseHandler(APIResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                break;
            case SUCCESS:
                switch (apiResponse.requestID) {
                    case AppCommonConstants.API_REQUEST.REQUEST_ID_1001:
                        crushList = (ArrayList<User>) apiResponse.data;
                        mAdapter.updateData(crushList);
                }
                break;
            case ERROR:
                Utility.showToast(this, getString(R.string.api_failure_error_msg));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
        }

    }

}