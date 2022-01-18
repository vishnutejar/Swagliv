package com.app.swagliv.constant;

public class AppConstant {

    public static final String USER = "USER";
    public static final String USER_SUBSCRIPTION_PLAN = "USER_SUBSCRIPTION_PLAN";
    public static final String PERSONAL_IMAGES = "personalImages";
    public static final String OTHER_IMAGES = "otherImages";
    public static final int MAX_PASSION_SELECTION_COUNT = 5;
    public static final String MEN = "Men";
    public static final String WOMEN = "Women";

    public class INTENT_KEYS {
        public static final String IS_NEED_TO_CHECK_GPS = "IS_NEED_TO_CHECK_GPS";
        public static final String IS_CALL_FOR_LOCATION_PERMISSION = "IS_CALL_FOR_LOCATION_PERMISSION";
        public static final String SELECTED_PASSIONS = "SELECTED_PASSIONS";
        public static final String USER_DATA = "USER_DATA";
    }

    public class PREFERENCE_KEYS {
        public static final String CURRENT_USER_PROFILE_STATUS = "CURRENT_USER_PROFILE_STATUS";
        public static final String CURRENT_USER_ID = "CURRENT_USER_ID";
        public static final String IS_APP_ALREADY_OPEN = "IS_FIRST_TIME_APP_OPEN";
        public static final String APP_TOKEN = "APP_TOKEN";
    }

    public class PROFILE_STATUS {
        public static final String SIGN_UP = "SIGN_UP";
        public static final String MOBILE_NO_UPDATED = "MOBILE_NO_UPDATED";
        public static final String PROFILE_COMPLETED = "PROFILE_COMPLETED";
    }

    public class RequestCodes {
        public static final int PROFILE = 101;
        public static final int OTHER_IMAGES = 102;
    }

    public class API {
        public static final String GET_ALL_PASSION = "getAllPassions";
        public static final String UPLOAD_PROFILE_IMAGES = "uploadImage";
        public static final String REGISTER = "createUser";
        public static final String UPDATE_MOBILE_NUMBER = "updateMobileNumber";
        public static final String VERIFY_MOBILE_OTP = "verifyMobileOtp";
        public static final String LOGIN = "login";
        public static final String GET_NEW_PROFILES = "getNewProfiles";
        public static final String ACTION_ON_PROFILE = "actionOnProfiles";
        public static final String GET_USER_LIKE = "getUserLikes";
        public static final String UPDATE_PROFILE = "updateProfile";
        public static final String GET_SUBSCRIPTION_PLAN = "getAllSubscriptionPlans";
        public static final String CREATE_ORDER = "createOrder";
        public static final String VERIFY_ORDER = "verifyOrder";
    }

    public class SWIPE {
        public static final int LIKE = 1;
        public static final int DISLIKE = 2;
        public static final int SUPER_LIKE = 3;

    }

}
