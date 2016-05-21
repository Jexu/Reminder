package com.tt.sharedbaseclass.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tt.sharedbaseclass.R;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.model.TaskBean;

/**
 * Created by zhengguo on 2016/5/21.
 */
public class RenderDbHelper extends SQLiteOpenHelper {
    public RenderDbHelper(Context context) {
        super(context, "RenderDb", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableGroup(db);
        createTableTasks(db);
    }

    private void createTableTasks(SQLiteDatabase db) {
        StringBuffer createTasks = new StringBuffer();
        createTasks.append("create table if not exists ")
                .append(Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS).append("(")
                .append(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_ID)
                .append(" integer primary key autoincrement,")
                .append(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_CONTENT)
                .append(" text not null,")
                .append(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_YEAR)
                .append(" integer default ")
                .append(TaskBean.DEFAULT_VALUE_OF_DATE_TIME).append(", ")
                .append(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_MONTH)
                .append(" integer default ")
                .append(TaskBean.DEFAULT_VALUE_OF_DATE_TIME).append(", ")
                .append(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_DAY_OF_MONTH)
                .append(" integer default ")
                .append(TaskBean.DEFAULT_VALUE_OF_DATE_TIME).append(", ")
                .append(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_HOUR)
                .append(" integer default ")
                .append(TaskBean.DEFAULT_VALUE_OF_DATE_TIME).append(", ")
                .append(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_MINUTE)
                .append(" integer default ")
                .append(TaskBean.DEFAULT_VALUE_OF_DATE_TIME).append(", ")
                .append(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_TIMILLS)
                .append(" integer default ")
                .append(TaskBean.DEFAULT_VALUE_OF_DATE_TIME).append(", ")
                .append(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_REPEAT_INTERVAL)
                .append(" integer default ")
                .append(TaskBean.DEFAULT_VALUE_OF_INTERVAL).append(", ")
                .append(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_REPEAT_UNIT)
                .append(" integer default ")
                .append(TaskBean.DEFAULT_VALUE_OF_DATE_TIME).append(", ")
                .append(Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP)
                .append(" text not null, foreign key (")
                .append(Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP)
                .append(")  references ")
                .append(Constant.RenderDbHelper.EXTRA_TABLE_NAME_GROUP)
                .append(" (").append(Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP).append(")")
                .append(")");
        db.execSQL(createTasks.toString());
        Log.i("Render", "create tasks table successfully");

    }

    private void createTableGroup(SQLiteDatabase db) {
        StringBuffer createGroup = new StringBuffer();
        createGroup.append("create table if not exists ")
                .append(Constant.RenderDbHelper.EXTRA_TABLE_NAME_GROUP).append("(")
                .append(Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_ID)
                .append(" integer primary key autoincrement,")
                .append(Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP)
                .append(" text not null )");
        db.execSQL(createGroup.toString());
        ContentValues cvGroup = new ContentValues();
        cvGroup.put(Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP, R.string.render_db_helper_group_my_task);
        cvGroup.put(Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP, R.string.remder_db_helper_group_finished);
        db.insertOrThrow(Constant.RenderDbHelper.EXTRA_TABLE_NAME_GROUP, null, cvGroup);
        Log.i("Render", "create group table successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS);
        db.execSQL("drop table if exists " + Constant.RenderDbHelper.EXTRA_TABLE_NAME_GROUP);
    }
}
