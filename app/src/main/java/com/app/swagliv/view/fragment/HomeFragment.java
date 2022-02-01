package com.app.swagliv.view.fragment;

import static com.yuyakaido.android.cardstackview.Direction.Left;
import static com.yuyakaido.android.cardstackview.Direction.Right;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.app.common.constant.AppCommonConstants;
import com.app.common.interfaces.APIResponseHandler;
import com.app.common.preference.AppPreferencesManager;
import com.app.common.utils.Utility;
import com.app.common.utils.api_response_handler.APIResponse;
import com.app.swagliv.R;
import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.constant.AppInstance;
import com.app.swagliv.databinding.FragmentHomeBinding;
import com.app.swagliv.model.home.pojo.DashboardBaseModel;
import com.app.swagliv.model.login.pojo.User;
import com.app.swagliv.view.activities.DashboardActivity;
import com.app.swagliv.view.activities.MatchActivity;
import com.app.swagliv.view.activities.SideBarActivity;
import com.app.swagliv.view.activities.SubscriptionActivity;
import com.app.swagliv.view.adaptor.CardStackAdapter;
import com.app.swagliv.viewmodel.dashboard.DashboardViewModel;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.RewindAnimationSetting;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment implements CardStackListener, View.OnClickListener, APIResponseHandler, MatchActivity.OnMatchButtonClickListener {

    //widgets
    private CardStackView mCardStackView;

    //variables
    private FragmentHomeBinding mBinding;
    private CardStackAdapter mCardStackAdapter;
    private CardStackLayoutManager manager;
    private ArrayList<User> mNearByPeoples = new ArrayList<>();
    private DashboardViewModel mViewModel;
    private User mSelectedUser;
    private boolean isProfileSuperLiked = false;

    public HomeFragment() {
        // Required empty public constructor prevent crash issue
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        initialize();
        return mBinding.getRoot();
    }

    private void initialize() {

        mViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        mViewModel.apiResponseMutableLiveData.observe(getActivity(), new Observer<APIResponse>() {
            @Override
            public void onChanged(APIResponse apiResponse) {
                onAPIResponseHandler(apiResponse);
            }
        });
        //---
        //-----------
        mCardStackView = mBinding.cardStackView;

        //----------
        mBinding.skipButton.setOnClickListener(this);
        mBinding.likeButton.setOnClickListener(this);
        mBinding.undoButton.setOnClickListener(this);
        mBinding.buttonContainer.setOnClickListener(this);
        mBinding.peoplesProfile.setOnClickListener(this);
        mBinding.filterIcon.setOnClickListener(this);
        mBinding.superLikeButton.setOnClickListener(this);
        mBinding.drawerIcon.setOnClickListener(this);
        //--------------
        setUpCardStackAdaptor();

    }

    private void setUpCardStackAdaptor() {
        manager = new CardStackLayoutManager(getContext(), this);
        manager.setStackFrom(StackFrom.Top);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(false);
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
        manager.setOverlayInterpolator(new LinearInterpolator());
        mCardStackView.setLayoutManager(manager);

        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        mCardStackView.setItemAnimator(defaultItemAnimator);

    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {
        int defaultChoice = direction.equals(Right) ? AppConstant.SWIPE.LIKE : AppConstant.SWIPE.DISLIKE;
        if (isProfileSuperLiked) {
            defaultChoice = AppConstant.SWIPE.SUPER_LIKE;
        }
        mViewModel.doUpdateActionOnPeople(defaultChoice, mSelectedUser.getId(), AppCommonConstants.API_REQUEST.REQUEST_ID_1002);
    }

    @Override
    public void onCardRewound() {
    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {
        if (AppInstance.getAppInstance().getAppUserCurrentSubscriptionPlan(getActivity()) == null) {
            if (position == AppConstant.SWIPE_LIMIT) {
                AppPreferencesManager.putString(AppConstant.SWIPE_LIMIT_EXCEED, Utility.convertDateToString(new Date(), AppCommonConstants.DATE_FORMAT_DEFAULT), getActivity());
                startActivity(new Intent(getContext(), SubscriptionActivity.class));
                getActivity().finish();
            }
        }
        mSelectedUser = mNearByPeoples.get(position);
        isProfileSuperLiked = false;
    }

    @Override
    public void onCardDisappeared(View view, int position) {
        if (position + 1 == mNearByPeoples.size()) {
            mBinding.peoplesProfile.setVisibility(View.GONE);
            mBinding.peoplesEmptyDataView.setVisibility(View.VISIBLE);
            mBinding.textViewEmptyPeopleMsg.setText(getString(R.string.profile_empty_msg));
//          mBinding.profileSettingBtn.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        mViewModel.doGetNearByPeoples(true, true, AppCommonConstants.API_REQUEST.REQUEST_ID_1001);

        mCardStackAdapter = new CardStackAdapter(getContext(), mNearByPeoples);
        mCardStackView.setAdapter(mCardStackAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.undo_button:
                RewindAnimationSetting setting = new RewindAnimationSetting.Builder()
                        .setDirection(Direction.Bottom)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(new DecelerateInterpolator())
                        .build();
                manager.setRewindAnimationSetting(setting);

                mCardStackView.rewind();
                break;
            case R.id.skip_button:
                SwipeAnimationSetting swipeSetting = new SwipeAnimationSetting.Builder()
                        .setDirection(Left)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(new AccelerateInterpolator())
                        .build();
                manager.setSwipeAnimationSetting(swipeSetting);
                mCardStackView.swipe();
                break;
            case R.id.like_button:
            case R.id.super_like_button:
                isProfileSuperLiked = v.getId() == R.id.super_like_button ? true : false;
                SwipeAnimationSetting likeSetting = new SwipeAnimationSetting.Builder()
                        .setDirection(Right)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(new AccelerateInterpolator())
                        .build();
                manager.setSwipeAnimationSetting(likeSetting);
                mCardStackView.swipe();
                break;
            case R.id.filter_icon:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                ViewGroup viewGroup = getView().findViewById(R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.layout_filter, viewGroup, false);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Window window = alertDialog.getWindow();
                WindowManager.LayoutParams windowManager = window.getAttributes();
                windowManager.gravity = Gravity.TOP;
                alertDialog.show();
                break;
            case R.id.drawerIcon:
                Intent i = new Intent(getActivity(), SideBarActivity.class);
                startActivity(i);
                break;
            case R.id.filter_back_btn:

        }
    }

    @Override
    public void onAPIResponseHandler(APIResponse apiResponse) {
        switch (apiResponse.status) {

            case LOADING:
                //------
                break;
            case SUCCESS:
                switch (apiResponse.requestID) {
                    case AppCommonConstants.API_REQUEST.REQUEST_ID_1001:
                        DashboardBaseModel.Data dashboardBaseModel = (DashboardBaseModel.Data) apiResponse.data;
                        AppInstance.getAppInstance().setAppUserCurrentSubscriptionPlan(dashboardBaseModel.getCurrentSubscribedPlan(), getActivity());
                        mNearByPeoples = dashboardBaseModel.getProfiles();
                        ArrayList<User> matchUser = dashboardBaseModel.getMatchedProfile();
                        if (matchUser != null && !matchUser.isEmpty()) {
                            MatchActivity.newInstance(getActivity(), matchUser.get(0), this).show();
                        } else {
                            if (mNearByPeoples != null && !mNearByPeoples.isEmpty()) {
                                mCardStackAdapter.updateData(mNearByPeoples);
                                mBinding.peoplesEmptyDataView.setVisibility(View.GONE);
                                mBinding.buttonContainer.setVisibility(View.VISIBLE);
                                //mBinding.backgroundLatout.setVisibility(View.VISIBLE);
                            } else {
                                mBinding.peoplesProfile.setVisibility(View.GONE);
                                mBinding.peoplesEmptyDataView.setVisibility(View.VISIBLE);
                                mBinding.textViewEmptyPeopleMsg.setText(getString(R.string.after_profile_disappered_msg));
//                              mBinding.profileSettingBtn.setVisibility(View.VISIBLE);
                            }
                        }
                        checkTotalLikedDoneForDay();
                        break;
                }
                break;
            case ERROR:
                //-------
                Utility.showToast(getContext(), getString(R.string.api_failure_error_msg));
                break;
            default:
                break;
        }
    }

    @Override
    public void onKeepSwiping() {
        mViewModel.doGetNearByPeoples(true, false, AppCommonConstants.API_REQUEST.REQUEST_ID_1001);
    }

    private void checkTotalLikedDoneForDay() {
        if (AppInstance.getAppInstance().getAppUserCurrentSubscriptionPlan(getActivity()) == null) {
            String dateStored = AppPreferencesManager.getString(AppConstant.SWIPE_LIMIT_EXCEED, getContext());
            if (dateStored != null) {
                Date lastDate = Utility.convertStringToDate(dateStored, AppCommonConstants.DATE_FORMAT_DEFAULT);
                Date latestDate = Utility.convertStringToDate(Utility.convertDateToString(new Date(), AppCommonConstants.DATE_FORMAT_DEFAULT), AppCommonConstants.DATE_FORMAT_DEFAULT);
                if (latestDate.compareTo(lastDate) == 0) {
                    startActivity(new Intent(getContext(), SubscriptionActivity.class));
                }
            }
        }
    }
}