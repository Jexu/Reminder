package com.tt.reminder.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.model.TaskBean;

/**
 * Created by zhengguo on 6/1/16.
 */
public class RenderAlarm {

  public RenderAlarm() {

  }

  public static void createAlarm(Context context, TaskBean taskBean) {
    boolean isRepeating = false;
    if (taskBean.getRepeatIntervalTimeInMillis() != TaskBean.DEFAULT_VALUE_OF_INTERVAL) {
      isRepeating = true;
    } else {
      isRepeating = false;
    }
    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    Intent intent = new Intent();
    intent.setAction(RenderNotificationService.ACTION);
    Bundle bundle = new Bundle();
    bundle.putSerializable(Constant.BundelExtra.EXTRA_TASK_BEAN, taskBean);
    intent.putExtras(bundle);
    PendingIntent pi = PendingIntent.getService(context, taskBean.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
    if (isRepeating) {
      alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,taskBean.getTimeInMillis(), taskBean.getRepeatIntervalTimeInMillis(), pi);
    } else {
      alarmManager.set(AlarmManager.RTC_WAKEUP, taskBean.getTimeInMillis(), pi);
    }
  }

  public static void removeAlarm(Context context, TaskBean taskBean) {
    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    Intent intent = new Intent(context, RenderNotificationService.class);
    PendingIntent pi = PendingIntent.getService(context, taskBean.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
    alarmManager.cancel(pi);
  }

  public static void updateAlarm(Context context, TaskBean taskBean) {
    createAlarm(context, taskBean);
  }

}
