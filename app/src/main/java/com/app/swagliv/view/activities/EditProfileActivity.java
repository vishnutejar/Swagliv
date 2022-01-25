package com.app.swagliv.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.app.common.constant.AppCommonConstants;
import com.app.common.interfaces.APIResponseHandler;
import com.app.common.interfaces.DatePickerDialogListener;
import com.app.common.utils.Utility;
import com.app.common.utils.api_response_handler.APIResponse;
import com.app.swagliv.R;
import com.app.swagliv.constant.AppInstance;
import com.app.swagliv.databinding.ActivityEditProfileBinding;
import com.app.swagliv.model.login.pojo.LoginResponseBaseModel;
import com.app.swagliv.model.login.pojo.User;
import com.app.swagliv.viewmodel.profile.ProfileViewModel;

import java.util.Date;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialogListener, APIResponseHandler {

    // variables
    private ActivityEditProfileBinding mBinding;
    private ProfileViewModel mProfileViewModel;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        mUser = AppInstance.getAppInstance().getAppUserInstance(this);
        mBinding.EditHeader.backBtn.setOnClickListener(this);
        mBinding.setUser(mUser);
        mBinding.editMobileTxt.setText(String.valueOf(mUser.getContactNumber()));

        //--------
        mBinding.datePickIc.setOnClickListener(this);
        mBinding.saveBtn.setOnClickListener(this);

        mProfileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        mProfileViewModel.mutableLiveData.observe(this, new Observer<APIResponse>() {
            @Override
            public void onChanged(APIResponse apiResponse) {
                onAPIResponseHandler(apiResponse);
            }
        });
        mBinding.EditHeader.headerLayout.setBackgroundResource(R.color.dark_pink);
        mBinding.EditHeader.headerTitle.setText(getString(R.string.title_edit));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_pick_ic:
                Utility utility = new Utility(this);
                utility.datePickerDialog(EditProfileActivity.this, this, new Date());
                break;
            case R.id.back_btn:
                finish();
                break;
            case R.id.save_btn:
                if (!mBinding.editNameText.getText().toString().trim().isEmpty()) {
                    if (!mBinding.editMobileTxt.getText().toString().trim().isEmpty() && Utility.checkContactNo(mBinding.editMobileTxt.getText().toString())) {
                        mUser.setName(mBinding.editNameText.getText().toString());
                        mUser.setContactNumber(mBinding.editMobileTxt.getText().toString());
                        mUser.setDob(mBinding.birthDateText.getText().toString());
                        mProfileViewModel.doUpdateProfile(mUser, AppCommonConstants.API_REQUEST.REQUEST_ID_1001);
                    }
                }
                break;
            default:
                break;

        }
    }

    @Override
    public void getSelectedDate(String selectedDate) {
        mBinding.birthDateText.setText(selectedDate);
    }

    @Override
    public void onAPIResponseHandler(APIResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                break;
            case SUCCESS:
                switch (apiResponse.requestID) {
                    case AppCommonConstants.API_REQUEST.REQUEST_ID_1001:
                        LoginResponseBaseModel profileEdit = (LoginResponseBaseModel) apiResponse.data;
                        AppInstance.getAppInstance().setAppUserInstance(profileEdit.getUser(), this);
                        Intent intent = new Intent(EditProfileActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        break;
                }
                break;
            case ERROR:
                Utility.showToast(this, getString(R.string.api_failure_error_msg));
                break;
        }
    }
}