package com.app.swagliv.viewmodel.searchCrush.repository;

import android.content.Context;

import com.app.common.interfaces.APIResponseListener;
import com.app.common.utils.Utility;
import com.app.swagliv.model.CrushSearch;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


public class SearchCrushRepository {
    public void getCrushList(Context context, String filePath, APIResponseListener apiResponseListener, Integer requestID) {
        String data = Utility.readJsonFromAssets(context, filePath);
        if (data != null) {
            Type crushSearchType = new TypeToken<CrushSearch>() {
            }.getType();
            CrushSearch crushSearch = new Gson().fromJson(data, crushSearchType);
            apiResponseListener.onSuccess(crushSearch.getCrushData(), requestID);
        } else
            apiResponseListener.onFailure(new Exception(), requestID);
    }
}
