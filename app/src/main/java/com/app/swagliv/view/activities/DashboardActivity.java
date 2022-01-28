package com.app.swagliv.view.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.app.common.interfaces.GPSUtilsGetGPSStatus;
import com.app.common.utils.Utility;
import com.app.swagliv.R;
import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.databinding.ActivityDashboadBinding;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;

public class DashboardActivity extends AppCompatActivity implements GPSUtilsGetGPSStatus, LocationListener {

    // variables
    private ActivityDashboadBinding binding;
    private boolean mIsLocationPermissionGranted, isGPS, mIsNeedToCheckGPS = true;
    private Location mCurrentLocation = null;
    public static SelectLocationImage selectLocationImage;

    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboad);

        //----------
        setUpBottomNavigation();


    }

    private void setUpBottomNavigation() {
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_like, R.id.navigation_message, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navigation, navController);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsLocationPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!mIsLocationPermissionGranted) {
            callGPSAllowActivity(true);
        } else {
            mCurrentLocation = Utility.getLastLocation(this, (LocationManager) this.getSystemService(LOCATION_SERVICE));

            //---------
            mIsNeedToCheckGPS = getIntent().getBooleanExtra(AppConstant.INTENT_KEYS.IS_NEED_TO_CHECK_GPS, true);
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (mIsNeedToCheckGPS && !isGPS) callGPSAllowActivity(false);
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //---------------
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && mIsNeedToCheckGPS) {
            callGPSAllowActivity(false);
        }
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        mCurrentLocation = location;
    }

    @Override
    public void receivedGPSStatus(boolean isGPSEnable) {
        if (!isGPSEnable && mIsNeedToCheckGPS) {
            callGPSAllowActivity(false);
        }
    }

    private void callGPSAllowActivity(boolean isCallForLocation) {
        if (!LocationGPSAllowPermissionActivity.isIsActivityVisible()) {
            LocationGPSAllowPermissionActivity.setIsActivityVisible(true);
            startActivity(new Intent(this, LocationGPSAllowPermissionActivity.class).putExtra(AppConstant.INTENT_KEYS.IS_CALL_FOR_LOCATION_PERMISSION, isCallForLocation).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && (requestCode == AppConstant.RequestCodes.OTHER_IMAGES || requestCode == AppConstant.RequestCodes.PROFILE)) {
            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
            List<Uri> uriList = new ArrayList<>();
            for (String path : mPaths) {
                uriList.add(Uri.fromFile(new File(path)));
            }
            selectLocationImage.getSelectedLocationImage(uriList, requestCode);
        }
    }


    public interface SelectLocationImage {
        void getSelectedLocationImage(List<Uri> uriList, int requestCode);
    }

    public static void initializeListener(SelectLocationImage listener) {
        selectLocationImage = listener;
    }

}