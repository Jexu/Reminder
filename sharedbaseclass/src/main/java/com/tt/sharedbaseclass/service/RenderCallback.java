package com.tt.sharedbaseclass.service;

import com.tt.sharedbaseclass.model.RenderObjectBeans;

/**
 * Created by zhengguo on 2016/5/21.
 */
public interface RenderCallback {
    void onHandleSelectSuccess(RenderObjectBeans renderObjectBeans, int requestCode, int resultCode);
    void onHandleUpdateSuccess(long row, int requestCode, int resultCode);
    void onHandleFail(int requestCode, int resultCode);
}
