package com.tt.sharedbaseclass.model;

import android.app.Activity;
import android.content.ComponentName;

import com.tt.sharedbaseclass.constant.Constant;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhengguo on 2016/5/21.
 */

public class RenderObjectBeans<T> extends ArrayList implements Serializable {

    public static final int TYPE_TASK_BEANS_HAS_DATE_ONLY
            = Constant.RenderServiceHelper.RESULT_CODE_GET_TASKS_SUCCESS_HAS_DATE_ONLY;
    public static final int TYPE_TASK_BEANS_NO_DATE_ONLY
            = Constant.RenderServiceHelper.RESULT_CODE_GET_TASKS_SUCCESS_NO_DATE_ONLY;
    public static final int TYPE_TASK_BEANS_HAS_DATE_AND_NO_DATE
            = Constant.RenderServiceHelper.RESULT_CODE_GET_TASKS_SUCCESS_HAS_DATE_AND_NO_DATE;
    public static final int TYPE_GROUP_BEANS = 1;

    public static final int TYPE_DEFAULT = -1;

    private int mType;
    private int mCountTaskHasDate;
    private int mCountTaskNoDate;

    public RenderObjectBeans() {
        mType = TYPE_DEFAULT;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public int getCountTaskHasDate() {
        return mCountTaskHasDate;
    }

    public void setCountTaskHasDate(int countTaskHasDate) {
        this.mCountTaskHasDate = countTaskHasDate;
    }

    public int getCountTaskNoDate() {
        return mCountTaskNoDate;
    }

    public void setmCountTaskNoDate(int countTaskNoDate) {
        this.mCountTaskNoDate = countTaskNoDate;
    }
}
