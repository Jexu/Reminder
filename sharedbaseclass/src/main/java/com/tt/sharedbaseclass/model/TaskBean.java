package com.tt.sharedbaseclass.model;

/**
 * Created by zhengguo on 5/19/16.
 */
public class TaskBean {
  private String mTaskContent;
  private int mYear;
  private int mMonth;
  private int mDayOfMonth;
  private int mHour;
  private int mMinuse;
  private int mRepeatIntervalDays;
  private String mGroup;
  private boolean mIsDeadline;
  private boolean mIsRepeat;
  private boolean mIsFinished;
  private String mPickedDate;
  private String mPickedTime;



  public TaskBean() {

  }

  public String getTaskContent() {
    return mTaskContent;
  }

  public void setTaskContent(String taskContent) {
    this.mTaskContent = taskContent;
  }

  public void setYear(int year) {
    this.mYear = mYear;
  }

  public void setMonth(int month) {
    this.mMonth = month;
  }

  public void setDayOfMonth(int dayOfMonth) {
    this.mDayOfMonth = dayOfMonth;
  }

  public void setHour(int mHour) {
    this.mHour = mHour;
  }

  public void setMinuse(int minuse) {
    this.mMinuse = minuse;
  }

  public int getRepeatIntervalDays() {
    return mRepeatIntervalDays;
  }

  public void setRepeatIntervalDays(int repeatIntervalDays) {
    this.mRepeatIntervalDays = repeatIntervalDays;
  }

  public String getGroup() {
    return mGroup;
  }

  public void setGroup(String group) {
    this.mGroup = group;
  }

  public boolean ismIsDeadline() {
    return mIsDeadline;
  }

  public boolean ismIsRepeat() {
    return mIsRepeat;
  }

  public boolean ismIsFinished() {
    return mIsFinished;
  }

  public String getPickedDate() {
    return mPickedDate;
  }

  public String getPickedTime() {
    return mPickedTime;
  }

}
