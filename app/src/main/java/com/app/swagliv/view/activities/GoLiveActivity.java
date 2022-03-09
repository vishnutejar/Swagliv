package com.app.swagliv.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.swagliv.R;
import com.bambuser.broadcaster.BroadcastStatus;
import com.bambuser.broadcaster.Broadcaster;
import com.bambuser.broadcaster.CameraError;
import com.bambuser.broadcaster.ConnectionError;

public class GoLiveActivity extends AppCompatActivity {
    private static final String LOGTAG = "GoLiveActivity";

    private static final String APPLICATION_ID = "qndgxaaWOXMd4J1Bkie4ag";
    SurfaceView mPreviewSurface;
    Broadcaster mBroadcaster;

    RelativeLayout BroadcastButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_live);
        mPreviewSurface = findViewById(R.id.PreviewSurfaceView);
        BroadcastButton = findViewById(R.id.BroadcastButton);
        mBroadcaster = new Broadcaster(this, APPLICATION_ID, mBroadcasterObserver);
        mBroadcaster.setRotation(getWindowManager().getDefaultDisplay().getRotation());
        BroadcastButton.setOnClickListener(view -> {
            if (mBroadcaster.canStartBroadcasting()) {
                mBroadcaster.startBroadcast();
                mBroadcaster.switchCamera();
            } else
                mBroadcaster.stopBroadcast();
        });
        findViewById(R.id.img_switch).setOnClickListener(view -> mBroadcaster.switchCamera());
        findViewById(R.id.img_crossbutton).setOnClickListener(view -> {
            mBroadcaster.stopBroadcast();
            finish();
        });
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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO}, 1);
        else if (!hasPermission(Manifest.permission.RECORD_AUDIO))
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        else if (!hasPermission(Manifest.permission.CAMERA))
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        mBroadcaster.setCameraSurface(mPreviewSurface);
        mBroadcaster.onActivityResume();
        mBroadcaster.setRotation(getWindowManager().getDefaultDisplay().getRotation());

    }

    private boolean hasPermission(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private Broadcaster.Observer mBroadcasterObserver = new Broadcaster.Observer() {
        @Override
        public void onConnectionStatusChange(BroadcastStatus broadcastStatus) {
            Log.i(LOGTAG, "Received status change: " + broadcastStatus);
            if (broadcastStatus == BroadcastStatus.STARTING)
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            if (broadcastStatus == BroadcastStatus.IDLE)
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            //((Button) findViewById(R.id.BroadcastButton)).setText(broadcastStatus == BroadcastStatus.IDLE ? "Broadcast" : "Disconnect");
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

}