package com.tt.reminder.notification;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import com.tt.reminder.activity.MainActivity;
import com.tt.sharedbaseclass.R;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.model.TaskBean;

/**
 * Created by zhengguo on 6/1/16.
 */
public class RenderNotificationService extends IntentService {

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

  @Override
  protected void onHandleIntent(Intent intent) {
    Log.i("Render", "RenderNotificationService onHandleIntent");
    Bundle bundle = intent.getExtras();
    TaskBean taskBean = (TaskBean) bundle.getSerializable(Constant.BundelExtra.EXTRA_TASK_BEAN);
    Intent i = new Intent(this, MainActivity.class);
    bundle.putInt(Constant.BundelExtra.EXTRA_START_FROM, Constant.BundelExtra.START_FROM_NOTIFICATION);
    i.putExtras(bundle);

    PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
    mNotificationBuilder
      .setAutoCancel(true)
      .setTicker("New schedule message")
      .setSmallIcon(R.drawable.label_outline2_24dp)
      .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.label_outline2_24dp))
      .setContentTitle("Task at " + taskBean.getPickedTime(true) + taskBean.getPickedDate(true))
      .setContentText(taskBean.getTaskContent())
      .setDefaults(
        Notification.DEFAULT_SOUND
          | Notification.DEFAULT_LIGHTS
          | Notification.DEFAULT_VIBRATE)
      .setWhen(System.currentTimeMillis()).setContentIntent(pi);

    mNotificationManager.notify(taskBean.getId(), mNotificationBuilder.build());
  }
}
