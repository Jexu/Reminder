package com.tt.sharedbaseclass.model;


import com.tt.sharedbaseclass.constant.Constant;


/**
 * Created by zhengguo on 2016/5/28.
 */
public class GroupBean extends RenderBeanBase {

    protected int mId;
    protected String mGroup;

    public GroupBean() {
        super();
        mGroup = Constant.RenderDbHelper.GROUP_NAME_MY_TASK;
    }

    public GroupBean(String group) {
        mGroup = group;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getGroup() {
        return mGroup;
    }

    public void setGroup(String group) {
        this.mGroup = group;
    }

    public void copy(RenderBeanBase groupBean) {
        mId = ((GroupBean)groupBean).mId;
        mGroup = ((GroupBean)groupBean).mGroup;
    }

    @Override
    public String toString() {
        return mGroup;
    }

    @Override
    public boolean equals(Object groupBean) {
        return mGroup.equals(((GroupBean)groupBean).getGroup());
    }

    @Override
    public int compareTo(Object another) {
        return mGroup.compareToIgnoreCase(((GroupBean) another).getGroup());
    }
}
