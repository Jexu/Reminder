package com.tt.sharedbaseclass.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import com.tt.sharedbaseclass.constant.Constant;
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

    public void destroyService() {
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
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.BundelExtra.EXTRA_REQUEST_CODE, requestCode);
        Message msg = new Message();
        if (StringUtil.isEmpty(groupName)) {
            if (handler != null) {
                msg.what = Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_HANDLE_FAIL;
                bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_GET_TASKS_FAIL_ERROR);
            } else {
                Log.e("Render", "this is no handler when get tasks by group name");
            }
            msg.obj = bundle;
            handler.sendMessage(msg);
            return;
        }
        Cursor cursorHasDate;
        Cursor cursorNoDate;
        if (requestCode == Constant.RenderServiceHelper.REQUEST_CODE_GET_ALL_TASKS_BEANS_EXCEPT_FINISHED) {
            cursorHasDate = mDbReader.rawQuery("select * from "
                  + Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS
                  + " where " + Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP
                  + " <> ? and "
                  + Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_TIMILLS
                  +" <> ?"
                  + " order by "
                  + Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_TIMILLS,
                  new String[]{Constant.RenderDbHelper.GROUP_NAME_FINISHED
                    , TaskBean.DEFAULT_VALUE_OF_DATE_TIME+""});
            cursorNoDate = mDbReader.rawQuery("select * from "
                  + Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS
                  + " where " + Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP
                  + " <> ? and "
                  + Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_TIMILLS
                  +" = ?",new String[]{Constant.RenderDbHelper.GROUP_NAME_FINISHED
                    ,TaskBean.DEFAULT_VALUE_OF_DATE_TIME+""});
        } else {
            cursorHasDate = mDbReader.rawQuery("select * from "
              + Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS
              + " where " + Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP
              + " = ? and "
              + Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_TIMILLS
              +" <> ?"
              +" order by "
              + Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_TIMILLS, new String[]{groupName
                    , TaskBean.DEFAULT_VALUE_OF_DATE_TIME+""});
            cursorNoDate = mDbReader.rawQuery("select * from "
              + Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS
              + " where " + Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP
              + " = ? and "
              + Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_TIMILLS
              +" = ?", new String[]{groupName
              , TaskBean.DEFAULT_VALUE_OF_DATE_TIME+""});
        }

        RenderObjectBeans<TaskBean> renderObjectBeans = new RenderObjectBeans<TaskBean>();
        boolean isCursorHasDate = false;
        if (cursorHasDate != null) {
            renderObjectBeans.setCountTaskHasDate(cursorHasDate.getCount());
            while (cursorHasDate.moveToNext()) {
                renderObjectBeans.add(cursorRowToTaskBean(cursorHasDate));
            }
            isCursorHasDate = true;
        }
        boolean isCursorNoDate = false;
        if (cursorNoDate != null) {
            renderObjectBeans.setCountTaskNoDate(cursorNoDate.getCount());
            while (cursorNoDate.moveToNext()) {
                renderObjectBeans.add(cursorRowToTaskBean(cursorNoDate));
            }
            isCursorNoDate = true;
        }

        if (isCursorHasDate || isCursorNoDate) {
            msg.what = Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_SELECT_SUCCESS;
            bundle.putSerializable(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN
                    , (RenderObjectBeans) renderObjectBeans);
            if (isCursorHasDate && !isCursorNoDate) {
                bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE
                        , Constant.RenderServiceHelper.RESULT_CODE_GET_TASKS_SUCCESS_HAS_DATE_ONLY);
                renderObjectBeans.setType(RenderObjectBeans.TYPE_TASK_BEANS_HAS_DATE_ONLY);
            } else if (isCursorHasDate && isCursorNoDate) {
                bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE
                        , Constant.RenderServiceHelper.RESULT_CODE_GET_TASKS_SUCCESS_HAS_DATE_AND_NO_DATE);
                renderObjectBeans.setType(RenderObjectBeans.TYPE_TASK_BEANS_NO_DATE_ONLY);
            } else if (!isCursorHasDate && isCursorNoDate) {
                bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE
                        , Constant.RenderServiceHelper.RESULT_CODE_GET_TASKS_SUCCESS_NO_DATE_ONLY);
                renderObjectBeans.setType(RenderObjectBeans.TYPE_TASK_BEANS_HAS_DATE_AND_NO_DATE);
            }
        }else {
            if (handler != null) {
                msg.what = Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_HANDLE_FAIL;
                bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_GET_TASKS_FAIL_NO_TASKS);
            }
        }
        msg.obj = bundle;
        handler.sendMessage(msg);
        if (cursorHasDate != null) {
            cursorHasDate.close();
        }
    }

    private void getGroupsExceptFinished(int action, int requestCode) {
        RenderCallback handler = mHandlers.get(Constant.RenderServiceHelper.ACTION.valueOf(action).toString());
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.BundelExtra.EXTRA_REQUEST_CODE, requestCode);
        Message msg = new Message();
        Cursor cursor = mDbReader.rawQuery("select * "
                + " from "
                + Constant.RenderDbHelper.EXTRA_TABLE_NAME_GROUP
                + " where "
                + Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP
                + " <> ?", new String[]{Constant.RenderDbHelper.GROUP_NAME_FINISHED});
        if (cursor != null) {
            List<GroupBean> renderObjectBeansGroup  = new RenderObjectBeans<GroupBean>();
            while(cursor.moveToNext()) {
                GroupBean groupBean = new GroupBean();
                groupBean.setId(cursor.getInt(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_ID)));
                groupBean.setGroup(cursor.getString(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP)));
                renderObjectBeansGroup.add(groupBean);
            }
            if (handler != null) {
                msg.what = Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_SELECT_SUCCESS;
                bundle.putSerializable(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN, (RenderObjectBeans) renderObjectBeansGroup);
                bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_GET_GROUPS_SUCCESS);
            }
        } else {
            if (handler != null) {
                msg.what = Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_HANDLE_FAIL;
                bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_GET_GROUPS_FAIL_NO_GROUPS);
            }
        }
        msg.obj = bundle;
        handler.sendMessage(msg);
        if (cursor != null) {
            cursor.close();
        }
    }

    private void addDataToTable(int action, String tableName,TaskBean taskBean, int requestCode) {
        RenderCallback handler = mHandlers.get(Constant.RenderServiceHelper.ACTION.valueOf(action).toString());
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.BundelExtra.EXTRA_REQUEST_CODE, requestCode);
        Message msg = new Message();
        if (StringUtil.isEmpty(tableName) || taskBean == null) {
            if (handler != null) {
                msg.what = Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_HANDLE_FAIL;
                bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_UPDATE_FAIL);
            } else {
                Log.e("Render", "this is no handler when get tasks by group name");
            }
            msg.obj = bundle;
            handler.sendMessage(msg);
            return;
        }
        long row = mDbWriter.insert(tableName, null, taskBeanToContentValues(taskBean, requestCode));
        if (row != -1) {
            if (handler != null) {
                msg.what = Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_UPDATE_SUCCESS;
                bundle.putLong(Constant.BundelExtra.EXTRA_UPDATE_ROW, row);
                bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_UPDATE_SUCCESS);
            }
        } else {
            if (handler != null) {
                msg.what = Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_HANDLE_FAIL;
                bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_UPDATE_FAIL);
            }
        }
        msg.obj = bundle;
        handler.sendMessage(msg);
    }

    private void updateTable(int action, String tableName,TaskBean oldTaskBean, TaskBean newTaskBean, int requestCode) {
        RenderCallback handler = mHandlers.get(Constant.RenderServiceHelper.ACTION.valueOf(action).toString());
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.BundelExtra.EXTRA_REQUEST_CODE, requestCode);
        Message msg = new Message();
        if (StringUtil.isEmpty(tableName) || oldTaskBean == null || newTaskBean == null) {
            if (handler != null) {
                msg.what = Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_HANDLE_FAIL;
                bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_UPDATE_FAIL);
            } else {
                Log.e("Render", "this is no handler when get tasks by group name");
            }
            msg.obj = bundle;
            handler.sendMessage(msg);
            return;
        }
        long row = mDbWriter.update(tableName, taskBeanToContentValues(oldTaskBean, newTaskBean, requestCode)
                , Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_ID + " = ?", new String[]{oldTaskBean.getId() + ""});
        if (row != -1) {
            if (handler != null) {
                msg.what = Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_UPDATE_SUCCESS;
                bundle.putLong(Constant.BundelExtra.EXTRA_UPDATE_ROW, row);
                bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_UPDATE_SUCCESS);
            }
        } else {
            if (handler != null) {
                msg.what = Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_HANDLE_FAIL;
                bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_UPDATE_FAIL);
            }
        }
        msg.obj = bundle;
        handler.sendMessage(msg);
    }

    private void deleteDateById(int action, String tableName,String id, int requestCode) {
        RenderCallback handler = mHandlers.get(Constant.RenderServiceHelper.ACTION.valueOf(action).toString());
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.BundelExtra.EXTRA_REQUEST_CODE, requestCode);
        Message msg = new Message();
        if (StringUtil.isEmpty(tableName)) {
            if (handler != null) {
                msg.what = Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_HANDLE_FAIL;
                bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_UPDATE_FAIL);
            } else {
                Log.e("Render", "this is no handler when get tasks by group name");
            }
            msg.obj = bundle;
            handler.sendMessage(msg);
            return;
        }
        long row = mDbWriter.delete(tableName, Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_ID + "=?"
                , new String[]{id + ""});
        if (row != -1) {
            if (handler != null) {
                msg.what = Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_UPDATE_SUCCESS;
                bundle.putLong(Constant.BundelExtra.EXTRA_UPDATE_ROW, row);
                bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_UPDATE_SUCCESS);
            }
        } else {
            if (handler != null) {
                msg.what = Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_HANDLE_FAIL;
                bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_UPDATE_FAIL);
            }
        }
        msg.obj = bundle;
        handler.sendMessage(msg);
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
            if (!oldTaskBean.getPickedDate(true).equals(newTaskBean.getPickedDate(true))) {
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_YEAR, newTaskBean.getYear());
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_MONTH, newTaskBean.getMonth());
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_DAY_OF_MONTH, newTaskBean.getDayOfMonth());
            }
            if (!oldTaskBean.getPickedTime(true).equals(newTaskBean.getPickedTime(true))) {
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
        taskBean.setId(cursor.getInt(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_ID)));
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
                if (cursor != null) {
                    renderObjectBeansTasks = new RenderObjectBeans<TaskBean>();
                    while (cursor.moveToNext()) {
                        renderObjectBeansTasks.add(cursorRowToTaskBean(cursor));
                    }
                    return renderObjectBeansTasks;
                }
                break;
            case Constant.RenderServiceHelper.REQUEST_CODE_GET_GROUPS:
            default:
                if (cursor != null) {
                    List<GroupBean> renderObjectBeansGroup  = new RenderObjectBeans<GroupBean>();
                    while(cursor.moveToNext()) {
                        GroupBean groupBean = new GroupBean();
                        groupBean.setId(cursor.getInt(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_ID)));
                        groupBean.setGroup(cursor.getString(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP)));
                        renderObjectBeansGroup.add(groupBean);
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
