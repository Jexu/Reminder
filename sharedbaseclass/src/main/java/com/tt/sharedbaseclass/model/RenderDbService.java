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

import java.util.List;

/**
 * Created by zhengguo on 2016/5/21.
 */
public class RenderDbService extends RenderServiceBase {

    public RenderDbService(Context context) {
        super(context);
    }

    @Override
    public void destroyService() {
        if (mRenderDbHelper != null) {
            mRenderDbHelper.close();
            mRenderDbHelper = null;
        }
        super.destroyService();
    }

    private void getTasksByGroupName(int action, String groupName, int requestCode) {
        RenderDbCallback handler = (RenderDbCallback) mHandlers.get(Constant.RenderServiceHelper.ACTION.valueOf(action).toString());
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.BundelExtra.EXTRA_REQUEST_CODE, requestCode);
        Message msg = new Message();
        if (StringUtil.isEmpty(groupName)) {
                msg.what = Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_HANDLE_FAIL;
                bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_GET_TASKS_FAIL_ERROR);
            if (handler != null) {
                msg.obj = bundle;
                handler.sendMessage(msg);
            } else {
                Log.e("Render", "this is no handler when get tasks by group name");
            }
            return;
        }
        Cursor cursorHasDate;
        Cursor cursorNoDate;
        SQLiteDatabase dbReader = mRenderDbHelper.getReadableDatabase();
        if (requestCode == Constant.RenderServiceHelper.REQUEST_CODE_GET_ALL_TASKS_BEANS_EXCEPT_FINISHED) {
            cursorHasDate = dbReader.rawQuery("select * from "
                  + Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS
                  + " where " + Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_IS_FINISHED
                  + " = ? and "
                  + Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_TIMILLS
                  +" <> ?"
                  + " order by ? "
                  , new String[]{TaskBean.VALUE_NOT_FINISHED+""
                    , TaskBean.DEFAULT_VALUE_OF_DATE_TIME+""
                    , Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_TIMILLS});
            cursorNoDate = dbReader.rawQuery("select * from "
                  + Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS
                  + " where " + Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_IS_FINISHED
                  + " = ? and "
                  + Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_TIMILLS
                  +" = ?",new String[]{TaskBean.VALUE_NOT_FINISHED+""
                    ,TaskBean.DEFAULT_VALUE_OF_DATE_TIME+""});
        } else {
            if (!groupName.equals(Constant.RenderDbHelper.GROUP_NAME_FINISHED)) {
                cursorHasDate = dbReader.rawQuery("select * from "
                  + Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS
                  + " where " + Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP
                  + " = ? and "
                  + Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_TIMILLS
                  +" <> ? and "
                  + Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_IS_FINISHED
                  +" = ?"
                  +" order by ? "
                  , new String[]{groupName
                  , TaskBean.DEFAULT_VALUE_OF_DATE_TIME+""
                  , TaskBean.VALUE_NOT_FINISHED+""
                  , Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_TIMILLS});
                cursorNoDate = dbReader.rawQuery("select * from "
                  + Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS
                  + " where " + Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP
                  + " = ? and "
                  + Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_IS_FINISHED
                  + " = ? and "
                  + Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_TIMILLS
                  +" = ?", new String[]{groupName
                  , TaskBean.VALUE_NOT_FINISHED+""
                  , TaskBean.DEFAULT_VALUE_OF_DATE_TIME+""});
            } else {
                cursorHasDate = dbReader.rawQuery("select * from "
                  + Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS
                  + " where " + Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_IS_FINISHED
                  + " = ? and "
                  + Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_TIMILLS
                  +" <> ?"
                  +" order by ? "
                  , new String[]{TaskBean.VALUE_FINISHED+""
                  , TaskBean.DEFAULT_VALUE_OF_DATE_TIME+""
                  , Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_TIMILLS});
                cursorNoDate = dbReader.rawQuery("select * from "
                  + Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS
                  + " where " + Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_IS_FINISHED
                  + " = ? and "
                  + Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_TIMILLS
                  +" = ?", new String[]{TaskBean.VALUE_FINISHED+""
                  , TaskBean.DEFAULT_VALUE_OF_DATE_TIME+""});
            }

        }

        RenderObjectBeans<TaskBean> renderObjectBeans = new RenderObjectBeans<>();
        boolean isCursorHasDate = false;
        if (cursorHasDate != null) {
            while (cursorHasDate.moveToNext()) {
                renderObjectBeans.addBeanInOrder(cursorRowToTaskBean(cursorHasDate), false);
            }
            isCursorHasDate = true;
        }
        boolean isCursorNoDate = false;
        if (cursorNoDate != null) {
            while (cursorNoDate.moveToNext()) {
                renderObjectBeans.add(cursorRowToTaskBean(cursorNoDate), false);
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
            msg.what = Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_HANDLE_FAIL;
            bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_GET_TASKS_FAIL_NO_TASKS);
        }
        msg.obj = bundle;
        if (handler != null) {
            handler.sendMessage(msg);
        }
        if (cursorHasDate != null) {
            cursorHasDate.close();
        }
        if (cursorNoDate != null) {
            cursorNoDate.close();
        }
        dbReader.close();
    }

    private void searchBeans(int action, String queryLike, int requestCode) {
        RenderDbCallback handler = (RenderDbCallback) mHandlers.get(Constant.RenderServiceHelper.ACTION.valueOf(action).toString());
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.BundelExtra.EXTRA_REQUEST_CODE, requestCode);
        Message msg = new Message();
        if (StringUtil.isEmpty(queryLike)) {
            msg.what = Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_HANDLE_FAIL;
            bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_GET_TASKS_FAIL_ERROR);
            msg.obj = bundle;
            if (handler != null) {
                handler.sendMessage(msg);
            } else {
                Log.e("Render", "this is no handler when get tasks by group name");
            }
            return;
        }
        SQLiteDatabase dbReader = mRenderDbHelper.getReadableDatabase();
        Cursor cursorHasDate = dbReader.rawQuery("select * from "
          + Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS
          + " where " + Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_CONTENT
          + " like ? and "
          + Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_TIMILLS
          + " <> ?"
          + " order by ? "
          , new String[]{"%"+queryLike+"%"
          , TaskBean.DEFAULT_VALUE_OF_DATE_TIME + ""
          , Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_TIMILLS});
        Cursor cursorNoDate = dbReader.rawQuery("select * from "
          + Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS
          + " where " + Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_CONTENT
          + " like ? and "
          + Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_TIMILLS
          +" = ?",new String[]{"%"+queryLike+"%"
          ,TaskBean.DEFAULT_VALUE_OF_DATE_TIME+""});

        RenderObjectBeans<TaskBean> renderObjectBeans = new RenderObjectBeans<>();
        boolean isCursorHasDate = false;
        if (cursorHasDate != null) {
            while (cursorHasDate.moveToNext()) {
                renderObjectBeans.addBeanInOrder(cursorRowToTaskBean(cursorHasDate), false);
            }
            isCursorHasDate = true;
        }
        boolean isCursorNoDate = false;
        if (cursorNoDate != null) {
            while (cursorNoDate.moveToNext()) {
                renderObjectBeans.add(cursorRowToTaskBean(cursorNoDate), false);
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
        if (handler != null) {
            handler.sendMessage(msg);
        }
        if (cursorHasDate != null) {
            cursorHasDate.close();
        }
        if (cursorNoDate != null) {
            cursorNoDate.close();
        }
        dbReader.close();
    }

    private void getGroupsExceptFinished(int action, int requestCode) {
        RenderDbCallback handler = (RenderDbCallback) mHandlers.get(Constant.RenderServiceHelper.ACTION.valueOf(action).toString());
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.BundelExtra.EXTRA_REQUEST_CODE, requestCode);
        Message msg = new Message();
        SQLiteDatabase dbReader = mRenderDbHelper.getReadableDatabase();
        Cursor cursor = dbReader.rawQuery("select * "
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
        if (handler != null) {
            handler.sendMessage(msg);
        }
        if (cursor != null) {
            cursor.close();
        }
        dbReader.close();
    }

    private void addDataToTable(int action, String tableName,RenderBeanBase bean, int requestCode) {
        RenderDbCallback handler = (RenderDbCallback) mHandlers.get(Constant.RenderServiceHelper.ACTION.valueOf(action).toString());
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.BundelExtra.EXTRA_REQUEST_CODE, requestCode);
        Message msg = new Message();
        if (StringUtil.isEmpty(tableName) || bean == null) {
            msg.what = Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_HANDLE_FAIL;
            bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_FAIL);
            msg.obj = bundle;
            if (handler != null) {
                handler.sendMessage(msg);
            } else {
                Log.e("Render", "this is no handler when get tasks by group name");
            }
            return;
        }
        SQLiteDatabase dbWrite = mRenderDbHelper.getWritableDatabase();
        long row = dbWrite.insert(tableName, null, taskBeanToContentValues(bean, requestCode));
        if (row != -1) {
            if (handler != null) {
                msg.what = Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_UPDATE_SUCCESS;
                bundle.putLong(Constant.BundelExtra.EXTRA_UPDATE_ROW, row);
                if (action == Constant.RenderServiceHelper.ACTION.ACTION_ADD_NEW_TASK.value()) {
                    bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_INSERT_TASK_SUCCESS);
                } else if (action == Constant.RenderServiceHelper.ACTION.ACTION__ADD_NEW_GROUP.value()) {
                    bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_INSERT_GROUP_SUCCESS);
                }
            }
        } else {
            if (handler != null) {
                msg.what = Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_HANDLE_FAIL;
                bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_FAIL);
            }
        }
        msg.obj = bundle;
        if (handler != null) {
            handler.sendMessage(msg);
        }
        dbWrite.close();
    }

    private void updateTable(int action, String tableName,RenderBeanBase oldBean, RenderBeanBase newBean, int requestCode) {
        RenderDbCallback handler = (RenderDbCallback) mHandlers.get(Constant.RenderServiceHelper.ACTION.valueOf(action).toString());
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.BundelExtra.EXTRA_REQUEST_CODE, requestCode);
        Message msg = new Message();
        if (StringUtil.isEmpty(tableName) || oldBean == null || newBean == null) {
            msg.what = Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_HANDLE_FAIL;
            bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_FAIL);
            msg.obj = bundle;
            if (handler != null) {
                handler.sendMessage(msg);
            } else {
                Log.e("Render", "this is no handler when get tasks by group name");
            }
            return;
        }

        int tempRequestCode;
        if (action == Constant.RenderServiceHelper.ACTION.ACTION_UPDATE_TASK.value()) {
            tempRequestCode = Constant.RenderServiceHelper.REQUEST_CODE_UPDATE_TASK_BEAN;
        } else if (action == Constant.RenderServiceHelper.ACTION.ACTION_UPDATE_GROUP_NAME.value()) {
            tempRequestCode = Constant.RenderServiceHelper.REQUEST_CODE_UPDATE_GROUP_NAME;
        } else {
            tempRequestCode = requestCode;
        }

        SQLiteDatabase dbWrite = mRenderDbHelper.getWritableDatabase();
        long row = dbWrite.update(tableName, taskBeanToContentValues(oldBean, newBean, tempRequestCode)
          , Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_ID + " = ?", new String[]{((GroupBean) oldBean).getId() + ""});
        if (row != -1) {
            if (handler != null) {
                msg.what = Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_UPDATE_SUCCESS;
                bundle.putLong(Constant.BundelExtra.EXTRA_UPDATE_ROW, row);
                if (action == Constant.RenderServiceHelper.ACTION.ACTION_UPDATE_TASK.value()) {
                    bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_UPDATE_TASK_SUCCESS);
                } else if (action == Constant.RenderServiceHelper.ACTION.ACTION_UPDATE_GROUP_NAME.value()) {
                    bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_UPDATE_GROUP_SUCCESS);
                }
            }
        } else {
            if (handler != null) {
                msg.what = Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_HANDLE_FAIL;
                bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_FAIL);
            }
        }
        msg.obj = bundle;
        if (handler != null) {
            handler.sendMessage(msg);
        }
        dbWrite.close();
    }

    private void deleteDateById(int action, String tableName,String[] wheres, int requestCode) {
        RenderDbCallback handler = (RenderDbCallback) mHandlers.get(Constant.RenderServiceHelper.ACTION.valueOf(action).toString());
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.BundelExtra.EXTRA_REQUEST_CODE, requestCode);
        Message msg = new Message();
        if (StringUtil.isEmpty(tableName) || wheres == null || wheres.length < 1) {
            msg.what = Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_HANDLE_FAIL;
            bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_FAIL);
            msg.obj = bundle;
            if (handler != null) {
                handler.sendMessage(msg);
            } else {
                Log.e("Render", "this is no handler when get tasks by group name");
            }
            return;
        }
        SQLiteDatabase dbWrite = mRenderDbHelper.getWritableDatabase();
        long row = dbWrite.delete(tableName, Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_ID + "=?"
                , new String[]{wheres[0]});
        if (action == Constant.RenderServiceHelper.ACTION.ACTION_DELETE_GROUP.value()) {
            //update group of tasks
            ContentValues cv = new ContentValues();
            cv.put(Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP, Constant.RenderDbHelper.GROUP_NAME_MY_TASK);
            dbWrite.update(Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS
                    , cv
                    , Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP+" = ?"
                    , new String[]{wheres[1]});
        }
        if (row != -1) {
            if (handler != null) {
                msg.what = Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_UPDATE_SUCCESS;
                bundle.putLong(Constant.BundelExtra.EXTRA_UPDATE_ROW, row);
                if (action == Constant.RenderServiceHelper.ACTION.ACTION_DELETE_TASK.value()) {
                    bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_DELETE_TASK_SUCCESS);
                } else if (action == Constant.RenderServiceHelper.ACTION.ACTION_DELETE_GROUP.value()) {
                    bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_DELETE_GROUP_SUCCESS);
                }
            }
        } else {
            if (handler != null) {
                msg.what = Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_HANDLE_FAIL;
                bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_FAIL);
            }
        }
        msg.obj = bundle;
        if (handler != null) {
            handler.sendMessage(msg);
        }
        dbWrite.close();
    }

    private ContentValues taskBeanToContentValues(RenderBeanBase bean, int requestCode) {
        ContentValues cv = new ContentValues();
        switch (requestCode) {
            case Constant.RenderServiceHelper.REQUEST_CODE_INSERT_TASK_BEAN:
                TaskBean taskBean = (TaskBean)bean;
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_CONTENT, taskBean.getTaskContent());
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_YEAR, taskBean.getYear());
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_MONTH, taskBean.getMonth());
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_DAY_OF_MONTH, taskBean.getDayOfMonth());
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_HOUR, taskBean.getHour());
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_MINUTE, taskBean.getMinute());
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_REPEAT_INTERVAL, taskBean.getRepeatInterval());
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_REPEAT_UNIT, taskBean.getRepeatUnit());
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_TIMILLS, taskBean.getTimeInMillis());
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_IS_FINISHED, taskBean.isFinished());
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP, taskBean.getGroup());
                break;
            case Constant.RenderServiceHelper.REQUEST_CODE__INSERT_NEW_GROUP:
            default:
                GroupBean groupBean = (GroupBean)bean;
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP, groupBean.getGroup());
                break;
        }
        return cv;
    }

    private ContentValues taskBeanToContentValues(RenderBeanBase oldBean, RenderBeanBase newBean, int requestCode) {
        ContentValues cv = new ContentValues();
        if (requestCode == Constant.RenderServiceHelper.REQUEST_CODE_UPDATE_TASK_BEAN) {
            TaskBean oldTaskBean = (TaskBean)oldBean;
            TaskBean newTaskBean = (TaskBean)newBean;
            if (!oldTaskBean.getTaskContent().equals(newTaskBean.getTaskContent())) {
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_CONTENT, newTaskBean.getTaskContent());
            }
            if (oldTaskBean.getTimeInMillis() != newTaskBean.getTimeInMillis()) {
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_TIMILLS, newTaskBean.getTimeInMillis());
                if ((oldTaskBean.isClearedPickedDate() && !newTaskBean.isClearedPickedDate())
                        || (!oldTaskBean.isClearedPickedDate() && newTaskBean.isClearedPickedDate())
                        || !oldTaskBean.getPickedDate(true).equals(newTaskBean.getPickedDate(true))) {
                    cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_YEAR, newTaskBean.getYear());
                    cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_MONTH, newTaskBean.getMonth());
                    cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_DAY_OF_MONTH, newTaskBean.getDayOfMonth());
                }
                if ((oldTaskBean.isClearedPickedTime() && !newTaskBean.isClearedPickedTime())
                        || (!oldTaskBean.isClearedPickedTime() && newTaskBean.isClearedPickedTime())
                        || !oldTaskBean.getPickedTime(true).equals(newTaskBean.getPickedTime(true))) {
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
            if (oldTaskBean.isFinished() != newTaskBean.isFinished()) {
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_IS_FINISHED, newTaskBean.isFinished());
            }
            if (!oldTaskBean.getGroup().equals(newTaskBean.getGroup())) {
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP, newTaskBean.getGroup());
            }
        } else if (requestCode == Constant.RenderServiceHelper.REQUEST_CODE_UPDATE_GROUP_NAME) {
            GroupBean oldGroupBean = (GroupBean)oldBean;
            GroupBean newGroupBean = (GroupBean)newBean;
            if (!oldGroupBean.getGroup().equals(newGroupBean.getGroup())) {
                cv.put(Constant.RenderDbHelper.EXTRA_TABLE_GROUP_COLUM_GROUP, newGroupBean.getGroup());
            }
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
        taskBean.setIsFinished(cursor.getInt(cursor.getColumnIndex(Constant.RenderDbHelper.EXTRA_TABLE_TASKS_COLUM_IS_FINISHED)));
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


    public void getOrUpdate(final int action, final String tableName, final RenderBeanBase oldBean, final RenderBeanBase newBean, final String[] whereArgs, final int requestCode) {
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
                        addDataToTable(action, tableName, newBean, requestCode);
                        break;
                    //update task
                    //update group name
                    case 5:
                    case 6:
                        updateTable(action, tableName, oldBean, newBean, requestCode);
                        break;
                    //delete task
                    //delete group
                    case 7:
                    case 8:
                        deleteDateById(action, tableName, whereArgs, requestCode);
                        break;
                    case 9:
                        searchBeans(action, whereArgs[0],requestCode);
                        break;
                }
                return null;
            }
        }.execute();
    }

}
