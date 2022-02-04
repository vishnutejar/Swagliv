package com.app.swagliv.image_upload_service;


import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.app.common.constant.AppCommonConstants;
import com.app.common.interfaces.APIResponseListener;
import com.app.common.utils.Utility;
import com.app.swagliv.R;
import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.constant.AppInstance;
import com.app.swagliv.model.login.pojo.User;
import com.app.swagliv.view.activities.DashboardActivity;
import com.app.swagliv.viewmodel.profile.repository.ProfileRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Service to handle uploading files to FireBase Storage.
 */
public class UserDocumentUploadService extends ForegroundServiceBaseTask implements APIResponseListener {

    private static final String TAG = UserDocumentUploadService.class.getSimpleName();

    /**
     * Intent Actions
     **/
    public static final String ACTION_UPLOAD_DOCUMENTS = "action_upload";
    public static final String DOCUMENT_UPLOAD_COMPLETED = "upload_document_completed";
    public static final String DOCUMENT_UPLOAD_ERROR = "upload_error";
    public static final String REFERENCE_FILE_NAME = "REFERENCE_FILE_NAME";
    public static final String DOCUMENT_STATUS_DATA = "DOCUMENT_STATUS_DATA";
    public static final String IMAGE_TYPE = "IMAGE_TYPE";

    /**
     * Intent Extras
     **/
    public static final String EXTRA_FILE_URI = "extra_file_uri";
    public static final String EXTRA_DOWNLOAD_URL = "extra_download_url";

    // [START declare_ref]
    private File mFileName;
    private HashSet<String> mUnUploadDriverDocuments;
    private Uri mFileUri;
    private int mImageType;
    private String mUploadedImageType;
    // [END declare_ref]

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mFileName = new File(intent.getStringExtra(UserDocumentUploadService.REFERENCE_FILE_NAME));
        mImageType = intent.getIntExtra(UserDocumentUploadService.IMAGE_TYPE, AppConstant.RequestCodes.PROFILE);
        if (ACTION_UPLOAD_DOCUMENTS.equals(intent.getAction())) {
            mFileUri = intent.getParcelableExtra(EXTRA_FILE_URI);
        }
        doUploadImage();
        return START_REDELIVER_INTENT;
    }

    /**
     * Broadcast finished upload (success or failure).
     *
     * @return true if a running receiver received the broadcast.
     */
    private boolean broadcastUploadFinished(@Nullable Uri downloadUrl, @Nullable Uri fileUri) {
        boolean success = downloadUrl != null;

        String action = success ? DOCUMENT_UPLOAD_COMPLETED : DOCUMENT_UPLOAD_ERROR;

        Intent broadcast = new Intent(action)
                .putExtra(EXTRA_DOWNLOAD_URL, downloadUrl.toString())
                .putExtra(REFERENCE_FILE_NAME, mFileName.getName())
                .putExtra(EXTRA_FILE_URI, fileUri);
        return LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(broadcast);
    }

    /**
     * Show a notification for a finished upload.
     */
    private void showUploadFinishedNotification(@Nullable Uri downloadUrl, @Nullable Uri fileUri, String message) {
        // Hide the progress notification
        dismissProgressNotification();
        Utility.printLogs("uploadmessage", message);
        // Make Intent to MainActivity
        Utility.showToast(this, message);

        Intent intent = new Intent(this, DashboardActivity.class)
                .putExtra(EXTRA_DOWNLOAD_URL, downloadUrl)
                .putExtra(EXTRA_FILE_URI, fileUri)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        boolean success = downloadUrl != null;
        String caption = success ? getString(R.string.upload_success) : getString(R.string.upload_failed);
        showFinishedNotification(caption, intent, success);
    }

    public static IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DOCUMENT_UPLOAD_COMPLETED);
        filter.addAction(DOCUMENT_UPLOAD_ERROR);
        return filter;
    }


    private void doUploadImage() {
        ProfileRepository profileRepository = new ProfileRepository();

        mUploadedImageType = mImageType == AppConstant.RequestCodes.PROFILE ? AppConstant.PROFILE_IMAGES : AppConstant.PERSONAL_IMAGES;

        profileRepository.doUploadImage(mFileName, mUploadedImageType, this, AppCommonConstants.API_REQUEST.REQUEST_ID_1001);

    }

    @Override
    public void onSuccess(Object callResponse, Integer requestID) {
        boolean success = mFileUri != null;
        String message = null;
        String action = success ? DOCUMENT_UPLOAD_COMPLETED : DOCUMENT_UPLOAD_ERROR;
        if (action.equalsIgnoreCase(DOCUMENT_UPLOAD_COMPLETED)) {
            String data = (String) callResponse;
            User appUserInstance = AppInstance.getAppInstance().getAppUserInstance(getApplicationContext());
            if (mUploadedImageType.equalsIgnoreCase(AppConstant.PROFILE_IMAGES)) {
                appUserInstance.setProfileImages(data);
                message = "Profile Image Uploaded";
            } else {
                ArrayList<String> personalImage = appUserInstance.getPersonalImage();
                if (personalImage == null) {
                    personalImage = new ArrayList<>();
                }
                personalImage.add(data);
                appUserInstance.setPersonalImage(personalImage);
                message = "Image Uploaded";

            }
            AppInstance.getAppInstance().setAppUserInstance(appUserInstance, getApplicationContext());
        }
        showUploadFinishedNotification(mFileUri, mFileUri, message);
    }

    @Override
    public void onFailure(Throwable error, Integer requestID) {
        showUploadFinishedNotification(null, mFileUri, "");
    }
}