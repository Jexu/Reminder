package com.tt.sharedbaseclass.model;



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

    public void setCountTaskNoDate(int countTaskNoDate) {
        this.mCountTaskNoDate = countTaskNoDate;
    }

    public void addBeanInOrder(Object bean) {
        if (bean instanceof TaskBean) {
            TaskBean tb = (TaskBean)bean;
            if (tb.isClearedPickedDate() && tb.isClearedPickedTime()) {
                add(tb);
                return;
            } else {
                int index = 0;

                int low = 0;
                int high = getCountTaskHasDate() - 1;
                int mid;
                while (low <= high) {
                    mid = (low + high)/2;
                    if (tb.compareTo(get(mid)) < 0) {
                        if (mid-1 >= 0 && tb.compareTo(get(mid - 1)) >= 0) {
                            index = mid;
                            break;
                        } else if (mid - 1 < 0) {
                            index = 0;
                            break;
                        }else {
                            high = mid - 1;
                        }
                    } else if (tb.compareTo(get(mid)) > 0) {
                        if (mid+1 <= getCountTaskHasDate()-1 && tb.compareTo(get(mid + 1)) <= 0) {
                            index = mid + 1;
                            break;
                        } else if (mid + 1 > getCountTaskHasDate() - 1) {
                            index = mid + 1;
                            break;
                        } else {
                            low = mid + 1;
                        }
                    } else {
                        index = mid;
                        break;
                    }
                    index = mid;
                }
                add(index, tb);
            }
        }
    }

}
