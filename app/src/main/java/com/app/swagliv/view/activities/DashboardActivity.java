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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.app.common.interfaces.GPSUtilsGetGPSStatus;
import com.app.common.utils.Utility;
import com.app.swagliv.R;
import com.app.swagliv.SocketChatApplication;
import com.app.swagliv.constant.AppConstant;
import com.app.swagliv.constant.AppInstance;
import com.app.swagliv.databinding.ActivityDashboadBinding;
import com.google.android.material.navigation.NavigationView;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class DashboardActivity extends AppCompatActivity implements GPSUtilsGetGPSStatus, LocationListener {

    // variables
    private ActivityDashboadBinding binding;
    private boolean mIsLocationPermissionGranted, isGPS, mIsNeedToCheckGPS = true;
    private Location mCurrentLocation = null;
    public static SelectLocationImage selectLocationImage;
    private AppBarConfiguration mAppBarConfiguration;

    private Socket mSocket;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboad);

        setupNavDrawer();
        //----------
        setUpBottomNavigation();
        mSocket = SocketChatApplication.doConnect();

        if (AppInstance.getAppInstance().getAppUserInstance(this) != null) {
            Utility.printLogs("APP", "firede event");
            mSocket.emit(AppConstant.CHAT.ADD_USER, AppInstance.getAppInstance().getAppUserInstance(this).getId());

        }
        mSocket.on(AppConstant.CHAT.USER_STATUS, OnUserStatus);
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

    private void setupNavDrawer() {
        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_edit_profile, R.id.nav_search_filter, R.id.nav_go_premium, R.id.nav_purchase_history, R.id.nav_dark_mode, R.id.nav_help_faq, R.id.nav_sign_out)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        //NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setItemIconTintList(null);
//        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawer.closeDrawers();
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        //Toast.makeText(DashboardActivity.this, ""+menuItem.toString(), Toast.LENGTH_SHORT).show();
                        Intent i = null;
                        switch (menuItem.toString()) {
                            case "Purchase History":
                                i = new Intent(getApplicationContext(), PurchaseHistoryActivity.class);
                                startActivity(i);
                                break;
                            case "Help/FAQ":
                                i = new Intent(getApplicationContext(), HelpAndFaqActivity.class);
                                startActivity(i);
                                break;
                            case "Search and Filter":
                                i = new Intent(getApplicationContext(), SearchCrushActivity.class);
                                startActivity(i);
                                break;
                            case "Go Premium":
                                i = new Intent(getApplicationContext(), SubscriptionActivity.class);
                                startActivity(i);
                                break;
                            case "Edit Profile":
                                i = new Intent(getApplicationContext(), EditProfileActivity.class);
                                startActivity(i);
                                break;
                        }
                        return true;
                    }
                });
        drawer.setVisibility(View.GONE);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                Utility.printLogs("adasd", "" + slideOffset);
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                drawer.setVisibility(View.GONE);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dasboard_nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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

    Emitter.Listener OnUserStatus = new io.socket.emitter.Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.e("test", "" + args);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    Log.e("test", "" + jsonObject);

                }
            });
        }
    };

    public DrawerLayout getDrawer() {
        return drawer;
    }
}