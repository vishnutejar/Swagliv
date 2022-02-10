package com.app.swagliv.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.app.common.constant.AppCommonConstants;
import com.app.common.interfaces.APIResponseHandler;
import com.app.common.interfaces.DatePickerDialogListener;
import com.app.common.preference.AppPreferencesManager;
import com.app.common.utils.FileUtils;
import com.app.common.utils.Utility;
import com.app.common.utils.api_response_handler.APIResponse;
import com.app.swagliv.R;
import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.constant.AppInstance;
import com.app.swagliv.databinding.ActivityUserProfileBinding;
import com.app.swagliv.image_upload_service.UserDocumentUploadService;
import com.app.swagliv.model.home.pojo.Passions;
import com.app.swagliv.model.login.pojo.LoginResponseBaseModel;
import com.app.swagliv.model.login.pojo.User;
import com.app.swagliv.viewmodel.profile.ProfileViewModel;
import com.bumptech.glide.Glide;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity implements DatePickerDialogListener, View.OnClickListener, APIResponseHandler {

    // constants
    private static final int REQUEST_CODE_FOR_PASSIONS_SELECTION = 200;

    // variables
    private ActivityUserProfileBinding mProfileBinding;
    private ArrayList<Passions> mSelectedPassionList = new ArrayList<>();
    private User mUser;
    private ProfileViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);
        User crush = getIntent().getParcelableExtra("crushList");
        User mSelectedUser = getIntent().getParcelableExtra("Selected_User");
        mUser = AppInstance.getAppInstance().getAppUserInstance(this);
        if (mSelectedUser != null) {
            mProfileBinding.setUser(mSelectedUser);
            Glide.with(this).load(mSelectedUser.getProfileImages()).into(mProfileBinding.profileImage);
            ArrayList<Passions> passions = mSelectedUser.getPassions();
            if (passions != null && !passions.isEmpty()) {
                boolean isFirst = true;
                StringBuilder stringBuilder = new StringBuilder();

                for (Passions p : passions) {
                    stringBuilder.append((isFirst ? "" : ", ") + p.getLabel());
                    isFirst = false;
                }
                mProfileBinding.passionTxt.setText(stringBuilder);
            }
            mProfileBinding.btnEditPofile.setVisibility(View.GONE);
            mProfileBinding.profileContinueBtn.setVisibility(View.INVISIBLE);
            mProfileBinding.addPasssionBtn.setVisibility(View.GONE);
            mProfileBinding.changePassword.setVisibility(View.GONE);
            mProfileBinding.hedderProfileName.setEnabled(false);
            mProfileBinding.genderTxt.setEnabled(false);
            mProfileBinding.profileDateSelected.setEnabled(false);
            mProfileBinding.sliderRange.setEnabled(false);
            mProfileBinding.CurrentLocation.setEnabled(false);
            mProfileBinding.profileNameText.setEnabled(false);
            mProfileBinding.profileDateSelected.setEnabled(false);
            mProfileBinding.aboutMeTxt.setEnabled(false);

        } else if (crush != null) {
            mProfileBinding.setUser(crush);
            Glide.with(this).load(crush.getProfileImages()).into(mProfileBinding.profileImage);
            mProfileBinding.btnEditPofile.setVisibility(View.GONE);
            mProfileBinding.profileContinueBtn.setVisibility(View.INVISIBLE);
            mProfileBinding.addPasssionBtn.setVisibility(View.GONE);
            mProfileBinding.changePassword.setVisibility(View.GONE);
            mProfileBinding.hedderProfileName.setEnabled(false);
            mProfileBinding.genderTxt.setEnabled(false);
            mProfileBinding.profileDateSelected.setEnabled(false);
            mProfileBinding.sliderRange.setEnabled(false);
            mProfileBinding.CurrentLocation.setEnabled(false);
            mProfileBinding.profileNameText.setEnabled(false);
            mProfileBinding.profileDateSelected.setEnabled(false);
            mProfileBinding.aboutMeTxt.setEnabled(false);
        } else {
            mProfileBinding.setUser(mUser);
        }

        if (mUser.getType() == AppCommonConstants.LOGIN_TYPE.GMAIL) {
            mProfileBinding.changePassword.setVisibility(View.GONE);
        }
        mProfileBinding.commonHeader.backBtn.setOnClickListener(this);
        mProfileBinding.addPasssionBtn.setOnClickListener(this);
        mProfileBinding.genderTxt.setOnClickListener(this);
        mProfileBinding.btnEditPofile.setOnClickListener(this);
        mProfileBinding.profileContinueBtn.setOnClickListener(this);
        mProfileBinding.profileDateSelected.setOnClickListener(this);

        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        mViewModel.mutableLiveData.observe(this, new Observer<APIResponse>() {
            @Override
            public void onChanged(APIResponse apiResponse) {
                onAPIResponseHandler(apiResponse);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                AppPreferencesManager.putString(AppConstant.PREFERENCE_KEYS.CURRENT_USER_PROFILE_STATUS, AppConstant.PROFILE_STATUS.PROFILE_COMPLETED, this);
                Intent intent = new Intent(UserProfileActivity.this, DashboardActivity.class);
                startActivity(intent);
                break;
            case R.id.profile_date_selected:
                Utility utility = new Utility(UserProfileActivity.this);
                utility.datePickerDialog(UserProfileActivity.this, this, new Date());
                break;
            case R.id.gender_txt:
                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
                ViewGroup viewGroup = findViewById(R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.showgenderselection, viewGroup, false);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogView.findViewById(R.id.male_txt).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProfileBinding.genderSelect.setText(AppCommonConstants.GENDER_MALE);
                        alertDialog.dismiss();
                    }
                });

                dialogView.findViewById(R.id.female_txt).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProfileBinding.genderSelect.setText(AppCommonConstants.GENDER_FEMALE);
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
                break;
            case R.id.add_passsion_btn:
                Intent intent1 = new Intent(UserProfileActivity.this, PassionSelectionActivity.class);
                if (mSelectedPassionList != null && !mSelectedPassionList.isEmpty())
                    intent1.putExtra(AppConstant.INTENT_KEYS.SELECTED_PASSIONS, mSelectedPassionList);
                startActivityForResult(intent1, REQUEST_CODE_FOR_PASSIONS_SELECTION);
                break;
            case R.id.profile_continue_btn:
                AppPreferencesManager.putString(AppConstant.PREFERENCE_KEYS.CURRENT_USER_PROFILE_STATUS, AppConstant.PROFILE_STATUS.PROFILE_COMPLETED, this);
                if (!mProfileBinding.profileNameText.getText().toString().trim().isEmpty()) {
                    mUser.setName(mProfileBinding.profileNameText.getText().toString());
                    mUser.setDob(mProfileBinding.selectedDate.getText().toString());
                    mUser.setGender(mProfileBinding.genderSelect.getText().toString());
                    mUser.setGender(mProfileBinding.genderSelect.getText().toString());
                    mUser.setPassions(mSelectedPassionList);
                    mUser.setAboutMe(mProfileBinding.aboutMeTxt.getText().toString());
                    mViewModel.doUpdateProfile(mUser, AppCommonConstants.API_REQUEST.REQUEST_ID_1001);
                }
                break;

            case R.id.btnEditPofile:
                new ImagePicker.Builder(this)
                        .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                        .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                        .extension(ImagePicker.Extension.PNG)
                        .setRequestCode(AppConstant.RequestCodes.PROFILE)
                        .scale(600, 600)
                        .allowMultipleImages(false)
                        .enableDebuggingMode(false)
                        .build();
                break;

//                startActivity(new Intent(UserProfileActivity.this, DashboardActivity.class));
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOR_PASSIONS_SELECTION && data != null) {
            mSelectedPassionList = data.getParcelableArrayListExtra(AppConstant.INTENT_KEYS.SELECTED_PASSIONS);
            if (mSelectedPassionList != null && !mSelectedPassionList.isEmpty()) {
                boolean isFirst = true;
                StringBuilder stringBuilder = new StringBuilder();

                for (Passions p : mSelectedPassionList) {
                    stringBuilder.append((isFirst ? "" : ", ") + p.getLabel());
                    isFirst = false;
                }
                mProfileBinding.passionTxt.setText(stringBuilder);
            }
        } else if (resultCode == Activity.RESULT_OK && (requestCode == AppConstant.RequestCodes.PROFILE)) {
            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
            List<Uri> uriList = new ArrayList<>();
            for (String path : mPaths) {
                uriList.add(Uri.fromFile(new File(path)));
            }
            if (AppConstant.RequestCodes.PROFILE == requestCode) {
                mProfileBinding.profileImage.setImageURI(uriList.get(0));
                File file = FileUtils.getFile(this, uriList.get(0));
                startService(new Intent(this, UserDocumentUploadService.class)
                        .putExtra(UserDocumentUploadService.EXTRA_FILE_URI, uriList.get(0))
                        .putExtra(UserDocumentUploadService.IMAGE_TYPE, requestCode)
                        .putExtra(UserDocumentUploadService.REFERENCE_FILE_NAME, file.toString())
                        .setAction(UserDocumentUploadService.ACTION_UPLOAD_DOCUMENTS));
            }
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
                        if (apiResponse.data instanceof String) {
                            String message = (String) apiResponse.data;
                            Utility.showToast(this, message);
                        } else {
                            LoginResponseBaseModel profileUpdate = (LoginResponseBaseModel) apiResponse.data;
                            User appUserInstance = AppInstance.getAppInstance().getAppUserInstance(this);
                            if (appUserInstance.getProfileImages() != null) {
                                profileUpdate.getUser().setProfileImages(appUserInstance.getProfileImages());
                            }
                            AppInstance.getAppInstance().setAppUserInstance(profileUpdate.getUser(), this);
                            Intent intent = new Intent(UserProfileActivity.this, DashboardActivity.class);
                            startActivity(intent);
                        }
                        break;
                }
                break;
            case ERROR:
                Utility.showToast(this, getString(R.string.api_failure_error_msg));
                break;
        }

    }

    @Override
    public void getSelectedDate(String selectedDate) {
        mProfileBinding.selectedDate.setText(selectedDate);
    }
}