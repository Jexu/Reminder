package com.tt.sharedbaseclass.model;

/**
 * Created by zhengguo on 6/17/16.
 */
public class FeedbackBean {
  private String mComment;
  private String mUserMail;
  private String mOsVersion;
  private String mDeviceName;
  private String mAndroidVersion;

  public FeedbackBean(String mComment, String mUserMail, String mOsVersion, String mDeviceName, String mAndroidVersion) {
    this.mComment = mComment;
    this.mUserMail = mUserMail;
    this.mOsVersion = mOsVersion;
    this.mDeviceName = mDeviceName;
    this.mAndroidVersion = mAndroidVersion;
  }

  public String getOsVersion() {
    return mOsVersion;
  }

  public void setOsVersion(String osVersion) {
    this.mOsVersion = osVersion;
  }

  public String getDeviceName() {
    return mDeviceName;
  }

  public void setDeviceName(String deviceName) {
    this.mDeviceName = deviceName;
  }

  public String getAndroidVersion() {
    return mAndroidVersion;
  }

  public void setAndroidVersion(String androidVersion) {
    this.mAndroidVersion = androidVersion;
  }

  public String getComment() {
    return mComment;
  }

  public void setComment(String comment) {
    this.mComment = comment;
  }

  public String getUserMail() {
    return mUserMail;
  }

  public void setUserMail(String userMail) {
    this.mUserMail = userMail;
  }
}
