package com.app.swagliv.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.app.common.constant.AppCommonConstants;
import com.app.common.interfaces.APIResponseHandler;
import com.app.common.utils.Utility;
import com.app.common.utils.api_response_handler.APIResponse;
import com.app.swagliv.R;
import com.app.swagliv.constant.AppInstance;
import com.app.swagliv.databinding.FragmentLikeBinding;
import com.app.swagliv.model.login.pojo.User;
import com.app.swagliv.model.profile.pojo.Subscription;
import com.app.swagliv.view.activities.DashboardActivity;
import com.app.swagliv.view.activities.SubscriptionActivity;
import com.app.swagliv.view.adaptor.LikeAdapter;
import com.app.swagliv.viewmodel.dashboard.DashboardViewModel;

import java.util.ArrayList;


public class LikeFragment extends Fragment implements View.OnClickListener, APIResponseHandler {
    private FragmentLikeBinding mBinding;
    private LikeAdapter likeAdapter;
    private ArrayList<User> mPeopleLike = new ArrayList<>();
    private DashboardViewModel mDashboardViewModel;
    private Subscription mSubscription;


    public LikeFragment() {
        // Required empty public constructor prevent crash issue
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_like, container, false);
        DashboardActivity activity = (DashboardActivity) getActivity();
        activity.getDrawer().close();
        initialize();
        return mBinding.getRoot();
    }

    private void initialize() {
        mSubscription = AppInstance.getAppInstance().getAppUserCurrentSubscriptionPlan(getActivity());

        mDashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        mDashboardViewModel.apiResponseMutableLiveData.observe(getActivity(), new Observer<APIResponse>() {
            @Override
            public void onChanged(APIResponse apiResponse) {
                onAPIResponseHandler(apiResponse);
            }
        });
        //----
        mBinding.seeLikesBtn.setVisibility(mSubscription == null ? View.VISIBLE : View.GONE);
        mBinding.seeLikesBtn.setOnClickListener(this);
        mBinding.commonHeader.headerTitle.setText(R.string.title_Likes);
        mBinding.commonHeader.headerLayout.setBackgroundResource(R.color.dark_pink);
        mBinding.commonHeader.backBtn.setVisibility(View.GONE);
        //----
        mBinding.profileWhoLike.setLayoutManager(new GridLayoutManager(getContext(), 2));
        likeAdapter = new LikeAdapter(getContext(), mPeopleLike, mSubscription);
        mBinding.profileWhoLike.setAdapter(likeAdapter);
        mDashboardViewModel.getUserLikes(AppCommonConstants.API_REQUEST.REQUEST_ID_1001);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.see_Likes_Btn:
                Intent i = new Intent(getContext(), SubscriptionActivity.class);
                startActivity(i);
                break;

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
                        mPeopleLike = (ArrayList<User>) apiResponse.data;
                        likeAdapter.updateData(mPeopleLike);
                        if (mPeopleLike != null) {
                            mBinding.likeTxt.setText(mPeopleLike.size() + " " + getActivity().getString(R.string.title_Likes));
                        }
                }
                break;
            case ERROR:
                Utility.showToast(getContext(), getString(R.string.api_failure_error_msg));
                break;
        }

    }
}
