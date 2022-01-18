package com.app.common.interfaces;

public interface APIResponseListener {

    public void onSuccess(Object callResponse, Integer requestID);

    public void onFailure(Throwable error, Integer requestID);
}