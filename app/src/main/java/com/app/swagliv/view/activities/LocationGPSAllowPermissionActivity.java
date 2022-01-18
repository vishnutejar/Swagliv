package com.app.swagliv.view.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.app.common.constant.AppCommonConstants;
import com.app.common.interfaces.GPSUtilsGetGPSStatus;
import com.app.common.utils.Utility;
import com.app.swagliv.R;
import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.databinding.ActivityLocationGpsallowPermissionBinding;

public class LocationGPSAllowPermissionActivity extends AppCompatActivity implements GPSUtilsGetGPSStatus {

    // variables
    private boolean mIsCallForLocationPermission;
    private ActivityLocationGpsallowPermissionBinding mBinding;
    private static boolean isActivityVisible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_location_gpsallow_permission);
        //--------------
        mIsCallForLocationPermission = getIntent().getBooleanExtra(AppConstant.INTENT_KEYS.IS_CALL_FOR_LOCATION_PERMISSION, false);
        mBinding.setIsForLocationPermission(mIsCallForLocationPermission);
    }

    public void onAllowClick(View view) {
        if (mIsCallForLocationPermission) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                            final Intent i = new Intent();
                            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            i.addCategory(Intent.CATEGORY_DEFAULT);
                            i.setData(Uri.parse("package:" + getPackageName()));
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            startActivity(i);
                        } else {
                            Utility.checkLocationPermission(this);
                        }
                    } else {
                        Utility.checkLocationPermission(this);
                    }
                }
            }
        } else {
            Utility.doAskToTurnGPS(this, this);
        }
    }

    @Override
    public void receivedGPSStatus(boolean isGPSEnable) {
        if (isGPSEnable) {
            if (!mIsCallForLocationPermission) callDashboard(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //---------
        if (mIsCallForLocationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                callDashboard(true);
            }
        }
    }

    private void callDashboard(boolean flag) {
        isActivityVisible = false;
        Intent main = new Intent(this, DashboardActivity.class);
        main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        main.putExtra(AppConstant.INTENT_KEYS.IS_NEED_TO_CHECK_GPS, flag);
        startActivity(main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AppCommonConstants.RUNTIME_PERMISSION_REQUEST_CODE.GPS_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    if (!mIsCallForLocationPermission) callDashboard(false);
                } else {
                    // TODO after denied the permission
                    //Utility.showToast(LocationGPSAllowPermissionActivity.this, getString(R.string.err_gps_off));
                    if (!mIsCallForLocationPermission) callDashboard(false);
                }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //---------------
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (!mIsCallForLocationPermission) callDashboard(false);
        }
    }

    public static boolean isIsActivityVisible() {
        return isActivityVisible;
    }

    public static void setIsActivityVisible(boolean isActivityVisible) {
        LocationGPSAllowPermissionActivity.isActivityVisible = isActivityVisible;
    }

    @Override
    public boolean onNavigateUp() {
        isActivityVisible = false;
        return super.onNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isActivityVisible = false;
    }
}