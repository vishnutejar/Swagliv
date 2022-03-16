package com.app.swagliv.view.fragment;

import static tvo.webrtc.ContextUtils.getApplicationContext;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.app.common.constant.AppCommonConstants;
import com.app.common.interfaces.APIResponseHandler;
import com.app.common.preference.AppPreferencesManager;
import com.app.common.utils.Utility;
import com.app.common.utils.api_response_handler.APIResponse;
import com.app.swagliv.R;
import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.constant.AppInstance;
import com.app.swagliv.model.home.pojo.DashboardBaseModel;
import com.app.swagliv.model.livestream.OneSignalNotification;
import com.app.swagliv.model.livestream.OneSignalNotificationSender;
import com.app.swagliv.model.livestream.pojo.AllLiveStreamCommentsResp;
import com.app.swagliv.model.livestream.pojo.StartLiveStreamResp;
import com.app.swagliv.model.login.pojo.User;
import com.app.swagliv.view.activities.BroadcastPlayerActivity;
import com.app.swagliv.view.activities.MatchedProfileDialog;
import com.app.swagliv.viewmodel.dashboard.DashboardViewModel;
import com.app.swagliv.viewmodel.livestream.LiveStreamViewModel;
import com.bambuser.broadcaster.BroadcastStatus;
import com.bambuser.broadcaster.Broadcaster;
import com.bambuser.broadcaster.CameraError;
import com.bambuser.broadcaster.ConnectionError;
import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GoLiveActivity extends Fragment implements APIResponseHandler {
    private static final String LOGTAG = "GoLiveActivity";
    private static final String API_KEY = "AymJuqtonAkJfAMQg5YKKm";
    private static final String APPLICATION_ID = "pZLjLsZY8QPXUPsrTr56pA";
    SurfaceView mPreviewSurface;
    Broadcaster mBroadcaster;
    RelativeLayout BroadcastButton;
    Context context;
    final OkHttpClient mOkHttpClient = new OkHttpClient();

    private LiveStreamViewModel liveStreamViewModel;
    StartLiveStreamResp startLiveStreamResp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        liveStreamViewModel = new ViewModelProvider(this).get(LiveStreamViewModel.class);
        liveStreamViewModel.responseMutableLiveData.observe(getActivity(), new Observer<APIResponse>() {
            @Override
            public void onChanged(APIResponse apiResponse) {
                onAPIResponseHandler(apiResponse);
            }
        });

        assert container != null;
        context = container.getContext();
        View view1 = inflater.inflate(R.layout.activity_go_live, container, false);
        mPreviewSurface = view1.findViewById(R.id.PreviewSurfaceView);
        BroadcastButton = view1.findViewById(R.id.BroadcastButton);
        mBroadcaster = new Broadcaster((Activity) context, APPLICATION_ID, mBroadcasterObserver);
        mBroadcaster.setRotation(((Activity) context).getWindowManager().getDefaultDisplay().getRotation());
        BroadcastButton.setOnClickListener(view -> {
            if (mBroadcaster.canStartBroadcasting()) {
                mBroadcaster.startBroadcast();
                mBroadcaster.switchCamera();
                //OneSignalNotificationSender.sendDeviceNotification(new OneSignalNotification());
                getLatestResourceUri();
            } else
                mBroadcaster.stopBroadcast();
        });
        view1.findViewById(R.id.img_switch).setOnClickListener(view -> mBroadcaster.switchCamera());
        view1.findViewById(R.id.img_crossbutton).setOnClickListener(view -> {
            mBroadcaster.stopBroadcast();
            getActivity().onBackPressed();

        });
        return view1;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mBroadcaster.onActivityDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBroadcaster.onActivityPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hasPermission(Manifest.permission.CAMERA)
                && !hasPermission(Manifest.permission.RECORD_AUDIO))
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO}, 1);
        else if (!hasPermission(Manifest.permission.RECORD_AUDIO))
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        else if (!hasPermission(Manifest.permission.CAMERA))
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, 1);
        mBroadcaster.setCameraSurface(mPreviewSurface);
        mBroadcaster.onActivityResume();
        mBroadcaster.setRotation(((Activity) context).getWindowManager().getDefaultDisplay().getRotation());

    }

    private boolean hasPermission(String permission) {
        return ActivityCompat.checkSelfPermission(((Activity) context), permission) == PackageManager.PERMISSION_GRANTED;
    }

    private Broadcaster.Observer mBroadcasterObserver = new Broadcaster.Observer() {
        @Override
        public void onConnectionStatusChange(BroadcastStatus broadcastStatus) {
            Log.i(LOGTAG, "Received status change: " + broadcastStatus);
            if (broadcastStatus == BroadcastStatus.STARTING)
                ((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            if (broadcastStatus == BroadcastStatus.IDLE)
                ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        @Override
        public void onStreamHealthUpdate(int i) {
        }

        @Override
        public void onConnectionError(ConnectionError connectionError, String s) {
            Log.w(LOGTAG, "Received connection error: " + connectionError + ", " + s);
        }

        @Override
        public void onCameraError(CameraError cameraError) {
            Log.w(LOGTAG, "Received camera error: " + cameraError);
        }

        @Override
        public void onChatMessage(String s) {
        }

        @Override
        public void onResolutionsScanned() {
        }

        @Override
        public void onCameraPreviewStateChanged() {
        }

        @Override
        public void onBroadcastInfoAvailable(String s, String s1) {
            // Toast.makeText(GoLiveActivity.this, "onBroadcastInfoAvailable " + s + "  " + s1, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBroadcastIdAvailable(String s) {
            //     Toast.makeText(GoLiveActivity.this, "onBroadcastIdAvailable " + s + "  ", Toast.LENGTH_SHORT).show();
        }

    };

    void getLatestResourceUri() {
        Request request = new Request.Builder()
                .url("https://api.bambuser.com/broadcasts")
                .addHeader("Accept", "application/vnd.bambuser.v1+json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + API_KEY)
                .get()
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                String body = response.body().string();
                String resourceUri = null;
                try {
                    JSONObject json = new JSONObject(body);
                    JSONArray results = json.getJSONArray("results");
                    JSONObject latestBroadcast = results.optJSONObject(0);
                    resourceUri = latestBroadcast.optString("resourceUri");
                    String finalResourceUri = resourceUri;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            liveStreamViewModel.startLiveStream(finalResourceUri, AppCommonConstants.API_REQUEST.REQUEST_ID_1022);
                        }
                    });

                } catch (Exception exception) {

                }
                final String uri = resourceUri;

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
                    case AppCommonConstants.API_REQUEST.REQUEST_ID_1022:
                        startLiveStreamResp = (StartLiveStreamResp) apiResponse.data;
                        if (startLiveStreamResp != null) {
                            AppPreferencesManager.putString(AppConstant.PREFERENCE_KEYS.LiveStreamId, startLiveStreamResp.getData().getLiveStreamId(), getActivity());
                            //AppPreferencesManager.putString(AppConstant.PREFERENCE_KEYS.LiveStreamId, "livestreamid_8231085860", getActivity());
                            //startActivity(new Intent(getActivity(), BroadcastPlayerActivity.class));
                        }
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
}