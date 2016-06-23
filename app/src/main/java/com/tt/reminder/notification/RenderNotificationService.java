package com.tt.reminder.notification;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import com.google.gson.Gson;
import com.tt.reminder.activity.MainActivity;
import com.tt.sharedbaseclass.R;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.model.ConfigBean;
import com.tt.sharedbaseclass.model.TaskBean;

/**
 * Created by zhengguo on 6/1/16.
 */
public class RenderNotificationService extends IntentService {

  public static final String ACTION = "com.tt.reminder.notification";

  private NotificationManager mNotificationManager;
  private Notification.Builder mNotificationBuilder;

  public RenderNotificationService() {
    super("RenderNotificationService");
  }

  @Override
  public void onCreate() {
    super.onCreate();
    Log.i("Render", "RenderNotificationService onCreate");
    mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    mNotificationBuilder = new Notification.Builder(getApplicationContext());
  }

  @SuppressLint("NewApi")
  @Override
  protected void onHandleIntent(Intent intent) {
    Log.i("Render", "RenderNotificationService onHandleIntent");
    Gson gson = new Gson();
    TaskBean taskBean = gson.fromJson(intent.getStringExtra(Constant.BundelExtra.EXTRA_TASK_BEAN), TaskBean.class);
    //TaskBean taskBean = (TaskBean) intent.getSerializableExtra(Constant.BundelExtra.EXTRA_TASK_BEAN);
    Intent i = new Intent(this, MainActivity.class);
    Bundle bundle = new Bundle();
    bundle.putInt(Constant.BundelExtra.EXTRA_START_FROM, Constant.BundelExtra.START_FROM_NOTIFICATION);
    bundle.putSerializable(Constant.BundelExtra.EXTRA_TASK_BEAN, taskBean);
    i.putExtras(bundle);

    PendingIntent pi = PendingIntent.getActivity(this, taskBean.getId(), i, PendingIntent.FLAG_UPDATE_CURRENT);
    if (!ConfigBean.isNotificationEnable()) {
      return;
    }
    mNotificationBuilder
      .setAutoCancel(true)
      .setTicker(getString(com.tt.reminder.R.string.notification_ticker_new_message))
      .setSmallIcon(R.drawable.ic_notification_small)
      .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification_large))
      .setContentTitle(getString(com.tt.reminder.R.string.notification_title_task_at, taskBean.getPickedTime(true), taskBean.getPickedDate(true)))
      .setContentText(taskBean.getTaskContent())
      //.addAction(android.R.drawable.stat_notify_more, "Finish", null)
      //.addAction(android.R.drawable.stat_notify_more, "Cancel", null)
      .setDefaults(
        Notification.DEFAULT_SOUND
          | Notification.DEFAULT_LIGHTS
          | Notification.DEFAULT_VIBRATE)
      .setWhen(System.currentTimeMillis()).setContentIntent(pi);

    mNotificationManager.notify(taskBean.getId(), mNotificationBuilder.build());
  }
}
