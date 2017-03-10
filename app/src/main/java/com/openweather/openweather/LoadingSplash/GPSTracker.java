package com.openweather.openweather.LoadingSplash;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by andy6804tw on 2017/2/26.
 */

public class GPSTracker extends Service implements LocationListener {
    private final Context context;
    boolean isGPSEnable=false;
    boolean isNetworkEnabled=false;
    public boolean canGetLocation=false;
    Location location;
    double latitude;
    double longtitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES=10;
    private static final long MIN_TIME_BW_UPDATES=1000*60*1;
    protected LocationManager locationManager;
    public GPSTracker(Context context){
        this.context=context;
        getLocation();
    }
    public Location getLocation(){
        try{
            locationManager=(LocationManager)context.getSystemService(LOCATION_SERVICE);
            isGPSEnable=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(!isGPSEnable&&isNetworkEnabled){
                Log.e("Data55","2");
            }
            else{
                    this.canGetLocation=true;

                    if(isNetworkEnabled){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
                }
                if(locationManager!=null){
                    location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if(location!=null){
                        latitude=location.getLatitude();
                        longtitude=location.getLongitude();
                    }
                }
                if(isGPSEnable){
                    if(location==null){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
                        if(locationManager!=null){
                            location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if(location!=null){
                                latitude=location.getLatitude();
                                longtitude=location.getLongitude();
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    private void requestPermissions(String[] strings, int requestCode) {
        switch (requestCode) {
            case 10:
                // 如果裝置版本是6.0（包含）以上
                if (Build.VERSION.SDK_INT >= M) {
                    // 取得授權狀態，參數是請求授權的名稱
                    checkSelfPermission(
                            Manifest.permission.ACCESS_FINE_LOCATION);
                }
                break;
            default:
                break;
        }
    }

    public void stopUsingGPS(){

        if(locationManager!=null){
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    public  double getLatitude(){
        if(location!=null){
            latitude=location.getLatitude();
        }
        return  latitude;
    }

    public double getLongtitude(){
        if(location!=null){
            longtitude=location.getLongitude();
        }
        return  longtitude;
    }

    public boolean isCanGetLocation(){
        return this.canGetLocation;
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);
        alertDialog.setTitle("Gps is settings");
        alertDialog.setMessage("Gps is not enabled.");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {

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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}


