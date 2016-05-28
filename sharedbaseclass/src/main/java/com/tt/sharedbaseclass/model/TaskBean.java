package com.tt.sharedbaseclass.model;

import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedutils.StringUtil;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by zhengguo on 5/19/16.
 */
public class TaskBean extends GroupBean {
  private String mTaskContent;
  private int mYear;
  private int mMonth;
  private int mDayOfMonth;
  private int mHour;
  private int mMinute;
  private long mTimeInMillis;
  private int mRepeatInterval;
  private int mRepeatUnit;
  private boolean mIsFinished;
  private Calendar mCalendar;

  public static int DEFAULT_VALUE_OF_DATE_TIME = -1;
  public static int DEFAULT_VALUE_OF_INTERVAL = 0;
  public static long TIMILLS_ONE_HOUR = 60*60*1000;


  public TaskBean() {
    super();
    mTaskContent = "";
    mYear = DEFAULT_VALUE_OF_DATE_TIME;
    mMonth = DEFAULT_VALUE_OF_DATE_TIME;
    mDayOfMonth = DEFAULT_VALUE_OF_DATE_TIME;
    mHour = DEFAULT_VALUE_OF_DATE_TIME;
    mMinute = DEFAULT_VALUE_OF_DATE_TIME;
    mRepeatInterval = DEFAULT_VALUE_OF_INTERVAL;
    mRepeatUnit = Constant.REPEAT_UNIT.NO_REPEAT.value();
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

  public long getRepeatIntervalTimeInMillis() {
    long repeatIntervalTimillis = DEFAULT_VALUE_OF_INTERVAL;
    if (mRepeatUnit == Constant.REPEAT_UNIT.NO_REPEAT.value()) {
      repeatIntervalTimillis = DEFAULT_VALUE_OF_INTERVAL;
    } else if(mRepeatUnit == Constant.REPEAT_UNIT.DAY.value()) {
      repeatIntervalTimillis = mRepeatInterval * 24 * TIMILLS_ONE_HOUR;
    } else if(mRepeatUnit == Constant.REPEAT_UNIT.HOUR.value()) {
      repeatIntervalTimillis = mRepeatInterval * TIMILLS_ONE_HOUR;
    } else if(mRepeatUnit == Constant.REPEAT_UNIT.WEEK.value()) {
      repeatIntervalTimillis = mRepeatInterval * 7 * 24 * TIMILLS_ONE_HOUR;
    } else if(mRepeatUnit == Constant.REPEAT_UNIT.MONTH.value()) {
      repeatIntervalTimillis = mRepeatInterval * 30 * 7 * 24 * TIMILLS_ONE_HOUR;
    } else if(mRepeatUnit == Constant.REPEAT_UNIT.YEAR.value()) {
      repeatIntervalTimillis = mRepeatInterval * 12 *  30 * 7 * 24 * TIMILLS_ONE_HOUR;
    } else if(mRepeatUnit == Constant.REPEAT_UNIT.MINUTE.value()) {
      repeatIntervalTimillis = mRepeatInterval * TIMILLS_ONE_HOUR / 60;
    }
    return repeatIntervalTimillis;
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

  public String getPickedDate(boolean isInEditFragment) {
    if (isClearedPickedDate()) {
      return null;
    }
    mCalendar.set(this.mYear, this.mMonth, this.mDayOfMonth);
    StringBuffer sb = new StringBuffer();
    if (isInEditFragment) {
      sb.append(Constant.WEEK.valueOf(mCalendar.get(Calendar.DAY_OF_WEEK) - 1))
              .append(", ")
              .append(Constant.MONTH.valueOf(mCalendar.get(Calendar.MONTH) + 1))
              .append(" ")
              .append(this.mDayOfMonth)
              .append(",")
              .append(this.mYear);
    } else {
      sb.append(mDayOfMonth<10?"0"+mDayOfMonth:mDayOfMonth)
              .append("/")
              .append(mMonth<9?"0"+(mMonth+1):(mMonth+1))
              .append("/")
              .append(mYear);
    }
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

  public String getPickedTime(boolean isInEditFragment) {
    if (isClearedPickedTime()) {
      return null;
    }
    StringBuffer sb = new StringBuffer();
    if (!isInEditFragment) {
      mCalendar.set(this.mYear, this.mMonth, this.mDayOfMonth);
      sb.append(Constant.WEEK.valueOf(mCalendar.get(Calendar.DAY_OF_WEEK) - 1))
              .append(", ");
    }
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

  public Constant.TASK_BEAN_STATUS checkTaskStatus() {
    if (StringUtil.isEmpty(mTaskContent)) {
      return Constant.TASK_BEAN_STATUS.TASK_CONTENT_NULL;
    } else if (!isClearedPickedTime() && isClearedPickedDate()) {
      return Constant.TASK_BEAN_STATUS.DATE_NOT_SET;
    } else if (isClearedPickedTime() && !isClearedPickedDate()){
      return Constant.TASK_BEAN_STATUS.TIME_NOT_SET;
    }
    return Constant.TASK_BEAN_STATUS.AVAILABLE_SAVE;
  }

  @Override
  public void copy(RenderBeanBase taskBean) {
    super.copy(taskBean);
    TaskBean tb = (TaskBean)taskBean;
    mTaskContent = tb.mTaskContent;
    mYear = tb.mYear;
    mMonth = tb.mMonth;
    mDayOfMonth = tb.mDayOfMonth;
    mHour = tb.mHour;
    mMinute = tb.mMinute;
    mRepeatInterval = tb.mRepeatInterval;
    mRepeatUnit = tb.mRepeatUnit;
  }

  @Override
  public String toString() {
    return mTaskContent;
  }

  @Override
  public int compareTo(Object another) {
    if (getTimeInMillis() > ((TaskBean) another).getTimeInMillis()) {
      return 1;
    } else if (getTimeInMillis() < ((TaskBean) another).getTimeInMillis()) {
      return -1;
    } else {
      return 0;
    }
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
      && super.equals(o));
  }
}
