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
import com.app.swagliv.custome_view.otpview.OnOtpCompletionListener;
import com.app.swagliv.custome_view.otpview.OtpEditText;
import com.app.swagliv.databinding.ActivityOtpBinding;
import com.app.swagliv.viewmodel.login.LoginViewModel;

/**
 * @https://github.com/mukeshsolanki/android-otpview-pinview
 */

public class OtpActivity extends AppCompatActivity implements View.OnClickListener, OnOtpCompletionListener, APIResponseHandler {
    private ActivityOtpBinding mbinding;
    private LoginViewModel loginViewModel;
    private AlertDialog mProgressbar;
    private String mobileNo;
    //    private TextView verifybtn;
    private OtpEditText otpView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_otp);

        otpView = mbinding.inputOtp;
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.responseMutableLiveData.observe(this, new Observer<APIResponse>() {
            @Override
            public void onChanged(APIResponse apiResponse) {
                onAPIResponseHandler(apiResponse);
            }
        });


        mProgressbar = new LoadingProgressBarDialog.Builder()
                .setContext(this)
                .setMessage(getString(R.string.please_wait))
                .build();



        mbinding.verificationbtn.setOnClickListener(this);
        //otpView.setOtpCompletionListener(this);


        mobileNo = getIntent().getStringExtra("mobileNo");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verificationbtn:
                if (Utility.isNetworkAvailable(this)) {
                    loginViewModel.toVerifyOtp(mbinding.inputOtp.getText().toString(),mobileNo, AppCommonConstants.API_REQUEST.REQUEST_ID_1001);
                }
                break;
            default:
                break;


        }
    }

    @Override
    public void onOtpCompleted(String otp) {
        Utility.showToast(this, "OnOtpCompletionListener called");
    }

    @Override
    public void onAPIResponseHandler(APIResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mProgressbar.show();
                break;
            case SUCCESS:
                switch (apiResponse.requestID) {
                    case AppCommonConstants.API_REQUEST.REQUEST_ID_1001:
                        mProgressbar.dismiss();
                        if (apiResponse.data instanceof String) {
                            String message = (String) apiResponse.data;
                            Utility.showToast(this, message);
                        } else {
                            Intent intent = new Intent(OtpActivity.this, UserProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }

                }
                break;
            case ERROR:
                mProgressbar.dismiss();
                Utility.showToast(this,"Enter valid otp");
                break;

        }
    }
}