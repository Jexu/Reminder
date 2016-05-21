package com.tt.sharedbaseclass.service;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tt.sharedbaseclass.R;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.model.RenderObjectBeans;
import com.tt.sharedbaseclass.model.TaskBean;
import com.tt.sharedutils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhengguo on 2016/5/21.
 */
public class RenderService {
    private Map<String, RenderCallback> mHandlers;
    private RenderDbHelper mRenderDbHelper;
    private SQLiteDatabase mDbReader;
    private SQLiteDatabase mDbWriter;

    public RenderService(Context context) {
        mHandlers = new HashMap<>();
        mRenderDbHelper = new RenderDbHelper(context);
        mDbReader = mRenderDbHelper.getReadableDatabase();
        mDbWriter = mRenderDbHelper.getWritableDatabase();
    }

    public void addHandler(String action, RenderCallback handler) {
        if (mHandlers != null && mHandlers.containsKey(action)) {
            mHandlers.put(action, handler);
        }
    }

    public void removeHandler(RenderCallback handler) {
        if (mHandlers != null) {
            mHandlers.remove(handler);
        }
    }

    public void getTasksByGroupName(String action, String groupName, int requestCode) {
        RenderCallback handler = mHandlers.get(action);
        if (StringUtil.isEmpty(action) || StringUtil.isEmpty(groupName)) {
            if (handler != null) {
                handler.onHandleFail(requestCode, Constant.RenderServiceHelper.RESULT_CODE_GET_TASKS_FAIL_ERROR);
            } else {
                Log.e("Render", "this is no handler when get tasks by group name");
            }
        }
        Cursor cursor = mDbReader.rawQuery("select * from "
                + Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS
                + " where " + Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP
                + " = ? order by "
                + Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_TIMILLS, new String[]{groupName});
        if (cursor.moveToFirst()) {
            List<TaskBean> renderObjectBeans = new RenderObjectBeans<TaskBean>();
            while (cursor.moveToNext()) {
                TaskBean taskBean = new TaskBean();
                taskBean.setTaskContent(cursor.getString(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_CONTENT)));
                taskBean.setYear(cursor.getInt(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_YEAR)));
                taskBean.setMonth(cursor.getInt(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_MONTH)));
                taskBean.setDayOfMonth(cursor.getInt(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_DAY_OF_MONTH)));
                taskBean.setHour(cursor.getInt(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_HOUR)));
                taskBean.setMinuse(cursor.getInt(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_MINUTE)));
                taskBean.setmRepeatInterval(cursor.getInt(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_REPEAT_INTERVAL)));
                taskBean.setmRepeatUnit(cursor.getInt(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_REPEAT_UNIT)));
                taskBean.setGroup(cursor.getString(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP)));
                renderObjectBeans.add(taskBean);
                taskBean = null;
            }
            if (handler != null) {
                handler.onHandleSuccess((RenderObjectBeans) renderObjectBeans, requestCode
                        , Constant.RenderServiceHelper.RESULT_CODE_GET_TASKS_SUCCESS);
            }
        } else {
            if (handler != null) {
                handler.onHandleFail(requestCode, Constant.RenderServiceHelper.RESULT_CODE_GET_TASKS_FAIL_NO_TASKS);
            }
        }
        cursor.close();
    }

    public void getGroupsExceptFinished(String action, int requestCode) {
        RenderCallback handler = mHandlers.get(action);
        if (StringUtil.isEmpty(action)) {
            if (handler != null) {
                handler.onHandleFail(requestCode, Constant.RenderServiceHelper.RESULT_CODE_GET_GROUPS_FAIL_ERROR);
            } else {
                Log.e("Render", "this is no handler when get tasks by group name");
            }
        }
        Cursor cursor = mDbReader.rawQuery("select "
                + Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP
                + " from "
                + Constant.RenderDbHelper.EXTRA_TABLE_NAME_GROUP
                + " where "
                + Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP
                + " <> ?", new String[R.string.remder_db_helper_group_finished]);
        if (cursor.moveToFirst()) {
            List<String> renderObjectBeans  = new RenderObjectBeans<String>();
            while(cursor.moveToNext()) {
                renderObjectBeans.add(cursor.getString(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP)));
            }
            if (handler != null) {
                handler.onHandleSuccess((RenderObjectBeans) renderObjectBeans, requestCode, Constant.RenderServiceHelper.RESULT_CODE_GET_GROUPS_SUCCESS);
            }
        } else {
            if (handler != null) {
                handler.onHandleFail(requestCode, Constant.RenderServiceHelper.RESULT_CODE_GET_GROUPS_FAIL_NO_GROUPS);
            }
        }
        cursor.close();
    }

    public void addDataToTable(String action, String tableName,ContentValues values, int requestCode) {

    }

}
