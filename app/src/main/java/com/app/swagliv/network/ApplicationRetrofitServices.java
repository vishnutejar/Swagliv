package com.app.swagliv.network;


import com.app.common.network.RetrofitClient;
import com.app.common.preference.AppPreferencesManager;
import com.app.common.utils.Utility;
import com.app.swagliv.SwagLivApplication;
import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.model.Payment.api.PaymentService;
import com.app.swagliv.model.call.api.TwilioVoiceTokenService;
import com.app.swagliv.model.chat.api.UserChatsService;
import com.app.swagliv.model.home.api.DashboardService;
import com.app.swagliv.model.home.api.ProfileService;
import com.app.swagliv.model.login.api.LoginService;

/**
 * This is a singleton class where we Create retrofit client and configure it to call web apis using retrofit library.
 */
public class ApplicationRetrofitServices {

    public ApplicationRetrofitServices() {

    }

    private static ApplicationRetrofitServices mInstance = new ApplicationRetrofitServices();

    public static synchronized ApplicationRetrofitServices getInstance() {
        if (RetrofitClient.getToken() == null) {
            String token = AppPreferencesManager.getString(AppConstant.PREFERENCE_KEYS.APP_TOKEN, SwagLivApplication.getInstance());
            if (token != null) RetrofitClient.setToken(token);
            Utility.printLogs("token", "" + token);
        }
        return mInstance;
    }

    public DashboardService getDashboardService() {
        return RetrofitClient.getInstance().getRetrofit().create(DashboardService.class);
    }

    public ProfileService getProfileService() {
        return RetrofitClient.getInstance().getRetrofit().create(ProfileService.class);
    }

    public LoginService getLoginService() {
        return RetrofitClient.getInstance().getRetrofit().create(LoginService.class);
    }

    public PaymentService getPaymentService() {
        return RetrofitClient.getInstance().getRetrofit().create(PaymentService.class);
    }

    public TwilioVoiceTokenService getTwilioTokenService() {
        return RetrofitClient.getInstance().getRetrofit().create(TwilioVoiceTokenService.class);
    }

    public UserChatsService getUserChatsService(){
        return RetrofitClient.getInstance().getRetrofit().create(UserChatsService.class);
    }

}
