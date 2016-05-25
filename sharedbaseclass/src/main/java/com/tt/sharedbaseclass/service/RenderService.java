package com.tt.sharedbaseclass.service;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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

    public void destoryService() {
        if (mDbWriter != null) {
            mDbWriter.close();
            mDbWriter = null;
        }
        if (mDbReader != null) {
            mDbReader.close();
            mDbReader = null;
        }
        if (mRenderDbHelper != null) {
            mRenderDbHelper.close();
            mRenderDbHelper = null;
        }
        removeAllHandlers();
        mHandlers = null;
    }

    public void addHandler(String action, RenderCallback handler) {
        if (mHandlers != null && !mHandlers.containsKey(action)) {
            mHandlers.put(action, handler);
        }
    }

    public void removeHandler(RenderCallback handler) {
        if (mHandlers != null) {
            mHandlers.remove(handler);
        }
    }

    public void removeAllHandlers() {
        if (mHandlers != null && mHandlers.size() > 0) {
            mHandlers.clear();
        }
    }

    private void getTasksByGroupName(int action, String groupName, int requestCode) {
        RenderCallback handler = mHandlers.get(Constant.RenderServiceHelper.ACTION.valueOf(action).toString());
        Cursor cursor;
        if (StringUtil.isEmpty(groupName)) {
            if (requestCode == Constant.RenderServiceHelper.REQUEST_CODE_GET_ALL_TASKS_BEANS_EXCEPT_FINISHED) {
                cursor = mDbReader.rawQuery("select * from "
                  + Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS
                  + " where " + Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP
                  + " <> ? order by "
                  + Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_TIMILLS,
                  new String[]{Constant.RenderDbHelper.GROUP_NAME_FINISHED});
            } else {
                cursor = mDbReader.rawQuery("select * from "
                  + Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS
                  + " order by "
                  + Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_TIMILLS, null);
            }
        } else {
            cursor = mDbReader.rawQuery("select * from "
              + Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS
              + " where " + Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP
              + " = ? order by "
              + Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_TIMILLS, new String[]{groupName});
        }
        if (cursor != null && cursor.moveToFirst()) {
            List<TaskBean> renderObjectBeans = new RenderObjectBeans<TaskBean>();
            while (cursor.moveToNext()) {
                TaskBean taskBean = new TaskBean();
                taskBean.setTaskContent(cursor.getString(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_CONTENT)));
                taskBean.setYear(cursor.getInt(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_YEAR)));
                taskBean.setMonth(cursor.getInt(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_MONTH)));
                taskBean.setDayOfMonth(cursor.getInt(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_DAY_OF_MONTH)));
                taskBean.setHour(cursor.getInt(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_HOUR)));
                taskBean.setMinuse(cursor.getInt(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_MINUTE)));
                taskBean.setRepeatInterval(cursor.getInt(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_REPEAT_INTERVAL)));
                taskBean.setRepeatUnit(cursor.getInt(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_REPEAT_UNIT)));
                taskBean.setGroup(cursor.getString(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP)));
                renderObjectBeans.add(taskBean);
                taskBean = null;
            }
            if (handler != null) {
                handler.onHandleSelectSuccess((RenderObjectBeans) renderObjectBeans, requestCode
                        , Constant.RenderServiceHelper.RESULT_CODE_GET_TASKS_SUCCESS);
            }
        } else {
            if (handler != null) {
                handler.onHandleFail(requestCode, Constant.RenderServiceHelper.RESULT_CODE_GET_TASKS_FAIL_NO_TASKS);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    private void getGroupsExceptFinished(int action, int requestCode) {
        RenderCallback handler = mHandlers.get(Constant.RenderServiceHelper.ACTION.valueOf(action).toString());
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
                renderObjectBeans.add(cursor.getString(cursor.getColumnIndex(
                        Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP)));
            }
            if (handler != null) {
                handler.onHandleSelectSuccess((RenderObjectBeans) renderObjectBeans, requestCode
                        , Constant.RenderServiceHelper.RESULT_CODE_GET_GROUPS_SUCCESS);
            }
        } else {
            if (handler != null) {
                handler.onHandleFail(requestCode
                        , Constant.RenderServiceHelper.RESULT_CODE_GET_GROUPS_FAIL_NO_GROUPS);
            }
        }
        cursor.close();
    }

    private void addDataToTable(int action, String tableName,TaskBean taskBean, int requestCode) {
        RenderCallback handler = mHandlers.get(Constant.RenderServiceHelper.ACTION.valueOf(action).toString());
        if (StringUtil.isEmpty(tableName) || taskBean == null) {
            if (handler != null) {
                handler.onHandleFail(requestCode
                        , Constant.RenderServiceHelper.RESULT_CODE_UPDATE_FAIL);
            } else {
                Log.e("Render", "this is no handler when get tasks by group name");
            }
        }
        long row = mDbWriter.insert(tableName, null, taskBeanToContentValues(taskBean, requestCode));
        if (row != -1) {
            if (handler != null) {
                handler.onHandleUpdateSuccess(row, requestCode
                        , Constant.RenderServiceHelper.RESULT_CODE_UPDATE_SUCCESS);
            }
        } else {
            if (handler != null) {
                handler.onHandleFail(requestCode
                        , Constant.RenderServiceHelper.RESULT_CODE_UPDATE_FAIL);
            }
        }
    }

    private void updateTable(int action, String tableName,TaskBean oldTaskBean, TaskBean newTaskBean, int requestCode) {
        RenderCallback handler = mHandlers.get(Constant.RenderServiceHelper.ACTION.valueOf(action).toString());
        if (StringUtil.isEmpty(tableName) || oldTaskBean == null || newTaskBean == null) {
            if (handler != null) {
                handler.onHandleFail(requestCode
                        , Constant.RenderServiceHelper.RESULT_CODE_UPDATE_FAIL);
            } else {
                Log.e("Render", "this is no handler when get tasks by group name");
            }
        }
        long row = mDbWriter.update(tableName, taskBeanToContentValues(oldTaskBean, newTaskBean, requestCode)
                , Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_ID + " = ?", new String[]{oldTaskBean.getId() + ""});
        if (row != -1) {
            if (handler != null) {
                handler.onHandleUpdateSuccess(row, requestCode
                        , Constant.RenderServiceHelper.RESULT_CODE_UPDATE_SUCCESS);
            }
        } else {
            if (handler != null) {
                handler.onHandleFail(requestCode
                        , Constant.RenderServiceHelper.RESULT_CODE_UPDATE_SUCCESS);
            }
        }
    }

    private void deleteDateById(int action, String tableName,String id, int requestCode) {
        RenderCallback handler = mHandlers.get(Constant.RenderServiceHelper.ACTION.valueOf(action).toString());
        if (StringUtil.isEmpty(tableName)) {
            if (handler != null) {
                handler.onHandleFail(requestCode
                        , Constant.RenderServiceHelper.RESULT_CODE_UPDATE_FAIL);
            } else {
                Log.e("Render", "this is no handler when get tasks by group name");
            }
        }
        long row = mDbWriter.delete(tableName, Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_ID + "=?"
                , new String[]{id + ""});
        if (row != -1) {
            if (handler != null) {
                handler.onHandleUpdateSuccess(row, requestCode
                        , Constant.RenderServiceHelper.RESULT_CODE_UPDATE_SUCCESS);
            }
        } else {
            if (handler != null) {
                handler.onHandleFail(requestCode
                        , Constant.RenderServiceHelper.RESULT_CODE_UPDATE_SUCCESS);
            }
        }
    }

    private ContentValues taskBeanToContentValues(TaskBean taskBean, int requestCode) {
        ContentValues cv = new ContentValues();
        switch (requestCode) {
            case Constant.RenderServiceHelper.REQUEST_CODE_INSERT_TASK_BEAN:
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_CONTENT, taskBean.getTaskContent());
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_YEAR, taskBean.getYear());
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_MONTH, taskBean.getMonth());
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_DAY_OF_MONTH, taskBean.getDayOfMonth());
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_HOUR, taskBean.getHour());
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_MINUTE, taskBean.getMinute());
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_REPEAT_INTERVAL, taskBean.getRepeatInterval());
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_REPEAT_UNIT, taskBean.getRepeatUnit());
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_TIMILLS, taskBean.getTimeInMillis());
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP, taskBean.getGroup());
                break;
            case Constant.RenderServiceHelper.REQUEST_CODE__INSERT_NEW_GROUP:
            default:
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP, taskBean.getGroup());
                break;
        }
        return cv;
    }

    private ContentValues taskBeanToContentValues(TaskBean oldTaskBean, TaskBean newTaskBean, int requestCode) {
        ContentValues cv = new ContentValues();
        if (!oldTaskBean.getTaskContent().equals(newTaskBean.getTaskContent())) {
            cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_CONTENT, newTaskBean.getTaskContent());
        }
        if (oldTaskBean.getTimeInMillis() != newTaskBean.getTimeInMillis()) {
            if (!oldTaskBean.getPickedDate().equals(newTaskBean.getPickedDate())) {
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_YEAR, newTaskBean.getYear());
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_MONTH, newTaskBean.getMonth());
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_DAY_OF_MONTH, newTaskBean.getDayOfMonth());
            }
            if (!oldTaskBean.getPickedTime().equals(newTaskBean.getPickedTime())) {
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_HOUR, newTaskBean.getHour());
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_MINUTE, newTaskBean.getMinute());
            }
        }
        if (oldTaskBean.getRepeatInterval() != newTaskBean.getRepeatInterval()) {
            cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_REPEAT_INTERVAL, newTaskBean.getRepeatInterval());
        }
        if (oldTaskBean.getRepeatUnit() != newTaskBean.getRepeatUnit()) {
            cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_REPEAT_UNIT, newTaskBean.getRepeatUnit());
        }
        if (!oldTaskBean.getGroup().equals(newTaskBean.getGroup())) {
            cv.put(Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP, newTaskBean.getGroup());
        }
        return cv;
    }

    private TaskBean cursorRowToTaskBean(Cursor cursor) {
        TaskBean taskBean = new TaskBean();
        taskBean.setTaskContent(cursor.getString(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_CONTENT)));
        taskBean.setYear(cursor.getInt(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_YEAR)));
        taskBean.setMonth(cursor.getInt(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_MONTH)));
        taskBean.setDayOfMonth(cursor.getInt(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_DAY_OF_MONTH)));
        taskBean.setHour(cursor.getInt(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_HOUR)));
        taskBean.setMinuse(cursor.getInt(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_MINUTE)));
        taskBean.setRepeatInterval(cursor.getInt(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_REPEAT_INTERVAL)));
        taskBean.setRepeatUnit(cursor.getInt(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_REPEAT_UNIT)));
        taskBean.setGroup(cursor.getString(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP)));
        return taskBean;
    }

    private List cursorToRenderObjectBeans(Cursor cursor, int requestCode) {
        switch (requestCode) {
            case Constant.RenderServiceHelper.REQUEST_CODE_GET_ALL_TASKS_BEANS_EXCEPT_FINISHED:
            case Constant.RenderServiceHelper.REQUEST_CODE_GET_TASKS_BEANS_BY_GROUP_NAME:
                List<TaskBean> renderObjectBeansTasks = null;
                if (cursor.moveToFirst()) {
                    renderObjectBeansTasks = new RenderObjectBeans<TaskBean>();
                    while (cursor.moveToNext()) {
                        renderObjectBeansTasks.add(cursorRowToTaskBean(cursor));
                    }
                    return renderObjectBeansTasks;
                }
                break;
            case Constant.RenderServiceHelper.REQUEST_CODE_GET_GROUPS:
            default:
                if (cursor.moveToFirst()) {
                    List<String> renderObjectBeansGroup  = new RenderObjectBeans<String>();
                    while(cursor.moveToNext()) {
                        renderObjectBeansGroup.add(cursor.getString(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP)));
                    }
                    return renderObjectBeansGroup;
                }
                break;
        }
        return null;
    }


    public void getOrUpdate(final int action, final String tableName, final TaskBean oldTaskBean, final TaskBean newTaskBean, final String[] whereArgs, final int requestCode) {
        new AsyncTask<Void, Void , Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                switch (action) {
                    //get all tasks by group name
                    case 1:
                        getTasksByGroupName(action,whereArgs == null ? null : whereArgs[0],requestCode);
                        break;
                    //get groups except finished
                    case 2:
                        getGroupsExceptFinished(action, requestCode);
                        break;
                    //add new group
                    //add new task
                    case 3:
                    case 4:
                        addDataToTable(action, tableName, newTaskBean, requestCode);
                        break;
                    //update task
                    //update group name
                    case 5:
                    case 6:
                        updateTable(action, tableName, oldTaskBean, newTaskBean, requestCode);
                        break;
                    //delete task
                    //delete group
                    case 7:
                    case 8:
                        deleteDateById(action, tableName, whereArgs[0], requestCode);
                        break;
                }
                return null;
            }
        }.execute();
    }

}
