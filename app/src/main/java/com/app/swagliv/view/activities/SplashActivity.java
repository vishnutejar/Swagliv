package com.app.swagliv.view.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.common.preference.AppPreferencesManager;
import com.app.swagliv.R;
import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.databinding.ActivitySplashBinding;
import com.onesignal.OneSignal;

public class SplashActivity extends AppCompatActivity {

    // constant
    private static int TIME_IN_MILLISECOND = 2000; //Display for 3 seconds

    //variables
    private ActivitySplashBinding mBinding;
    private String mCurrentUserProfileStatus;
    private boolean mIsAppAlreadyOpen;
    private static final String ONESIGNAL_APP_ID = "ca7f07ce-8f58-414c-bfad-2b4fa59a3c26";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        mCurrentUserProfileStatus = AppPreferencesManager.getString(AppConstant.PREFERENCE_KEYS.CURRENT_USER_PROFILE_STATUS, this);


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
                        startActivity(new Intent(SplashActivity.this, IntroActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    }
                }
            }
        };
        timer.start();
    }
}