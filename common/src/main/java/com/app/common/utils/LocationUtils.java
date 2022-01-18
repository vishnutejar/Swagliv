package com.app.common.utils;

import static android.content.Context.LOCATION_SERVICE;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.annotation.NonNull;

import com.app.common.interfaces.LocationStatus;

public class LocationUtils implements LocationListener {

    //Constants
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 60 * 60 * 1000; // 1 hr
    private static final String TAG = LocationUtils.class.getSimpleName();

    //Variables
    private Context context;
    private LocationStatus locationStatus;
    private LocationManager mLocationManager;

    public LocationUtils(Context context, LocationStatus locationStatus) {
        this.context = context;
        this.locationStatus = locationStatus;
    }

    public Location getLocation() {
        Location location = null;
        try {
            mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            boolean mIsGPS = mLocationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = mLocationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!mIsGPS && !isNetworkEnabled) {
                // no network provider is enabled
            } else {

                try {
                    boolean canGetLocation = true;
                    if (isNetworkEnabled) {
                        mLocationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Utility.printLogs("Network", "Network Enabled");
                        if (mLocationManager != null) {
                            location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }
                    }
                    // if GPS Enabled get lat/long using GPS Services
                    if (mIsGPS) {
                        if (location == null) {
                            mLocationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Utility.printLogs("GPS", "GPS Enabled");
                            if (mLocationManager != null) {
                                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            }
                        }
                    }
                }catch (SecurityException e){
                    Utility.printLogs(TAG, "Location permission required");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        locationStatus.onLocationChanged(location);
    }
}
