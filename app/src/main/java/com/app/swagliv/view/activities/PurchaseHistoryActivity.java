package com.app.swagliv.view.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.common.constant.AppCommonConstants;
import com.app.common.interfaces.APIResponseHandler;
import com.app.common.utils.Utility;
import com.app.common.utils.api_response_handler.APIResponse;
import com.app.swagliv.R;
import com.app.swagliv.databinding.ActivityPurchaseHistoryBinding;
import com.app.swagliv.model.profile.pojo.Subscription;
import com.app.swagliv.view.adaptor.PurchaseHistoryAdapter;
import com.app.swagliv.viewmodel.profile.ProfileViewModel;

import java.util.ArrayList;

public class PurchaseHistoryActivity extends AppCompatActivity implements APIResponseHandler, View.OnClickListener {
    private ActivityPurchaseHistoryBinding mBinding;
    private PurchaseHistoryAdapter mAdapter;
    private ArrayList<Subscription> mPurchaseArrayList;
    private ProfileViewModel mProfileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_purchase_history);
        mBinding.purchaseHistoryHeader.backBtn.setOnClickListener(this);
        mProfileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        mProfileViewModel.mutableLiveData.observe(this, new Observer<APIResponse>() {
            @Override
            public void onChanged(APIResponse apiResponse) {
                onAPIResponseHandler(apiResponse);
            }
        });
        mProfileViewModel.getPurchaseHistory(this, "purchaseHistory.json", AppCommonConstants.API_REQUEST.REQUEST_ID_1001);
        mBinding.purchaseHistoryView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PurchaseHistoryAdapter(this, mPurchaseArrayList);
        mBinding.purchaseHistoryView.setAdapter(mAdapter);
        mBinding.purchaseHistoryHeader.headerTitle.setText(getString(R.string.txt_purchase_history));
    }

    @Override
    public void onAPIResponseHandler(APIResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                break;
            case SUCCESS:
                switch (apiResponse.requestID) {
                    case AppCommonConstants.API_REQUEST.REQUEST_ID_1001:
                        mPurchaseArrayList = (ArrayList<Subscription>) apiResponse.data;
                        mAdapter.updateData(mPurchaseArrayList);
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