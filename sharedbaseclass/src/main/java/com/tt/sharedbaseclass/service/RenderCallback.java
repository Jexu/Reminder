package com.tt.sharedbaseclass.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.model.RenderObjectBeans;


/**
 * Created by zhengguo on 2016/5/21.
 */
public abstract class RenderCallback extends Handler {

    @Override
    public void handleMessage(Message msg){
        int what = msg.what;
        Bundle bundle = (Bundle) msg.obj;
        switch (what) {
            //select successfully
            case 1:
                onHandleSelectSuccess((RenderObjectBeans) bundle.getSerializable(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN),
                  bundle.getInt(Constant.BundelExtra.EXTRA_REQUEST_CODE),
                  bundle.getInt(Constant.BundelExtra.EXTRA_RESULT_CODE));
                break;
            //update successfully
            case 2:
                onHandleUpdateSuccess(bundle.getLong(Constant.BundelExtra.EXTRA_UPDATE_ROW),
                  bundle.getInt(Constant.BundelExtra.EXTRA_REQUEST_CODE),
                  bundle.getInt(Constant.BundelExtra.EXTRA_RESULT_CODE));
                break;
            //on handle fail
            case 3:
                break;
        }
    }

    protected abstract void onHandleSelectSuccess(RenderObjectBeans renderObjectBeans, int requestCode, int resultCode);
    protected abstract void onHandleUpdateSuccess(long row, int requestCode, int resultCode);
    protected abstract void onHandleFail(int requestCode, int resultCode);
}
