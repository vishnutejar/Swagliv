package com.app.swagliv.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.common.constant.AppCommonConstants;
import com.app.common.interfaces.APIResponseHandler;
import com.app.common.utils.Utility;
import com.app.common.utils.api_response_handler.APIResponse;
import com.app.swagliv.R;
import com.app.swagliv.databinding.FragmentMessageBinding;
import com.app.swagliv.model.chat.pojo.UserChats;
import com.app.swagliv.view.adaptor.ChatAdapter;
import com.app.swagliv.viewmodel.chats.repository.ChatsViewModel;

import java.util.ArrayList;

public class MessageFragment extends Fragment implements View.OnClickListener, APIResponseHandler {
    private FragmentMessageBinding mBinding;
    private ChatsViewModel mViewModel;
    private ChatAdapter mAdapter;
    private ArrayList<UserChats> userChatsArrayList;

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false);
        mViewModel = new ViewModelProvider(this).get(ChatsViewModel.class);
        mBinding.commonHeader.headerTitle.setText(R.string.txt_chat);
        mBinding.commonHeader.headerLayout.setBackgroundResource(R.color.screen_background);
        mBinding.commonHeader.backBtn.setVisibility(View.GONE);
        mViewModel.mutableLiveData.observe(getActivity(), new Observer<APIResponse>() {
            @Override
            public void onChanged(APIResponse apiResponse) {
                onAPIResponseHandler(apiResponse);
            }
        });

        mBinding.chatView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ChatAdapter(getContext(), userChatsArrayList);
        mBinding.chatView.setAdapter(mAdapter);
        mViewModel.getChatList(getContext(), "getChatDetail.json", AppCommonConstants.API_REQUEST.REQUEST_ID_1001);
        return mBinding.getRoot();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onAPIResponseHandler(APIResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                break;
            case SUCCESS:
                switch (apiResponse.requestID) {
                    case AppCommonConstants.API_REQUEST.REQUEST_ID_1001:
                        userChatsArrayList = (ArrayList<UserChats>) apiResponse.data;
                        mAdapter.updateData(userChatsArrayList);
                }
                break;
            case ERROR:
                Utility.showToast(getContext(), getString(R.string.api_failure_error_msg));
                break;
        }
    }
}