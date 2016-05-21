package com.tt.sharedbaseclass.service;

import com.tt.sharedbaseclass.model.RenderObjectBeans;

/**
 * Created by Administrator on 2016/5/21.
 */
public interface RenderCallback {
    void onHandleSuccess(RenderObjectBeans renderObjectBeans, int requestCode, int resultCode);
    void onHandleFail(int requestCode, int resultCode);
}
