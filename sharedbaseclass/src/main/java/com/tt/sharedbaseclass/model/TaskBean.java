package com.tt.sharedbaseclass.model;

import android.text.TextUtils;

import com.tt.sharedbaseclass.constant.Constant;

import java.util.Calendar;
import java.util.Locale;

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
    this.mYear = year;
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
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    calendar.set(this.mYear, this.mMonth, this.mDayOfMonth);
    StringBuffer sb = new StringBuffer();
    sb.append(Constant.WEEK.valueOf(calendar.get(Calendar.DAY_OF_WEEK)))
            .append(", ")
            .append(Constant.MONTH.valueOf(calendar.get(Calendar.MONTH)))
            .append(" ")
            .append(this.mDayOfMonth)
            .append(", ")
            .append(this.mYear);
    return sb.toString();
  }

  public String getPickedTime() {
    return mPickedTime;
  }

}
