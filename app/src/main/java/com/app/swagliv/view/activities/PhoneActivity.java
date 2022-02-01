package com.app.swagliv.view.activities;

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
import com.app.swagliv.databinding.ActivityAddPhoneBinding;
import com.app.swagliv.viewmodel.login.LoginViewModel;


public class PhoneActivity extends AppCompatActivity implements View.OnClickListener, APIResponseHandler {

    // variables
    private ActivityAddPhoneBinding mBinding;
    private LoginViewModel mViewModel;
    private AlertDialog mProgressbar;
    private LoginViewModel loginViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_phone);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        mViewModel.responseMutableLiveData.observe(this, new Observer<APIResponse>() {
            @Override
            public void onChanged(APIResponse apiResponse) {
                onAPIResponseHandler(apiResponse);
            }
        });

        mProgressbar = new LoadingProgressBarDialog.Builder()
                .setContext(this)
                .setMessage(getString(R.string.please_wait))
                .build();

        mBinding.btnContinue.setOnClickListener(this);
        mBinding.backHedder.backBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_continue:
                if (!mBinding.phoneNo.getText().toString().isEmpty() && Utility.checkContactNo(mBinding.phoneNo.getText().toString())) {
                    if (Utility.isNetworkAvailable(this)) {
                        mViewModel.doUpdateMobileNumber(mBinding.phoneNo.getText().toString(), AppCommonConstants.API_REQUEST.REQUEST_ID_1001);
                    }
                } else
                    Utility.showToast(PhoneActivity.this, getString(R.string.phone_no_error_msg));
                break;
            case R.id.back_btn:
                finish();
                break;
        }

    }

    @Override
    public void onAPIResponseHandler(APIResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mProgressbar.show();
                break;
            case SUCCESS:
                mProgressbar.dismiss();
                switch (apiResponse.requestID) {
                    case AppCommonConstants.API_REQUEST.REQUEST_ID_1001:

                        if (apiResponse.data instanceof String) {
                            String message = (String) apiResponse.data;
                            Utility.showToast(this, message);
                        } else {
                            Intent intent = new Intent(PhoneActivity.this, OtpVerifyActivity.class);
                            intent.putExtra(getString(R.string.mobile_number), mBinding.phoneNo.getText().toString().trim());
                            startActivity(intent);
                        }
                        break;
                }
                break;
            case ERROR:
                mProgressbar.dismiss();
                Utility.showToast(this, getString(R.string.api_failure_error_msg));
                break;
        }

    }
}
