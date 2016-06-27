package com.tt.sharedbaseclass.model;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import com.google.gson.Gson;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedutils.AndroidUtil;
import com.tt.sharedutils.DeviceUtil;
import com.tt.sharedutils.MailUtil;
import com.tt.sharedutils.StringUtil;
import org.apache.commons.mail.EmailException;

/**
 * Created by zxu on 6/17/16.
 */
public class RenderFeedbackService extends RenderServiceBase {

  public RenderFeedbackService(Context context) {
    super(context);
  }

  public void sendFeedback(int action, final String comment, final String userMailAddr, int requestCode) {
    final RenderFeedbackCallback handler = (RenderFeedbackCallback) mHandlers.get(Constant.RenderServiceHelper.ACTION.valueOf(action).toString());
    final Bundle bundle = new Bundle();
    bundle.putInt(Constant.BundelExtra.EXTRA_REQUEST_CODE, requestCode);
    final Message msg = new Message();
    final Gson gson = new Gson();
    final FeedbackBean feedbackBean = new FeedbackBean(comment
            , StringUtil.isEmpty(userMailAddr)?"":userMailAddr
            , DeviceUtil.getOSVersion()
            , DeviceUtil.getDeviceName()
            , AndroidUtil.getAndroidVersion()+"");
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          //suvdexofamapdaci
          MailUtil.sendEmail("3338449608@qq.com", "suvdexofamapdaci", "3338449608@qq.com", "Feedback", gson.toJson(feedbackBean));
          msg.what = Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_HANDLE_FEEDBACK_SUCCESS;
          bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_SEND_FEEDBACK_SUCCESS);
        } catch (EmailException e) {
          e.printStackTrace();
          Log.e("Render", "fail to send feedback");
            msg.what = Constant.RenderServiceHelper.HANDLER_MSG_WHAT_ON_HANDLE_FAIL;
            bundle.putInt(Constant.BundelExtra.EXTRA_RESULT_CODE, Constant.RenderServiceHelper.RESULT_CODE_SEND_FEEDBACK_FAIL);
        }
        msg.obj = bundle;
        if (handler != null) {
          handler.sendMessage(msg);
        }
      }
    }).start();
  }

}
