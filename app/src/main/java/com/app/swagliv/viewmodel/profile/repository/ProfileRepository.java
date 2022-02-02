package com.app.swagliv.viewmodel.profile.repository;

import android.content.Context;

import com.app.common.constant.AppCommonConstants;
import com.app.common.interfaces.APIResponseListener;
import com.app.common.utils.Utility;
import com.app.swagliv.model.common.Common;
import com.app.swagliv.model.home.api.ProfileService;
import com.app.swagliv.model.home.pojo.DashboardBaseModel;
import com.app.swagliv.model.home.pojo.PassionListBaseModel;
import com.app.swagliv.model.home.pojo.Passions;
import com.app.swagliv.model.home.pojo.UploadImageBaseModel;
import com.app.swagliv.model.login.pojo.LoginResponseBaseModel;
import com.app.swagliv.model.login.pojo.User;
import com.app.swagliv.model.profile.pojo.PersonalImages;
import com.app.swagliv.model.profile.pojo.Subscription;
import com.app.swagliv.model.profile.pojo.SubscriptionBaseModel;
import com.app.swagliv.network.ApplicationRetrofitServices;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {
    private ArrayList<Subscription> subscriptionArrayList;

    public void doGetPassionsList(APIResponseListener apiResponseListener, Integer requestID) {
        ProfileService profileService = ApplicationRetrofitServices.getInstance().getProfileService();
        Call<PassionListBaseModel> call = profileService.getPassionList();
        call.enqueue(new Callback<PassionListBaseModel>() {
            @Override
            public void onResponse(Call<PassionListBaseModel> call, Response<PassionListBaseModel> response) {
                PassionListBaseModel passionListBaseModel = response.body();
                if (passionListBaseModel != null) {
                    apiResponseListener.onSuccess(passionListBaseModel, requestID);
                } else {

                }
            }

            @Override
            public void onFailure(Call<PassionListBaseModel> call, Throwable t) {
                apiResponseListener.onFailure(new Exception(), requestID);
            }
        });

    }

    public void doUploadImage(File imagePath, String imageUploadType, APIResponseListener apiResponseListener, Integer requestID) {

        RequestBody imageUploadTypeReqBody = RequestBody.create(MediaType.parse("text/plain"), "" + imageUploadType);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imagePath);
        MultipartBody.Part imageFileBody = MultipartBody.Part.createFormData("file", imagePath.getName(), requestBody);

        ProfileService profileService = ApplicationRetrofitServices.getInstance().getProfileService();
        Call<JsonObject> call = profileService.updatePhoto(imageUploadTypeReqBody, imageFileBody);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject passionListResponse = response.body();

                String imgURL = passionListResponse.get("Data").getAsJsonObject().get(imageUploadType).getAsString();

                if (imgURL != null) {
                    apiResponseListener.onSuccess(imgURL, requestID);
                } else {
                    apiResponseListener.onFailure(new Exception(Utility.getApiFailureErrorMsg(response.errorBody())), requestID);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                apiResponseListener.onFailure(new Exception(), requestID);
            }
        });

    }


    public void getSubscriptionPlan(APIResponseListener apiResponseListener, Integer requestID) {
        /*String data = Utility.readJsonFromAssets(context, filePath);
        if (data != null) {
            Type subscriptionType = new TypeToken<SubscriptionBaseModel>() {
            }.getType();
            SubscriptionBaseModel subscriptionBaseModel = new Gson().fromJson(data, subscriptionType);
            subscriptionArrayList = subscriptionBaseModel.getSubscriptionPlans();
            apiResponseListener.onSuccess(subscriptionArrayList, requestID);
        } else
            apiResponseListener.onFailure(new Exception(), requestID);*/
        ProfileService profileService = ApplicationRetrofitServices.getInstance().getProfileService();
        Call<SubscriptionBaseModel> call = profileService.getSubscriptionPlan();
        call.enqueue(new Callback<SubscriptionBaseModel>() {
            @Override
            public void onResponse(Call<SubscriptionBaseModel> call, Response<SubscriptionBaseModel> response) {
                SubscriptionBaseModel subscriptionBaseModel = response.body();
                if (response.isSuccessful() && subscriptionBaseModel != null) {
                    if (subscriptionBaseModel.getStatus() == AppCommonConstants.API_SUCCESS_STATUS_CODE) {
                        apiResponseListener.onSuccess(subscriptionBaseModel.getSubscriptionPlans(), requestID);
                    } else {
                    }

                } else {
                    apiResponseListener.onSuccess(Utility.getApiFailureErrorMsg(response.errorBody()), requestID);
                }
            }

            @Override
            public void onFailure(Call<SubscriptionBaseModel> call, Throwable t) {
                apiResponseListener.onFailure(new Exception(), requestID);
            }
        });
    }

    public void doUpdateProfile(User user, APIResponseListener apiResponseListener, Integer requestID) {
        ProfileService profileService = ApplicationRetrofitServices.getInstance().getProfileService();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", user.getName());
        jsonObject.addProperty("dob", user.getDob());
        jsonObject.addProperty("gender", user.getGender());
        jsonObject.addProperty("mobileNumber", user.getContactNumber());
        jsonObject.addProperty("aboutMe", user.getAboutMe());

        JsonArray passionArray = new JsonArray();
        for (Passions passion :
                user.getPassions()) {
            JsonObject jsonObject1 = new JsonObject();
            jsonObject1.addProperty("_id", passion.getId());
            jsonObject1.addProperty("label", passion.getLabel());
            passionArray.add(jsonObject1);
        }
        jsonObject.add("passions", passionArray);

        Utility.printLogs("asdasd", jsonObject.toString());

        Call<LoginResponseBaseModel> call = profileService.doUpdateProfile(jsonObject);
        call.enqueue(new Callback<LoginResponseBaseModel>() {
            @Override
            public void onResponse(Call<LoginResponseBaseModel> call, Response<LoginResponseBaseModel> response) {
                LoginResponseBaseModel loginResponseBaseModel = response.body();
                if (response.isSuccessful() && loginResponseBaseModel != null) {
                    if (loginResponseBaseModel.getStatus() == AppCommonConstants.API_SUCCESS_STATUS_CODE) {
                        apiResponseListener.onSuccess(loginResponseBaseModel, requestID);
                    } else {
                    }

                } else {
                    apiResponseListener.onSuccess(Utility.getApiFailureErrorMsg(response.errorBody()), requestID);
                }


            }

            @Override
            public void onFailure(Call<LoginResponseBaseModel> call, Throwable t) {
                apiResponseListener.onFailure(new Exception(), requestID);
            }
        });

    }

    public void getPurchaseHistory(Context context, String filePath, APIResponseListener apiResponseListener, Integer requestID) {
        String data = Utility.readJsonFromAssets(context, filePath);
        if (data != null) {
            Type subscriptionType = new TypeToken<SubscriptionBaseModel>() {
            }.getType();
            SubscriptionBaseModel subscriptionBaseModel = new Gson().fromJson(data, subscriptionType);
            subscriptionArrayList = subscriptionBaseModel.getSubscriptionPlans();
            apiResponseListener.onSuccess(subscriptionArrayList, requestID);
        } else
            apiResponseListener.onFailure(new Exception(), requestID);
    }

    public void removeImage(PersonalImages personalImages, APIResponseListener apiResponseListener, Integer requestID) {
        ProfileService profileService = ApplicationRetrofitServices.getInstance().getProfileService();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("image", personalImages.getUrl());
        Call<LoginResponseBaseModel> call = profileService.doRemoveImage(jsonObject);
        call.enqueue(new Callback<LoginResponseBaseModel>() {
            @Override
            public void onResponse(Call<LoginResponseBaseModel> call, Response<LoginResponseBaseModel> response) {
                LoginResponseBaseModel loginResponseBaseModel = response.body();
                if (response.isSuccessful() && loginResponseBaseModel != null) {
                    if (loginResponseBaseModel.getStatus() == AppCommonConstants.API_SUCCESS_STATUS_CODE) {
                        apiResponseListener.onSuccess(loginResponseBaseModel, requestID);
                    } else {
                    }

                } else {
                    apiResponseListener.onSuccess(Utility.getApiFailureErrorMsg(response.errorBody()), requestID);
                }
            }

            @Override
            public void onFailure(Call<LoginResponseBaseModel> call, Throwable t) {
                apiResponseListener.onFailure(new Exception(), requestID);
            }
        });

    }

}
