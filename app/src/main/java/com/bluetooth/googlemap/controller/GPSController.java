package com.bluetooth.googlemap.controller;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bluetooth.googlemap.PermissionUtils;
import com.google.android.gms.maps.model.LatLng;

import java.util.Observable;

public class GPSController extends Observable implements LocationListener {
    private static final String TAG = "GPS";
    private LocationManager lm = null;
    private boolean enabled = false;
    private Location current = null;
    private AppCompatActivity activity;

    private boolean isEnabled() {
        return enabled;
    }

    public void setLocationManager(LocationManager lm) {
        this.lm = lm;
    }

    public boolean enable(AppCompatActivity activity) {
        this.activity = activity;
        if (this.lm == null) return false;

        if (ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission(activity, 1, Manifest.permission.ACCESS_FINE_LOCATION, false);
        }

        this.enabled = true;
        return this.enabled;
    }


    public void startLocationUpdates() {
        try {
            if (ActivityCompat.checkSelfPermission(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                this.lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 900000, 0, this);
                this.lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 900000, 0, this);
            }
        } catch (NullPointerException n) {
            Log.w(TAG, "Request for location before handling it.");
        }
    }


    public void stopLocationUpdates() {
        this.lm.removeUpdates(this);
    }


    public LatLng getCurrentPosition() {
        if (!this.isEnabled() || this.current == null) {
            Criteria criteria = new Criteria();
            String bestProvider = lm.getBestProvider(criteria, false);
            Location lastKnownLocation = null;

            try {
                if (ActivityCompat.checkSelfPermission(activity,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    lastKnownLocation = this.lm.getLastKnownLocation(bestProvider);
                }
            } catch (NullPointerException n) {
                Log.w(TAG, "Request for location before handling it.");
            }

            return (lastKnownLocation == null) ? new LatLng(0, 0) :
                    new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
        }
        return new LatLng(this.current.getLatitude(), this.current.getLongitude());
    }

    @Override
    public void onLocationChanged(Location location) {
        this.current = location;
        this.setChanged();
        this.notifyObservers(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
