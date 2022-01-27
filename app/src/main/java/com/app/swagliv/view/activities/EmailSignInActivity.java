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
import com.app.common.preference.AppPreferencesManager;
import com.app.common.utils.Utility;
import com.app.common.utils.api_response_handler.APIResponse;
import com.app.progressbar.LoadingProgressBarDialog;
import com.app.swagliv.R;
import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.constant.AppInstance;
import com.app.swagliv.databinding.ActivityEmailSigninBinding;
import com.app.swagliv.model.login.pojo.LoginResponseBaseModel;
import com.app.swagliv.viewmodel.login.LoginViewModel;

public class EmailSignInActivity extends AppCompatActivity implements APIResponseHandler {
    private ActivityEmailSigninBinding mbinding;
    private LoginViewModel loginViewModel;
    private AlertDialog mProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_email_signin);

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

        mbinding.emailSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mbinding.email.getText().toString().isEmpty() && Utility.checkEmail(mbinding.email.getText().toString())) {
                    if (!mbinding.password.getText().toString().isEmpty()) {
                        if (Utility.isNetworkAvailable(EmailSignInActivity.this)) {
                            loginViewModel.doLoginWithEmail(mbinding.email.getText().toString(), mbinding.password.getText().toString(), AppCommonConstants.API_REQUEST.REQUEST_ID_1001);
                        }
/*
                        Intent intent = new Intent(EmailSignInActivity.this, UserProfileActivity.class);
                        startActivity(intent);*/
                    } else {
                        Utility.showToast(EmailSignInActivity.this, getString(R.string.password_error_msg));
                    }
                } else {
                    Utility.showToast(EmailSignInActivity.this, getString(R.string.email_error_msg));
                }

            }
        });

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
                            LoginResponseBaseModel loginResponse = (LoginResponseBaseModel) apiResponse.data;
                            if (loginResponse.getUser() != null) {
                                AppInstance.getAppInstance().setAppUserInstance(loginResponse.getUser(), this);
                                AppPreferencesManager.putString(AppConstant.PREFERENCE_KEYS.CURRENT_USER_PROFILE_STATUS, AppConstant.PROFILE_STATUS.PROFILE_COMPLETED, this);
                                AppPreferencesManager.putString(AppConstant.PREFERENCE_KEYS.APP_TOKEN, loginResponse.getUser().getToken(), this);
                                AppPreferencesManager.putString(AppConstant.PREFERENCE_KEYS.CURRENT_USER_ID, loginResponse.getUser().getId(), this);
                                Intent intent = new Intent(EmailSignInActivity.this, DashboardActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                Utility.showToast(this, loginResponse.getMessage());
                            }
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
