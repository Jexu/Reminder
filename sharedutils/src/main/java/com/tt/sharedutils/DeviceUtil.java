package com.tt.sharedutils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by zhengguo on 5/13/16.
 */
public class DeviceUtil {

    public static int getScreenWidth( Context context ) {
        WindowManager windowManager = ( WindowManager ) context.getSystemService( Context.WINDOW_SERVICE );
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize( point );
        return point.x;
    }

    public static int getScreenHeight( Context context ) {
        WindowManager windowManager = ( WindowManager ) context.getSystemService( Context.WINDOW_SERVICE );
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize( point );
        return point.y;
    }

    public static String getDeviceId( Context context ) {
        String deviceId = Settings.Secure.getString( context.getContentResolver(), Settings.Secure.ANDROID_ID );
        if ( deviceId == null || deviceId.equals( " " ) ) {
            deviceId = Build.SERIAL;
            if ( deviceId == null || deviceId.equals( " " ) ) {
                deviceId = getAppInstallTime( context ) + context.getPackageName();
                if ( deviceId == null || deviceId.equals( " " ) ) {
                    Log.e( "TAG", "DeviceId is null" );
                } else {
                    Log.i( "DeviceId", "DeviceId is from appInstallTime+packageName" );
                }
            } else {
                Log.i( "DeviceId", "DeviceId is from Build.SERIAL" );
            }
        } else {
            Log.i( "DeviceId", "DeviceId is from Secure.ANDROID_ID" );
        }
        return deviceId;
    }

    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getDeviceName() {
        return Build.MODEL;
    }

    public static long getAppInstallTime( Context context ) {
        long retval = -1;
        try {
            retval = context.getPackageManager().getPackageInfo( context.getPackageName(), 0 ).firstInstallTime;
        } catch ( PackageManager.NameNotFoundException e ) {
            e.printStackTrace();
        }

        return retval;
    }

    public static boolean isNetAvailable( Context context ) {
        ConnectivityManager cm = ( ConnectivityManager ) context.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static boolean isWifiConnection( Context context ) {
        ConnectivityManager cm = ( ConnectivityManager ) context.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo networkInfo = cm.getNetworkInfo( ConnectivityManager.TYPE_WIFI );
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean isMobileConnection( Context context ) {
        ConnectivityManager cm = ( ConnectivityManager ) context.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo networkInfo = cm.getNetworkInfo( ConnectivityManager.TYPE_MOBILE );
        return networkInfo != null && networkInfo.isConnected();
    }

}