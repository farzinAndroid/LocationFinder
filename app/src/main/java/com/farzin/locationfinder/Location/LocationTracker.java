package com.farzin.locationfinder.Location;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.farzin.locationfinder.MainActivity;
import com.farzin.locationfinder.R;

import java.util.List;

public class LocationTracker extends Service implements LocationListener {

    //برای گرفتن لوکیشن نیاز به چند شی داریم
    Location location;
    LocationManager locationManager;
    Context context;
    Activity activity;

    //چک کردن برای اینکه gps روشن است یا نه
    boolean isGPSEnabled = false;
    //چک کردن برای اینکه اینترنت روشن است یا نه
    boolean isNetworkEnabled = true;
    //تایید کنیم که ایا میتونیم location را داشته باشین یا نه
    boolean canGetLocation;

    //طول جغرافیایی
    double longitude;
    //عرض جغرافیایی
    double latitude;

    static final long MIN_DISTANCE = 10;
    static final long MIN_TIME = 1000 * 60;

    public LocationTracker(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        getLocation();
    }


    public Location getLocation() {

        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        //gps check
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        //network check
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isNetworkEnabled && !isGPSEnabled) {
            canGetLocation = false;
        } else {
            canGetLocation = true;


            //اول network را میگیریم چون اگر اول لوکیشن رو بگیریم
            //دیتاها به جای یکدیگر قرار میگیرند

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    MIN_TIME, MIN_DISTANCE, this);

            if (locationManager != null) {

                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if (location != null) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                }
            }

            if (isGPSEnabled) {

                if (location == null) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_TIME, MIN_DISTANCE, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if (location != null) {
                            longitude = location.getLongitude();
                            latitude = location.getLatitude();
                        }
                    }
                }
            }
        }
        return location;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    //چک تا بفهمیم میتونیم لوکیشن را بگیریم یا نه
    public boolean hasLocation() {
        return canGetLocation;
    }

    //اگر هیچکدام از ایتم های ما (network and location) فعال نبود
    //پیغام نشان بدهد
    public void showAlertSettings() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle(R.string.warning);

        alertDialog.setMessage(R.string.gps_warning_massage);

        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
    }


    //
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //


    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}
