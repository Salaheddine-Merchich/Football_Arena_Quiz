package com.example.quizapp_merchich;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationHelper {
    private static final String TAG = "LocationHelper";

    public interface LocationResultCallback {
        void onResult(String locationInfo, String continent);
    }

    @SuppressLint("MissingPermission")
    public static void detectLocation(Activity activity, LocationResultCallback callback) {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);

        fusedLocationClient.getLastLocation().addOnSuccessListener(activity, location -> {
            if (location != null) {
                processLocation(activity, location, callback);
            } else {
                requestFreshLocation(activity, fusedLocationClient, callback);
            }
        }).addOnFailureListener(e -> {
            requestFreshLocation(activity, fusedLocationClient, callback);
        });
    }

    @SuppressLint("MissingPermission")
    private static void requestFreshLocation(Activity activity, FusedLocationProviderClient client, LocationResultCallback callback) {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMaxUpdates(1)
                .build();

        client.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null && locationResult.getLastLocation() != null) {
                    processLocation(activity, locationResult.getLastLocation(), callback);
                } else {
                    callback.onResult("Global Mode", "Global");
                }
            }
        }, Looper.getMainLooper());
    }

    private static void processLocation(Activity activity, Location location, LocationResultCallback callback) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address addr = addresses.get(0);
                String countryName = addr.getCountryName();
                String countryCode = addr.getCountryCode();
                String continent = ContinentHelper.getContinent(countryCode);
                
                String display = countryName + " — " + continent;
                callback.onResult(display, continent);
                return;
            }
        } catch (IOException e) {
            Log.e(TAG, "Geocoder error", e);
        }
        callback.onResult("Global Mode", "Global");
    }
}
