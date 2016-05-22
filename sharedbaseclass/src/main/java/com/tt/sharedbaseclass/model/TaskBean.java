package com.tt.sharedbaseclass.model;

import com.tt.sharedbaseclass.constant.Constant;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by zhengguo on 5/19/16.
 */
public class TaskBean implements Serializable {
  private int mId;
  private String mTaskContent;
  private int mYear;
  private int mMonth;
  private int mDayOfMonth;
  private int mHour;
  private int mMinute;
  private long mTimeInMillis;
  private int mRepeatInterval;
  private int mRepeatUnit;
  private String mGroup;
  private boolean mIsFinished;
  private Calendar mCalendar;

  public static int DEFAULT_VALUE_OF_DATE_TIME = -1;
  public static int DEFAULT_VALUE_OF_INTERVAL = 0;


  public TaskBean() {

    mTaskContent = "";
    this.mYear = DEFAULT_VALUE_OF_DATE_TIME;
    this.mMonth = DEFAULT_VALUE_OF_DATE_TIME;
    this.mDayOfMonth = DEFAULT_VALUE_OF_DATE_TIME;
    this.mHour = DEFAULT_VALUE_OF_DATE_TIME;
    this.mMinute = DEFAULT_VALUE_OF_DATE_TIME;
    mRepeatInterval = DEFAULT_VALUE_OF_INTERVAL;
    mRepeatUnit = Constant.REPEAT_UNIT.MINUTE.value();
    mGroup = "";
    mCalendar = Calendar.getInstance(Locale.ENGLISH);
  }

  public int getId() {
    return mId;
  }

  public void setId(int id) {
    this.mId = id;
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

  public int getYear() {
    return this.mYear;
  }

  public void setMonth(int month) {
    this.mMonth = month;
  }

  public int getMonth() {
    return this.mMonth;
  }

  public void setDayOfMonth(int dayOfMonth) {
    this.mDayOfMonth = dayOfMonth;
  }

  public int getDayOfMonth() {
    return this.mDayOfMonth;
  }

  public void setHour(int hour) {
    this.mHour = hour;
  }

  public int getHour() {
    return this.mHour;
  }

  public void setMinuse(int minuse) {
    this.mMinute = minuse;
  }

  public int getMinute() {
    return this.mMinute;
  }

  public long getTimeInMillis() {
    if (isClearedPickedDate() || isClearedPickedTime()) {
      return DEFAULT_VALUE_OF_DATE_TIME;
    }
    mCalendar.set(mYear, mMonth, mDayOfMonth, mHour, mMinute);
    return mCalendar.getTimeInMillis();
  }

  public void setRepeatInterval(int repeatInterval) {
    this.mRepeatInterval = repeatInterval;
  }

  public int getRepeatInterval() {
    return mRepeatInterval;
  }

  public int getRepeatUnit() {
    return mRepeatUnit;
  }

  public void setRepeatUnit(int repeatUnit) {
    this.mRepeatUnit = repeatUnit;
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
      mCalendar.set(mYear, mMonth, mDayOfMonth, mHour, mMinute);
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

  public boolean isFinished() {
    return mIsFinished;
  }

  public String getPickedDate() {
    if (isClearedPickedDate()) {
      return null;
    }
    mCalendar.set(this.mYear, this.mMonth, this.mDayOfMonth);
    StringBuffer sb = new StringBuffer();
    sb.append(Constant.WEEK.valueOf(mCalendar.get(Calendar.DAY_OF_WEEK) - 1))
            .append(", ")
            .append(Constant.MONTH.valueOf(mCalendar.get(Calendar.MONTH) + 1))
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
    if (mMinute < 10) {
      sb.append("0").append(mMinute);
    } else {
      sb.append(mMinute);
    }
    return sb.toString();
  }

  public void clearPickedTime() {
    mHour = DEFAULT_VALUE_OF_DATE_TIME;
    mMinute = DEFAULT_VALUE_OF_DATE_TIME;
  }

  public boolean isClearedPickedTime() {
    return (mHour == DEFAULT_VALUE_OF_DATE_TIME
      || mMinute ==DEFAULT_VALUE_OF_DATE_TIME);
  }

  @Override
  public boolean equals(Object o) {
    TaskBean taskBean = (TaskBean)o;
    return (taskBean.mTaskContent.equals(mTaskContent)
      && taskBean.mYear == mYear
      && taskBean.mMonth == mMonth
      && taskBean.mDayOfMonth == mDayOfMonth
      && taskBean.mHour == mHour
      && taskBean.mMinute == mMinute
      && taskBean.mRepeatInterval == mRepeatInterval
      && taskBean.mRepeatUnit == mRepeatUnit
      && taskBean.mGroup.equals(mGroup));
  }
}
