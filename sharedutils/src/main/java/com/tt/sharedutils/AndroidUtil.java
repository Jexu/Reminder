package com.tt.sharedutils;

import android.os.Build;

/**
 * Created by zhengguo on 5/13/16.
 */
public class AndroidUtil {

    public static int getAndroidVersion() {
        return Build.VERSION.SDK_INT;
    }
}
