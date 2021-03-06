package com.tt.sharedbaseclass.fragment;

import android.os.Bundle;
import android.transition.Transition;

/**
 * Created by zhengguo on 2016/5/26.
 */
public interface RenderBase {
    String getFragmentTag();
    void initServices();
    void fetchData();
    void setContextAndReqCode(RenderFragmentBase context, int requestCode);
    Transition enterTransition();
    Transition exitTransition();
    void finish();
    void finishWithResultCode(int resultCode, Bundle bundle);
    void onFinishedWithResult(int requestCode, int resultCode, Bundle bundle);
    boolean onBackPressed();
    void destroyServices();
}
