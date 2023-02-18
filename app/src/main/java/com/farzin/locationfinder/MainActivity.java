package com.farzin.locationfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import com.farzin.locationfinder.Location.LocationTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    double lat, lon;

    LocationTracker locationTracker;
    private static final int ACCESS_FINE_LOCATION_PERMISSION_CODE = 100;
    private static final int ACCESS_COARSE_LOCATION_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        locationTracker = new LocationTracker(getApplicationContext(), MainActivity.this);

        if (locationTracker.hasLocation()) {

            lat = locationTracker.getLatitude();
            lon = locationTracker.getLongitude();

            //تبدیل ادرس جغرافیایی به فیزیکی
            Geocoder geocoder = new Geocoder(getApplicationContext(), new Locale("fa"));
            List<Address> address;
            try {

                address = geocoder.getFromLocation(lat, lon, 1);
                String countryName = address.get(0).getCountryName();
                Toast.makeText(this, countryName, Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            locationTracker.showAlertSettings();
        }


        //مپ را وصل میکنیم به supportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        //مدل مپ های مختلف
//        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
//        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //نمایش موقیعت جاری
        googleMap.setMyLocationEnabled(true);
        //نمایش زوم کردن
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        //قطب نما
        googleMap.getUiSettings().setCompassEnabled(true);
        //اماکن چرخیدن
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        //تول بار
        googleMap.getUiSettings().setMapToolbarEnabled(true);

        //ایجاد مارکر بر روی مپ
        LatLng latLng = new LatLng(35.72564, 51.37231);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title("Android Learning")
                .snippet("Hello")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

        //مارکر را وصل میکنیم به گوگل مپ
        googleMap.addMarker(markerOptions);


        LatLng latLng2 = new LatLng(65.72564, 51.97231);
        MarkerOptions markerOptions2 = new MarkerOptions()
                .position(latLng2)
                .title("Home")
                .snippet("My Home")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        googleMap.addMarker(markerOptions2);


        //وصل مردن مارکر ها به یکدیگر
        googleMap.addPolyline(new PolylineOptions().add(latLng,latLng2).color(Color.parseColor("#ff0000"))
                .width(5));

        //اضافه کردن دایره
        googleMap.addCircle(new CircleOptions().radius(1000)
                .strokeColor(Color.parseColor("#ff0000")).center(latLng));

        //اضافه کردن محدوده
        googleMap.addPolygon(new PolygonOptions()
                .add(latLng).add(latLng2).add(new LatLng(21.8989,12.7777)).strokeColor(Color.RED));


        //زوم کردن روی مپ بر اساس لوکیشن
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == ACCESS_FINE_LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == ACCESS_COARSE_LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}