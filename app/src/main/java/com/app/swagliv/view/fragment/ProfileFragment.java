package com.app.swagliv.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.app.common.preference.AppPreferencesManager;
import com.app.common.utils.FileUtils;
import com.app.common.utils.Utility;
import com.app.swagliv.R;
import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.constant.AppInstance;
import com.app.swagliv.databinding.FragmentProfileBinding;
import com.app.swagliv.image_upload_service.UserDocumentUploadService;
import com.app.swagliv.model.login.pojo.User;
import com.app.swagliv.model.profile.pojo.PersonalImages;
import com.app.swagliv.view.activities.DashboardActivity;
import com.app.swagliv.view.activities.EditProfileActivity;
import com.app.swagliv.view.activities.LoginActivity;
import com.app.swagliv.view.activities.SubscriptionActivity;
import com.app.swagliv.view.adaptor.PicturesAttachmentAdapter;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import net.alhazmy13.mediapicker.Image.ImagePicker;
import net.alhazmy13.mediapicker.Video.VideoPicker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment implements View.OnClickListener, DashboardActivity.SelectLocationImage, PicturesAttachmentAdapter.OnImageSelectedListener {

    private FragmentProfileBinding mBinding;
    private User mUser;
    private PicturesAttachmentAdapter picturesAttachmentAdapter;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(container.getContext()), R.layout.fragment_profile, container, false);
        initialize();
        return mBinding.getRoot();

    }


    private void initialize() {


        //------------

        mUser = AppInstance.getAppInstance().getAppUserInstance(getContext());
        mBinding.setUser(mUser);
        mBinding.mobile.setText(String.valueOf(mUser.getContactNumber()));
        mBinding.picturesView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        DashboardActivity.initializeListener(this);
        mBinding.currentPlan.setOnClickListener(this);
        mBinding.passionBtnContinue.setOnClickListener(this);
        mBinding.profileEditTxt.setOnClickListener(this);
        mBinding.showMeMenTxt.setOnClickListener(this);
        mBinding.btnEditPofile.setOnClickListener(this);
        mBinding.icCamera.setOnClickListener(this);
        mBinding.icVideo.setOnClickListener(this);
        mBinding.currentLocation.setOnClickListener(this);

      /*  mBinding.commonHedder.headerTitle.setText(getString(R.string.txt_profile));
        mBinding.commonHedder.headerLayout.setBackgroundResource(R.color.violate200);
        mBinding.commonHedder.backBtn.setVisibility(View.GONE);*/

        //ToDo set string value in current plan

       /* if (peoplesInfo.getCurrentSubscribedPlan() == null) {
            mBinding.currentPlan.setText(getString(R.string.free));
        } else {
            mBinding.currentPlan.setText(peoplesInfo.getCurrentSubscribedPlan().getSubscriptionName());
        }*/
        if (mUser.getProfileImages() != null) {
            Glide.with(this).load(mUser.getProfileImages()).into(mBinding.profileImage);
        }

        ArrayList<String> personalImage = mUser.getPersonalImage();
        if (personalImage != null && !personalImage.isEmpty()) {
            List<PersonalImages> personalImagesList = new ArrayList<>();
            for (String urlOfImages : personalImage) {
                PersonalImages p = new PersonalImages();
                p.setImagesURI(null);
                p.setUrl(urlOfImages);
                personalImagesList.add(p);
            }
            setPersonalImages(null, personalImagesList, 1);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEditPofile:
            case R.id.ic_camera:
                new ImagePicker.Builder(getActivity())
                        .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                        .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                        .extension(ImagePicker.Extension.PNG)
                        .setRequestCode(v.getId() == R.id.btnEditPofile ? AppConstant.RequestCodes.PROFILE : AppConstant.RequestCodes.OTHER_IMAGES)
                        .scale(600, 600)
                        .allowMultipleImages(v.getId() == R.id.btnEditPofile ? false : true)
                        .enableDebuggingMode(false)
                        .build();
                break;
            case R.id.ic_video:
                new VideoPicker.Builder(getActivity())
                        .mode(VideoPicker.Mode.CAMERA_AND_GALLERY)
                        .directory(VideoPicker.Directory.DEFAULT)
                        .extension(VideoPicker.Extension.MP4)
                        .enableDebuggingMode(false)
                        .build();
                break;
            case R.id.current_plan:
                startActivity(new Intent(getContext(), SubscriptionActivity.class));
                break;
            case R.id.passion_btn_continue:
                logout();
                break;
            case R.id.profile_edit_txt:
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.show_me_men_txt:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                ViewGroup viewGroup = getView().findViewById(R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.genderselection, viewGroup, false);
                TextView show = dialogView.findViewById(R.id.txt_show_me);
                show.setVisibility(View.VISIBLE);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
                dialogView.findViewById(R.id.men_text).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBinding.showMeMenTxt.setText(AppConstant.MEN);
                        alertDialog.dismiss();
                    }
                });
                dialogView.findViewById(R.id.women_text).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBinding.showMeMenTxt.setText(AppConstant.WOMEN);
                        alertDialog.dismiss();
                    }
                });
                break;
            case R.id.current_location:
                break;


        }
    }

    private void logout() {
        // logout from facebook
        // LoginManager.getInstance().logOut();
        // logout from gmail

        GoogleSignIn.getClient(getContext(), GoogleSignInOptions.DEFAULT_SIGN_IN).signOut().addOnSuccessListener(aVoid -> {
            //-----------
            AppPreferencesManager.clearSharedPref(getContext());
            Intent login = new Intent(getContext(), LoginActivity.class);
            login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(login);
        })
                .addOnFailureListener(e -> Utility.showToast(getContext(), getString(R.string.err_logout_failed)));

    }

    @Override
    public void getSelectedLocationImage(List<Uri> list, int requestCode) {
        switch (requestCode) {
            case AppConstant.RequestCodes.PROFILE:
            case AppConstant.RequestCodes.OTHER_IMAGES:
                if (AppConstant.RequestCodes.PROFILE == requestCode) {
                    mBinding.profileImage.setImageURI(list.get(0));
                    File file = FileUtils.getFile(getActivity(), list.get(0));
                    getActivity().startService(new Intent(getContext(), UserDocumentUploadService.class)
                            .putExtra(UserDocumentUploadService.EXTRA_FILE_URI, list.get(0))
                            .putExtra(UserDocumentUploadService.IMAGE_TYPE, requestCode)
                            .putExtra(UserDocumentUploadService.REFERENCE_FILE_NAME, file.toString())
                            .setAction(UserDocumentUploadService.ACTION_UPLOAD_DOCUMENTS));
                } else {
                    setPersonalImages(list, null, 0);

                    for (PersonalImages uri :
                            picturesAttachmentAdapter.getSelectedPhotosList()) {
                        if (uri.getImagesURI() != null) {
                            File file = FileUtils.getFile(getActivity(), uri.getImagesURI());
                            getActivity().startService(new Intent(getContext(), UserDocumentUploadService.class)
                                    .putExtra(UserDocumentUploadService.EXTRA_FILE_URI, uri.getImagesURI())
                                    .putExtra(UserDocumentUploadService.IMAGE_TYPE, requestCode)
                                    .putExtra(UserDocumentUploadService.REFERENCE_FILE_NAME, file.toString())
                                    .setAction(UserDocumentUploadService.ACTION_UPLOAD_DOCUMENTS));
                        }

                    }
                }
                // Start MyUploadService to upload the file, so that the file is uploaded
                // even if this Activity is killed or put in the background

                break;
        }
    }


    // type 0 -> URI, 1-> images
    public void setPersonalImages(List<Uri> list, List<PersonalImages> personalImagesList, int type) {

        if (type == 0) {
            personalImagesList = new ArrayList<>();
            for (Uri obj :
                    list) {
                PersonalImages p = new PersonalImages();
                p.setImagesURI(obj);
                p.setUrl(null);
                personalImagesList.add(p);
            }
        }

        if (picturesAttachmentAdapter == null) {
            picturesAttachmentAdapter = new PicturesAttachmentAdapter(getContext(), personalImagesList, this);
            mBinding.picturesView.setAdapter(this.picturesAttachmentAdapter);
            mBinding.otherImagesParentLayout.setVisibility(picturesAttachmentAdapter.getSelectedPhotosList().isEmpty() ? View.GONE : View.VISIBLE);
        } else {
            personalImagesList.addAll(picturesAttachmentAdapter.getSelectedPhotosList());
            picturesAttachmentAdapter.updateSelectedPhotosList(personalImagesList, null);
        }
    }

    @Override
    public void imageListUpdated() {
        mBinding.otherImagesParentLayout.setVisibility(picturesAttachmentAdapter.getSelectedPhotosList().isEmpty() ? View.GONE : View.VISIBLE);
    }


}