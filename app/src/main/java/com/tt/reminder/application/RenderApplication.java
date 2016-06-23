package com.tt.reminder.application;

import android.app.Application;

import com.tt.sharedbaseclass.utils.RenderSharedPreference;

/**
 * Created by zhengguo on 6/22/16.
 */
public class RenderApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    RenderSharedPreference.getInstance(getApplicationContext()).setConfigBean();
//    String languageToLoad  = "zh";
//    Locale locale = new Locale(languageToLoad);
//    Locale.setDefault(locale);
//    Configuration config = getResources().getConfiguration();
//    DisplayMetrics metrics = getResources().getDisplayMetrics();
//    config.locale = Locale.SIMPLIFIED_CHINESE;
//    getResources().updateConfiguration(config, metrics);
  }
}
