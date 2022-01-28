package com.app.swagliv.viewmodel.payment.repository;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.common.interfaces.APIResponseListener;
import com.app.common.utils.api_response_handler.APIResponse;
import com.app.swagliv.model.login.pojo.User;
import com.app.swagliv.model.profile.pojo.Subscription;
import com.razorpay.PaymentData;

public class PaymentViewModel extends ViewModel implements APIResponseListener {
    public MutableLiveData<APIResponse> mMutableLiveData;
    private PaymentRepository mPaymentRepository;

    public PaymentViewModel() {
        mMutableLiveData = new MutableLiveData<>();
        mPaymentRepository = new PaymentRepository();
    }

    public void getOrderEntity(Subscription subscription, User registeredUser, Integer requestId) {
        mMutableLiveData.setValue(APIResponse.loading(requestId));
        mPaymentRepository.getOrderEntity(subscription,registeredUser, this, requestId);
    }

    public void verifyOrder(Subscription subscription, PaymentData paymentData, Integer requestId) {
        mMutableLiveData.setValue(APIResponse.loading(requestId));
        mPaymentRepository.verifyOrder(subscription, paymentData, this, requestId);
    }

    @Override
    public void onSuccess(Object callResponse, Integer requestID) {
        mMutableLiveData.setValue(APIResponse.success(callResponse, requestID));
    }

    @Override
    public void onFailure(Throwable error, Integer requestID) {
        mMutableLiveData.setValue(APIResponse.error(error, requestID));
    }
}
