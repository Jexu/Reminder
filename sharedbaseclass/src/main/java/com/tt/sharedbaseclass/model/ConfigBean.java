package com.tt.sharedbaseclass.model;

/**
 * Created by zhengguo on 6/23/16.
 */
public class ConfigBean extends RenderBeanBase {

  public static final String IS_NOTIFICATION_ENABLE = "mIsNotificationEnable";
  public static final String IS_NOTIFICATION_LIGHT_DISABLE = "mIsNotificationLightDisable";

  private static boolean mIsNotificationEnable;
  private static boolean mIsNotificationLightDisable;

  private ConfigBean() {

  }

  public static boolean isNotificationEnable() {
    return mIsNotificationEnable;
  }

  public static void setIsNotificationEnable(boolean isNotificationEnable) {
    mIsNotificationEnable = isNotificationEnable;
  }

  public static boolean isNotificationLightDisable() {
    return mIsNotificationLightDisable;
  }

  public static void setIsNotificationLightDisable(boolean isNotificationLightDisable) {
    mIsNotificationLightDisable = isNotificationLightDisable;
  }



  @Override
  public int compareTo(Object another) {
    return 0;
  }
}
