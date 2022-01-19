package com.app.swagliv.view.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.common.preference.AppPreferencesManager;
import com.app.swagliv.R;
import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    // constant
    private static int TIME_IN_MILLISECOND = 2000; //Display for 3 seconds

    //variables
    private ActivitySplashBinding mBinding;
    private String mCurrentUserProfileStatus;
    private boolean mIsAppAlreadyOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        mCurrentUserProfileStatus = AppPreferencesManager.getString(AppConstant.PREFERENCE_KEYS.CURRENT_USER_PROFILE_STATUS, this);
        mIsAppAlreadyOpen = AppPreferencesManager.getBoolean(AppConstant.PREFERENCE_KEYS.IS_APP_ALREADY_OPEN, this);

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(TIME_IN_MILLISECOND);
                } catch (InterruptedException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {
                    if (mCurrentUserProfileStatus != null && !mCurrentUserProfileStatus.isEmpty()) {
                        switch (mCurrentUserProfileStatus) {
                            case AppConstant.PROFILE_STATUS.SIGN_UP:
                                startActivity(new Intent(SplashActivity.this, PhoneActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                break;
                            case AppConstant.PROFILE_STATUS.MOBILE_NO_UPDATED:
                                startActivity(new Intent(SplashActivity.this, UserProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                break;
                            case AppConstant.PROFILE_STATUS.PROFILE_COMPLETED:
                                startActivity(new Intent(SplashActivity.this, DashboardActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                break;
                        }
                    } else {
                        if (!mIsAppAlreadyOpen) {
                            AppPreferencesManager.putBoolean(AppConstant.PREFERENCE_KEYS.IS_APP_ALREADY_OPEN, true, SplashActivity.this);
                            startActivity(new Intent(SplashActivity.this, IntroActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        } else {
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        }
                    }
                }
            }
        };
        timer.start();

    }
}