package com.example.racelogic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;




import android.annotation.SuppressLint;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import android.widget.CheckBox;

import android.widget.CompoundButton;
import android.widget.TextView;




public class MainActivity extends AppCompatActivity implements IBaseGpsListener {


    private TextView txtCurrentSpeed;
    private TextView txtMetricUnits;
    private CheckBox isHeadUpModeOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isGeoDisabled()){


            CustomDialogFragment dialog = new CustomDialogFragment();
            dialog.show(getSupportFragmentManager(), "custom");

                  //



        }

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        txtCurrentSpeed = findViewById(R.id.txtCurrentSpeed);
        isHeadUpModeOn = findViewById(R.id.chkHeadUpModeOn);
        txtMetricUnits = findViewById(R.id.txtMetricUnits);



        if(checkLocationPermission()) {
            assert locationManager != null;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }

    }



        public boolean isGeoDisabled() {
        LocationManager mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            assert mLocationManager != null;
            boolean mIsGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean mIsNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            return !mIsGPSEnabled && !mIsNetworkEnabled;
    }


    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    @SuppressLint("SetTextI18n")
    private void updateSpeed(CLocation location) {
        // TODO Auto-generated method stub
        int nCurrentSpeed = 0;

        if(location != null)
        {
            location.setUseMetricunits(this.useMetricUnits());
            nCurrentSpeed = (int) location.getSpeed();
        } else {
            txtCurrentSpeed.setText("\t" +0 );
        }


        String strUnits = "m/h";
        if(this.useMetricUnits())
        {
            strUnits = "km/h";
        }

        isHeadUpModeOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    txtCurrentSpeed.setRotationX(180);
                } else
                {
                    txtCurrentSpeed.setRotationX(360);
                }
            }
        });


        txtCurrentSpeed.setText(String.valueOf(nCurrentSpeed));
        txtMetricUnits.setText(strUnits);
    }

    private boolean useMetricUnits() {
        // TODO Auto-generated method stub
        CheckBox chkUseMetricUnits = this.findViewById(R.id.chkMetricUnits);
        return !chkUseMetricUnits.isChecked();
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        if(location != null)
        {
            CLocation myLocation = new CLocation(location, this.useMetricUnits());
            this.updateSpeed(myLocation);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGpsStatusChanged(int event) {
        // TODO Auto-generated method stub

    }

}