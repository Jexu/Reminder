package com.tt.sharedbaseclass.model;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.tt.sharedbaseclass.constant.Constant;

/**
 * Created by zxu on 6/17/16.
 */
public abstract class RenderFeedbackCallback extends Handler {


  @Override
  public void handleMessage(Message msg) {
    super.handleMessage(msg);
    int what = msg.what;
    Bundle bundle = (Bundle) msg.obj;
    if (what == Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_HANDLE_FEEDBACK_SUCCESS) {
      onSendSuccess(bundle.getInt(Constant.BundelExtra.EXTRA_REQUEST_CODE), bundle.getInt(Constant.BundelExtra.EXTRA_RESULT_CODE));
    } else if (what == Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_HANDLE_FAIL) {
      onSendFail(bundle.getInt(Constant.BundelExtra.EXTRA_REQUEST_CODE), bundle.getInt(Constant.BundelExtra.EXTRA_RESULT_CODE));
    }
  }

  protected abstract void onSendSuccess(int requestCode, int resultCode);
  protected abstract void onSendFail(int requestCode, int resultCode);
}
