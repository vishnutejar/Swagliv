package com.app.swagliv.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.app.common.constant.AppCommonConstants;
import com.app.common.interfaces.APIResponseHandler;
import com.app.common.interfaces.OtpReceivedInterface;
import com.app.common.preference.AppPreferencesManager;
import com.app.common.utils.Utility;
import com.app.common.utils.api_response_handler.APIResponse;
import com.app.progressbar.LoadingProgressBarDialog;
import com.app.swagliv.R;
import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.constant.AppInstance;
import com.app.swagliv.custome_view.otpview.OnOtpCompletionListener;
import com.app.swagliv.databinding.ActivityVerifyOtpBinding;
import com.app.swagliv.model.login.pojo.LoginResponseBaseModel;
import com.app.swagliv.receiver.otp.OTPReceiver;
import com.app.swagliv.viewmodel.login.LoginViewModel;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

/**
 * @https://github.com/mukeshsolanki/android-otpview-pinview
 */

public class OtpVerifyActivity extends AppCompatActivity implements View.OnClickListener, OnOtpCompletionListener, APIResponseHandler, OtpReceivedInterface {

    // variables
    private ActivityVerifyOtpBinding mbinding;
    private LoginViewModel loginViewModel;
    private AlertDialog mProgressbar;
    private String mobileNo;
    //    private TextView verifybtn;
    //private OtpView otpView;
    private OTPReceiver mOTPReceiver;
    //Constants
    private static final String TAG = OtpVerifyActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_verify_otp);

        initialize();
    }

    private void initialize() {
        mOTPReceiver = new OTPReceiver();
        mOTPReceiver.setOnOtpListeners(this);
        startSMSListener();

        //---------
        // otpView = mbinding.inputOtp;
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
        mbinding.hedderback.backBtn.setOnClickListener(this);
        //otpView.setOtpCompletionListener(this);

        mobileNo = getIntent().getStringExtra(getString(R.string.mobile_number));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verificationbtn:
                if (Utility.isNetworkAvailable(this)) {
                    loginViewModel.toVerifyOtp(mbinding.inputOtp.getText().toString(), mobileNo, AppCommonConstants.API_REQUEST.REQUEST_ID_1001);
                }
                break;
            case R.id.back_btn:
                finish();
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
                            LoginResponseBaseModel loginResponse = (LoginResponseBaseModel) apiResponse.data;
                            AppInstance.getAppInstance().setAppUserInstance(loginResponse.getUser(), this);
                            AppPreferencesManager.putString(AppConstant.PREFERENCE_KEYS.CURRENT_USER_PROFILE_STATUS, AppConstant.PROFILE_STATUS.MOBILE_NO_UPDATED, this);
                            Intent intent = new Intent(OtpVerifyActivity.this, UserProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }

                }
                break;
            case ERROR:
                mProgressbar.dismiss();
                Utility.showToast(this, getString(R.string.api_failure_error_msg));
                break;

        }
    }

    /**
     * Start sms listner.
     */
    private void startSMSListener() {
        SmsRetrieverClient mClient = SmsRetriever.getClient(this);
        Task<Void> mTask = mClient.startSmsRetriever();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
        mTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Utility.showToast(OtpVerifyActivity.this, e.getMessage());
            }
        });
    }

    @Override
    public void onOtpReceived(String otp) {
        Utility.showToast(this, otp);
        mbinding.inputOtp.setText(otp);
    }

    @Override
    public void onOtpTimeout() {
        // This method use for Otp resend in future, When resend functionality require.
    }
}