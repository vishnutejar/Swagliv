package com.app.swagliv.constant;

import android.content.Context;

import com.app.common.interfaces.OtpReceivedInterface;
import com.app.common.preference.AppPreferencesManager;
import com.app.swagliv.model.login.pojo.User;
import com.app.swagliv.model.profile.pojo.Subscription;
import com.google.gson.Gson;

public class AppInstance {

    public static User mCurrentUser = null;
    private static Subscription mUserSubscriptionPlan = null;
    private static OtpReceivedInterface otpReceiveInterface;

    private static AppInstance appInstance = new AppInstance();

    public static AppInstance getAppInstance() {
        return appInstance;
    }


    public User getAppUserInstance(Context context) {
        if (mCurrentUser == null) {
            try {
                String s = AppPreferencesManager.getString(AppConstant.USER, context);
                mCurrentUser = new Gson().fromJson(s, User.class);
            } catch (Exception e) {
            }
        }
        return mCurrentUser;
    }

    public void setAppUserInstance(User localAppCustomerInstance, Context context) {
        mCurrentUser = localAppCustomerInstance;
        String s = new Gson().toJson(mCurrentUser);
        //TODO-->> THIS IS HACK TO STORE DATA IN SHARED PREFERENCES;
        AppPreferencesManager.putString(AppConstant.USER, s, context);
    }

    public static OtpReceivedInterface getOtpReceiveInterface() {
        return otpReceiveInterface;
    }

    public static void setOtpReceiveInterface(OtpReceivedInterface otpReceiveInterface) {
        AppInstance.otpReceiveInterface = otpReceiveInterface;
    }

    public Subscription getAppUserCurrentSubscriptionPlan(Context context) {
        if (mUserSubscriptionPlan == null) {
            try {
                String s = AppPreferencesManager.getString(AppConstant.USER_SUBSCRIPTION_PLAN, context);
                mUserSubscriptionPlan = new Gson().fromJson(s, Subscription.class);
            } catch (Exception e) {
            }
        }
        return mUserSubscriptionPlan;
    }

    public void setAppUserCurrentSubscriptionPlan(Subscription localSubscription, Context context) {
        mUserSubscriptionPlan = localSubscription;
        String s = new Gson().toJson(mUserSubscriptionPlan);
        //TODO-->> THIS IS HACK TO STORE DATA IN SHARED PREFERENCES;
        AppPreferencesManager.putString(AppConstant.USER_SUBSCRIPTION_PLAN, s, context);
    }

}
