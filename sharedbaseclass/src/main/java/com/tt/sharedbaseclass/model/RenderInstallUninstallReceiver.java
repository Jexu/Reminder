package com.tt.sharedbaseclass.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.tt.sharedbaseclass.constant.Constant;

/**
 * Created by zhengguo on 6/3/16.
 */
public class  RenderInstallUninstallReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {

    if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
      Log.i("Render", "Render uninstall");
      SQLiteDatabase writer = new RenderDbHelper(context).getWritableDatabase();
      writer.execSQL("drop table if exists " + Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS);
      writer.execSQL("drop table if exists " + Constant.RenderDbHelper.EXTRA_TABLE_NAME_GROUP);
      writer.close();
    } else if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
      Log.i("Render", "Render install");
    }
  }
}
