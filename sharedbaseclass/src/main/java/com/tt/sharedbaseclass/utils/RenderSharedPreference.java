package com.tt.sharedbaseclass.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.tt.sharedbaseclass.model.ConfigBean;

import java.util.Set;

/**
 * Created by zhengguo on 6/23/16.
 */
public class RenderSharedPreference {

  private final String RENDER_SHARED_PREFERENCE_FILE_NAME = "com.tt.reminder.sp";

  private SharedPreferences mSp;
  private SharedPreferences.Editor mEditor;

  private RenderSharedPreference(Context context) {
    mSp = context.getSharedPreferences(RENDER_SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
    mEditor = mSp.edit();
  }

  public static RenderSharedPreference getInstance(Context context) {
    return new RenderSharedPreference(context);
  }

  public void putString(String key, String value) {
    mEditor.putString(key, value);
    mEditor.commit();
  }

  public void putInt(String key, int value) {
    mEditor.putInt(key, value);
    mEditor.commit();
  }

  public void putBoolean(String key, boolean value) {
    mEditor.putBoolean(key, value);
    mEditor.commit();
  }

  public void putFloat(String key, float value) {
    mEditor.putFloat(key, value);
    mEditor.commit();
  }

  public void putLong(String key, long value) {
    mEditor.putLong(key, value);
    mEditor.commit();
  }

  public void putStringSet(String key, Set<String> value) {
    mEditor.putStringSet(key, value);
    mEditor.commit();
  }

  public static final String DEFAULT_STRING_VALUE = "";

  public String getString(String key) {
    return mSp.getString(key, DEFAULT_STRING_VALUE);
  }

  public static final int DEFAULT_INT_VALUE = -0x111111;

  public int getInt(String key) {
    return mSp.getInt(key, DEFAULT_INT_VALUE);
  }

  public static final boolean DEFAULT_BOOLEAN_VALUE = false;

  public boolean getBoolean(String key) {
    return mSp.getBoolean(key, DEFAULT_BOOLEAN_VALUE);
  }

  public static final float DEFAULT_FLOAT_VALUE = Float.MAX_VALUE;

  public float getFloat(String key) {
    return mSp.getFloat(key, DEFAULT_FLOAT_VALUE);
  }

  public static final long DEFAULT_LONG_VALUE = Long.MAX_VALUE;

  public long getLong(String key) {
    return mSp.getLong(key, DEFAULT_LONG_VALUE);
  }

  public Set<String> getStringSet(String key, Set<String> value) {
    return mSp.getStringSet(key, null);
  }

  public void setConfigBean() {
    if (mSp.getAll() == null || mSp.getAll().isEmpty()) {
      putBoolean(ConfigBean.IS_NOTIFICATION_ENABLE, true);
      putBoolean(ConfigBean.IS_NOTIFICATION_LIGHT_DISABLE, false);
    }
    ConfigBean.setIsNotificationEnable(getBoolean(ConfigBean.IS_NOTIFICATION_ENABLE));
    ConfigBean.setIsNotificationLightDisable(getBoolean(ConfigBean.IS_NOTIFICATION_LIGHT_DISABLE));
  }


}
