package com.app.swagliv.view.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.app.common.constant.AppCommonConstants;
import com.app.common.interfaces.APIResponseHandler;
import com.app.common.interfaces.DatePickerDialogListener;
import com.app.common.preference.AppPreferencesManager;
import com.app.common.utils.Utility;
import com.app.common.utils.api_response_handler.APIResponse;
import com.app.progressbar.LoadingProgressBarDialog;
import com.app.swagliv.R;
import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.constant.AppInstance;
import com.app.swagliv.databinding.ActivitySignupBinding;
import com.app.swagliv.model.login.pojo.LoginResponseBaseModel;
import com.app.swagliv.model.login.pojo.User;
import com.app.swagliv.viewmodel.login.LoginViewModel;

import java.util.Date;

public class SignUpActivity extends AppCompatActivity implements DatePickerDialogListener, View.OnClickListener, APIResponseHandler {
    private ActivitySignupBinding mSignupbinding;
    private LoginViewModel loginViewModel;
    private AlertDialog mProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSignupbinding = DataBindingUtil.setContentView(this, R.layout.activity_signup);


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


        mSignupbinding.dateClick.setOnClickListener(this);
        mSignupbinding.genderText.setOnClickListener(this);
        mSignupbinding.signup.setOnClickListener(this);

        String email = getIntent().getStringExtra("email");

        if (email != null) {
            mSignupbinding.nameText.setText(getIntent().getStringExtra("name"));
            mSignupbinding.emailText.setText(email);
            mSignupbinding.emailText.setEnabled(false);
        } else {
            mSignupbinding.emailText.setEnabled(true);
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup:
                if (!mSignupbinding.nameText.getText().toString().trim().isEmpty()) {
                    if (!mSignupbinding.genderText.getText().toString().trim().isEmpty()) {
                        if (!mSignupbinding.dateClick.getText().toString().isEmpty()) {
                            if (!mSignupbinding.emailText.getText().toString().trim().isEmpty() && Utility.checkEmail(mSignupbinding.emailText.getText().toString().trim())) {
                                if (!mSignupbinding.txtPassword.getText().toString().isEmpty()) {
                                    if (!mSignupbinding.txtConformPassword.getText().toString().isEmpty()) {
                                        if (mSignupbinding.txtPassword.getText().toString().equalsIgnoreCase(mSignupbinding.txtConformPassword.getText().toString())) {

                                            User user = new User();
                                            user.setName(mSignupbinding.nameText.getText().toString());
                                            user.setGender(mSignupbinding.genderText.getText().toString());
                                            user.setDob(mSignupbinding.dateClick.getText().toString());
                                            user.setEmail(mSignupbinding.emailText.getText().toString());
                                            user.setPassword(mSignupbinding.txtPassword.getText().toString());

                                            if (Utility.isNetworkAvailable(this)) {
                                                String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                                                loginViewModel.doUserRegistration(user, AppCommonConstants.LOGIN_TYPE.NORMAL, AppCommonConstants.API_REQUEST.REQUEST_ID_1001, deviceId);
                                            }
                                        } else {
                                            Utility.showToast(SignUpActivity.this, getString(R.string.check_password));
                                        }

                                    } else {
                                        Utility.showToast(SignUpActivity.this, getString(R.string.conform_password_error_msg));
                                    }
                                } else {
                                    Utility.showToast(SignUpActivity.this, getString(R.string.password_error_msg));
                                }
                            } else {
                                Utility.showToast(SignUpActivity.this, getString(R.string.email_error_msg));
                            }

                        } else {
                            Utility.showToast(SignUpActivity.this, getString(R.string.date_error_msg));
                        }
                    } else {
                        Utility.showToast(SignUpActivity.this, getString(R.string.gender_text__error_msg));

                    }
                } else {
                    Utility.showToast(SignUpActivity.this, getString(R.string.name_text_error_msg));
                }
                break;
            case R.id.date_click:
                Utility utility = new Utility(SignUpActivity.this);
                utility.datePickerDialog(SignUpActivity.this, SignUpActivity.this, new Date());
                break;

            case R.id.gender_text:
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                ViewGroup viewGroup = findViewById(R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.showgenderselection, viewGroup, false);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogView.findViewById(R.id.male_txt).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSignupbinding.genderText.setText(AppCommonConstants.GENDER_MALE);
                        alertDialog.dismiss();
                    }
                });

                dialogView.findViewById(R.id.female_txt).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSignupbinding.genderText.setText(AppCommonConstants.GENDER_FEMALE);
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
                break;
            case R.id.back_btn:
                finish();
                break;
            default:
                break;
        }

    }

    @Override
    public void getSelectedDate(String selectedDate) {
        mSignupbinding.dateClick.setText(selectedDate);
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
                            LoginResponseBaseModel registrationResponse = (LoginResponseBaseModel) apiResponse.data;
                            AppPreferencesManager.putString(AppConstant.PREFERENCE_KEYS.CURRENT_USER_PROFILE_STATUS, AppConstant.PROFILE_STATUS.SIGN_UP, this);
                            AppPreferencesManager.putString(AppConstant.PREFERENCE_KEYS.CURRENT_USER_ID, registrationResponse.getUser().getId(), this);
                            AppPreferencesManager.putString(AppConstant.PREFERENCE_KEYS.APP_TOKEN, registrationResponse.getUser().getToken(), this);
                            AppInstance.getAppInstance().setAppUserInstance(registrationResponse.getUser(), this);
                            //Utility.showToast(this, registrationResponse.getMessage());
                            //--------
                            Intent intent = new Intent(SignUpActivity.this, PhoneActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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



