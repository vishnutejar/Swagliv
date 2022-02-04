package com.app.swagliv.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.app.common.constant.AppCommonConstants;
import com.app.common.interfaces.APIResponseHandler;
import com.app.common.utils.Utility;
import com.app.common.utils.api_response_handler.APIResponse;
import com.app.swagliv.R;
import com.app.swagliv.constant.AppInstance;
import com.app.swagliv.databinding.ActivitySubscriptionBinding;
import com.app.swagliv.model.Payment.pojo.OrderVerifyBaseModel;
import com.app.swagliv.model.login.pojo.User;
import com.app.swagliv.model.profile.pojo.Order;
import com.app.swagliv.model.profile.pojo.OrderResponse;
import com.app.swagliv.model.profile.pojo.Subscription;
import com.app.swagliv.view.adaptor.AdvertiseAdapter;
import com.app.swagliv.view.adaptor.PriceAdapter;
import com.app.swagliv.viewmodel.payment.repository.PaymentViewModel;
import com.app.swagliv.viewmodel.profile.ProfileViewModel;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import org.json.JSONObject;

import java.util.ArrayList;


public class SubscriptionActivity extends AppCompatActivity implements PriceAdapter.onItemSelected, APIResponseHandler, View.OnClickListener, PaymentResultWithDataListener {
    private ActivitySubscriptionBinding mBinding;
    // widgets
    private ViewPager viewPager;

    // variables
    private AdvertiseAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Subscription> planPriceArrayList = new ArrayList<>();
    private ProfileViewModel profileViewModel;
    private PaymentViewModel paymentViewModel;
    private Subscription mSubscription;
    private ArrayList<Subscription> subscriptionsTypeArrayList = new ArrayList<>();
    private LinearLayoutManager HorizontalLayout;
    private WormDotsIndicator wormDotsIndicator;
    private PriceAdapter priceAdapter;
    private ImageView header_back;
    private User mRegisteredUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRegisteredUser = AppInstance.getAppInstance().getAppUserInstance(this);
        Subscription appUserCurrentSubscriptionPlan = AppInstance.getAppInstance().getAppUserCurrentSubscriptionPlan(this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_subscription);
        Checkout.preload(getApplicationContext());
        wormDotsIndicator = (WormDotsIndicator) findViewById(R.id.worm_dots_indicator);
        viewPager = findViewById(R.id.viewpager);
        recyclerView = findViewById(R.id.recyclerview);
        header_back = findViewById(R.id.edit_back_icon);
        mBinding.subscriptionContinueBtn.setOnClickListener(this);
        mBinding.upgradeBtn.setOnClickListener(this);
        mBinding.commonHeader.backBtn.setOnClickListener(this);
        mBinding.commonHeader.headerLayout.setBackgroundResource(R.color.dark_pink);
        mBinding.commonHeader.headerTitle.setText(R.string.subscription);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.mutableLiveData.observe(this, new Observer<APIResponse>() {
            @Override
            public void onChanged(APIResponse apiResponse) {
                onAPIResponseHandler(apiResponse);
            }
        });

        profileViewModel.getSubscriptionPlan(AppCommonConstants.API_REQUEST.REQUEST_ID_1001);

        adapter = new AdvertiseAdapter(this, subscriptionsTypeArrayList);
        viewPager.setAdapter(adapter);
        wormDotsIndicator.setViewPager(viewPager);

        HorizontalLayout
                = new LinearLayoutManager(
                SubscriptionActivity.this,
                LinearLayoutManager.HORIZONTAL,
                true);
        recyclerView.setLayoutManager(HorizontalLayout);

        priceAdapter = new PriceAdapter(this, planPriceArrayList, this);
        recyclerView.setAdapter(priceAdapter);
        paymentViewModel = new ViewModelProvider(this).get(PaymentViewModel.class);
        paymentViewModel.mMutableLiveData.observe(this, new Observer<APIResponse>() {
            @Override
            public void onChanged(APIResponse apiResponse) {
                onAPIResponseHandler(apiResponse);
            }
        });

        if (appUserCurrentSubscriptionPlan != null) {
            mBinding.planName.setText(appUserCurrentSubscriptionPlan.getSubscriptionName());
            String purchase = Utility.convertDate(appUserCurrentSubscriptionPlan.getPurchasedAt(), AppCommonConstants.API_DATE_FORMAT, AppCommonConstants.DATE_FORMAT_SHOW_UI);
            String expired = Utility.convertDate(appUserCurrentSubscriptionPlan.getPlanExpiresAt(), AppCommonConstants.API_DATE_FORMAT, AppCommonConstants.DATE_FORMAT_SHOW_UI);
            mBinding.time.setText(purchase + " - " + expired);
        }
    }

    @Override
    public void OnItemSelected(Subscription price_model) {
        ArrayList<Subscription> newUpdatedList = new ArrayList<>();
        for (Subscription item : planPriceArrayList) {

            if (item.getId().equals(price_model.getId()))
                item.setSelected(true);
            else
                item.setSelected(false);
            newUpdatedList.add(item);
        }
        mSubscription = price_model;
        priceAdapter.updateList(newUpdatedList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                callDashboard();
                break;
            case R.id.subscription_continue_btn:
            case R.id.upgrade_btn:
                if (Utility.isNetworkAvailable(SubscriptionActivity.this)) {
                    if (mSubscription != null) {
                        paymentViewModel.getOrderEntity(mSubscription, mRegisteredUser, AppCommonConstants.API_REQUEST.REQUEST_ID_1002);
                    } else {
                        Utility.showToast(this, getString(R.string.err_select_package));
                    }
                }
        }
    }

    public void startPayment(Order order) {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_TPDKL5xRkmbkg4");
        try {
            JSONObject options = order.toJSON();

            options.put("name", getString(R.string.app_name));
            options.put("prefill.contact", mRegisteredUser.getContactNumber());
            if (mRegisteredUser.getEmail() != null && !mRegisteredUser.getEmail().isEmpty()) {
                options.put("prefill.email", mRegisteredUser.getEmail());
            }
            checkout.open(SubscriptionActivity.this, options);
        } catch (Exception e) {
            Utility.showToast(this, getString(R.string.err_payment_un_successfull));
        }

    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        paymentViewModel.verifyOrder(mSubscription, paymentData, AppCommonConstants.API_REQUEST.REQUEST_ID_1003);
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        switch (i) {
            case Checkout.NETWORK_ERROR:
                Utility.showToast(this, getString(R.string.err_network_connection));
                break;
            case Checkout.INVALID_OPTIONS:
                Utility.showToast(this, getString(R.string.err_payment_details_invalid));
                break;
            default:
                Utility.showToast(this, getString(R.string.err_payment_un_successfull));
        }
    }


    @Override
    public void onAPIResponseHandler(APIResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                break;
            case SUCCESS:
                switch (apiResponse.requestID) {
                    case AppCommonConstants.API_REQUEST.REQUEST_ID_1001:
                        subscriptionsTypeArrayList = (ArrayList<Subscription>) apiResponse.data;
                        adapter.updateData(subscriptionsTypeArrayList);
                        planPriceArrayList = (ArrayList<Subscription>) apiResponse.data;
                        priceAdapter.updateList(planPriceArrayList);
                        break;
                    case AppCommonConstants.API_REQUEST.REQUEST_ID_1002:
                        OrderResponse orderCreatedBaseModel = (OrderResponse) apiResponse.data;
                        startPayment(orderCreatedBaseModel.getOrder());
                        break;
                    case AppCommonConstants.API_REQUEST.REQUEST_ID_1003:
                        OrderVerifyBaseModel orderVerifyBaseModel = (OrderVerifyBaseModel) apiResponse.data;
                        callDashboard();
                        break;
                }
                break;
            case ERROR:
                Utility.showToast(this, getString(R.string.api_failure_error_msg));
                break;
        }

    }


    private void callDashboard() {
        Intent i = new Intent(SubscriptionActivity.this, DashboardActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        callDashboard();
    }
}