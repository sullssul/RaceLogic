package com.example.racelogic;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


import android.annotation.SuppressLint;

import android.content.Context;


import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.CheckBox;

import android.widget.CompoundButton;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements IBaseGpsListener {


    private TextView txtCurrentSpeed;
    private TextView txtMetricUnits;
    private CheckBox isHeadUpModeOn;
    private CheckBox isDarkModeOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isGeoDisabled()) {
            CustomDialogFragment dialog = new CustomDialogFragment();
            dialog.show(getSupportFragmentManager(), "custom");
        }

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        txtCurrentSpeed = findViewById(R.id.txtCurrentSpeed);
        txtMetricUnits = findViewById(R.id.txtMetricUnits);
        isHeadUpModeOn = findViewById(R.id.chkHeadUpModeOn);
        isDarkModeOn = findViewById(R.id.chkDarkMode);

        checkDarkMode();

        if (checkLocationPermission()) {
            assert locationManager != null;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    private void checkDarkMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            isDarkModeOn.setVisibility(View.VISIBLE);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode());

            isDarkModeOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    } else {
                        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }

                }
            });
        } else {
            isDarkModeOn.setVisibility(View.GONE);
        }
    }


    public boolean isGeoDisabled() {
        LocationManager mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        assert mLocationManager != null;
        boolean mIsGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean mIsNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return !mIsGPSEnabled && !mIsNetworkEnabled;
    }


    public boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    @SuppressLint("SetTextI18n")
    private void updateSpeed(CLocation location) {
        int nCurrentSpeed = 0;

        if (location != null) {
            location.setUseMetricUnits(this.useMetricUnits());
            nCurrentSpeed = (int) location.getSpeed();
        } else {
            txtCurrentSpeed.setText("\t" + 0);
        }


        String strUnits = "m/h";
        if (this.useMetricUnits()) {
            strUnits = "km/h";
        }

        isHeadUpModeOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtCurrentSpeed.setRotationX(180);
                } else {
                    txtCurrentSpeed.setRotationX(360);
                }
            }
        });


        txtCurrentSpeed.setText(String.valueOf(nCurrentSpeed));
        txtMetricUnits.setText(strUnits);
    }

    private boolean useMetricUnits() {
        CheckBox chkUseMetricUnits = this.findViewById(R.id.chkMetricUnits);
        return !chkUseMetricUnits.isChecked();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            CLocation myLocation = new CLocation(location, this.useMetricUnits());
            this.updateSpeed(myLocation);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onGpsStatusChanged(int event) {

    }

}