package com.tt.sharedbaseclass.model;

import com.tt.sharedbaseclass.constant.Constant;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by zhengguo on 5/19/16.
 */
public class TaskBean implements Serializable {
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
  Calendar mCalendar;

  private static int DEFAULT_VALUE_OF_DATE_TIME = -1;
  private static int DEFAULT_VALUE_OF_INTERVAL = 0;


  public TaskBean() {

    this.mYear = DEFAULT_VALUE_OF_DATE_TIME;
    this.mMonth = DEFAULT_VALUE_OF_DATE_TIME;
    this.mDayOfMonth = DEFAULT_VALUE_OF_DATE_TIME;
    this.mHour = DEFAULT_VALUE_OF_DATE_TIME;
    this.mMinuse = DEFAULT_VALUE_OF_DATE_TIME;
    mTaskContent = "";
    mRepeatIntervalDays = DEFAULT_VALUE_OF_INTERVAL;
    mGroup = "";
    mCalendar = Calendar.getInstance(Locale.ENGLISH);
  }

  public String getTaskContent() {
    return mTaskContent;
  }

  public void setTaskContent(String taskContent) {
    this.mTaskContent = taskContent;
  }

  public void setYear(int year) {
    this.mYear = year;
  }

  public void setMonth(int month) {
    this.mMonth = month;
  }

  public void setDayOfMonth(int dayOfMonth) {
    this.mDayOfMonth = dayOfMonth;
  }

  public void setHour(int hour) {
    this.mHour = hour;
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

  public boolean isDeadline() {
    boolean isDeadLine = false;
    if (isClearedPickedTime() && !isClearedPickedDate()) {
      mCalendar.set(mYear, mMonth, mDayOfMonth);
      if (mCalendar.getTimeInMillis() <= System.currentTimeMillis()) {
        isDeadLine = true;
      } else {
        isDeadLine = false;
      }
    } else if (!isClearedPickedTime() && !isClearedPickedDate()){
      mCalendar.set(mYear, mMonth, mDayOfMonth, mHour, mMinuse);
      if (mCalendar.getTimeInMillis() <= System.currentTimeMillis()) {
        isDeadLine = true;
      } else {
        isDeadLine = false;
      }
    } else {
      isDeadLine = false;
    }
    return isDeadLine;
  }

  public boolean isIsRepeat() {
    return mIsRepeat;
  }

  public boolean isFinished() {
    return mIsFinished;
  }

  public String getPickedDate() {
    if (isClearedPickedDate()) {
      return null;
    }
    mCalendar.set(this.mYear, this.mMonth, this.mDayOfMonth);
    StringBuffer sb = new StringBuffer();
    sb.append(Constant.WEEK.valueOf(mCalendar.get(Calendar.DAY_OF_WEEK)))
            .append(", ")
            .append(Constant.MONTH.valueOf(mCalendar.get(Calendar.MONTH)))
            .append(" ")
            .append(this.mDayOfMonth)
            .append(", ")
            .append(this.mYear);
    return sb.toString();
  }

  public void clearPickedDate() {
    mYear = DEFAULT_VALUE_OF_DATE_TIME;
    mMonth = DEFAULT_VALUE_OF_DATE_TIME;
    mDayOfMonth = DEFAULT_VALUE_OF_DATE_TIME;
  }

  public boolean isClearedPickedDate() {
    return (mYear == DEFAULT_VALUE_OF_DATE_TIME
      || mMonth == DEFAULT_VALUE_OF_DATE_TIME
      || mDayOfMonth == DEFAULT_VALUE_OF_DATE_TIME);
  }

  public String getPickedTime() {
    if (isClearedPickedTime()) {
      return null;
    }
    StringBuffer sb = new StringBuffer();
    if (mHour < 10) {
      sb.append("0").append(mHour);
    } else {
      sb.append(mHour);
    }
    sb.append(":");
    if (mMinuse < 10) {
      sb.append("0").append(mMinuse);
    } else {
      sb.append(mMinuse);
    }
    return sb.toString();
  }

  public void clearPickedTime() {
    mHour = DEFAULT_VALUE_OF_DATE_TIME;
    mMinuse = DEFAULT_VALUE_OF_DATE_TIME;
  }

  public boolean isClearedPickedTime() {
    return (mHour == DEFAULT_VALUE_OF_DATE_TIME
      || mMinuse ==DEFAULT_VALUE_OF_DATE_TIME);
  }

  @Override
  public boolean equals(Object o) {
    TaskBean taskBean = (TaskBean)o;
    return (taskBean.mTaskContent.equals(mTaskContent)
      && taskBean.mYear == mYear
      && taskBean.mMonth == mMonth
      && taskBean.mDayOfMonth == mDayOfMonth
      && taskBean.mHour == mHour
      && taskBean.mMinuse == mMinuse
      && taskBean.mRepeatIntervalDays == mRepeatIntervalDays
      && taskBean.mGroup.equals(mGroup));
  }
}
