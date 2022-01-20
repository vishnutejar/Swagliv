package com.app.swagliv.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.app.common.constant.AppCommonConstants;
import com.app.common.interfaces.APIResponseHandler;
import com.app.common.utils.Utility;
import com.app.common.utils.api_response_handler.APIResponse;
import com.app.progressbar.LoadingProgressBarDialog;
import com.app.swagliv.R;
import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.databinding.ActivityPassionSelectionBinding;
import com.app.swagliv.model.home.pojo.PassionListBaseModel;
import com.app.swagliv.model.home.pojo.Passions;
import com.app.swagliv.view.adaptor.PassionAdapter;
import com.app.swagliv.viewmodel.profile.ProfileViewModel;

import java.util.ArrayList;
import java.util.List;

public class PassionSelectionActivity extends AppCompatActivity implements View.OnClickListener, APIResponseHandler {

    // variables
    private ActivityPassionSelectionBinding mBinding;
    private ArrayList<Passions> mSelectedPassionList = new ArrayList<>();
    private ArrayList<Passions> mPassionList = new ArrayList<>();
    private ArrayList<Passions> mPassionFilterList = new ArrayList<>();
    private String interestString = "";
    private ProfileViewModel viewModel;
    private PassionAdapter passionAdapter;
    private AlertDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_passion_selection);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        viewModel.mutableLiveData.observe(this, new Observer<APIResponse>() {
            @Override
            public void onChanged(APIResponse apiResponse) {
                onAPIResponseHandler(apiResponse);
            }
        });
        setInterestAdapterMulti();
        //---------
        mProgressDialog = new LoadingProgressBarDialog.Builder()
                .setContext(this)
                .setMessage(getString(R.string.please_wait))
                .build();

        if (getIntent() != null) {
            mSelectedPassionList = getIntent().getParcelableArrayListExtra(AppConstant.INTENT_KEYS.SELECTED_PASSIONS);
        }

        if (Utility.isNetworkAvailable(this)) {
            viewModel.doGetPassionsList(AppCommonConstants.API_REQUEST.REQUEST_ID_1001);
        }

        mBinding.commonHeader.backBtn.setOnClickListener(this);
        mBinding.passionBtnContinue.setOnClickListener(this);
    }

    public void setInterestAdapterMulti() {
        passionAdapter = new PassionAdapter(this, mSelectedPassionList);
        mBinding.passionChipsRecycle.setAdapter(passionAdapter);

    }

    public void selectGuestUserListData(List<Passions> modifiedListUserData) {
        mSelectedPassionList = new ArrayList<>();
        for (int i = 0; i < modifiedListUserData.size(); i++) {
            if (modifiedListUserData.get(i).isSelected()) {
                mSelectedPassionList.add(modifiedListUserData.get(i));
            }
        }
        mBinding.numberCount.setText("CONTINUE(" + mSelectedPassionList.size() + "/" + AppConstant.MAX_PASSION_SELECTION_COUNT + ")");
        interestString = mSelectedPassionList.toString().replaceAll("[\\[.\\].\\s+]", "");

//        Utility.showToast(PassionSelectionActivity.this, interestString);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.passion_btn_continue:
                Intent i = new Intent();
                i.putExtra(AppConstant.INTENT_KEYS.SELECTED_PASSIONS, mSelectedPassionList);
                setResult(Activity.RESULT_OK, i);
                finish();

        }
    }

    @Override
    public void onAPIResponseHandler(APIResponse apiResponse) {
        switch (apiResponse.status) {

            case LOADING:
                //------
                mProgressDialog.show();
                break;
            case SUCCESS:
                mProgressDialog.dismiss();
                switch (apiResponse.requestID) {
                    case AppCommonConstants.API_REQUEST.REQUEST_ID_1001:
                        if (apiResponse.data instanceof String) {
                            String message = (String) apiResponse.data;
                            Utility.showToast(this, message);
                        } else {
                            PassionListBaseModel passionListBaseModel = (PassionListBaseModel) apiResponse.data;
                            mPassionList = passionListBaseModel.getData().getPassions();

                            if (mSelectedPassionList != null && !mSelectedPassionList.isEmpty()) {
                                for (Passions passions : mPassionList) {
                                    for (Passions selectedPassion : mSelectedPassionList) {
                                        if (passions.getId().equalsIgnoreCase(selectedPassion.getId())) {
                                            passions.setSelected(true);
                                        }
                                    }
                                    mPassionFilterList.add(passions);
                                }

                                mBinding.numberCount.setText("CONTINUE(" + mSelectedPassionList.size() + "/" + AppConstant.MAX_PASSION_SELECTION_COUNT + ")");
                                passionAdapter.updateData(mPassionFilterList);
                            } else {
                                passionAdapter.updateData(mPassionList);
                            }
                        }
                        break;
                }
                break;
            case ERROR:
                mProgressDialog.dismiss();
                //-------
                Utility.showToast(PassionSelectionActivity.this, getString(R.string.api_failure_error_msg));
                break;
            default:
                break;
        }
    }
}