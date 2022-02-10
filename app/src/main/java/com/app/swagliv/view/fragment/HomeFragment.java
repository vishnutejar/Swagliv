package com.app.swagliv.view.fragment;

import static com.yuyakaido.android.cardstackview.Direction.Left;
import static com.yuyakaido.android.cardstackview.Direction.Right;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.app.swagliv.view.activities.MatchedProfileDialog;
import com.app.swagliv.view.activities.SubscriptionActivity;
import com.app.swagliv.view.activities.UserProfileActivity;
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

import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment implements CardStackListener, View.OnClickListener, APIResponseHandler, MatchedProfileDialog.OnMatchButtonClickListener {

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

    private Spinner spin;
    private String gender;


    private String[] distance = new String[]{
            "0km-100km", "100km-200km", "200km-300km", "300km-400km", "400km-500km", "500km-100km", "2000km-1100km", "0km-100km"
    };


    private Dialog dialog;

    public HomeFragment() {
        // Required empty public constructor prevent crash issue
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        initialize();
        dialog = new Dialog(getContext());
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
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), UserProfileActivity.class);
                i.putExtra("Selected_User", mSelectedUser);
                startActivity(i);
            }
        });
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
                openDialog();
                break;
            case R.id.drawerIcon:
                DashboardActivity activity = (DashboardActivity) getActivity();
                activity.getDrawer().open();
                activity.getDrawer().setVisibility(View.VISIBLE);
                //Intent i = new Intent(getActivity(), SideBarActivity.class);
                //startActivity(i);
                break;

        }
    }

    private void openDialog() {
        dialog.setContentView(R.layout.layout_filter);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.TOP);
        dialog.show();
        TextView male = dialog.findViewById(R.id.male_unselected);
        TextView female = dialog.findViewById(R.id.female_unselected_text);
        TextView other = dialog.findViewById(R.id.shemale_unselected_text);
        ImageView back_btn = dialog.findViewById(R.id.filter_back_btn);
        ImageView check_btn = dialog.findViewById(R.id.filter_check_btn);
        spin = (Spinner) dialog.findViewById(R.id.spinner_txt);
        ArrayAdapter<String> adapter_Distance = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, distance);
        adapter_Distance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter_Distance);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male.setBackgroundResource(R.drawable.selected);
                female.setBackgroundResource(R.drawable.unselected);
                other.setBackgroundResource(R.drawable.unselected);
                gender = "male";
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                female.setBackgroundResource(R.drawable.selected);
                male.setBackgroundResource(R.drawable.unselected);
                other.setBackgroundResource(R.drawable.unselected);
                gender = "female";
            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                other.setBackgroundResource(R.drawable.selected);
                female.setBackgroundResource(R.drawable.unselected);
                male.setBackgroundResource(R.drawable.unselected);
                gender = "other";
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = spin.getSelectedItem().toString();
                Utility.printLogs("texte", text);
            }
        });
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
                            MatchedProfileDialog.newInstance(getActivity(), matchUser.get(0), this, mViewModel).show();
                        } else {
                            if (mNearByPeoples != null && !mNearByPeoples.isEmpty()) {
                                mCardStackAdapter.updateData(mNearByPeoples);
                                mBinding.peoplesEmptyDataView.setVisibility(View.GONE);
                                mBinding.buttonContainer.setVisibility(View.VISIBLE);
                                mBinding.backgroundLayout.setVisibility(View.VISIBLE);
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